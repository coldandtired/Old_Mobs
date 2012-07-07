package me.coldandtired.mobs;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInput;
import java.io.ObjectInputStream;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.logging.Logger;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathFactory;

import me.coldandtired.api.Mob;
import me.coldandtired.mobs.data.Autospawn;
import me.coldandtired.mobs.data.Autospawn_location;
import me.coldandtired.mobs.data.Autospawn_time;
import me.coldandtired.mobs.data.Config;
import me.coldandtired.mobs.data.Creature_data;
import me.coldandtired.mobs.data.Selected_outcomes;
import me.coldandtired.mobs.listeners.*;
import net.milkbowl.vault.economy.Economy;

import org.bukkit.ChatColor;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitScheduler;
import org.fusesource.jansi.Ansi;
import org.fusesource.jansi.AnsiConsole;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import com.herocraftonline.heroes.Heroes;
import com.khorn.terraincontrol.bukkit.BukkitWorld;
import com.khorn.terraincontrol.bukkit.TCPlugin;
import com.sk89q.worldguard.bukkit.WorldGuardPlugin;

public class Main extends JavaPlugin
{
	public static Map<String, Mob> all_mobs = new HashMap<String, Mob>();
	public static WorldGuardPlugin world_guard = null;
	public static Economy economy = null;
	public static Heroes heroes = null;
	public static TCPlugin tc = null;
	public static XPath xpath;
	public static ConcurrentMap<String, Calendar> mobs_with_lifetimes = null;
	public static Map<String, Creature_data> tracked_mobs = null;
	public static Logger logger;	
	public static Main plugin;
	public static Map<String, Selected_outcomes> previous_mobs = null; 
	List<Autospawn> autospawns = null;
	
	boolean is_latest_version()
	{
		DocumentBuilder dbf;
		xpath = XPathFactory.newInstance().newXPath();
		try 
		{
			dbf = DocumentBuilderFactory.newInstance().newDocumentBuilder();
			Document doc = dbf.parse("http://dev.bukkit.org/server-mods/mobs/files.rss");
			String s = ((Element) xpath.evaluate("//item[1]/title", doc, XPathConstants.NODE)).getTextContent();
			return (s.equalsIgnoreCase(getDescription().getVersion()));
		} 
		catch (Exception e) {return true;}		
	}
	
	boolean load_xml_config()
	{
		File f = new File(getDataFolder(), "data.mobs");
		if (f.exists())
		try
		{	
			InputSource input = new InputSource(f.getPath());

			Element config = (Element)xpath.evaluate("Mobs/config", input, XPathConstants.NODE);
			Config.setup_config(config);
			PluginManager pm = getServer().getPluginManager();
			world_guard = get_world_guard();
			tc = get_tc();
			heroes = get_heroes();
			BukkitScheduler scheduler = getServer().getScheduler();
			scheduler.cancelTasks(this);

			NodeList list = (NodeList) xpath.evaluate("Mobs/creatures/Mob[@has_autospawns = \"true\"]", input, XPathConstants.NODESET);

			if (list.getLength() > 0)
			{
				if (Config.log_level > 0)
				{
					L.log("-----------------");
					L.log("Autospawning mobs");
					L.log("-----------------");
				}

				autospawns = new ArrayList<Autospawn>();
				for (int i = 0; i < list.getLength(); i++)
				{				
					String mob_name = ((Element)list.item(i)).getAttributeNode("name").getValue();
					NodeList autos = (NodeList) xpath.evaluate("autospawns/Autospawn", list.item(i), XPathConstants.NODESET);
					for (int j = 0; j < autos.getLength(); j++)
					{

						Element as_settings = (Element) xpath.evaluate("autospawn_settings", autos.item(j), XPathConstants.NODE);
						NodeList locations = (NodeList) xpath.evaluate("autospawn_locations/locations/Autospawn_location", autos.item(j), XPathConstants.NODESET);
						NodeList times = (NodeList) xpath.evaluate("autospawn_times/times/Autospawn_time", autos.item(j), XPathConstants.NODESET);						
						if (times.getLength() > 0)
						{
							for (int k = 0; k < times.getLength(); k++)
							{
								Element element = (Element)times.item(k);
								autospawns.add(new Autospawn(element, locations, mob_name, as_settings));
							}
						}
						else autospawns.add(new Autospawn(null, locations, mob_name, as_settings));
					}
					if (Config.log_level > 0) L.log(mob_name);
				}
				if (Config.log_level > 0) L.log("-----------------");	 

				for (final Autospawn as : autospawns)
				{
					if (as.manual) continue;
					long l = as.spawn_time.interval;
					scheduler.scheduleSyncRepeatingTask(this, new Runnable() 
					{			 
						public void run() {activate_autospawn(as, false);}
					}, l, l);
				}
			}

			// end autospawn mobs		

			list = (NodeList) xpath.evaluate("Mobs/creatures/Mob[@track_mob = \"true\"]", input, XPathConstants.NODESET);

			if (list.getLength() == 0)
			{
				L.log("No mobs in the config!  Autospawning vanilla mobs.");				
				return true;
			}

			tracked_mobs = new HashMap<String, Creature_data>();
			List<String> mob_names = new ArrayList<String>();

			if (Config.log_level > 0)
			{
				L.log("------------");
				L.log("Tracked mobs");
				L.log("------------");
			}

			for (int i = 0; i < list.getLength(); i++)
			{
				Element element = (Element)list.item(i);
				String mob_name = element.getAttributeNode("name").getValue();
				tracked_mobs.put(mob_name, new Creature_data(element));
				if (Config.log_level > 0) L.log(mob_name);
				mob_names.add(mob_name);
			}
			if (Config.log_level > 0) L.log("------------");						

			if (mob_names.contains("SHEEP") || mob_names.contains("MUSHROOM_COW"))
			{
				pm.registerEvents(new Sheep_listener(), this);
			}
			if (mob_names.contains("PIG"))
			{
				pm.registerEvents(new Pig_listener(), this);
			}
			if (mob_names.contains("WOLF"))
			{
				pm.registerEvents(new Wolf_listener(), this);
			}
			if (mob_names.contains("ENDERMAN"))
			{
				pm.registerEvents(new Enderman_listener(), this);
			}
			if (mob_names.contains("SLIME") || mob_names.contains("MAGMA_CUBE"))
			{
				pm.registerEvents(new Slime_listener(), this);
			}
			if (mob_names.contains("CREEPER"))
			{
				pm.registerEvents(new Creeper_listener(), this);
			}
			if (mob_names.contains("ENDER_DRAGON"))
			{
				pm.registerEvents(new Ender_dragon_listener(), this);
			}
			if (mob_names.contains("PLAYER"))
			{
				//pm.registerEvents(new Player_listener(), this);
			}
			
			pm.registerEvents(new Main_listener(), this);		

			if (pm.getPlugin("Vault") != null) setup_economy();

			scheduler.scheduleSyncDelayedTask(this, new Runnable() 
			{			 
				public void run() {convert_mobs();}
			}, 1L);

			list = (NodeList) xpath.evaluate("//Property[contains(@name, 'max_lifetime') and @property_value != \"\"]", input, XPathConstants.NODESET);

			if (list.getLength() > 0)
			{
				mobs_with_lifetimes = new ConcurrentHashMap<String, Calendar>();				
				scheduler.scheduleSyncRepeatingTask(this, new Runnable() 
				{			 
					public void run() {check_lifetimes();}
				}, 1L, 300L);
			}
			return true;
		}
		catch (Exception ne)
		{
			L.log("Bad XML formatting somewhere in the config file!");
			ne.printStackTrace();
			return false;
		}
		else
		{
			// No data file			
			f = this.getDataFolder();
			if (!f.exists()) f.mkdir();
		}
		return false;
	}
	
	public void onDisable() 
	{
		save_mobs();
		all_mobs = null;
		world_guard = null;
		economy = null;
		tc = null;
		xpath = null;
		mobs_with_lifetimes = null;
		tracked_mobs = null;
		logger = null;
		plugin = null;
		previous_mobs = null;
		autospawns = null;
	}
	
	public void activate_autospawn(Autospawn as, boolean from_command)
	{
		Autospawn_time st = as.spawn_time;
		if (st != null && st.has_real_time && (!from_command || (from_command && as.command_time_check)))
		{
			Calendar cal = Calendar.getInstance();
			cal.set(Calendar.YEAR, 0);
			cal.set(Calendar.MONTH, 0);
			cal.set(Calendar.DATE, 0);
			cal.set(Calendar.MILLISECOND, 0);
			if (st.real_start != null && cal.compareTo(st.real_start) < 0) return;
			if (st.real_end != null && cal.compareTo(st.real_end) > 0) return;
		}
		
		for (Autospawn_location sl : as.spawn_locations)
		{
			if (sl.all_values) for (World w : sl.worlds)
			{
				if (!L.ignore_world(w) || (from_command && !as.command_time_check)) spawn_mobs(as, sl, w, from_command);
			}
			else
			{
				World w = sl.worlds.get(L.get_random_choice(sl.worlds.size()));
				if (!L.ignore_world(w) || (from_command && !as.command_time_check)) spawn_mobs(as, sl, w, from_command);						
			}
		}		
	}
	
	void spawn_mobs(Autospawn as, Autospawn_location sl, World w, boolean from_command)
	{
		int count = 0;
		for (Player p : w.getPlayers()) if (p.isOnline()) count++;
				
		if (count < as.min_player_count && (!from_command || (from_command && as.command_time_check))) return;
			
		if (as.spawn_time != null && as.spawn_time.has_mc_time && (!from_command || (from_command && as.command_time_check)))
		{
			long mc_time = w.getTime();
			if (mc_time < as.spawn_time.mc_start && mc_time > as.spawn_time.mc_end) return;
		}
		
		Boolean above_ground = true;
		switch (sl.autospawn_placement)
		{			
			case 1:
				above_ground = false;
				break;
			case 2:
				above_ground = null;
				break;
		}
		
		List<String> temp_biomes = null;
		if (sl.biomes != null)
		{
			if (sl.all_values) temp_biomes = sl.biomes;
			else
			{
				temp_biomes = new ArrayList<String>();
				temp_biomes.add(sl.biomes.get(L.get_random_choice(sl.biomes.size())));			
			}
		}
		
		List<String> temp_regions = null;
		if (sl.regions != null)
		{
			if (sl.all_values) temp_regions = sl.regions;
			else
			{
				temp_regions = new ArrayList<String>();
				temp_regions.add(sl.regions.get(L.get_random_choice(sl.regions.size())));
			}
		}
		
		// not near players or range
		if (sl.location_type.equalsIgnoreCase(""))
		{					
			if (temp_regions != null)
			{
				// regions and biomes
				for (String s : temp_regions)
				{
					L.spawn_mob(L.get_blocks_in_region(as.mob_name, w, s, above_ground, temp_biomes),
							as, from_command, w);					
				}
			}
			else if (temp_biomes != null)
			{
				// biomes only
				BukkitWorld bw = tc != null ? tc.worlds.get(w.getUID()) : null;
				for (String s : temp_biomes)
				{
					L.spawn_mob(L.get_blocks_in_biome(bw, as.mob_name, w, s, above_ground),
							as, from_command, w);
				} 
			}			
		}		
		else if (sl.location_type.equalsIgnoreCase("near_players"))
		{
			BukkitWorld bw = tc != null ? tc.worlds.get(w.getUID()) : null;
			for (Player p : w.getPlayers())
			{
				if (p.isOnline())
				{
					Location loc = p.getLocation();	
					L.spawn_mob(L.get_blocks_in_range(bw, as.mob_name, (int)loc.getX(), (int)loc.getY() + 1, (int)loc.getZ(), sl, w, above_ground, temp_biomes, temp_regions),
							as, from_command, w);				
				}
			}
		}
		else if (sl.location_type.equalsIgnoreCase("range"))
		{
			BukkitWorld bw = tc != null ? tc.worlds.get(w.getUID()) : null;
			L.spawn_mob(L.get_blocks_in_range(bw, as.mob_name, sl.xbase, sl.ybase, sl.zbase, sl, w, above_ground, temp_biomes, temp_regions),
					as, from_command, w);
		}
	}	
	
	@SuppressWarnings("unchecked")
	public Map<String, Selected_outcomes> get_mobs()
	{
		File f = new File(getDataFolder(), "mobs_list");
		if (f.exists())
		try
		{
			InputStream file = new FileInputStream(f);
		    InputStream buffer = new BufferedInputStream(file);
		    ObjectInput input = new ObjectInputStream (buffer);
		    try
		    {
		    	return (HashMap<String, Selected_outcomes>)input.readObject();
		    }
		    finally {input.close();}
		}
		catch(Exception ne){ne.printStackTrace();}
		else
		try 
		{
				f.createNewFile();
				L.log("No mobs_list found - created!");
				return null;
		} 
		catch (Exception ne) {ne.printStackTrace();}
		return null;
	}
	
	private void save_mobs()
	{
		if (all_mobs == null) return;
		
		File f = new File(getDataFolder(), "mobs_list");
		if (f.exists())
		try
		{
			OutputStream file = new FileOutputStream(f);
		    OutputStream buffer = new BufferedOutputStream(file);
		    ObjectOutput output = new ObjectOutputStream(buffer);
		    try
		    {
		    	Map<String, Selected_outcomes> temp_mobs = new HashMap<String, Selected_outcomes>();
		    	for (World w : getServer().getWorlds())
		    	{
		    		if (L.ignore_world(w)) continue;
		    		
		    		for (LivingEntity le : w.getLivingEntities())
		    		{
		    			String s = le.getUniqueId().toString();
		    			if (all_mobs.containsKey(s)) temp_mobs.put(s, all_mobs.get(s).selected_outcomes);	    		
		    		}
		    	}
		    	
		        output.writeObject(temp_mobs);
		    }
		    finally {output.close();}
		}  
		catch(Exception ne) {ne.printStackTrace();}
	}
	
	public Mob get_mob(Entity entity)
	{
		return all_mobs != null ? all_mobs.get((LivingEntity)entity) : null;
	}
	
	private Boolean setup_economy()
    {
		if (getServer().getPluginManager().getPlugin("Vault") == null) return false;
		
        RegisteredServiceProvider<Economy> economyProvider = getServer().getServicesManager().getRegistration(net.milkbowl.vault.economy.Economy.class);
        if (economyProvider != null) economy = economyProvider.getProvider();

        return (economy != null);
    }
	
	private WorldGuardPlugin get_world_guard() 
	{
	    Plugin plugin = getServer().getPluginManager().getPlugin("WorldGuard");
	    if (plugin == null || !(plugin instanceof WorldGuardPlugin)) 
	    {
	        return null;
	    }
	 
	    return (WorldGuardPlugin) plugin;
	}
		
	private void check_lifetimes()
	{
		if (mobs_with_lifetimes == null || mobs_with_lifetimes.size() == 0) return;
		
		Calendar cal = Calendar.getInstance();
		
		for (String s : mobs_with_lifetimes.keySet())
		{
			LivingEntity le = L.get_mob_from_id(s);
			if (le != null && cal.after(mobs_with_lifetimes.get(s)))
			{
				mobs_with_lifetimes.remove(s);
				le.damage(10000);
			}
		}
	}
	
	private TCPlugin get_tc() 
	{
	    Plugin plugin = getServer().getPluginManager().getPlugin("TerrainControl");
	    if (plugin == null || !(plugin instanceof TCPlugin)) 
	    {
	        return null;
	    }
	 
	    return (TCPlugin) plugin;
	}
	
	private Heroes get_heroes() 
	{
	    Plugin plugin = getServer().getPluginManager().getPlugin("Heroes");
	    if (plugin == null || !(plugin instanceof Heroes)) 
	    {
	        return null;
	    }
	    getServer().getPluginManager().registerEvents(new Heroes_listener(), this);
	    return (Heroes) plugin;
	}
	
	public void onEnable() 
	{		
		logger = getLogger();
		if (!is_latest_version()) logger.info("There's a new version of Mobs available!");
    	
		if (!load_xml_config())
		{
			L.warn("There was no data file found!  Stopping");
			setEnabled(false);
			return;
		}
		plugin = this;		
		
		try 
		{
		    Metrics metrics = new Metrics(plugin);
		    metrics.start();
		} 
		catch (IOException e) 
		{
		    L.warn("Something went wrong with Metrics - it will be disabled.");
		}
	}	

	void convert_mobs()
	{
		previous_mobs = get_mobs();
		for (World world : getServer().getWorlds())
		{ 		
			if (!L.ignore_world(world))	for (Chunk c : world.getLoadedChunks()) L.convert_chunk(c);
		}
	}
	
	public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args)
	{
		if (cmd.getName().equalsIgnoreCase("mobstest"))
    	{
    		if (sender.isOp())
    		{
    			getLogger().info(Ansi.ansi().fg(Ansi.Color.GREEN).bold().toString() + "Test 1"
    					+ Ansi.ansi().fg(Ansi.Color.WHITE).bold().toString());
    			AnsiConsole.out.println(Ansi.ansi().fg(Ansi.Color.YELLOW).bold().toString() +"test 2"
    					+ Ansi.ansi().fg(Ansi.Color.WHITE).bold().toString());
    		}
    		return true;
    	}
		
		if (cmd.getName().equalsIgnoreCase("spawn_mobs") && args.length < 2)
		{
			if (sender instanceof Player && !sender.hasPermission("mobs.can_spawn_mobs"))
			{
				sender.sendMessage(ChatColor.RED + "[Mobs] You don't have permission to spawn mobs!");
				return true;
			}
			
			if (autospawns == null)
			{
				sender.sendMessage(ChatColor.RED + "[Mobs] No auto_spawns found!");
				return true;
			}
			
			if (args.length == 1)
			{		
				int count = 0;
				for (Autospawn a : autospawns)
				{
					if (a.id.equalsIgnoreCase(args[0])) 
					{
						activate_autospawn(a, true);
						count++;
					}
				}
				if (count == 0) sender.sendMessage(ChatColor.RED + "[Mobs] Couldn't find an id with that name!");
				else sender.sendMessage(ChatColor.GREEN + "[Mobs] Mobs manually spawned!");
			}
			else
			{
				for (Autospawn a : autospawns) activate_autospawn(a, true);
				sender.sendMessage(ChatColor.GREEN + "[Mobs] All autospawns manually spawned!");
			}
			
			return true;
		}
		return false;
	}
}
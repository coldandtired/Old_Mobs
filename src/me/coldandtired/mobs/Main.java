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
import java.util.UUID;
import java.util.logging.Logger;

import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathFactory;

import me.coldandtired.mobs.data.Autospawn;
import me.coldandtired.mobs.data.Autospawn_location;
import me.coldandtired.mobs.data.Autospawn_time;
import me.coldandtired.mobs.data.Config;
import me.coldandtired.mobs.data.Creature_data;
import me.coldandtired.mobs.data.Selected_outcomes;
import me.coldandtired.mobs.listeners.*;
import net.milkbowl.vault.economy.Economy;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitScheduler;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import com.khorn.terraincontrol.bukkit.BukkitWorld;
import com.khorn.terraincontrol.bukkit.TCPlugin;
import com.sk89q.worldedit.BlockVector;
import com.sk89q.worldguard.bukkit.WorldGuardPlugin;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;

public class Main extends JavaPlugin
{
	public static Map<LivingEntity, Mob> all_mobs = null;
	public static WorldGuardPlugin world_guard = null;
	public static Economy economy = null;
	public static TCPlugin tc = null;
	public static XPath xpath;
	public static Map<LivingEntity, Calendar> mobs_with_lifetimes = null;
	public static Map<String, Creature_data> tracked_mobs = null;
	public static Logger logger;	
	public static Main plugin;
	public static Map<UUID, Selected_outcomes> previous_mobs = null; 
	public static HashMap<String, Biome_data> chunks = null;
	List<Autospawn> autospawns = null;
	
	boolean load_xml_config()
	{
		File f = new File(getDataFolder(), "data.mobs");
		if (f.exists())
		try
		{	
			InputSource input = new InputSource(f.getPath());
			xpath = XPathFactory.newInstance().newXPath();
			
			Element config = (Element)xpath.evaluate("Mobs/config", input, XPathConstants.NODE);
			Config.setup_config(config);
			PluginManager pm = getServer().getPluginManager();
			world_guard = get_world_guard();
			tc = get_tc();
			setup_economy();
			BukkitScheduler scheduler = getServer().getScheduler();
			
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
				
				chunks = new HashMap<String, Biome_data>();
				
				for (World w : Bukkit.getWorlds())
				{
					for (Chunk c : w.getLoadedChunks()) add_chunk(c);
				}			 
				
				scheduler.cancelTasks(this);
				for (final Autospawn as : autospawns)
				{
					if (as.manual) continue;
					long l = as.spawn_time.interval;
					scheduler.scheduleSyncRepeatingTask(this, new Runnable() 
					{			 
						public void run() {activate_autospawn(as, false);}
					}, l, l);
				}	
				pm.registerEvents(new Chunk_listener(), this);
			}
			
			// end autospawn mobs		
			
			list = (NodeList) xpath.evaluate("Mobs/creatures/Mob[@track_mob = \"true\"]", input, XPathConstants.NODESET);
			
			if (list.getLength() == 0)
			{
				L.log("No mobs in the config!  Autospawning vanilla mobs.");				
				return true;
			}
			
			tracked_mobs = new HashMap<String, Creature_data>();
			all_mobs = new HashMap<LivingEntity, Mob>();
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
				pm.registerEvents(new Player_listener(), this);
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
				mobs_with_lifetimes = new HashMap<LivingEntity, Calendar>();				
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
		chunks = null;
		autospawns = null;
	}
	
	public static void add_chunk(Chunk chunk)
	{
		Block b = chunk.getBlock(7, 0, 7);
		
		String world_name = chunk.getWorld().getName();
		Biome_data bd = chunks.get(world_name);
		if (bd == null) bd = new Biome_data();
		
		String biome = null;
		
		if (Main.tc != null)
		{
			BukkitWorld bw = Main.tc.worlds.get(chunk.getWorld().getUID());
			if (bw != null)
			{
				int id = bw.getBiome(b.getX(), b.getZ());
				biome = bw.getBiomeById(id).getName().toUpperCase();
			}
		}
		
		if (biome == null) biome = b.getBiome().name();
		
		Integer[] loc = { chunk.getX(), chunk.getZ() };
		List<Integer[]> locs = bd.chunks.get(biome);
		
		if (locs == null)
		{
			locs = new ArrayList<Integer[]>();
			locs.add(loc);
			bd.chunks.put(biome, locs);
			chunks.put(world_name, bd);
			if (Config.log_level > 1) L.log("added new biome " + biome + " to " + world_name);
		}
		else if (!locs.contains(loc)) 
		{
			locs.add(loc);
			bd.chunks.put(biome, locs);
			chunks.put(world_name, bd);

			if (biome.equalsIgnoreCase("gold")) L.log(loc[0] * 16 + loc[1] * 16);
		}
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
		
		List<String> safe_blocks = new ArrayList<String>();
		
		boolean above_ground = true;
		switch (sl.autospawn_placement)
		{			
			case 0:
				above_ground = true;
				break;
			case 1:
				above_ground = false;
				break;
			case 2:
				above_ground = L.rng.nextBoolean();
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
		
		if (sl.location_type.equalsIgnoreCase(""))
		{					
			if (temp_regions != null)
			{
				for (String s : temp_regions)
				{
					safe_blocks.clear();
					ProtectedRegion pr = Main.world_guard.getRegionManager(w).getRegions().get(s);
					if (pr != null)
					{
						BlockVector min = pr.getMinimumPoint();
						BlockVector max = pr.getMaximumPoint();
						if (above_ground)
						{
							for (int x = min.getBlockX(); x < max.getBlockX(); x++)
							{
								for (int z = min.getBlockZ(); z < max.getBlockZ(); z++)
								{
									for (int y = min.getBlockY(); y < max.getBlockY(); y++)
									{
										if (y < w.getMaxHeight())
										{
											if (L.is_safe_above_ground_block(w.getBlockAt(x, y, z), sl.loaded_chunks_only,
													temp_biomes, null, w)) safe_blocks.add(x + "," + y + "," + z);
										}
									}
								}
							}
						}
						else
						{
							for (int x = min.getBlockX(); x <= max.getBlockX(); x++)
							{
								for (int z = min.getBlockZ(); z <= max.getBlockZ(); z++)
								{
									for (int y = min.getBlockY(); y <= max.getBlockY(); y++)
									{
										if (y < w.getMaxHeight())
										{
											if (L.is_safe_below_ground_block(w.getBlockAt(x, y, z), sl.loaded_chunks_only,
													temp_biomes, null, w)) safe_blocks.add(x + "," + y + "," + z);
										}
									}
								}
							}
						}
						if (safe_blocks.size() > 0)
						{			
							int amount = 1;
							if (as.manual && from_command && as.manual_amount != null) amount = as.manual_amount.get(L.get_random_choice(as.manual_amount.size()));
							else if (as.amount != null) amount = as.amount.get(L.get_random_choice(as.amount.size()));
							
							for (int i = 0; i < amount; i++)
							{
								String[] temp = safe_blocks.get(L.get_random_choice(safe_blocks.size())).split(",");

								Main_listener.autospawn = as;
								w.spawnCreature(w.getBlockAt(Integer.parseInt(temp[0]), Integer.parseInt(temp[1]), Integer.parseInt(temp[2])).getLocation(), EntityType.valueOf(as.mob_name));
								if (Config.log_level > 1) L.log("Autospawned " + as.mob_name);
							}
						}
					}
					else L.log("No region called " + s + " in world " + w.getName() + "!");
				}
			}
			else if (temp_biomes != null)
			{
				Biome_data bd = Main.chunks.get(w.getName());
				for (String s : temp_biomes)
				{
					List<Integer[]> locs = bd.chunks.get(s);
					if (locs == null) continue;
					for (Integer[] ints : locs)
					{
						Chunk c = w.getChunkAt(ints[0], ints[1]);
						if (sl.loaded_chunks_only && !c.isLoaded()) continue;
						int cx = L.get_random_choice(16);
						int cz = L.get_random_choice(16);
						if (above_ground)
						{			
							int amount = 1;
							if (as.manual && from_command && as.manual_amount != null) amount = as.manual_amount.get(L.get_random_choice(as.manual_amount.size()));
							else if (as.amount != null) amount = as.amount.get(L.get_random_choice(as.amount.size()));
							
							for (int i = 0; i < amount; i++)
							{								
								Main_listener.autospawn = as;
								w.spawnCreature(c.getBlock(cx, w.getHighestBlockYAt(cx, cz), cz).getLocation(), EntityType.valueOf(as.mob_name));
								if (Config.log_level > 1) L.log("Autospawned " + as.mob_name);								
							}
						}
						else
						{
							for (int x = 0; x < 16; x++)
							{
								for (int z = 0; z < 16; z++)
								{
									for (int y = 0; y < w.getMaxHeight(); y++)
									{
										Block b = c.getBlock(x, y, z);
										if (b.getLightFromSky() < 14 && b.getType() == Material.AIR && b.getRelative(BlockFace.UP).getType() == Material.AIR
												&& b.getRelative(BlockFace.DOWN).getType() != Material.AIR) safe_blocks.add(x + "," + y + "," + z);
									}
								}
							}
							if (safe_blocks.size() > 0)
							{						
								int amount = 1;
								if (as.manual && from_command && as.manual_amount != null) amount = as.manual_amount.get(L.get_random_choice(as.manual_amount.size()));
								else if (as.amount != null) amount = as.amount.get(L.get_random_choice(as.amount.size()));
								
								for (int i = 0; i < amount; i++)
								{
									String[] temp = safe_blocks.get(L.get_random_choice(safe_blocks.size())).split(",");

									Main_listener.autospawn = as;
									w.spawnCreature(w.getBlockAt(Integer.parseInt(temp[0]), Integer.parseInt(temp[1]), Integer.parseInt(temp[2])).getLocation(), EntityType.valueOf(as.mob_name));
									if (Config.log_level > 1) L.log("Autospawned " + as.mob_name);									
								}
							}
						}
					}
				}
			}			
		}		
		else if (sl.location_type.equalsIgnoreCase("near_players") || sl.location_type.equalsIgnoreCase("range"))
		{	
			if (above_ground)
			{
				if (sl.location_type.equalsIgnoreCase("near_players"))
				{
					for (Player p : w.getPlayers())
					{
						if (p.isOnline())
						{
							Location loc = p.getLocation();						
							L.check_above_ground_block((int)loc.getX(), (int)loc.getY() + 1, (int)loc.getZ(), w, sl, temp_biomes, temp_regions, safe_blocks);
						}
					}
				}
				else L.check_above_ground_block(sl.xbase, sl.ybase, sl.zbase, w, sl, temp_biomes, temp_regions, safe_blocks);
			}
			else
			{
				if (sl.location_type.equalsIgnoreCase("near_players"))
				{
					for (Player p : w.getPlayers())
					{
						if (p.isOnline())
						{
							Location loc = p.getLocation();
							L.check_below_ground_block((int)loc.getX(), (int)loc.getY(), (int)loc.getZ(), w, sl, temp_biomes, temp_regions, safe_blocks);
						}
					}
				}
				else L.check_below_ground_block(sl.xbase, sl.ybase, sl.zbase, w, sl, temp_biomes, temp_regions, safe_blocks);
			}
			if (safe_blocks.size() > 0)
			{				
				int amount = 1;
				if (as.manual && from_command && as.manual_amount != null) amount = as.manual_amount.get(L.get_random_choice(as.manual_amount.size()));
				else if (as.amount != null) amount = as.amount.get(L.get_random_choice(as.amount.size()));
				
				for (int i = 0; i < amount; i++)
				{
					String[] temp = safe_blocks.get(L.get_random_choice(safe_blocks.size())).split(",");

					Main_listener.autospawn = as;
					w.spawnCreature(w.getBlockAt(Integer.parseInt(temp[0]), Integer.parseInt(temp[1]), Integer.parseInt(temp[2])).getLocation(), EntityType.valueOf(as.mob_name));
					if (Config.log_level > 1) L.log("Autospawned " + as.mob_name);					
				}
			}
		}
	}	
	
	@SuppressWarnings("unchecked")
	public Map<UUID, Selected_outcomes> get_mobs()
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
		    	Map<UUID, Selected_outcomes> temp_mobs = (HashMap<UUID, Selected_outcomes>)input.readObject();
		    	return temp_mobs;
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
		    	Map<UUID, Selected_outcomes> temp_mobs = new HashMap<UUID, Selected_outcomes>();
		    	for (LivingEntity le : all_mobs.keySet())
		    	{
		    		if (le != null && !le.isDead()) temp_mobs.put(le.getUniqueId(), all_mobs.get(le).selected_outcomes);
		    	}
		        output.writeObject(temp_mobs);
		    }
		    finally {output.close();}
		}  
		catch(Exception ne) {ne.printStackTrace();}
	}
	
	public Mob get_mob(Entity entity)
	{
		return all_mobs.get((LivingEntity)entity);
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
		Map<LivingEntity, Calendar> temp = new HashMap<LivingEntity, Calendar>();
		
		for (LivingEntity e : mobs_with_lifetimes.keySet()) temp.put(e, mobs_with_lifetimes.get(e));
		
		for (LivingEntity e : temp.keySet())
		{
			if (e == null || e.isDead() || cal.after(temp.get(e)))
			{
				mobs_with_lifetimes.remove(e);
				if (e != null && !e.isDead()) e.damage(10000);
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
	
	public void onEnable() 
	{		
		logger = getLogger();
		if (!load_xml_config())
		{
			L.warn("There was no data file found!  Stopping");
			setEnabled(false);
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
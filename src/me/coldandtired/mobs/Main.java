package me.coldandtired.mobs;

import java.io.File;
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
import me.coldandtired.mobs.data.Config;
import me.coldandtired.mobs.data.Creature_data;
import me.coldandtired.mobs.listeners.*;
import net.milkbowl.vault.economy.Economy;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Chunk;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitScheduler;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import com.sk89q.worldguard.bukkit.WorldGuardPlugin;

public class Main extends JavaPlugin
{
	Main_listener listener;
	public static WorldGuardPlugin world_guard = null;
	public static Economy economy = null;
	Autospawner spawner;
	public static XPath xpath;
	public static HashMap<String, Creature_data> tracked_mobs;
	public static Map<UUID, Mob> mobs = new HashMap<UUID, Mob>();
	public static Logger logger;	
	
	boolean load_xml_config()
	{
		File f = new File(getDataFolder(), "data.mobs");
		if (f.exists())
		try
		{		
			ArrayList<String> mob_names = new ArrayList<String>();
			InputSource input = new InputSource(f.getPath());
			xpath = XPathFactory.newInstance().newXPath();
			
			Element config = (Element)xpath.evaluate("Mobs/config", input, XPathConstants.NODE);
			Config.setup_config(config);
			PluginManager pm = getServer().getPluginManager();
			world_guard = get_world_guard();
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
				
				ArrayList<Autospawn> autospawns = new ArrayList<Autospawn>();
				for (int i = 0; i < list.getLength(); i++)
				{				
					String mob_name = ((Element)list.item(i)).getAttributeNode("name").getValue();
					NodeList autos = (NodeList) xpath.evaluate("autospawns/Autospawn", list.item(i), XPathConstants.NODESET);
					for (int j = 0; j < autos.getLength(); j++)
					{

						Element as_settings = (Element) xpath.evaluate("autospawn_settings", autos.item(j), XPathConstants.NODE);
						NodeList locations = (NodeList) xpath.evaluate("autospawn_locations/locations/Autospawn_location", autos.item(j), XPathConstants.NODESET);
						NodeList times = (NodeList) xpath.evaluate("autospawn_times/times/Autospawn_time", autos.item(j), XPathConstants.NODESET);						
						for (int k = 0; k < times.getLength(); k++)
						{
							Element element = (Element)times.item(k);
							autospawns.add(new Autospawn(element, locations, mob_name, as_settings));
						}
					}
					if (Config.log_level > 0) L.log(mob_name);
				}
				if (Config.log_level > 0) L.log("-----------------");
				
				for (World w : Bukkit.getWorlds())
				{
					for (Chunk c : w.getLoadedChunks()) Autospawner.add_chunk(c);
				}			 
				
				scheduler.cancelTasks(this);
				spawner = new Autospawner(this, autospawns);
				
				long check_interval = Config.check_interval;
				scheduler.scheduleSyncRepeatingTask(this, spawner, check_interval, check_interval);	
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
			pm.registerEvents(new Main_listener(), this);			

			if (pm.getPlugin("Vault") != null) setup_economy();
			
			scheduler.scheduleSyncRepeatingTask(this, new Runnable()
			{
				public void run() {purge_mobs_list();}
			}, 72000L, 72000L);
			
			scheduler.scheduleSyncDelayedTask(this, new Runnable() 
			{			 
				public void run() {convert_mobs();}
			}, 1L);
			
			return true;
		}
		catch (Exception ne)
		{
			L.log("Bad XML formatting somewhere in the config file!");
			ne.printStackTrace();
			return false;
		}
		return false;
	}
	
	public void onDisable() 
	{
		listener = null;
		world_guard = null;
		economy = null;
		spawner = null;
		xpath = null;
		tracked_mobs = null;
		mobs = null;
		logger = null;
	}
	
	public Mob get_mob(Entity entity)
	{
		if (tracked_mobs == null) return null;
		if (tracked_mobs.get(entity.getType().name()) == null) return null;
		if (mobs == null) return null;
		
		Mob mob = mobs.get(entity.getUniqueId());
		if (mob != null) return mob;
		
		return null;
	}
	
	private Boolean setup_economy()
    {
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
		
	public void onEnable() 
	{		
		logger = getLogger();
		if (!load_xml_config()) this.setEnabled(false);
	}	

	//convert mobs disabled
	void convert_mobs()
	{/*
		for (World world : getServer().getWorlds())
		{
			for (Entity ee: world.getEntities()) 
			{
				if (ee instanceof LivingEntity && old_listener.tracked_mobs.contains(Utils.get_mob(ee)))
				{
					old_listener.mobs.put(ee.getUniqueId(), new Old_mob(Configs.get_section(Utils.get_mob(ee)), null, SpawnReason.NATURAL.name()));
				}
			}
		}	*/		
	}
	
	void purge_mobs_list()
	{
		Calendar old = Calendar.getInstance();
		old.add(Calendar.HOUR, -1);
		List<UUID> temp = new ArrayList<UUID>();
		for (UUID id : mobs.keySet())
		{
			if (mobs.get(id).spawned_at.before(old.getTime())) temp.add(id);
		}
		for (UUID id : temp) mobs.remove(id);
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
			
			if (spawner == null)
			{
				sender.sendMessage(ChatColor.RED + "[Mobs] No auto_spawns found!");
				return true;
			}
			
			if (args.length == 1)
			{		
				int count = 0;
				for (Autospawn a : spawner.spawns)
					if (a.id.equalsIgnoreCase(args[0])) 
					{
						spawner.activate_autospawn(a, true);
						count++;
					}
				if (count == 0) sender.sendMessage(ChatColor.RED + "[Mobs] Couldn't find an id with that name!");
				else sender.sendMessage(ChatColor.GREEN + "[Mobs] Mobs manually spawned!");
			}
			else for (Autospawn a : spawner.spawns) spawner.activate_autospawn(a, true);
			
			return true;
		}
		
		if (cmd.getName().equalsIgnoreCase("reload_mobs") && args.length == 0)
		{
			if (sender instanceof Player && !sender.hasPermission("mobs.can_reload_mobs"))
			{
				sender.sendMessage(ChatColor.RED + "[Mobs] You don't have permission to reload Mobs!");
				return true;
			}
			
			convert_mobs();
			L.log("Config reloaded!");
			if (sender instanceof Player) sender.sendMessage(ChatColor.GREEN + "[Mobs] Config reloaded!");
			return true;
		}
		
		if (cmd.getName().equalsIgnoreCase("purge_mobs") && args.length == 0)
		{
			if (sender instanceof Player && !sender.hasPermission("mobs.can_purge_mobs"))
			{
				sender.sendMessage(ChatColor.RED + "[Mobs] You don't have permission to purge mobs!");
				return true;
			}
			
			purge_mobs_list();
			L.log("Mobs purged!");
			if (sender instanceof Player) sender.sendMessage(ChatColor.GREEN + "[Mobs] Mobs purged!");
			return true;
		}
		return false;
	}
}
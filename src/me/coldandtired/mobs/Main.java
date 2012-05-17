package me.coldandtired.mobs;

import java.io.File;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.HashMap;
import java.util.logging.Logger;

import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathFactory;

import me.coldandtired.mobs.data.Autospawn;
import me.coldandtired.mobs.data.Config;
import me.coldandtired.mobs.data.Creature_data;
import me.coldandtired.mobs.data.Damage_value;
import me.coldandtired.mobs.data.Drops;
import me.coldandtired.mobs.data.Mob_properties;
import me.coldandtired.mobs.listeners.*;
import net.milkbowl.vault.economy.Economy;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Chunk;
import org.bukkit.DyeColor;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Animals;
import org.bukkit.entity.Creeper;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Ocelot;
import org.bukkit.entity.Pig;
import org.bukkit.entity.PigZombie;
import org.bukkit.entity.Player;
import org.bukkit.entity.Sheep;
import org.bukkit.entity.Slime;
import org.bukkit.entity.Villager;
import org.bukkit.entity.Wolf;
import org.bukkit.event.entity.CreatureSpawnEvent.SpawnReason;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.scheduler.BukkitScheduler;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import com.khorn.terraincontrol.bukkit.TCPlugin;
import com.sk89q.worldguard.bukkit.WorldGuardPlugin;

public class Main extends JavaPlugin
{
	Main_listener listener;
	public static WorldGuardPlugin world_guard = null;
	public static TCPlugin tc = null;
	public static Economy economy = null;
	Autospawner spawner;
	public static XPath xpath;
	public static HashMap<LivingEntity, Calendar> mobs_with_lifetimes = null;
	public static HashMap<String, Creature_data> tracked_mobs = null;
	public static Logger logger;	
	public static Main plugin;
	
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
		logger = null;
		mobs_with_lifetimes = null;
	}
	
	public Mob get_mob(Entity entity)
	{
		if (!entity.hasMetadata("mobs_data")) return null;
		
		Object o = entity.getMetadata("mobs_data").get(0).value();
		return (Mob)o;
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
		Calendar cal = Calendar.getInstance();
		for (LivingEntity e : mobs_with_lifetimes.keySet())
		{
			if (e == null || e.isDead() || cal.after(mobs_with_lifetimes.get(e)))
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
		if (!load_xml_config()) this.setEnabled(false);
		plugin = this;
	}	

	void convert_mobs()
	{
		for (World world : getServer().getWorlds())
		{			
			for (Entity e: world.getEntities()) 
			{
				if (e instanceof LivingEntity)
				{	
					LivingEntity le = (LivingEntity)e;
					Mob m = null;
					Collection<PotionEffect> potion_effects = null;
					Mob_properties props = null;
					
					Creature_data cd = Main.tracked_mobs.get(le.getType().name());	
					if (cd != null) 
					{
						String mob_name = le.getType().name().toLowerCase();
						String spawn_reason = SpawnReason.NATURAL.name();
						int random = L.rng.nextInt(100) + 1;
						
						props = L.merge_mob_properties(cd, le, spawn_reason, random, null);		
						potion_effects = L.merge_potion_effects(cd, le, spawn_reason, random, null);
						Drops drops = null;
						HashMap<String, Damage_value> damage_properties = null;		
						
						if (cd.gen_drops_check_type != 1) drops = L.merge_drops(cd, le, spawn_reason, null, random, null);
						
						if (cd.gen_damages_check_type != 1) damage_properties = L.merge_damage_properties(cd, le, spawn_reason, null, random, null);
						
						if (props == null && potion_effects == null && drops == null && damage_properties == null) 
						{
							if (Config.log_level > 1) L.log("No default outcome for " + mob_name + "  - vanilla mob spawned!");
							m = new Mob(props, drops, damage_properties, spawn_reason, random);
						}
	
						m =  new Mob(props, drops, damage_properties, spawn_reason, random);
					}
					if (m == null) e.removeMetadata("mobs_data", plugin);	 else
					{
						e.setMetadata("mobs_data", new FixedMetadataValue(plugin, m));
						if (potion_effects != null) le.addPotionEffects(potion_effects);
						if (props != null)
						{
							if (le instanceof Slime)
							{
								Slime slime = (Slime)le;
								if (props.hp_per_size != null) m.hp = slime.getSize() * L.return_int_from_array(props.hp_per_size);
							}	
							
							if (le instanceof Animals && props.adult != null)
							{
								Animals animal = (Animals)le;
								boolean b = L.return_bool_from_string(props.adult);
							
								if (b == true) animal.setAdult(); else animal.setBaby();
							}
							
							if (le instanceof Pig && props.saddled != null)
							{
								((Pig)le).setSaddle(L.return_bool_from_string(props.saddled));
							}
							else if (le instanceof PigZombie && props.angry != null)
							{
								((PigZombie)le).setAngry(L.return_bool_from_string(props.angry));
							}
							else if (le instanceof Wolf)
							{
								Wolf wolf = (Wolf)le;			
								if (props.angry != null) wolf.setAngry(L.return_bool_from_string(props.angry));
								if (!wolf.isAngry())
								{
									if (props.tamed != null) wolf.setTamed(L.return_bool_from_string(props.tamed));
									if (props.can_be_tamed != null && !L.return_bool_from_string(props.can_be_tamed)) wolf.setTamed(false);
									if (wolf.isTamed() && props.tamed_hp != null) m.hp = L.return_int_from_array(props.tamed_hp);
								}
								else wolf.damage(0, L.get_nearby_player(wolf));
							}
							else if (le instanceof Sheep)
							{
								Sheep sheep = (Sheep)le;
								if (props.sheared != null) sheep.setSheared(L.return_bool_from_string(props.sheared));
								if (props.can_grow_wool != null && !L.return_bool_from_string(props.can_grow_wool)) sheep.setSheared(false);
								
								DyeColor dc = L.set_wool_colour(props.wool_colours);
								if (dc != null) sheep.setColor(dc);
							}
							else if (le instanceof Ocelot && props.ocelot_types != null)
							{
								Ocelot.Type ot = L.set_ocelot_type(props.ocelot_types);
								if (ot != null) ((Ocelot)le).setCatType(ot);
							}
							else if (le instanceof Villager && props.villager_types != null)
							{
								Villager.Profession prof = L.set_villager_type(props.villager_types);
								if (prof != null) ((Villager)le).setProfession(prof);			
							}
							else if (le instanceof Creeper && props.powered != null) 
							{
								((Creeper)le).setPowered(L.return_bool_from_string(props.powered));		
							}
						}
					}
				}
			}
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
			
			if (spawner == null)
			{
				sender.sendMessage(ChatColor.RED + "[Mobs] No auto_spawns found!");
				return true;
			}
			
			if (args.length == 1)
			{		
				int count = 0;
				for (Autospawn a : spawner.spawns)
				{
					if (a.id.equalsIgnoreCase(args[0])) 
					{
						spawner.activate_autospawn(a, true);
						count++;
					}
				}
				if (count == 0) sender.sendMessage(ChatColor.RED + "[Mobs] Couldn't find an id with that name!");
				else sender.sendMessage(ChatColor.GREEN + "[Mobs] Mobs manually spawned!");
			}
			else
			{
				for (Autospawn a : spawner.spawns) spawner.activate_autospawn(a, true);
				sender.sendMessage(ChatColor.GREEN + "[Mobs] All autospawns manually spawned!");
			}
			
			return true;
		}
		
		if (cmd.getName().equalsIgnoreCase("reload_mobs") && args.length == 0)
		{
			/*if (sender instanceof Player && !sender.hasPermission("mobs.can_reload_mobs"))
			{
				sender.sendMessage(ChatColor.RED + "[Mobs] You don't have permission to reload Mobs!");
				return true;
			}
			
			convert_mobs();
			L.log("Config reloaded!");
			if (sender instanceof Player) sender.sendMessage(ChatColor.GREEN + "[Mobs] Config reloaded!");*/
			sender.sendMessage("[Mobs] This command is currently disabled");
			return true;
		}
		return false;
	}
}
package me.coldandtired.mobs;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import net.milkbowl.vault.economy.Economy;
import org.bukkit.ChatColor;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.MemorySection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.CreatureSpawnEvent.SpawnReason;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitScheduler;

import com.sk89q.worldguard.bukkit.WorldGuardPlugin;

public class Main extends JavaPlugin
{
	Mobs_listener listener;
	public static WorldGuardPlugin world_guard = null;
	public static Economy economy = null;
	Mob_spawner spawner;
	Mob_purger purger;
	FileConfiguration config;
	
	public void onDisable() 
	{
		listener = null;
		world_guard = null;
		economy = null;
		config = null;
		spawner = null;
		purger = null;
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
		Utils.setup_utils(this);
		make_example();
		load_config();		
		world_guard = get_world_guard();
		if (getServer().getPluginManager().getPlugin("Vault") != null) setup_economy();
		listener = new Mobs_listener();
		listener.setup(this);

		if (listener.tracked_mobs.size() == 0)
		{
			Utils.log("No mobs found in the config - stopping!");
			this.setEnabled(false);
			return;
		}
        getServer().getPluginManager().registerEvents(listener, this);
        convert_mobs();
	}

	void make_example()
	{
		File f = new File(getDataFolder() + File.separator + "example.yml");
		try 
		{
			if (!getDataFolder().exists()) getDataFolder().mkdir();
			f.createNewFile();
			FileWriter fw = new FileWriter(f);
			fw.write("# Here are the global settings for the plugin.\n");
			fw.write("# See http://dev.bukkit.org/server-mods/mobs/pages/tutorial/ for help\n");
			fw.write("\n");
			fw.write("# This is the time in seconds between each spawn_event.\n");
			fw.write("# Set it big and with large quantities of mobs for wave spawning,\n");
			fw.write("# or set it small and with small quantities of mobs for steady spawning.\n");
			fw.write("spawn_interval: 600\n");
			fw.write("\n");
			fw.write("# This is the number of players that must be on the server\n");
			fw.write("# for any spawn_event to activate.  Set it to a high number to test things before\n");
			fw.write("# unleashing the config.\n");
			fw.write("auto_spawn_min_player_count: 1\n");
			fw.write("\n");
			fw.write("# This section controls whether Mobs should overrule other plugins if they try to\n");
			fw.write("# cancel an event.  Try setting them all to yes if you experience unexpected behaviour\n");
			fw.write("# (or try disabling any other plugins that affect mobs, of course).\n");
			fw.write("# Note that setting any of these to no will not disable the functionality in Mobs.\n");
			fw.write("overrule:\n");
			fw.write("# Controls the hp and damages of mobs.\n");
			fw.write("  damaging: no\n");
			fw.write("\n");
			fw.write("# Controls whether mobs are allowed to burn or not.\n");
			fw.write("  burning: no\n");
			fw.write("\n");
			fw.write("# Controls whether mobs should be allowed to spawn.\n");
			fw.write("  spawning: no\n");
			fw.write("\n");
			fw.write("# Controls whether mobs should ignore the player.\n");
			fw.write("  targeting: no\n");
			fw.write("\n");
			fw.write("# Controls whether mobs are allowed to explode\n");
			fw.write("  exploding: no\n");
			fw.write("\n");
			fw.write("# Controls whether endermen can pick up blocks.\n");
			fw.write("  enderman_moving_blocks: no\n");
			fw.write("\n");
			fw.write("# Controls whether ender dragons should create a portal when killed.\n");
			fw.write("  creating_portal: no\n");
			fw.write("\n");
			fw.write("# Controls whether slimes and magmacubes are allowed to split when killed.\n");
			fw.write("  splitting: no\n");
			fw.write("\n");
			fw.write("# Controls whether sheep are allowed to grow their wool back.\n");
			fw.write("  regrowing_wool: no\n");
			fw.write("\n");
			fw.write("# Controls whether sheep are allowed to have their colour changed.\n");
			fw.write("  dying_wool: no\n");
			fw.write("\n");
			fw.write("# Controls whether sheep are allowed to be sheared.\n");
			fw.write("  shearing: no\n");
			fw.write("\n");
			fw.write("# Controls whether sheep are allowed to destroy grass.\n");
			fw.write("  changing_block: no\n");
			fw.write("\n");
			fw.write("# Controls whether wolves are allowed to be tamed.\n");
			fw.write("  taming: no\n");
			fw.write("\n");
			fw.write("# Controls whether creepers are allowed to become powered creepers\n");
			fw.write("  becoming_powered_creeper: no\n");
			fw.write("\n");
			fw.write("# Controls whether pigs are allowed to become pig zombies\n");
			fw.write("  becoming_pig_zombie: no\n");
			fw.write("\n");
			fw.write("# Each mob section looks like this.\n");
			fw.write("# The availabe mobs names are blaze, cavespider, chicken, cow, creeper,\n");
			fw.write("# enderdragon, enderman, ghast, giant, magmacube, mushroom_cow, pig, pigzombie,\n");
			fw.write("# sheep, silverfish, skeleton, slime, snowman, spider, squid, villager, wolf,\n");
			fw.write("# zombie\n");
			fw.write("# The mob_name must match one of these exactly.\n");
			fw.write("mob_name:\n");
			fw.write("  general:\n");
			fw.write("  auto_spawn:\n");
			fw.write("  spawn_rules:\n");
			fw.write("  death_rules:\n");
			fw.write("\n");
			fw.write("# Here is an example for the creeper:\n");
			fw.write("creeper:\n");
			fw.write("  general:\n");
			fw.write("    hp: [100, 150 to 200]\n");
			fw.write("    powered: random\n");
			fw.write("    safe: yes\n");
			fw.write("  auto_spawn:\n");
			fw.write("  - spawn_event:\n");
			fw.write("      where_to_spawn:\n");
			fw.write("      - location:\n");
			fw.write("          regions: [name_of_region1, name_of_region2]\n");
			fw.write("      - location:\n");
			fw.write("          worlds: [world1, world2]\n");
			fw.write("          regions: [name_of_region1, name_of_region2]\n");
			fw.write("      - location:\n");
			fw.write("          base: {x: 0, y: 120, z: 0}\n");
			fw.write("          range: {x: 2, y: 2, z: 2}\n");
			fw.write("      general:\n");
			fw.write("        hp: [500]\n");
			fw.write("      quantities: [1 to 5, 10, 20 to 25]\n");
			fw.write("      shortcut: 1\n");
			fw.write("  spawn_rules:\n");
			fw.write("    spawn: yes\n");
			fw.write("    unless:\n");
			fw.write("    - condition_group:\n");
			fw.write("        game_times: [above 12000]\n");
			fw.write("  burn_rules:\n");
			fw.write("    burn: no\n");
			fw.write("    unless:\n");
			fw.write("    - condition_group:\n");
			fw.write("        raining: no\n");
			fw.write("  death_rules:\n");
			fw.write("  - action:\n");
			fw.write("      replace_drops: yes\n");
			fw.write("      replace_exp: yes\n");
			fw.write("      will_drop:\n");
			fw.write("      - item:\n");
			fw.write("          names: [red_wool, green_wool]\n");
			fw.write("      will_give_exp: [10, 30 to 50]\n");
			fw.write("      will_give_money: [200, 300]\n");
			fw.write("      if:\n");
			fw.write("      - condition_group:\n");
			fw.write("          percent: [above 75]\n");
			fw.close();				
		} 
		catch (IOException e) {e.printStackTrace();}				
	}
	
	void convert_mobs()
	{
		listener.allow = false;
		for (World world : getServer().getWorlds())
		{
			for (Entity ee: world.getEntities()) 
			{
				if (ee instanceof LivingEntity && listener.tracked_mobs.contains(Utils.get_mob(ee)))
				{
					MemorySection ms = (MemorySection) config.get(Utils.get_mob(ee));
					listener.mobs.put(ee.getUniqueId(), new Mob(ms, null, SpawnReason.NATURAL.name()));
				}
			}
		}
		listener.allow = true;
		setup_spawns();
	}
	
	void load_config()
	{
		world_guard = null;
		economy = null;
		config = null;
		spawner = null;
		purger = null;
		File f = new File(getDataFolder() + File.separator + "config.yml");
		if (!f.exists())
		{
			Utils.log("No config found - created!");
			try 
			{
				if (!getDataFolder().exists()) getDataFolder().mkdir();
				f.createNewFile();
				FileWriter fw = new FileWriter(f);
				fw.write("spawn_interval: 600\n");
				fw.write("auto_spawn_min_player_count: 1\n");
				fw.write("overrule:\n");
				fw.write("  damaging: no\n");
				fw.write("  burning: no\n");
				fw.write("  spawning: no\n");
				fw.write("  targeting: no\n");
				fw.write("  exploding: no\n");
				fw.write("  enderman_moving_blocks: no\n");
				fw.write("  creating_portal: no\n");
				fw.write("  splitting: no\n");
				fw.write("  regrowing_wool: no\n");
				fw.write("  dying_wool: no\n");
				fw.write("  shearing: no\n");
				fw.write("  changing_block: no\n");
				fw.write("  taming: no\n");
				fw.write("  becoming_powered_creeper: no\n");
				fw.write("  becoming_pig_zombie: no\n");
				fw.close();
				config = YamlConfiguration.loadConfiguration(f);
			} 
			catch (Exception e) {e.printStackTrace();}
		}
		else
		try 
		{
			config = YamlConfiguration.loadConfiguration(f);
		}		
		catch (Exception e) {e.printStackTrace();}
	}
	
	void setup_spawns()
	{
		BukkitScheduler scheduler = getServer().getScheduler();
		scheduler.cancelTasks(this);
		boolean needs_spawner = false;
		
		for (String s : Utils.mobs)
		{
			if (config.contains(s + ".auto_spawn"))
			{
				List<Map<String, Object>> o = config.getMapList(s + ".auto_spawn");
				if (o.size() > 0)
				{
					if(spawner == null) spawner = new Mob_spawner(this);			
					for (Map<String, Object> m : o) spawner.spawns.add(new Auto_spawn(s, m, this));
					needs_spawner = true;
				}
			}
		}
		
		if (needs_spawner)
		{
			long spawn_interval = config.contains("spawn_interval") ? config.getLong("spawn_interval") * 20 : 12000L;
			scheduler.scheduleSyncRepeatingTask(this, spawner, spawn_interval, spawn_interval);
		}
		scheduler.scheduleSyncRepeatingTask(this, new Mob_purger(this), 72000, 72000);
	}

	void purge_unique()
	{
		Calendar old = Calendar.getInstance();
		old.add(Calendar.HOUR, -1);
		List<UUID> temp = new ArrayList<UUID>();
		for (UUID id : listener.mobs.keySet())
		{
			if (listener.mobs.get(id).spawned_at.before(old.getTime())) temp.add(id);
		}
		for (UUID id : temp) listener.mobs.remove(id);
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
				for (Auto_spawn a : spawner.spawns)
					if (a.shortcut.equalsIgnoreCase(args[0])) 
					{
						spawner.spawn_mobs(a, null);
						count++;
					}
				if (count == 0) sender.sendMessage(ChatColor.RED + "[Mobs] Couldn't find a shortcut with that name!");
				else sender.sendMessage(ChatColor.GREEN + "[Mobs] Mobs manually spawned!");
			}
			else for (Auto_spawn a : spawner.spawns) spawner.spawn_mobs(a, null);
			
			return true;
		}
		
		if (cmd.getName().equalsIgnoreCase("reload_mobs") && args.length == 0)
		{
			if (sender instanceof Player && !sender.hasPermission("mobs.can_reload_mobs"))
			{
				sender.sendMessage(ChatColor.RED + "[Mobs] You don't have permission to reload Mobs!");
				return true;
			}
			
			load_config();
			listener.setup(this);
			convert_mobs();
			Utils.log("Config reloaded!");
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
			
			purge_unique();
			Utils.log("Mobs purged!");
			if (sender instanceof Player) sender.sendMessage(ChatColor.GREEN + "[Mobs] Mobs purged!");
			return true;
		}
		return false;
	}
}

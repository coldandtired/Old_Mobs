package me.coldandtired.mobs;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;

import net.milkbowl.vault.economy.Economy;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
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
	FileConfiguration config;
	
	public void onDisable() 
	{
		listener = null;	
		world_guard = null;
		economy = null;
		config = null;
		spawner = null;
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
	
	@SuppressWarnings({ "rawtypes" })
	public void onEnable() 
	{
		setup_config();
		Utils.setup_utils();
		world_guard = get_world_guard();
		if (getServer().getPluginManager().getPlugin("Vault") != null) setup_economy();
		listener = new Mobs_listener(this);
        Bukkit.getServer().getPluginManager().registerEvents(this.listener, this);        
        
		try
		{
            Class[] args = new Class[3];
            args[0] = Class.class;
            args[1] = String.class;
            args[2] = int.class;
            Method a = net.minecraft.server.EntityTypes.class.getDeclaredMethod("a", args);
            a.setAccessible(true);
            a.invoke(a, Mobs_blaze.class, "Blaze", 61);
            a.invoke(a, Mobs_cave_spider.class, "CaveSpider", 59);
            a.invoke(a, Mobs_chicken.class, "Chicken", 93);
            a.invoke(a, Mobs_cow.class, "Cow", 92);
            a.invoke(a, Mobs_creeper.class, "Creeper", 50);
            a.invoke(a, Mobs_ender_dragon.class, "EnderDragon", 63);
            a.invoke(a, Mobs_enderman.class, "Enderman", 58);
            a.invoke(a, Mobs_ghast.class, "Ghast", 56);
            a.invoke(a, Mobs_giant.class, "Giant", 53);
            a.invoke(a, Mobs_magma_cube.class, "LavaSlime", 62);
            a.invoke(a, Mobs_mushroom_cow.class, "MushroomCow", 96);
            a.invoke(a, Mobs_pig.class, "Pig", 90);
            a.invoke(a, Mobs_pig_zombie.class, "PigZombie", 57);
            a.invoke(a, Mobs_sheep.class, "Sheep", 91);
            a.invoke(a, Mobs_silverfish.class, "Silverfish", 60);
            a.invoke(a, Mobs_skeleton.class, "Skeleton", 51);
            a.invoke(a, Mobs_slime.class, "Slime", 55);
            a.invoke(a, Mobs_snowman.class, "SnowMan", 97);
            a.invoke(a, Mobs_spider.class, "Spider", 52);
            a.invoke(a, Mobs_squid.class, "Squid", 94);
            a.invoke(a, Mobs_villager.class, "Villager", 120);
            a.invoke(a, Mobs_wolf.class, "Wolf", 95);
            a.invoke(a, Mobs_zombie.class, "Zombie", 54);
            
            setup_spawns();
        }
		catch (Exception e)
		{
            e.printStackTrace();
            this.setEnabled(false);
        }	
		
		for (World world : Bukkit.getWorlds())
		{
			for (Entity ee: world.getEntities()) 
			{
				if (ee instanceof LivingEntity && !listener.skipped_mobs.contains(ee.toString()) && !(ee instanceof Player))
				{
					listener.replace_mob(ee, world);
				}
			}
		}
	}

	void setup_config()
	{
		File f = new File(getDataFolder() + File.separator + "config.yml");
		if (!f.exists())
		{
			try 
			{
				if (!getDataFolder().exists()) getDataFolder().mkdir();
				f.createNewFile();
				FileWriter fw = new FileWriter(f);
				fw.write("spawn_interval: 60\n");
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
				fw.write("\n");
				fw.write("blaze:\n");
				fw.write("  general:\n");
				fw.write("  auto_spawn:\n");
				fw.write("  spawn_rules:\n");
				fw.write("  death_rules:\n");
				fw.write("\n");
				fw.write("cave_spider:\n");
				fw.write("  general:\n");
				fw.write("  auto_spawn:\n");
				fw.write("  spawn_rules:\n");
				fw.write("  death_rules:\n");
				fw.write("\n");
				fw.write("chicken:\n");
				fw.write("  general:\n");
				fw.write("  auto_spawn:\n");
				fw.write("  spawn_rules:\n");
				fw.write("  death_rules:\n");
				fw.write("\n");
				fw.write("cow:\n");
				fw.write("  general:\n");
				fw.write("  auto_spawn:\n");
				fw.write("  spawn_rules:\n");
				fw.write("  death_rules:\n");
				fw.write("\n");
				fw.write("creeper:\n");
				fw.write("  general:\n");
				fw.write("  auto_spawn:\n");
				fw.write("  spawn_rules:\n");
				fw.write("  death_rules:\n");
				fw.write("\n");
				fw.write("ender_dragon:\n");
				fw.write("  general:\n");
				fw.write("  auto_spawn:\n");
				fw.write("  spawn_rules:\n");
				fw.write("  death_rules:\n");
				fw.write("\n");
				fw.write("enderman:\n");
				fw.write("  general:\n");
				fw.write("  auto_spawn:\n");
				fw.write("  spawn_rules:\n");
				fw.write("  death_rules:\n");
				fw.write("\n");
				fw.write("ghast:\n");
				fw.write("  general:\n");
				fw.write("  auto_spawn:\n");
				fw.write("  spawn_rules:\n");
				fw.write("  death_rules:\n");
				fw.write("\n");
				fw.write("giant:\n");
				fw.write("  general:\n");
				fw.write("  auto_spawn:\n");
				fw.write("  spawn_rules:\n");
				fw.write("  death_rules:\n");
				fw.write("\n");
				fw.write("magma_cube:\n");
				fw.write("  general:\n");
				fw.write("  auto_spawn:\n");
				fw.write("  spawn_rules:\n");
				fw.write("  death_rules:\n");
				fw.write("\n");
				fw.write("mushroom_cow:\n");
				fw.write("  general:\n");
				fw.write("  auto_spawn:\n");
				fw.write("  spawn_rules:\n");
				fw.write("  death_rules:\n");
				fw.write("\n");
				fw.write("pig:\n");
				fw.write("  general:\n");
				fw.write("  auto_spawn:\n");
				fw.write("  spawn_rules:\n");
				fw.write("  death_rules:\n");
				fw.write("\n");
				fw.write("pig_zombie:\n");
				fw.write("  general:\n");
				fw.write("  auto_spawn:\n");
				fw.write("  spawn_rules:\n");
				fw.write("  death_rules:\n");
				fw.write("\n");
				fw.write("sheep:\n");
				fw.write("  general:\n");
				fw.write("  auto_spawn:\n");
				fw.write("  spawn_rules:\n");
				fw.write("  death_rules:\n");
				fw.write("\n");
				fw.write("silverfish:\n");
				fw.write("  general:\n");
				fw.write("  auto_spawn:\n");
				fw.write("  spawn_rules:\n");
				fw.write("  death_rules:\n");
				fw.write("\n");
				fw.write("skeleton:\n");
				fw.write("  general:\n");
				fw.write("  auto_spawn:\n");
				fw.write("  spawn_rules:\n");
				fw.write("  burn_rules:\n");
				fw.write("  death_rules:\n");
				fw.write("\n");
				fw.write("slime:\n");
				fw.write("  general:\n");
				fw.write("  auto_spawn:\n");
				fw.write("  spawn_rules:\n");
				fw.write("  death_rules:\n");
				fw.write("\n");
				fw.write("snowman:\n");
				fw.write("  general:\n");
				fw.write("  auto_spawn:\n");
				fw.write("  spawn_rules:\n");
				fw.write("  death_rules:\n");
				fw.write("\n");
				fw.write("spider:\n");
				fw.write("  general:\n");
				fw.write("  auto_spawn:\n");
				fw.write("  spawn_rules:\n");
				fw.write("  death_rules:\n");
				fw.write("\n");
				fw.write("squid:\n");
				fw.write("  general:\n");
				fw.write("  auto_spawn:\n");
				fw.write("  spawn_rules:\n");
				fw.write("  death_rules:\n");
				fw.write("\n");
				fw.write("villager:\n");
				fw.write("  general:\n");
				fw.write("  auto_spawn:\n");
				fw.write("  spawn_rules:\n");
				fw.write("  death_rules:\n");
				fw.write("\n");
				fw.write("wolf:\n");
				fw.write("  general:\n");
				fw.write("  auto_spawn:\n");
				fw.write("  spawn_rules:\n");
				fw.write("  death_rules:\n");
				fw.write("\n");
				fw.write("zombie:\n");
				fw.write("  general:\n");
				fw.write("  auto_spawn:\n");
				fw.write("  spawn_rules:\n");
				fw.write("  burn_rules:\n");
				fw.write("  death_rules:\n");
				fw.write("\n");
				fw.write("example_mob:\n");
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
		config = getConfig();		
	}
	
	void setup_spawns()
	{
		BukkitScheduler scheduler = getServer().getScheduler();
		scheduler.cancelTasks(this);
		boolean needs_spawner = false;
		
		if (config.contains("blaze.auto_spawn"))
		{
			List<Map<String, Object>> o = config.getMapList("blaze.auto_spawn");
			if (o.size() > 0)
			{
				if(spawner == null) spawner = new Mob_spawner(this);			
				for (Map<String, Object> m : o) spawner.spawns.add(new Auto_spawn("BLAZE", m));
				needs_spawner = true;
			}
		}
		
		if (config.contains("cave_spider.auto_spawn"))
		{
			List<Map<String, Object>> o = config.getMapList("cave_spider.auto_spawn");
			if (o.size() > 0)
			{
				if(spawner == null) spawner = new Mob_spawner(this);			
				for (Map<String, Object> m : o) spawner.spawns.add(new Auto_spawn("CAVE_SPIDER", m));
				needs_spawner = true;
			}
		}
		
		if (config.contains("chicken.auto_spawn"))
		{
			List<Map<String, Object>> o = config.getMapList("chicken.auto_spawn");
			if (o.size() > 0)
			{
				if(spawner == null) spawner = new Mob_spawner(this);			
				for (Map<String, Object> m : o) spawner.spawns.add(new Auto_spawn("CHICKEN", m));
				needs_spawner = true;
			}
		}
		
		if (config.contains("cow.auto_spawn"))
		{
			List<Map<String, Object>> o = config.getMapList("cow.auto_spawn");
			if (o.size() > 0)
			{
				if(spawner == null) spawner = new Mob_spawner(this);			
				for (Map<String, Object> m : o) spawner.spawns.add(new Auto_spawn("COW", m));
				needs_spawner = true;
			}
		}
		
		if (config.contains("creeper.auto_spawn"))
		{
			List<Map<String, Object>> o = config.getMapList("creeper.auto_spawn");
			if (o.size() > 0)
			{
				if(spawner == null) spawner = new Mob_spawner(this);			
				for (Map<String, Object> m : o) spawner.spawns.add(new Auto_spawn("CREEPER", m));
				needs_spawner = true;
			}
		}
		
		if (config.contains("ender_dragon.auto_spawn"))
		{
			List<Map<String, Object>> o = config.getMapList("ender_dragon.auto_spawn");
			if (o.size() > 0)
			{
				if(spawner == null) spawner = new Mob_spawner(this);			
				for (Map<String, Object> m : o) spawner.spawns.add(new Auto_spawn("ENDER_DRAGON", m));
				needs_spawner = true;
			}
		}
		
		if (config.contains("enderman.auto_spawn"))
		{
			List<Map<String, Object>> o = config.getMapList("enderman.auto_spawn");
			if (o.size() > 0)
			{
				if(spawner == null) spawner = new Mob_spawner(this);			
				for (Map<String, Object> m : o) spawner.spawns.add(new Auto_spawn("ENDERMAN", m));
				needs_spawner = true;
			}
		}
		
		if (config.contains("ghast.auto_spawn"))
		{
			List<Map<String, Object>> o = config.getMapList("ghast.auto_spawn");
			if (o.size() > 0)
			{
				if(spawner == null) spawner = new Mob_spawner(this);			
				for (Map<String, Object> m : o) spawner.spawns.add(new Auto_spawn("GHAST", m));
				needs_spawner = true;
			}
		}
		
		if (config.contains("giant.auto_spawn"))
		{
			List<Map<String, Object>> o = config.getMapList("giant.auto_spawn");
			if (o.size() > 0)
			{
				if(spawner == null) spawner = new Mob_spawner(this);			
				for (Map<String, Object> m : o) spawner.spawns.add(new Auto_spawn("GIANT", m));
				needs_spawner = true;
			}
		}
		
		if (config.contains("magma_cube.auto_spawn"))
		{
			List<Map<String, Object>> o = config.getMapList("magma_cube.auto_spawn");
			if (o.size() > 0)
			{
				if(spawner == null) spawner = new Mob_spawner(this);			
				for (Map<String, Object> m : o) spawner.spawns.add(new Auto_spawn("MAGMA_CUBE", m));
				needs_spawner = true;
			}
		}
		
		if (config.contains("mushroom_cow.auto_spawn"))
		{
			List<Map<String, Object>> o = config.getMapList("mushroom_cow.auto_spawn");
			if (o.size() > 0)
			{
				if(spawner == null) spawner = new Mob_spawner(this);			
				for (Map<String, Object> m : o) spawner.spawns.add(new Auto_spawn("MUSHROOM_COW", m));
				needs_spawner = true;
			}
		}
		
		if (config.contains("pig_zombie.auto_spawn"))
		{
			List<Map<String, Object>> o = config.getMapList("pig_zombie.auto_spawn");
			if (o.size() > 0)
			{
				if(spawner == null) spawner = new Mob_spawner(this);			
				for (Map<String, Object> m : o) spawner.spawns.add(new Auto_spawn("PIG_ZOMBIE", m));
				needs_spawner = true;
			}
		}
		
		if (config.contains("pig.auto_spawn"))
		{
			List<Map<String, Object>> o = config.getMapList("pig.auto_spawn");
			if (o.size() > 0)
			{
				if(spawner == null) spawner = new Mob_spawner(this);			
				for (Map<String, Object> m : o) spawner.spawns.add(new Auto_spawn("PIG", m));
				needs_spawner = true;
			}
		}
		
		if (config.contains("sheep.auto_spawn"))
		{
			List<Map<String, Object>> o = config.getMapList("sheep.auto_spawn");
			if (o.size() > 0)
			{
				if(spawner == null) spawner = new Mob_spawner(this);			
				for (Map<String, Object> m : o) spawner.spawns.add(new Auto_spawn("SHEEP", m));
				needs_spawner = true;
			}
		}
		
		if (config.contains("silverfish.auto_spawn"))
		{
			List<Map<String, Object>> o = config.getMapList("silverfish.auto_spawn");
			if (o.size() > 0)
			{
				if(spawner == null) spawner = new Mob_spawner(this);			
				for (Map<String, Object> m : o) spawner.spawns.add(new Auto_spawn("SILVERFISH", m));
				needs_spawner = true;
			}
		}
		
		if (config.contains("skeleton.auto_spawn"))
		{
			List<Map<String, Object>> o = config.getMapList("skeleton.auto_spawn");
			if (o.size() > 0)
			{
				if(spawner == null) spawner = new Mob_spawner(this);			
				for (Map<String, Object> m : o) spawner.spawns.add(new Auto_spawn("SKELETON", m));
				needs_spawner = true;
			}
		}
		
		if (config.contains("slime.auto_spawn"))
		{
			List<Map<String, Object>> o = config.getMapList("slime.auto_spawn");
			if (o.size() > 0)
			{
				if(spawner == null) spawner = new Mob_spawner(this);			
				for (Map<String, Object> m : o) spawner.spawns.add(new Auto_spawn("SLIME", m));
				needs_spawner = true;
			}
		}
		
		if (config.contains("snowman.auto_spawn"))
		{
			List<Map<String, Object>> o = config.getMapList("snowman.auto_spawn");
			if (o.size() > 0)
			{
				if(spawner == null) spawner = new Mob_spawner(this);			
				for (Map<String, Object> m : o) spawner.spawns.add(new Auto_spawn("SNOWMAN", m));
				needs_spawner = true;
			}
		}
		
		if (config.contains("spider.auto_spawn"))
		{
			List<Map<String, Object>> o = config.getMapList("spider.auto_spawn");
			if (o.size() > 0)
			{
				if(spawner == null) spawner = new Mob_spawner(this);			
				for (Map<String, Object> m : o) spawner.spawns.add(new Auto_spawn("SPIDER", m));
				needs_spawner = true;
			}
		}
		
		if (config.contains("squid.auto_spawn"))
		{
			List<Map<String, Object>> o = config.getMapList("squid.auto_spawn");
			if (o.size() > 0)
			{
				if(spawner == null) spawner = new Mob_spawner(this);			
				for (Map<String, Object> m : o) spawner.spawns.add(new Auto_spawn("SQUID", m));
				needs_spawner = true;
			}
		}
		
		if (config.contains("villager.auto_spawn"))
		{
			List<Map<String, Object>> o = config.getMapList("villager.auto_spawn");
			if (o.size() > 0)
			{
				if(spawner == null) spawner = new Mob_spawner(this);			
				for (Map<String, Object> m : o) spawner.spawns.add(new Auto_spawn("VILLAGER", m));
				needs_spawner = true;
			}
		}
		
		if (config.contains("wolf.auto_spawn"))
		{
			List<Map<String, Object>> o = config.getMapList("wolf.auto_spawn");
			if (o.size() > 0)
			{
				if(spawner == null) spawner = new Mob_spawner(this);			
				for (Map<String, Object> m : o) spawner.spawns.add(new Auto_spawn("WOLF", m));
				needs_spawner = true;
			}
		}		
		
		if (config.contains("zombie.auto_spawn"))
		{
			List<Map<String, Object>> o = config.getMapList("zombie.auto_spawn");
			if (o.size() > 0)
			{
				if(spawner == null) spawner = new Mob_spawner(this);			
				for (Map<String, Object> m : o) spawner.spawns.add(new Auto_spawn("ZOMBIE", m));
				needs_spawner = true;
			}
		}
		
		if (needs_spawner)
		{
			long spawn_interval = config.contains("spawn_interval") ? config.getLong("spawn_interval") * 20 : 1200L;
			scheduler.scheduleSyncRepeatingTask(this, spawner, spawn_interval, spawn_interval);
		}
	}

	public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args)
	{		
		if (cmd.getName().equalsIgnoreCase("spawn_mob") && args.length < 2)
		{
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
			}
			else for (Auto_spawn a : spawner.spawns) spawner.spawn_mobs(a, null);
			
			return true;
		}
		return false;
	}
}

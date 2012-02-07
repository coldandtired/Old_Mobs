package me.coldandtired.mobs;

import java.util.ArrayList;
import java.util.Map;
import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.configuration.MemorySection;
import org.bukkit.craftbukkit.CraftWorld;
import org.bukkit.craftbukkit.entity.CraftEntity;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.CreatureType;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Fireball;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.CreeperPowerEvent;
import org.bukkit.event.entity.EndermanPickupEvent;
import org.bukkit.event.entity.EntityChangeBlockEvent;
import org.bukkit.event.entity.EntityCombustEvent;
import org.bukkit.event.entity.CreatureSpawnEvent.SpawnReason;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.entity.EntityCreatePortalEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.event.entity.EntityTameEvent;
import org.bukkit.event.entity.EntityTargetEvent;
import org.bukkit.event.entity.ExplosionPrimeEvent;
import org.bukkit.event.entity.ItemDespawnEvent;
import org.bukkit.event.entity.PigZapEvent;
import org.bukkit.event.entity.SheepDyeWoolEvent;
import org.bukkit.event.entity.SheepRegrowWoolEvent;
import org.bukkit.event.entity.SlimeSplitEvent;
import org.bukkit.event.player.PlayerShearEntityEvent;
import org.bukkit.event.world.ChunkLoadEvent;
import org.bukkit.inventory.ItemStack;

public class General_listener implements Listener
{
	Main plugin;
	Map<String, Object> unique = null;
	ArrayList<String> tracked_mobs = new ArrayList<String>();
	Random rng = new Random();
	boolean overrule_damage = false;
	boolean overrule_burn = false;
	boolean overrule_spawn = false;
	boolean overrule_target = false;
	boolean overrule_explode = false;
	boolean overrule_enderman_pickup = false;
	boolean overrule_create_portal = false;
	boolean overrule_split = false;
	boolean overrule_regrow_wool = false;
	boolean overrule_dye_wool = false;
	boolean overrule_shear = false;
	boolean overrule_change_block = false;
	boolean overrule_become_powered_creeper = false;
	boolean overrule_tame = false;
	boolean overrule_become_pig_zombie = false;
	boolean allow = true;
	
	General_listener(Main plugin)
	{
		this.plugin = plugin;
		overrule_damage = plugin.config.getBoolean("overrule.damaging", false);
		overrule_burn = plugin.config.getBoolean("overrule.burning", false);
		overrule_spawn = plugin.config.getBoolean("overrule.spawning", false);
		overrule_target = plugin.config.getBoolean("overrule.targeting", false);
		overrule_explode = plugin.config.getBoolean("overrule.exploding", false);
		overrule_enderman_pickup = plugin.config.getBoolean("overrule.enderman_moving_blocks", false);
		overrule_create_portal = plugin.config.getBoolean("overrule.creating_portal", false);
		overrule_split = plugin.config.getBoolean("overrule.splitting", false);
		overrule_regrow_wool = plugin.config.getBoolean("overrule.regrowing_wool", false);
		overrule_dye_wool = plugin.config.getBoolean("overrule.dying_wool", false);
		overrule_shear = plugin.config.getBoolean("overrule.shearing", false);
		overrule_change_block = plugin.config.getBoolean("overrule.changing_block", false);
		overrule_tame = plugin.config.getBoolean("overrule.taming", false);
		overrule_become_powered_creeper = plugin.config.getBoolean("overrule.becoming_powered_creeper", false);		
		overrule_become_pig_zombie = plugin.config.getBoolean("overrule.becoming_pig_zombie", false);
		
		MemorySection ms = (MemorySection)plugin.config.get("blaze");
		if (ms != null) 
		{
			if (!(ms.get("general") == null && ms.get("auto_spawn") == null && ms.get("spawn_rules") == null
					&& ms.get("burn_rules") == null && ms.get("death_rules") == null)) tracked_mobs.add("CraftBlaze");
		}
		
		ms = (MemorySection)plugin.config.get("cave_spider");
		if (ms != null) 
		{
			if (!(ms.get("general") == null && ms.get("auto_spawn") == null && ms.get("spawn_rules") == null
					&& ms.get("burn_rules") == null && ms.get("death_rules") == null)) tracked_mobs.add("CraftCaveSpider");
		}
		
		ms = (MemorySection)plugin.config.get("chicken");
		if (ms != null) 
		{
			if (!(ms.get("general") == null && ms.get("auto_spawn") == null && ms.get("spawn_rules") == null
					&& ms.get("burn_rules") == null && ms.get("death_rules") == null)) tracked_mobs.add("CraftChicken");
		}
		
		ms = (MemorySection)plugin.config.get("cow");
		if (ms != null) 
		{
			if (!(ms.get("general") == null && ms.get("auto_spawn") == null && ms.get("spawn_rules") == null
					&& ms.get("burn_rules") == null && ms.get("death_rules") == null)) tracked_mobs.add("CraftCow");
		}
		
		ms = (MemorySection)plugin.config.get("creeper");
		if (ms != null) 
		{
			if (!(ms.get("general") == null && ms.get("auto_spawn") == null && ms.get("spawn_rules") == null
					&& ms.get("burn_rules") == null && ms.get("death_rules") == null)) tracked_mobs.add("CraftCreeper");
		}
		
		ms = (MemorySection)plugin.config.get("ender_dragon");
		if (ms != null) 
		{
			if (!(ms.get("general") == null && ms.get("auto_spawn") == null && ms.get("spawn_rules") == null
					&& ms.get("burn_rules") == null && ms.get("death_rules") == null)) tracked_mobs.add("CraftEnderDragon");
		}
		
		ms = (MemorySection)plugin.config.get("enderman");
		if (ms != null) 
		{
			if (!(ms.get("general") == null && ms.get("auto_spawn") == null && ms.get("spawn_rules") == null
					&& ms.get("burn_rules") == null && ms.get("death_rules") == null)) tracked_mobs.add("CraftEnderman");
		}
		
		ms = (MemorySection)plugin.config.get("ghast");
		if (ms != null) 
		{
			if (!(ms.get("general") == null && ms.get("auto_spawn") == null && ms.get("spawn_rules") == null
					&& ms.get("burn_rules") == null && ms.get("death_rules") == null)) tracked_mobs.add("CraftGhast");
		}
		
		ms = (MemorySection)plugin.config.get("giant");
		if (ms != null) 
		{
			if (!(ms.get("general") == null && ms.get("auto_spawn") == null && ms.get("spawn_rules") == null
					&& ms.get("burn_rules") == null && ms.get("death_rules") == null)) tracked_mobs.add("CraftGiant");
		}
		
		ms = (MemorySection)plugin.config.get("magma_cube");
		if (ms != null) 
		{
			if (!(ms.get("general") == null && ms.get("auto_spawn") == null && ms.get("spawn_rules") == null
					&& ms.get("burn_rules") == null && ms.get("death_rules") == null)) tracked_mobs.add("CraftMagmaCube");
		}
		
		ms = (MemorySection)plugin.config.get("mushroom_cow");
		if (ms != null) 
		{
			if (!(ms.get("general") == null && ms.get("auto_spawn") == null && ms.get("spawn_rules") == null
					&& ms.get("burn_rules") == null && ms.get("death_rules") == null)) tracked_mobs.add("CraftMushroomCow");
		}
		
		ms = (MemorySection)plugin.config.get("pig");
		if (ms != null) 
		{
			if (!(ms.get("general") == null && ms.get("auto_spawn") == null && ms.get("spawn_rules") == null
					&& ms.get("burn_rules") == null && ms.get("death_rules") == null)) tracked_mobs.add("CraftPig");
		}
		
		ms = (MemorySection)plugin.config.get("pig_zombie");
		if (ms != null) 
		{
			if (!(ms.get("general") == null && ms.get("auto_spawn") == null && ms.get("spawn_rules") == null
					&& ms.get("burn_rules") == null && ms.get("death_rules") == null)) tracked_mobs.add("CraftPigZombie");
		}
		
		ms = (MemorySection)plugin.config.get("sheep");
		if (ms != null) 
		{
			if (!(ms.get("general") == null && ms.get("auto_spawn") == null && ms.get("spawn_rules") == null
					&& ms.get("burn_rules") == null && ms.get("death_rules") == null)) tracked_mobs.add("CraftSheep");
		}
		
		ms = (MemorySection)plugin.config.get("silverfish");
		if (ms != null) 
		{
			if (!(ms.get("general") == null && ms.get("auto_spawn") == null && ms.get("spawn_rules") == null
					&& ms.get("burn_rules") == null && ms.get("death_rules") == null)) tracked_mobs.add("CraftSilverfish");
		}
		
		ms = (MemorySection)plugin.config.get("skeleton");
		if (ms != null) 
		{
			if (!(ms.get("general") == null && ms.get("auto_spawn") == null && ms.get("spawn_rules") == null
					&& ms.get("burn_rules") == null && ms.get("death_rules") == null)) tracked_mobs.add("CraftSkeleton");
		}
		
		ms = (MemorySection)plugin.config.get("slime");
		if (ms != null) 
		{
			if (!(ms.get("general") == null && ms.get("auto_spawn") == null && ms.get("spawn_rules") == null
					&& ms.get("burn_rules") == null && ms.get("death_rules") == null)) tracked_mobs.add("CraftSlime");
		}
		
		ms = (MemorySection)plugin.config.get("snowman");
		if (ms != null) 
		{
			if (!(ms.get("general") == null && ms.get("auto_spawn") == null && ms.get("spawn_rules") == null
					&& ms.get("burn_rules") == null && ms.get("death_rules") == null)) tracked_mobs.add("CraftSnowman");
		}
		
		ms = (MemorySection)plugin.config.get("spider");
		if (ms != null) 
		{
			if (!(ms.get("general") == null && ms.get("auto_spawn") == null && ms.get("spawn_rules") == null
					&& ms.get("burn_rules") == null && ms.get("death_rules") == null)) tracked_mobs.add("CraftSpider");
		}
		
		ms = (MemorySection)plugin.config.get("squid");
		if (ms != null) 
		{
			if (!(ms.get("general") == null && ms.get("auto_spawn") == null && ms.get("spawn_rules") == null
					&& ms.get("burn_rules") == null && ms.get("death_rules") == null)) tracked_mobs.add("CraftSquid");
		}		
		
		ms = (MemorySection)plugin.config.get("villager");		
		if (ms != null) 
		{
			if (!(ms.get("general") == null && ms.get("auto_spawn") == null && ms.get("spawn_rules") == null
					&& ms.get("burn_rules") == null && ms.get("death_rules") == null)) tracked_mobs.add("CraftVillager");
		}
		
		ms = (MemorySection)plugin.config.get("wolf");
		if (ms != null) 
		{
			if (!(ms.get("general") == null && ms.get("auto_spawn") == null && ms.get("spawn_rules") == null
					&& ms.get("burn_rules") == null && ms.get("death_rules") == null)) tracked_mobs.add("CraftWolf");
		}
		
		ms = (MemorySection)plugin.config.get("zombie");
		if (ms != null) 
		{
			if (!(ms.get("general") == null && ms.get("auto_spawn") == null && ms.get("spawn_rules") == null
					&& ms.get("burn_rules") == null && ms.get("death_rules") == null)) tracked_mobs.add("CraftZombie");
		}
		if (tracked_mobs.size() > 0)
		{
			Bukkit.getLogger().info("[Mobs] Replaced mobs:");
			for (String s : tracked_mobs) Bukkit.getLogger().info(s.replace("Craft", ""));
		} else this.plugin.getServer().getPluginManager().disablePlugin(plugin);
	}
	
	@EventHandler(priority = EventPriority.HIGHEST)
	public void onCreatureSpawn(CreatureSpawnEvent event)
	{		
		if (event.isCancelled())
		{
			if (overrule_spawn) event.setCancelled(false); else return;
		}
		
		Entity entity = event.getEntity();
		
        Location location = event.getLocation();        
        CreatureType creatureType = event.getCreatureType();
        SpawnReason spawn_reason = event.getSpawnReason();
        //int x = location.getChunk().getX();
       // int z = location.getChunk().getZ();
        net.minecraft.server.World mcWorld = ((CraftWorld) location.getWorld()).getHandle();
        net.minecraft.server.Entity mcEntity = (((CraftEntity) entity).getHandle());

        if (creatureType == CreatureType.BLAZE)
        {
        	MemorySection ms = (MemorySection) plugin.config.get("blaze");
        	
        	if (!can_spawn(ms, (LivingEntity)entity, spawn_reason, null))
        	{
        		event.setCancelled(true);
        		return;
        	}
        	
        	Mobs_blaze blaze;
        	
        	if (mcEntity instanceof Mobs_blaze)
        	{
        		blaze = (Mobs_blaze)mcEntity;
        		blaze.setup(ms, unique, spawn_reason.name());
        	}
        	else
        	{
        		if (!tracked_mobs.contains(entity.toString())) return;
        		event.setCancelled(true);
        		blaze = new Mobs_blaze(mcWorld);
        		blaze.setPosition(location.getX(), location.getY(), location.getZ());
        		mcWorld.addEntity(blaze, spawn_reason); 
        	} 	     	         	     
            
            /*int sr = blaze.spawn_rate;
            for (int i = 0; i < sr; i++)
            {
            	blaze = new Mobs_blaze(mcWorld);
            	blaze.setup(ms, unique, spawn_reason.name());
            	Location loc = Utils.get_safe_block(entity.getWorld().getBlockAt(x + rng.nextInt(16), (int) location.getY(), z+ rng.nextInt(16)));
            	blaze.setPosition(loc.getX(), loc.getY(), loc.getZ()); 
            	
            	mcWorld.addEntity(blaze, spawn_reason);
            }*/
        	unique = null;
            return;
        }
        
        if (creatureType == CreatureType.CAVE_SPIDER)
        {
        	MemorySection ms = (MemorySection) plugin.config.get("cave_spider");
        	
        	if (!can_spawn(ms, (LivingEntity)entity, spawn_reason, null))
        	{
        		event.setCancelled(true);
        		return;
        	}
        	
        	Mobs_cave_spider cave_spider;
        	
        	if (mcEntity instanceof Mobs_cave_spider)
        	{
        		cave_spider = (Mobs_cave_spider)mcEntity;
        		cave_spider.setup(ms, unique, spawn_reason.name());
        	}
        	else
        	{
        		if (!tracked_mobs.contains(entity.toString())) return;
        		event.setCancelled(true);
        		cave_spider = new Mobs_cave_spider(mcWorld);
        		cave_spider.setPosition(location.getX(), location.getY(), location.getZ());
        		mcWorld.addEntity(cave_spider, spawn_reason); 
        	} 	     	         	     
            
           /* int sr = cave_spider.spawn_rate;
            for (int i = 0; i < sr; i++)
            {
            	cave_spider = new Mobs_cave_spider(mcWorld);
            	cave_spider.setup(ms, unique, spawn_reason.name());
            	Location loc = Utils.get_safe_block(entity.getWorld().getBlockAt(x + rng.nextInt(16), (int) location.getY(), z+ rng.nextInt(16)));
            	cave_spider.setPosition(loc.getX(), loc.getY(), loc.getZ()); 
            	
            	mcWorld.addEntity(cave_spider, spawn_reason);
            }*/
        	unique = null;
            return;
        }
        
        if (creatureType == CreatureType.CHICKEN)
        {
        	MemorySection ms = (MemorySection) plugin.config.get("chicken");
        	
        	if (!can_spawn(ms, (LivingEntity)entity, spawn_reason, null))
        	{
        		event.setCancelled(true);
        		return;
        	}
        	
        	Mobs_chicken chicken;
        	
        	if (mcEntity instanceof Mobs_chicken)
        	{
        		chicken = (Mobs_chicken)mcEntity;
        		chicken.setup(ms, unique, spawn_reason.name());
        	}
        	else
        	{
        		if (!tracked_mobs.contains(entity.toString())) return;
        		event.setCancelled(true);
        		chicken = new Mobs_chicken(mcWorld);
        		chicken.setPosition(location.getX(), location.getY(), location.getZ());
        		mcWorld.addEntity(chicken, spawn_reason); 
        	} 	     	         	     
            
            /*int sr = chicken.spawn_rate;
            for (int i = 0; i < sr; i++)
            {
            	chicken = new Mobs_chicken(mcWorld);
            	chicken.setup(ms, unique, spawn_reason.name());
            	Location loc = Utils.get_safe_block(entity.getWorld().getBlockAt(x + rng.nextInt(16), (int) location.getY(), z+ rng.nextInt(16)));
            	chicken.setPosition(loc.getX(), loc.getY(), loc.getZ()); 
            	
            	mcWorld.addEntity(chicken, spawn_reason);
            }*/
        	unique = null;
            return;
        }
        
        if (creatureType == CreatureType.COW)
        {
        	MemorySection ms = (MemorySection) plugin.config.get("cow");
        	
        	if (!can_spawn(ms, (LivingEntity)entity, spawn_reason, null))
        	{
        		event.setCancelled(true);
        		return;
        	}
        	
        	Mobs_cow cow;
        	
        	if (mcEntity instanceof Mobs_cow)
        	{
        		cow = (Mobs_cow)mcEntity;
        		cow.setup(ms, unique, spawn_reason.name());
        	}
        	else
        	{
        		if (!tracked_mobs.contains(entity.toString())) return;
        		event.setCancelled(true);
        		//entity.remove();
        		cow = new Mobs_cow(mcWorld);
        		cow.setPosition(location.getX(), location.getY(), location.getZ());
        		mcWorld.addEntity(cow, spawn_reason); 
        	} 	     	         	     
            
           /* int sr = cow.spawn_rate;
            for (int i = 0; i < sr; i++)
            {
            	cow = new Mobs_cow(mcWorld);
            	cow.setup(ms, unique, spawn_reason.name());
            	Location loc = Utils.get_safe_block(entity.getWorld().getBlockAt(x + rng.nextInt(16), (int) location.getY(), z+ rng.nextInt(16)));
            	cow.setPosition(loc.getX(), loc.getY(), loc.getZ()); 
            	
            	mcWorld.addEntity(cow, spawn_reason);
            }*/
        	unique = null;
            return;
        }
        
        if (creatureType == CreatureType.CREEPER)
        {
        	MemorySection ms = (MemorySection) plugin.config.get("creeper");
        	
        	if (!can_spawn(ms, (LivingEntity)entity, spawn_reason, null))
        	{
        		event.setCancelled(true);
        		return;
        	}
        	
        	Mobs_creeper creeper;
        	
        	if (mcEntity instanceof Mobs_creeper)
        	{
        		creeper = (Mobs_creeper)mcEntity;
        		creeper.setup(ms, unique, spawn_reason.name());
        	}
        	else
        	{
        		if (!tracked_mobs.contains(entity.toString())) return;
        		event.setCancelled(true);
        		creeper = new Mobs_creeper(mcWorld);
        		creeper.setPosition(location.getX(), location.getY(), location.getZ());
        		mcWorld.addEntity(creeper, spawn_reason); 
        	} 	     	         	     
            
            /*int sr = creeper.spawn_rate;
            for (int i = 0; i < sr; i++)
            {
            	creeper = new Mobs_creeper(mcWorld);
            	creeper.setup(ms, unique, spawn_reason.name());
            	Location loc = Utils.get_safe_block(entity.getWorld().getBlockAt(x + rng.nextInt(16), (int) location.getY(), z+ rng.nextInt(16)));
            	creeper.setPosition(loc.getX(), loc.getY(), loc.getZ()); 
            	
            	mcWorld.addEntity(creeper, spawn_reason);
            }*/
        	unique = null;
            return;
        }
        
        if (creatureType == CreatureType.ENDER_DRAGON)
        {
        	MemorySection ms = (MemorySection) plugin.config.get("ender_dragon");
        	
        	if (!can_spawn(ms, (LivingEntity)entity, spawn_reason, null))
        	{
        		event.setCancelled(true);
        		return;
        	}
        	
        	Mobs_ender_dragon ender_dragon;
        	
        	if (mcEntity instanceof Mobs_ender_dragon)
        	{
        		ender_dragon = (Mobs_ender_dragon)mcEntity;
        		ender_dragon.setup(ms, unique, spawn_reason.name());
        	}
        	else
        	{
        		if (!tracked_mobs.contains(entity.toString())) return;
        		event.setCancelled(true);
        		ender_dragon = new Mobs_ender_dragon(mcWorld);
        		ender_dragon.setPosition(location.getX(), location.getY(), location.getZ());
        		mcWorld.addEntity(ender_dragon, spawn_reason); 
        	} 	     	         	     
            
            /*int sr = ender_dragon.spawn_rate;
            for (int i = 0; i < sr; i++)
            {
            	ender_dragon = new Mobs_ender_dragon(mcWorld);
            	ender_dragon.setup(ms, unique, spawn_reason.name());
            	Location loc = Utils.get_safe_block(entity.getWorld().getBlockAt(x + rng.nextInt(16), (int) location.getY(), z+ rng.nextInt(16)));
            	ender_dragon.setPosition(loc.getX(), loc.getY(), loc.getZ()); 
            	
            	mcWorld.addEntity(ender_dragon, spawn_reason);
            }*/
        	unique = null;
            return;
        }
        
        if (creatureType == CreatureType.ENDERMAN)
        {
        	MemorySection ms = (MemorySection) plugin.config.get("enderman");
        	
        	if (!can_spawn(ms, (LivingEntity)entity, spawn_reason, null))
        	{
        		event.setCancelled(true);
        		return;
        	}
        	
        	Mobs_enderman enderman;
        	
        	if (mcEntity instanceof Mobs_enderman)
        	{
        		enderman = (Mobs_enderman)mcEntity;
        		enderman.setup(ms, unique, spawn_reason.name());
        	}
        	else
        	{
        		if (!tracked_mobs.contains(entity.toString())) return;
        		event.setCancelled(true);
        		enderman = new Mobs_enderman(mcWorld);
        		enderman.setPosition(location.getX(), location.getY(), location.getZ());
        		mcWorld.addEntity(enderman, spawn_reason); 
        	} 	     	         	     
            
           /* int sr = enderman.spawn_rate;
            for (int i = 0; i < sr; i++)
            {
            	enderman = new Mobs_enderman(mcWorld);
            	enderman.setup(ms, unique, spawn_reason.name());
            	Location loc = Utils.get_safe_block(entity.getWorld().getBlockAt(x + rng.nextInt(16), (int) location.getY(), z+ rng.nextInt(16)));
            	enderman.setPosition(loc.getX(), loc.getY(), loc.getZ()); 
            	
            	mcWorld.addEntity(enderman, spawn_reason);
            }*/
        	unique = null;
            return;
        }
        
        if (creatureType == CreatureType.GHAST)
        {
        	MemorySection ms = (MemorySection) plugin.config.get("ghast");
        	
        	if (!can_spawn(ms, (LivingEntity)entity, spawn_reason, null))
        	{
        		event.setCancelled(true);
        		return;
        	}
        	
        	Mobs_ghast ghast;
        	
        	if (mcEntity instanceof Mobs_ghast)
        	{
        		ghast = (Mobs_ghast)mcEntity;
        		ghast.setup(ms, unique, spawn_reason.name());
        	}
        	else
        	{
        		if (!tracked_mobs.contains(entity.toString())) return;
        		event.setCancelled(true);
        		ghast = new Mobs_ghast(mcWorld);
        		ghast.setPosition(location.getX(), location.getY(), location.getZ());
        		mcWorld.addEntity(ghast, spawn_reason); 
        	} 	     	         	     
            
          /*  int sr = ghast.spawn_rate;
            for (int i = 0; i < sr; i++)
            {
            	ghast = new Mobs_ghast(mcWorld);
            	ghast.setup(ms, unique, spawn_reason.name());
            	Location loc = Utils.get_safe_block(entity.getWorld().getBlockAt(x + rng.nextInt(16), (int) location.getY(), z+ rng.nextInt(16)));
            	ghast.setPosition(loc.getX(), loc.getY(), loc.getZ()); 
            	
            	mcWorld.addEntity(ghast, spawn_reason);
            }*/
        	unique = null;
            return;
        }
        
        if (creatureType == CreatureType.GIANT)
        {
        	MemorySection ms = (MemorySection) plugin.config.get("giant");
        	
        	if (!can_spawn(ms, (LivingEntity)entity, spawn_reason, null))
        	{
        		event.setCancelled(true);
        		return;
        	}
        	
        	Mobs_giant giant;
        	
        	if (mcEntity instanceof Mobs_giant)
        	{
        		giant = (Mobs_giant)mcEntity;
        		giant.setup(ms, unique, spawn_reason.name());
        	}
        	else
        	{
        		if (!tracked_mobs.contains(entity.toString())) return;
        		event.setCancelled(true);
        		giant = new Mobs_giant(mcWorld);
        		giant.setPosition(location.getX(), location.getY(), location.getZ());
        		mcWorld.addEntity(giant, spawn_reason); 
        	} 	     	         	     
            
           /* int sr = giant.spawn_rate;
            for (int i = 0; i < sr; i++)
            {
            	giant = new Mobs_giant(mcWorld);
            	giant.setup(ms, unique, spawn_reason.name());
            	Location loc = Utils.get_safe_block(entity.getWorld().getBlockAt(x + rng.nextInt(16), (int) location.getY(), z+ rng.nextInt(16)));
            	giant.setPosition(loc.getX(), loc.getY(), loc.getZ()); 
            	
            	mcWorld.addEntity(giant, spawn_reason);
            }*/
        	unique = null;
            return;
        }
        
        if (creatureType == CreatureType.MAGMA_CUBE)
        {
        	MemorySection ms = (MemorySection) plugin.config.get("magma_cube");
        	
        	if (!can_spawn(ms, (LivingEntity)entity, spawn_reason, null))
        	{
        		event.setCancelled(true);
        		return;
        	}
        	
        	Mobs_magma_cube magma_cube;
        	
        	if (mcEntity instanceof Mobs_magma_cube)
        	{
        		magma_cube = (Mobs_magma_cube)mcEntity;
        		magma_cube.setup(ms, unique, spawn_reason.name());
        	}
        	else
        	{
        		if (!tracked_mobs.contains(entity.toString())) return;
        		event.setCancelled(true);
        		magma_cube = new Mobs_magma_cube(mcWorld);
        		magma_cube.setPosition(location.getX(), location.getY(), location.getZ());
        		mcWorld.addEntity(magma_cube, spawn_reason); 
        	} 	     	         	     
            
        	/*int sr = magma_cube.spawn_rate;
            for (int i = 0; i < sr; i++)
            {
            	magma_cube = new Mobs_magma_cube(mcWorld);
            	magma_cube.setup(ms, unique, spawn_reason.name());
            	Location loc = Utils.get_safe_block(entity.getWorld().getBlockAt(x + rng.nextInt(16), (int) location.getY(), z+ rng.nextInt(16)));
            	magma_cube.setPosition(loc.getX(), loc.getY(), loc.getZ()); 
            	
            	mcWorld.addEntity(magma_cube, spawn_reason);
            }*/
        	unique = null;
            return;
        }
        
        if (creatureType == CreatureType.MUSHROOM_COW)
        {
        	MemorySection ms = (MemorySection) plugin.config.get("mushroom_cow");
        	
        	if (!can_spawn(ms, (LivingEntity)entity, spawn_reason, null))
        	{
        		event.setCancelled(true);
        		return;
        	}
        	
        	Mobs_mushroom_cow mushroom_cow;
        	
        	if (mcEntity instanceof Mobs_mushroom_cow)
        	{
        		mushroom_cow = (Mobs_mushroom_cow)mcEntity;
        		mushroom_cow.setup(ms, unique, spawn_reason.name());
        	}
        	else
        	{
        		if (!tracked_mobs.contains(entity.toString())) return;
        		event.setCancelled(true);
        		mushroom_cow = new Mobs_mushroom_cow(mcWorld);
        		mushroom_cow.setPosition(location.getX(), location.getY(), location.getZ());
        		mcWorld.addEntity(mushroom_cow, spawn_reason); 
        	} 	     	         	     
            
        	/*int sr = mushroom_cow.spawn_rate;
            for (int i = 0; i < sr; i++)
            {
            	mushroom_cow = new Mobs_mushroom_cow(mcWorld);
            	mushroom_cow.setup(ms, unique, spawn_reason.name());
            	Location loc = Utils.get_safe_block(entity.getWorld().getBlockAt(x + rng.nextInt(16), (int) location.getY(), z+ rng.nextInt(16)));
            	mushroom_cow.setPosition(loc.getX(), loc.getY(), loc.getZ()); 
            	
            	mcWorld.addEntity(mushroom_cow, spawn_reason);
            }*/
        	unique = null;
            return;
        }
        
        if (creatureType == CreatureType.PIG)
        {
        	MemorySection ms = (MemorySection) plugin.config.get("pig");
        	
        	if (!can_spawn(ms, (LivingEntity)entity, spawn_reason, null))
        	{
        		event.setCancelled(true);
        		return;
        	}
        	
        	Mobs_pig pig = null;
        	
        	if (mcEntity instanceof Mobs_pig)
        	{
        		pig = (Mobs_pig)mcEntity;
        		pig.setup(ms, unique, spawn_reason.name());
        	}
        	else
        	{
        		if (!tracked_mobs.contains(entity.toString())) return;
        		event.setCancelled(true);
        		pig = new Mobs_pig(mcWorld);
            	pig.setPosition(location.getX(), location.getY(), location.getZ());
        		mcWorld.addEntity(pig, spawn_reason);
        	}  	         	                
        	
        	/*if (allow)
        	{
        		allow = false;
        		int sr = pig.spawn_rate;
        		for (int i = 0; i < sr; i++)
        		{
        			pig = new Mobs_pig(mcWorld);
        			pig.setup(ms, unique, spawn_reason.name());
        			Location loc = Utils.get_safe_block(entity.getWorld().getBlockAt(x + rng.nextInt(16), (int) location.getY(), z + rng.nextInt(16)));
        			pig.setPosition(loc.getX(), loc.getY(), loc.getZ()); 
            	
        			mcWorld.addEntity(pig, spawn_reason);
        		}
        		allow = true;
        	}*/
            unique = null;
            return;
        }
        
        if (creatureType == CreatureType.PIG_ZOMBIE)
        {
        	MemorySection ms = (MemorySection) plugin.config.get("pig_zombie");
        	
        	if (!can_spawn(ms, (LivingEntity)entity, spawn_reason, null))
        	{
        		event.setCancelled(true);
        		return;
        	}
        	
        	Mobs_pig_zombie pig_zombie;
        	
        	if (mcEntity instanceof Mobs_pig_zombie)
        	{
        		pig_zombie = (Mobs_pig_zombie)mcEntity;
        		pig_zombie.setup(ms, unique, spawn_reason.name());
        	}
        	else
        	{
        		if (!tracked_mobs.contains(entity.toString())) return;
        		event.setCancelled(true);
        		pig_zombie = new Mobs_pig_zombie(mcWorld);
        		pig_zombie.setPosition(location.getX(), location.getY(), location.getZ());
        		mcWorld.addEntity(pig_zombie, spawn_reason); 
        	} 	     	         	     
            
           /* int sr = pig_zombie.spawn_rate;
            for (int i = 0; i < sr; i++)
            {
            	pig_zombie = new Mobs_pig_zombie(mcWorld);
            	pig_zombie.setup(ms, unique, spawn_reason.name());
            	Location loc = Utils.get_safe_block(entity.getWorld().getBlockAt(x + rng.nextInt(16), (int) location.getY(), z+ rng.nextInt(16)));
            	pig_zombie.setPosition(loc.getX(), loc.getY(), loc.getZ()); 
            	
            	mcWorld.addEntity(pig_zombie, spawn_reason);
            }*/
        	unique = null;
            return;
        }
        
        if (creatureType == CreatureType.SHEEP)
        {
        	MemorySection ms = (MemorySection) plugin.config.get("sheep");
        	
        	if (!can_spawn(ms, (LivingEntity)entity, spawn_reason, null))
        	{
        		event.setCancelled(true);
        		return;
        	}
        	
        	Mobs_sheep sheep;
        	
        	if (mcEntity instanceof Mobs_sheep)
        	{
        		sheep = (Mobs_sheep)mcEntity;
        		sheep.setup(ms, unique, spawn_reason.name());
        	}
        	else
        	{
        		if (!tracked_mobs.contains(entity.toString())) return;
        		event.setCancelled(true);
        		sheep = new Mobs_sheep(mcWorld);
        		sheep.setPosition(location.getX(), location.getY(), location.getZ());
        		mcWorld.addEntity(sheep, spawn_reason);
        		//sheep.addEffect(new MobEffect("potion.moveSpeed", -1, 4));
        	} 	     	         	     
            
          /*  int sr = sheep.spawn_rate;
            for (int i = 0; i < sr; i++)
            {
            	sheep = new Mobs_sheep(mcWorld);
            	sheep.setup(ms, unique, spawn_reason.name());
            	Location loc = Utils.get_safe_block(entity.getWorld().getBlockAt(x + rng.nextInt(16), (int) location.getY(), z+ rng.nextInt(16)));
            	sheep.setPosition(loc.getX(), loc.getY(), loc.getZ()); 
            	
            	mcWorld.addEntity(sheep, spawn_reason);
            }*/
        	unique = null;
            return;
        }
        
        if (creatureType == CreatureType.SILVERFISH)
        {
        	MemorySection ms = (MemorySection) plugin.config.get("silverfish");
        	
        	if (!can_spawn(ms, (LivingEntity)entity, spawn_reason, null))
        	{
        		event.setCancelled(true);
        		return;
        	}
        	
        	Mobs_silverfish silverfish;
        	
        	if (mcEntity instanceof Mobs_silverfish)
        	{
        		silverfish = (Mobs_silverfish)mcEntity;
        		silverfish.setup(ms, unique, spawn_reason.name());
        	}
        	else
        	{
        		if (!tracked_mobs.contains(entity.toString())) return;
        		event.setCancelled(true);
        		silverfish = new Mobs_silverfish(mcWorld);
        		silverfish.setPosition(location.getX(), location.getY(), location.getZ());
        		mcWorld.addEntity(silverfish, spawn_reason); 
        	} 	     	         	     
            
            /*int sr = silverfish.spawn_rate;
            for (int i = 0; i < sr; i++)
            {
            	silverfish = new Mobs_silverfish(mcWorld);
            	silverfish.setup(ms, unique, spawn_reason.name());
            	Location loc = Utils.get_safe_block(entity.getWorld().getBlockAt(x + rng.nextInt(16), (int) location.getY(), z+ rng.nextInt(16)));
            	silverfish.setPosition(loc.getX(), loc.getY(), loc.getZ()); 
            	
            	mcWorld.addEntity(silverfish, spawn_reason);
            }*/
        	unique = null;
            return;
        }
        
        if (creatureType == CreatureType.SKELETON)
        {        	
        	MemorySection ms = (MemorySection) plugin.config.get("skeleton");
        	
        	if (!can_spawn(ms, (LivingEntity)entity, spawn_reason, null))
        	{
        		event.setCancelled(true);
        		return;
        	}
        	
        	Mobs_skeleton skeleton;
        	
        	if (mcEntity instanceof Mobs_skeleton)
        	{
        		skeleton = (Mobs_skeleton)mcEntity;
        		skeleton.setup(ms, unique, spawn_reason.name());
        	}
        	else
        	{
        		if (!tracked_mobs.contains(entity.toString())) return;
        		event.setCancelled(true);
        		skeleton = new Mobs_skeleton(mcWorld);
        		skeleton.setPosition(location.getX(), location.getY(), location.getZ());
        		mcWorld.addEntity(skeleton, spawn_reason); 
        	} 	     	         	     
            
          /*  int sr = skeleton.spawn_rate;
            for (int i = 0; i < sr; i++)
            {
            	skeleton = new Mobs_skeleton(mcWorld);
            	skeleton.setup(ms, unique, spawn_reason.name());
            	Location loc = Utils.get_safe_block(entity.getWorld().getBlockAt(x + rng.nextInt(16), (int) location.getY(), z+ rng.nextInt(16)));
            	skeleton.setPosition(loc.getX(), loc.getY(), loc.getZ()); 
            	
            	mcWorld.addEntity(skeleton, spawn_reason);
            }*/
        	unique = null;
            return;
        }
        
        if (creatureType == CreatureType.SLIME)
        {
        	MemorySection ms = (MemorySection) plugin.config.get("slime");
        	
        	if (!can_spawn(ms, (LivingEntity)entity, spawn_reason, null))
        	{
        		event.setCancelled(true);
        		return;
        	}
        	
        	Mobs_slime slime;
        	
        	if (mcEntity instanceof Mobs_slime)
        	{
        		slime = (Mobs_slime)mcEntity;
        		slime.setup(ms, unique, spawn_reason.name());
        	}
        	else
        	{
        		if (!tracked_mobs.contains(entity.toString())) return;
        		event.setCancelled(true);
        		slime = new Mobs_slime(mcWorld);
        		slime.setPosition(location.getX(), location.getY(), location.getZ());
        		mcWorld.addEntity(slime, spawn_reason); 
        	} 	     	         	     
            
           /* int sr = slime.spawn_rate;
            for (int i = 0; i < sr; i++)
            {
            	slime = new Mobs_slime(mcWorld);
            	slime.setup(ms, unique, spawn_reason.name());
            	Location loc = Utils.get_safe_block(entity.getWorld().getBlockAt(x + rng.nextInt(16), (int) location.getY(), z+ rng.nextInt(16)));
            	slime.setPosition(loc.getX(), loc.getY(), loc.getZ()); 
            	
            	mcWorld.addEntity(slime, spawn_reason);
            }*/
        	unique = null;
            return;
        }
        
        if (creatureType == CreatureType.SNOWMAN)
        {
        	MemorySection ms = (MemorySection) plugin.config.get("snowman");
        	
        	if (!can_spawn(ms, (LivingEntity)entity, spawn_reason, null))
        	{
        		event.setCancelled(true);
        		return;
        	}
        	
        	Mobs_snowman snowman;
        	
        	if (mcEntity instanceof Mobs_snowman)
        	{
        		snowman = (Mobs_snowman)mcEntity;
        		snowman.setup(ms, unique, spawn_reason.name());
        	}
        	else
        	{
        		if (!tracked_mobs.contains(entity.toString())) return;
        		event.setCancelled(true);
        		snowman = new Mobs_snowman(mcWorld);
        		snowman.setPosition(location.getX(), location.getY(), location.getZ());
        		mcWorld.addEntity(snowman, spawn_reason); 
        	} 	     	         	     
            
           /* int sr = snowman.spawn_rate;
            for (int i = 0; i < sr; i++)
            {
            	snowman = new Mobs_snowman(mcWorld);
            	snowman.setup(ms, unique, spawn_reason.name());
            	Location loc = Utils.get_safe_block(entity.getWorld().getBlockAt(x + rng.nextInt(16), (int) location.getY(), z+ rng.nextInt(16)));
            	snowman.setPosition(loc.getX(), loc.getY(), loc.getZ()); 
            	
            	mcWorld.addEntity(snowman, spawn_reason);
            }*/
        	unique = null;
            return;
        }
        
        if (creatureType == CreatureType.SPIDER)
        {
        	MemorySection ms = (MemorySection) plugin.config.get("spider");
        	
        	if (!can_spawn(ms, (LivingEntity)entity, spawn_reason, null))
        	{
        		event.setCancelled(true);
        		return;
        	}
        	
        	Mobs_spider spider;
        	
        	if (mcEntity instanceof Mobs_spider)
        	{
        		spider = (Mobs_spider)mcEntity;
        		spider.setup(ms, unique, spawn_reason.name());
        	}
        	else
        	{
        		if (!tracked_mobs.contains(entity.toString())) return;
        		event.setCancelled(true);
        		spider = new Mobs_spider(mcWorld);
        		spider.setPosition(location.getX(), location.getY(), location.getZ());
        		mcWorld.addEntity(spider, spawn_reason); 
        	} 	     	         	     
            
        	/*int sr = spider.spawn_rate;
            for (int i = 0; i < sr; i++)
            {
            	spider = new Mobs_spider(mcWorld);
            	spider.setup(ms, unique, spawn_reason.name());
            	Location loc = Utils.get_safe_block(entity.getWorld().getBlockAt(x + rng.nextInt(16), (int) location.getY(), z+ rng.nextInt(16)));
            	spider.setPosition(loc.getX(), loc.getY(), loc.getZ()); 
            	
            	mcWorld.addEntity(spider, spawn_reason);
            }  */
        	unique = null;
            return;
        }
        
        if (creatureType == CreatureType.SQUID)
        {
        	MemorySection ms = (MemorySection) plugin.config.get("squid");
        	
        	if (!can_spawn(ms, (LivingEntity)entity, spawn_reason, null))
        	{
        		event.setCancelled(true);
        		return;
        	}
        	
        	Mobs_squid squid;
        	
        	if (mcEntity instanceof Mobs_squid)
        	{
        		squid = (Mobs_squid)mcEntity;
        		squid.setup(ms, unique, spawn_reason.name());
        	}
        	else
        	{
        		if (!tracked_mobs.contains(entity.toString())) return;
        		event.setCancelled(true);
        		squid = new Mobs_squid(mcWorld);
        		squid.setPosition(location.getX(), location.getY(), location.getZ());
        		mcWorld.addEntity(squid, spawn_reason); 
        	} 	     	         	     
            
           /* int sr = squid.spawn_rate;
            for (int i = 0; i < sr; i++)
            {
            	squid = new Mobs_squid(mcWorld);
            	squid.setup(ms, unique, spawn_reason.name());
            	Location loc = Utils.get_safe_block(entity.getWorld().getBlockAt(x + rng.nextInt(16), (int) location.getY(), z+ rng.nextInt(16)));
            	squid.setPosition(loc.getX(), loc.getY(), loc.getZ()); 
            	
            	mcWorld.addEntity(squid, spawn_reason);
            }*/
        	unique = null;
            return;
        }
        
        if (creatureType == CreatureType.VILLAGER)
        {
        	MemorySection ms = (MemorySection) plugin.config.get("villager");
        	
        	if (!can_spawn(ms, (LivingEntity)entity, spawn_reason, null))
        	{
        		event.setCancelled(true);
        		return;
        	}
        	
        	Mobs_villager villager;
        	
        	if (mcEntity instanceof Mobs_villager)
        	{
        		villager = (Mobs_villager)mcEntity;
        		villager.setup(ms, unique, spawn_reason.name());
        	}
        	else
        	{
        		if (!tracked_mobs.contains(entity.toString())) return;
        		event.setCancelled(true);
        		villager = new Mobs_villager(mcWorld);
        		villager.setPosition(location.getX(), location.getY(), location.getZ());
        		mcWorld.addEntity(villager, spawn_reason); 
        	} 	     	         	     
            
            /*int sr = villager.spawn_rate;
            for (int i = 0; i < sr; i++)
            {
            	villager = new Mobs_villager(mcWorld);
            	villager.setup(ms, unique, spawn_reason.name());
            	Location loc = Utils.get_safe_block(entity.getWorld().getBlockAt(x + rng.nextInt(16), (int) location.getY(), z+ rng.nextInt(16)));
            	villager.setPosition(loc.getX(), loc.getY(), loc.getZ()); 
            	
            	mcWorld.addEntity(villager, spawn_reason);
            }*/
        	unique = null;
            return;
        }
        
        if (creatureType == CreatureType.WOLF)
        {
        	MemorySection ms = (MemorySection) plugin.config.get("wolf");
        	
        	if (!can_spawn(ms, (LivingEntity)entity, spawn_reason, null))
        	{
        		event.setCancelled(true);
        		return;
        	}
        	
        	Mobs_wolf wolf;
        	
        	if (mcEntity instanceof Mobs_wolf)
        	{
        		wolf = (Mobs_wolf)mcEntity;
        		wolf.setup(ms, unique, spawn_reason.name());
        	}
        	else
        	{
        		if (!tracked_mobs.contains(entity.toString())) return;
        		event.setCancelled(true);
        		wolf = new Mobs_wolf(mcWorld);
        		wolf.setPosition(location.getX(), location.getY(), location.getZ());
        		mcWorld.addEntity(wolf, spawn_reason); 
        	} 	     	         	     
            
          /*  int sr = wolf.spawn_rate;
            for (int i = 0; i < sr; i++)
            {
            	wolf = new Mobs_wolf(mcWorld);
            	wolf.setup(ms, unique, spawn_reason.name());
            	Location loc = Utils.get_safe_block(entity.getWorld().getBlockAt(x + rng.nextInt(16), (int) location.getY(), z+ rng.nextInt(16)));
            	wolf.setPosition(loc.getX(), loc.getY(), loc.getZ()); 
            	
            	mcWorld.addEntity(wolf, spawn_reason);
            }*/
        	unique = null;
            return;
        }
        
        if (creatureType == CreatureType.ZOMBIE)
        {
        	MemorySection ms = (MemorySection) plugin.config.get("zombie");
        	
        	if (!can_spawn(ms, (LivingEntity)entity, spawn_reason, null))
        	{
        		event.setCancelled(true);
        		return;
        	}
        	
        	Mobs_zombie zombie;
        	
        	if (mcEntity instanceof Mobs_zombie)
        	{
        		zombie = (Mobs_zombie)mcEntity;
        		zombie.setup(ms, unique, spawn_reason.name());
        	}
        	else
        	{
        		if (!tracked_mobs.contains(entity.toString())) return;
        		event.setCancelled(true);
        		zombie = new Mobs_zombie(mcWorld);
        		zombie.setPosition(location.getX(), location.getY(), location.getZ());
        		mcWorld.addEntity(zombie, spawn_reason); 
        	} 	     	         	     
            
           /* int sr = zombie.spawn_rate;
            for (int i = 0; i < sr; i++)
            {
            	zombie = new Mobs_zombie(mcWorld);
            	zombie.setup(ms, unique, spawn_reason.name());
            	Location loc = Utils.get_safe_block(entity.getWorld().getBlockAt(x + rng.nextInt(16), (int) location.getY(), z+ rng.nextInt(16)));
            	zombie.setPosition(loc.getX(), loc.getY(), loc.getZ()); 
            	
            	mcWorld.addEntity(zombie, spawn_reason);
            }*/
        	unique = null;
            return;
        }       
	}
	
	@EventHandler(priority = EventPriority.HIGHEST)
	public void onEntityDamage(EntityDamageEvent event)
	{
		if (event.isCancelled())
		{
			if (overrule_damage) event.setCancelled(false); else return;
		}
		
		Entity entity = event.getEntity();
		if (!tracked_mobs.contains(entity.toString())) return;
	}
	
	@EventHandler(priority = EventPriority.HIGHEST)
	public void onEntityDeath(EntityDeathEvent event)
	{		
		Entity entity = event.getEntity();
		if (!tracked_mobs.contains(entity.toString())) return;
		
		LivingEntity le = (LivingEntity)entity;
		Player p = le.getKiller();
		 
		net.minecraft.server.Entity mcEntity = (((CraftEntity)entity).getHandle());
		ArrayList<Death_action> death_actions = null;
		String spawn_reason = "";
		
		if (mcEntity instanceof Mobs_blaze)
		{
			Mobs_blaze blaze = (Mobs_blaze)mcEntity;
			death_actions = blaze.death_actions;
			spawn_reason = blaze.spawn_reason;
		}
		else if (mcEntity instanceof Mobs_cave_spider)
		{
			Mobs_cave_spider cave_spider = (Mobs_cave_spider)mcEntity;
			death_actions = cave_spider.death_actions;
			spawn_reason = cave_spider.spawn_reason;
		}
		else if (mcEntity instanceof Mobs_chicken)
		{
			Mobs_chicken chicken = (Mobs_chicken)mcEntity;
			death_actions = chicken.death_actions;
			spawn_reason = chicken.spawn_reason;
		}
		else if (mcEntity instanceof Mobs_cow)
		{
			Mobs_cow cow = (Mobs_cow)mcEntity;
			death_actions = cow.death_actions;
			spawn_reason = cow.spawn_reason;
		}
		else if (mcEntity instanceof Mobs_creeper)
		{
			Mobs_creeper creeper = (Mobs_creeper)mcEntity;
			death_actions = creeper.death_actions;
			spawn_reason = creeper.spawn_reason;
		}
		else if (mcEntity instanceof Mobs_ender_dragon)
		{
			Mobs_ender_dragon ender_dragon = (Mobs_ender_dragon)mcEntity;
			death_actions = ender_dragon.death_actions;
			spawn_reason = ender_dragon.spawn_reason;
		}
		else if (mcEntity instanceof Mobs_enderman)
		{
			Mobs_enderman enderman = (Mobs_enderman)mcEntity;
			death_actions = enderman.death_actions;
			spawn_reason = enderman.spawn_reason;
		}
		else if (mcEntity instanceof Mobs_ghast)
		{
			Mobs_ghast ghast = (Mobs_ghast)mcEntity;
			death_actions = ghast.death_actions;
			spawn_reason = ghast.spawn_reason;
		}
		else if (mcEntity instanceof Mobs_giant)
		{
			Mobs_giant giant = (Mobs_giant)mcEntity;
			death_actions = giant.death_actions;
			spawn_reason = giant.spawn_reason;
		}
		else if (mcEntity instanceof Mobs_magma_cube)
		{
			Mobs_magma_cube magma_cube = (Mobs_magma_cube)mcEntity;
			death_actions = magma_cube.death_actions;
			spawn_reason = magma_cube.spawn_reason;
		}
		else if (mcEntity instanceof Mobs_mushroom_cow)
		{
			Mobs_mushroom_cow mushroom_cow = (Mobs_mushroom_cow)mcEntity;
			death_actions = mushroom_cow.death_actions;
			spawn_reason = mushroom_cow.spawn_reason;
		}
		else if (mcEntity instanceof Mobs_pig_zombie)
		{
			Mobs_pig_zombie pig_zombie = (Mobs_pig_zombie)mcEntity;
			death_actions = pig_zombie.death_actions;
			spawn_reason = pig_zombie.spawn_reason;
		}
		else if (mcEntity instanceof Mobs_pig)
		{
			Mobs_pig pig = (Mobs_pig)mcEntity;
			death_actions = pig.death_actions;
			spawn_reason = pig.spawn_reason;
		}
		else if (mcEntity instanceof Mobs_sheep)
		{
			Mobs_sheep sheep = (Mobs_sheep)mcEntity;
			death_actions = sheep.death_actions;
			spawn_reason = sheep.spawn_reason;
		}
		else if (mcEntity instanceof Mobs_silverfish)
		{
			Mobs_silverfish silverfish = (Mobs_silverfish)mcEntity;
			death_actions = silverfish.death_actions;
			spawn_reason = silverfish.spawn_reason;
		}
		else if (mcEntity instanceof Mobs_skeleton)
		{
			Mobs_skeleton skeleton = (Mobs_skeleton)mcEntity;
			death_actions = skeleton.death_actions;
			spawn_reason = skeleton.spawn_reason;
		}
		else if (mcEntity instanceof Mobs_slime)
		{
			Mobs_slime slime = (Mobs_slime)mcEntity;
			death_actions = slime.death_actions;
			spawn_reason = slime.spawn_reason;
		}
		else if (mcEntity instanceof Mobs_snowman)
		{
			Mobs_snowman snowman = (Mobs_snowman)mcEntity;
			death_actions = snowman.death_actions;
			spawn_reason = snowman.spawn_reason;
		}
		else if (mcEntity instanceof Mobs_spider) 
		{
			Mobs_spider spider = (Mobs_spider)mcEntity;
			death_actions = spider.death_actions;
			spawn_reason = spider.spawn_reason;
		}
		else if (mcEntity instanceof Mobs_squid)
		{
			Mobs_squid squid = (Mobs_squid)mcEntity;
			death_actions = squid.death_actions;
			spawn_reason = squid.spawn_reason;
		}
		else if (mcEntity instanceof Mobs_villager)
		{
			Mobs_villager villager = (Mobs_villager)mcEntity;
			death_actions = villager.death_actions;
			spawn_reason = villager.spawn_reason;
		}
		else if (mcEntity instanceof Mobs_wolf)
		{
			Mobs_wolf wolf = (Mobs_wolf)mcEntity;
			death_actions = wolf.death_actions;
			spawn_reason = wolf.spawn_reason;
		}
		else if (mcEntity instanceof Mobs_zombie)
		{
			Mobs_zombie zombie = (Mobs_zombie)mcEntity;
			death_actions = zombie.death_actions;
			spawn_reason = zombie.spawn_reason;
		}		
		
		if (death_actions != null)
		{
			for (Death_action da : death_actions)
			{
				if (matches_condition(da.conditions, le, spawn_reason, p))
				{
					if (da.exp != null)
					{
						int exp = Utils.get_quantity(da.exp);						
						if (!da.replace_exp) exp += event.getDroppedExp();
						event.setDroppedExp(exp);
					}
					
					if (da.money != null && Main.economy != null && p != null)
					{
						Main.economy.depositPlayer(p.getName(), Utils.get_quantity(da.money));
					}
					
					if (da.items != null)
					{
						if (da.replace_items) event.getDrops().clear();
						for (Mobs_item i : da.items)
						{
							if (!i.name.equalsIgnoreCase("nothing"))
							{									
								int quantity = i.quantities != null ? Utils.get_quantity(i.quantities) : 1;	
								Material material = Material.matchMaterial(i.name);
								for (int count = 0; count < quantity; count++)
								{
									ItemStack is = null;
									if (material == null)
									{
										String s = i.name.toUpperCase();
										for (Matlist m : Utils.matlists)
										{
											if (m.names.contains(s))
											{
												is = new ItemStack(m.id, 1, (short)m.names.indexOf(s));	
												break;
											}			
										}
									} 
									else is = new ItemStack(material, 1);
									if (i.enchantments != null)
									{
										for (Enchantment e : i.enchantments.keySet())
										{
											ArrayList<Integer> tempi = i.enchantments.get(e);
											int level = tempi != null ? Utils.get_quantity(tempi) : 1;
											is.addUnsafeEnchantment(e, level);
										}
									}
									event.getDrops().add(is);
								}
							} 
							else
							{
								event.getDrops().clear();
								break;
							}
						}
					}
				}
			}
		}
	}
	
	static boolean matches_condition(ArrayList<Condition_group> conditions, LivingEntity le, String spawn_reason, Player player)
	{
		if (conditions == null || conditions.size() == 0) return true;
		
		for (Condition_group c : conditions) if (c.matches_all_conditions(le, spawn_reason, player)) return true;
		return false;
	}
	
	@EventHandler(priority = EventPriority.HIGHEST)
	public void onEntityCombust(EntityCombustEvent event)
	{
		if (event.isCancelled())
		{
			if (overrule_burn) event.setCancelled(false); else return;
		}
		
		Entity entity = event.getEntity();
		if (!tracked_mobs.contains(entity.toString())) return;
		
		net.minecraft.server.Entity mcEntity = (((CraftEntity)entity).getHandle());	
		
		if (mcEntity instanceof Mobs_skeleton)
		{
			Mobs_skeleton skeleton = (Mobs_skeleton)mcEntity;
			boolean burn = skeleton.burn;
			if (skeleton.burn_rules != null)
			{
				if (matches_condition(skeleton.burn_rules, (LivingEntity)event.getEntity(), skeleton.spawn_reason, null)) burn = !burn;
			}
			if (!burn)
			{
				event.setCancelled(true);
				return;
			}
		}
		
		if (mcEntity instanceof Mobs_zombie)
		{
			Mobs_zombie zombie = (Mobs_zombie)mcEntity;
			boolean burn = zombie.burn;
			if (zombie.burn_rules != null)
			{
				if (matches_condition(zombie.burn_rules, (LivingEntity)event.getEntity(), zombie.spawn_reason, null)) burn = !burn;
			}
			if (!burn)
			{
				event.setCancelled(true);
				return;
			}
		}
	}
	
	@SuppressWarnings("unchecked")
	boolean can_spawn(MemorySection ms, LivingEntity le, SpawnReason spawn_reason, Player player)
	{
		if (ms == null) return true;
		
		ArrayList<Condition_group> conditions = null;
		boolean def = true;
		if (unique != null)
		{
			Map<String, Object> temp = (Map<String, Object>)unique.get("spawn_rules");
			if (temp != null)
			{
				if (temp.containsKey("spawn")) def = (Boolean)temp.get("spawn");
				ArrayList<Object> conds = (ArrayList<Object>)temp.get("unless");
				if (conds != null && conds.size() > 0)
				{
					if (conditions == null) conditions = new ArrayList<Condition_group>();
					for (Object o : conds) conditions.add(new Condition_group(o));
				}
			}
		}
		else
		{
			def = ms.getBoolean("spawn_rules.spawn", true);
			ArrayList<Object> conds = (ArrayList<Object>)ms.get("spawn_rules.unless");
			if (conds != null && conds.size() > 0)
			{
				if (conditions == null) conditions = new ArrayList<Condition_group>();
				for (Object o : conds) conditions.add(new Condition_group(o));
			}
		}
		
		if (conditions == null || conditions.size() == 0) return def;
		
		for (Condition_group c : conditions) if (c.matches_all_conditions(le, spawn_reason.name(), player)) return !def;
		return def;
	}
	
	
	@EventHandler(priority = EventPriority.HIGHEST)
	public void onEntityTarget(EntityTargetEvent event)
	{
		if (event.isCancelled())
		{
			if (overrule_target) event.setCancelled(false); else return;
		}
		
		Entity entity = event.getEntity();
		if (!tracked_mobs.contains(entity.toString())) return;
		
		net.minecraft.server.Entity mcEntity = (((CraftEntity)entity).getHandle());
		
		if (mcEntity instanceof Mobs_blaze)
        {
			Mobs_blaze blaze = (Mobs_blaze)mcEntity;
        	if (blaze.safe) event.setCancelled(true);
        	return;
        }
		
		if (mcEntity instanceof Mobs_cave_spider)
        {
			Mobs_cave_spider cave_spider = (Mobs_cave_spider)mcEntity;
        	if (cave_spider.safe) event.setCancelled(true);
        	return;
        }
		
		if (mcEntity instanceof Mobs_creeper)
        {
			Mobs_creeper creeper = (Mobs_creeper)mcEntity;
        	if (creeper.safe) event.setCancelled(true);
        	return;
        }
		
		if (mcEntity instanceof Mobs_ender_dragon)
        {
			Mobs_ender_dragon ender_dragon = (Mobs_ender_dragon)mcEntity;
        	if (ender_dragon.safe) event.setCancelled(true);
        	return;
        }
		
        if (mcEntity instanceof Mobs_enderman)
        {
			Mobs_enderman enderman = (Mobs_enderman)mcEntity;
        	if (enderman.safe) event.setCancelled(true);
        	return;
        }
        
        if (mcEntity instanceof Mobs_ghast)
        {
			Mobs_ghast ghast = (Mobs_ghast)mcEntity;
        	if (ghast.safe) event.setCancelled(true);
        	return;
        }
        
        if (mcEntity instanceof Mobs_giant)
        {
			Mobs_giant giant = (Mobs_giant)mcEntity;
        	if (giant.safe) event.setCancelled(true);
        	return;
        }
        
        if (mcEntity instanceof Mobs_pig_zombie)
        {
			Mobs_pig_zombie pig_zombie = (Mobs_pig_zombie)mcEntity;
        	if (pig_zombie.safe) event.setCancelled(true);
        	return;
        }
        
        if (mcEntity instanceof Mobs_silverfish)
        {
			Mobs_silverfish silverfish = (Mobs_silverfish)mcEntity;
        	if (silverfish.safe) event.setCancelled(true);
        	return;
        }
        
        if (mcEntity instanceof Mobs_skeleton)
        {
			Mobs_skeleton skeleton = (Mobs_skeleton)mcEntity;
        	if (skeleton.safe) event.setCancelled(true);
        	return;
        }
        
        if (mcEntity instanceof Mobs_snowman)
        {
			Mobs_snowman snowman = (Mobs_snowman)mcEntity;
        	if (snowman.safe) event.setCancelled(true);
        	return;
        }
        
        if (mcEntity instanceof Mobs_spider)
        {
			Mobs_spider spider = (Mobs_spider)mcEntity;
        	if (spider.safe) event.setCancelled(true);
        	return;
        }
        
        if (mcEntity instanceof Mobs_squid)
        {
			Mobs_squid squid = (Mobs_squid)mcEntity;
        	if (squid.safe) event.setCancelled(true);
        	return;
        }
        
        if (mcEntity instanceof Mobs_wolf)
        {
			Mobs_wolf wolf = (Mobs_wolf)mcEntity;
        	if (wolf.safe) event.setCancelled(true);
        	return;
        }
        
        if (mcEntity instanceof Mobs_zombie)
        {
			Mobs_zombie zombie = (Mobs_zombie)mcEntity;
        	if (zombie.safe) event.setCancelled(true);
        	return;
        }
	}
		
	@EventHandler(priority = EventPriority.HIGHEST)
	public void onExplosionPrime(ExplosionPrimeEvent event)
	{
		if (event.isCancelled())
		{
			if (overrule_explode) event.setCancelled(false); else return;
		}
		
		Entity entity = event.getEntity();
		if (!tracked_mobs.contains(entity.toString())) return;
		
		net.minecraft.server.Entity mcEntity = (((CraftEntity)entity).getHandle());
		  
        if (mcEntity instanceof Mobs_creeper)
        {
        	Mobs_creeper creeper = (Mobs_creeper)mcEntity;
        	if (!creeper.fire_explosion) event.setFire(true);
        	if (creeper.safe) event.setCancelled(true);
        	return;
        }
        
        if (entity instanceof Fireball)
        {
        	Fireball fireball = (Fireball)entity;
        	if (fireball.getShooter() == null) return;
        	
        	net.minecraft.server.Entity mcshooter = (((CraftEntity)fireball.getShooter()).getHandle());
        	if (mcshooter instanceof Mobs_ghast)
        	{
        		Mobs_ghast ghast = (Mobs_ghast)mcshooter;
        		fireball.setIsIncendiary(ghast.fire_explosion);
        		event.setFire(ghast.fire_explosion);
        		if (ghast.safe) event.setCancelled(true);
        		return;
        	}
        }
	}
	
	@EventHandler(priority = EventPriority.HIGHEST)
	public void onEntityExplode(EntityExplodeEvent event)
	{
		if (event.isCancelled())
		{
			if (overrule_explode) event.setCancelled(false); else return;
		}
		Entity entity = event.getEntity();
		if (!tracked_mobs.contains(entity.toString())) return;
		
		net.minecraft.server.Entity mcEntity = (((CraftEntity)entity).getHandle());
		  
        if (mcEntity instanceof Mobs_creeper)
        {
        	Mobs_creeper creeper = (Mobs_creeper)mcEntity;
        	if (!creeper.destroy_blocks) event.blockList().clear();
        	if (creeper.safe) event.setCancelled(true);
        	return;
        }
        
        if (mcEntity instanceof Mobs_ender_dragon)
        {
        	Mobs_ender_dragon ender_dragon = (Mobs_ender_dragon)mcEntity;
        	if (!ender_dragon.destroy_blocks) event.blockList().clear();
        	if (ender_dragon.safe) event.setCancelled(true);
        	return;
        }
	}
	
	@EventHandler(priority = EventPriority.HIGHEST)
	public void onEndermanPickup(EndermanPickupEvent event)
	{
		if (event.isCancelled())
		{
			if (overrule_enderman_pickup) event.setCancelled(false); else return;
		}
		
		Entity entity = event.getEntity();
		if (!tracked_mobs.contains(entity.toString())) return;
		
		net.minecraft.server.Entity mcEntity = (((CraftEntity)entity).getHandle());
		  
        if (mcEntity instanceof Mobs_enderman)
        {
        	Mobs_enderman enderman = (Mobs_enderman)mcEntity;
        	if (!enderman.move_blocks) event.setCancelled(true);
        }
	}
	
	@EventHandler(priority = EventPriority.HIGHEST)
	public void onEntityCreatePortal(EntityCreatePortalEvent event)
	{
		if (event.isCancelled())
		{
			if (overrule_create_portal) event.setCancelled(false); else return;
		}
		
		Entity entity = event.getEntity();
		if (!tracked_mobs.contains(entity.toString())) return;
		
		net.minecraft.server.Entity mcEntity = (((CraftEntity)entity).getHandle());
		  
        if (mcEntity instanceof Mobs_ender_dragon)
        {
        	Mobs_ender_dragon ender_dragon = (Mobs_ender_dragon)mcEntity;
        	if (!ender_dragon.create_portal) event.setCancelled(true);
        }
	}

	@EventHandler(priority = EventPriority.HIGHEST)
	public void onSlimeSplit(SlimeSplitEvent event)
	{
		if (event.isCancelled())
		{
			if (overrule_split) event.setCancelled(false); else return;
		}
		
		Entity entity = event.getEntity();
		if (!tracked_mobs.contains(entity.toString())) return;
		
		net.minecraft.server.Entity mcEntity = (((CraftEntity)entity).getHandle());
		  
		if (mcEntity instanceof Mobs_magma_cube)
        {
			Mobs_magma_cube magma_cube = (Mobs_magma_cube)mcEntity;
			if (!magma_cube.split) event.setCancelled(true);
        	return;
        }
		
        if (mcEntity instanceof Mobs_slime)
        {
        	Mobs_slime slime = (Mobs_slime)mcEntity;
			if (!slime.split) event.setCancelled(true);
        	event.setCancelled(true);
        	return;
        }
	}
	
	@EventHandler(priority = EventPriority.HIGHEST)
	public void onSheepRegrowWool(SheepRegrowWoolEvent event)
	{		
		if (event.isCancelled())
		{
			if (overrule_regrow_wool) event.setCancelled(false); else return;
		}
		
		Entity entity = event.getEntity();
		if (!tracked_mobs.contains(entity.toString())) return;
		
		net.minecraft.server.Entity mcEntity = (((CraftEntity)entity).getHandle());
		  
        if (mcEntity instanceof Mobs_sheep)
        {
        	Mobs_sheep sheep = (Mobs_sheep)mcEntity;
        	if (sheep.always_sheared) event.setCancelled(true);// else sheep.setColor(sheep.colour);
        }
	}
	
	@EventHandler(priority = EventPriority.HIGHEST)
	public void onSheepDyeWool(SheepDyeWoolEvent event)
	{		
		if (event.isCancelled())
		{
			if (overrule_dye_wool) event.setCancelled(false); else return;
		}
		
		Entity entity = event.getEntity();
		if (!tracked_mobs.contains(entity.toString())) return;
		
		net.minecraft.server.Entity mcEntity = (((CraftEntity)entity).getHandle());
		  
        if (mcEntity instanceof Mobs_sheep)
        {
        	Mobs_sheep sheep = (Mobs_sheep)mcEntity;
        	if (!sheep.can_be_dyed) event.setCancelled(true);
        }
	}
	
	@EventHandler(priority = EventPriority.HIGHEST)
	public void onPlayerShearEntity(PlayerShearEntityEvent event)
	{
		if (event.isCancelled())
		{
			if (overrule_shear) event.setCancelled(false); else return;
		}
		
		Entity entity = event.getEntity();
		if (!tracked_mobs.contains(entity.toString())) return;
		
		net.minecraft.server.Entity mcEntity = (((CraftEntity)entity).getHandle());
		  
		if (mcEntity instanceof Mobs_mushroom_cow)
        {
        	Mobs_mushroom_cow mushroom_cow = (Mobs_mushroom_cow)mcEntity;
        	if (!mushroom_cow.can_be_sheared) event.setCancelled(true);
        	return;
        }
		
        if (mcEntity instanceof Mobs_sheep)
        {
        	Mobs_sheep sheep = (Mobs_sheep)mcEntity;
        	if (!sheep.can_be_sheared) event.setCancelled(true);
        	return;
        }
	}
	
	@EventHandler(priority = EventPriority.HIGHEST)
	public void onEntityChangeBlock(EntityChangeBlockEvent event)
	{
		if (event.isCancelled())
		{
			if (overrule_change_block) event.setCancelled(false); else return;
		}
		
		Entity entity = event.getEntity();
		if (!tracked_mobs.contains(entity.toString())) return;
		
		net.minecraft.server.Entity mcEntity = (((CraftEntity)entity).getHandle());
		
        if (mcEntity instanceof Mobs_sheep)
        {
        	Mobs_sheep sheep = (Mobs_sheep)mcEntity;
        	if (!sheep.can_remove_grass) event.setCancelled(true);
        	return;
        }
	}
	
	@EventHandler(priority = EventPriority.HIGHEST)
	public void onCreeperPower(CreeperPowerEvent event)
	{
		if (event.isCancelled())
		{
			if (overrule_become_powered_creeper) event.setCancelled(false); else return;
		}
		
		Entity entity = event.getEntity();
		if (!tracked_mobs.contains(entity.toString())) return;
		
		net.minecraft.server.Entity mcEntity = (((CraftEntity)entity).getHandle());
		  
        if (mcEntity instanceof Mobs_creeper)
        {
        	Mobs_creeper creeper = (Mobs_creeper)mcEntity;
        	if (!creeper.can_become_powered) event.setCancelled(true);
        }
	}
	
	public void onItemDespawn(ItemDespawnEvent event)
	{
		//??
	}
	
	@EventHandler(priority = EventPriority.HIGHEST)
	public void onEntityTame(EntityTameEvent event)
	{		
		if (event.isCancelled())
		{
			if (overrule_tame) event.setCancelled(false); else return;
		}
		
		Entity entity = event.getEntity();
		if (!tracked_mobs.contains(entity.toString())) return;
		
		net.minecraft.server.Entity mcEntity = (((CraftEntity)entity).getHandle());
		  
        if (mcEntity instanceof Mobs_wolf)
        {
        	Mobs_wolf wolf = (Mobs_wolf)mcEntity;
        	if (!wolf.can_be_tamed) event.setCancelled(true);
        }
	}
	
	@EventHandler(priority = EventPriority.HIGHEST)
	public void onPigZap(PigZapEvent event)
	{		
		if (event.isCancelled())
		{
			if (overrule_become_pig_zombie) event.setCancelled(false); else return;
		}
		
		Entity entity = event.getEntity();
		if (!tracked_mobs.contains(entity.toString())) return;
		
		net.minecraft.server.Entity mcEntity = (((CraftEntity)entity).getHandle());
		  
        if (mcEntity instanceof Mobs_pig)
        {
        	Mobs_pig pig = (Mobs_pig)mcEntity;
        	if (!pig.can_become_pig_zombie) event.setCancelled(true);
        }
	}
	
	@EventHandler
	public void onChunkLoad(ChunkLoadEvent event)
	{
		allow = false;
		for (Entity e :event.getChunk().getEntities())
		if (e instanceof LivingEntity && tracked_mobs.contains(e.toString()) && !(e instanceof Player))
		{
			replace_mob(e, event.getWorld());
		}
		allow = false;
	}
	
	void replace_mob(Entity ee, World world)
	{		
		Location loc = ee.getLocation();
		ee.remove();
		net.minecraft.server.World mcWorld = ((CraftWorld) loc.getWorld()).getHandle();
		mcWorld.addEntity(Utils.get_entity(ee, "", mcWorld, loc), SpawnReason.NATURAL);
	}
}

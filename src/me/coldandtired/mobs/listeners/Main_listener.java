package me.coldandtired.mobs.listeners;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

import me.coldandtired.api.Mob;
import me.coldandtired.mobs.L;
import me.coldandtired.mobs.Main;
import me.coldandtired.mobs.api.events.Mob_died_event;
import me.coldandtired.mobs.data.Autospawn;
import me.coldandtired.mobs.data.Config;
import me.coldandtired.mobs.data.Creature_data;
import me.coldandtired.mobs.data.Damage_value;
import me.coldandtired.mobs.data.Death_message;
import me.coldandtired.mobs.data.Drops;
import me.coldandtired.mobs.data.Outcome;

import org.bukkit.Effect;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Fireball;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.entity.EntityChangeBlockEvent;
import org.bukkit.event.entity.EntityCombustEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.event.entity.EntityRegainHealthEvent;
import org.bukkit.event.entity.EntityTargetLivingEntityEvent;
import org.bukkit.event.entity.ExplosionPrimeEvent;
import org.bukkit.event.world.ChunkLoadEvent;
import org.bukkit.inventory.ItemStack;

import com.herocraftonline.heroes.characters.Hero;
import com.herocraftonline.heroes.characters.classes.HeroClass;
import com.herocraftonline.heroes.characters.party.HeroParty;

public class Main_listener implements Listener 
{		
	public static Autospawn autospawn = null;
	
	@EventHandler(priority = EventPriority.HIGHEST)
	public void onCreatureSpawn(CreatureSpawnEvent event)
	{
		if (L.ignore_world(event.getEntity().getWorld()) && autospawn == null) return;

		if (event.isCancelled())
		{
			if (Config.overrule_spawning) event.setCancelled(false);
			else
			{
				autospawn = null;
				return;
			}
		}

		LivingEntity le = L.return_le(event.getEntity());
		if (le == null)		
		{
			autospawn = null;
			return;
		}
		
		Creature_data cd = Main.tracked_mobs.get(le.getType().name());	
		if (cd == null)
		{
			autospawn = null;
			return; 
		} // not tracked		
		
		// end intro		
		
		String spawn_reason = autospawn != null? "autospawned" : event.getSpawnReason().name();
		String autospawn_id = autospawn != null ? autospawn.id : null;
		int random = L.rng.nextInt(100) + 1;
		
		Boolean can_spawn = L.merge_spawning(cd, le, spawn_reason, random, autospawn_id);

		if (can_spawn != null && !can_spawn)
		{
			if (Config.log_level > 1) L.log(le.getType().name().toLowerCase() + " was blocked from spawning!");
			event.setCancelled(true);
			autospawn = null;
			return;
		}
		
		L.setup_mob(cd, le, spawn_reason, random, autospawn_id);		
		
		autospawn = null;			
	}	
	
	@EventHandler(priority = EventPriority.HIGHEST)
	public void onEntityDeath(EntityDeathEvent event)
	{
		LivingEntity le = event.getEntity();
		String mob_name = le.getType().name();
		
		Creature_data cd = Main.tracked_mobs.get(mob_name);	
		if (cd == null)
		{
			if (Config.messages != null && !Config.tracked_mobs_only)
			{
				for (Death_message dm : Config.messages) L.send_death_message(dm, mob_name, event.getDroppedExp(), le.getKiller(), event.getDrops(), 0);
			}
			else return; // not tracked
		}
		
		Mob mob = Main.all_mobs.get(le.getUniqueId().toString());	
		if (mob == null) return;
	
		Main.all_mobs.remove(le.getUniqueId().toString());
		
		mob_name = mob_name.toLowerCase();
		
		if (Config.log_level > 1) L.log("Tracked mob " + mob_name + " died");
		
		Player p = le.getKiller();
		
		Drops drops = null;		

		// check drops == death or both
		if ((cd.gen_drops_check_type > 0 && !mob.getSpawn_reason().equalsIgnoreCase("autospawned")) || (cd.as_drops_check_type > 0 && mob.getSpawn_reason().equalsIgnoreCase("autospawned")))
		{
			if (Config.log_level > 1) L.log("Checking conditions for drops");
			Outcome o = L.get_drops_outcome(cd, le, mob.getSpawn_reason(), p, mob.getRandom(), mob.getAutospawn_id());
			drops = L.merge_drops(o, cd, mob.getSpawn_reason());
		} 
		else
		{
			if (Config.log_level > 1) L.log("Using spawn conditions");
			drops = mob.getDrops();
		}
		
		List<ItemStack> old_drops = drops != null ? L.get_drops(mob, drops, event.getDrops()) : event.getDrops();
		int old_exp = drops != null ? L.get_exp(mob, drops, event.getDroppedExp()) : event.getDroppedExp();
		int old_bounties = drops != null ? L.get_bounties(mob, drops) : 0;
		List<Death_message> old_messages = drops != null ? drops.messages : Config.messages;
		
		Mob_died_event mob_died_event = new Mob_died_event(mob, le, p, old_drops, old_exp, old_bounties,
				old_messages);
		Main.plugin.getServer().getPluginManager().callEvent(mob_died_event);
		
		if (mob_died_event.isCancelled()) return;
		
		if (mob_died_event.get_drops() != null)
		{
			event.getDrops().clear();
			Iterator<ItemStack> iter = mob_died_event.get_drops().iterator();
			while (iter.hasNext()) event.getDrops().add(iter.next());
		}
		
		event.setDroppedExp(mob_died_event.get_exp());
		
		if (Main.economy != null && p != null) Main.economy.depositPlayer(p.getName(), mob_died_event.get_bounty());
		
		if (p != null && Main.heroes != null)
		{
			Hero hero = Main.heroes.getCharacterManager().getHero(p);
			HeroParty hp = hero.getParty();
			if (hp != null)
			{
				hp.gainExp(Double.parseDouble(Integer.toString(mob_died_event.get_exp())) , HeroClass.ExperienceType.KILLING, le.getLocation());
			}
			else
			{
				if (hero.canGain(HeroClass.ExperienceType.KILLING)) hero.gainExp(Double.parseDouble(Integer.toString(mob_died_event.get_exp())) , HeroClass.ExperienceType.KILLING, le.getLocation());
			}
			event.setDroppedExp(0);
		}
			
		if (mob_died_event.get_death_messages() != null)
		{
			for (Death_message dm : mob_died_event.get_death_messages()) L.send_death_message(dm, mob_name, mob_died_event.get_exp(), p, mob_died_event.get_drops(), mob_died_event.get_bounty());				
		}		
	}

	@EventHandler(priority = EventPriority.HIGH)
	public void onEntityDamage(EntityDamageEvent event)
	{	
		if (!(event.getEntity() instanceof LivingEntity)) return;
		
		if (Main.heroes != null) return;
		
		if (event.isCancelled())
		{
			if (Config.overrule_damaging) event.setCancelled(false);
			else return;
		}			
		
		LivingEntity le = (LivingEntity)event.getEntity();
		
		if(le.getNoDamageTicks() > 10)
		{
			event.setCancelled(true);
			return;
		}
		
		int damage = event.getDamage();		
	
		Entity damager = null;
		if (event instanceof EntityDamageByEntityEvent) damager = ((EntityDamageByEntityEvent)event).getDamager();
		
		if (damager != null)
		{
			Mob attacker = Main.all_mobs.get(damager.getUniqueId().toString());
			if (attacker != null)
			{
				if (attacker.getDamage() != null) damage = attacker.getDamage();
			}
			else if (damager instanceof Projectile)
			{
				LivingEntity le2 = ((Projectile)damager).getShooter();
				if (le2 != null)
				{
					attacker = Main.all_mobs.get(le2.getUniqueId().toString());
					if (attacker != null && attacker.getDamage() != null) damage = attacker.getDamage();	
				}
			}
			event.setDamage(damage);
		}				
		
		Mob mob = Main.all_mobs.get(event.getEntity().getUniqueId().toString());
		
		if (mob == null) return;	
		
		if (mob.getBoss_mob() != null && mob.getBoss_mob()) le.getWorld().playEffect(le.getLocation(), Effect.MOBSPAWNER_FLAMES, 100);
		
		Creature_data cd = Main.tracked_mobs.get(le.getType().name());	
		if (cd == null) return; // not tracked
		
		// end setup
		
		Player p = null;
		if (damager instanceof Player) p = (Player)damager;
		
		Map<String, Damage_value> damage_values = null;	
		
		// check damages == damage or both
		if ((cd.gen_damages_check_type > 0 && !mob.getSpawn_reason().equalsIgnoreCase("autospawned")) || (cd.as_damages_check_type > 0 && mob.getSpawn_reason().equalsIgnoreCase("autospawned")))
		{
			if (Config.log_level > 1) L.log("Checking conditions for damages");
			Outcome o = L.get_damages_outcome(cd, le, mob.getSpawn_reason(), p, mob.getRandom(), mob.getAutospawn_id());
			damage_values = L.merge_damage_properties(o, cd, mob.getSpawn_reason());
		} 
		else 
		{
			if (Config.log_level > 1) L.log("Using spawn conditions");
			//damage_values = mob.getDamage_properties();
		}
		
		if (damage_values != null)
		{
			Damage_value dv = damage_values.get(event.getCause().name());
			if (dv != null)
			{
				double hurt = L.get_quantity(dv.amount);
				if (dv.use_percent)
				{
					hurt = hurt / 100;
					damage = (int) Math.ceil(damage * hurt);
				}
				else damage = (int) hurt;
			}	
		}
		
		if (mob.getHp() != null)
		{
			mob.setHp(mob.getHp() - damage);
			if (mob.getHp() < 1) event.setDamage(10000); else
			{
				event.setDamage(-1);
			}
		} else event.setDamage(damage);
	}
	
	@EventHandler(priority = EventPriority.HIGHEST)
	public void onEntityRegainHealth(EntityRegainHealthEvent event)
	{
		Mob mob = Main.all_mobs.get(event.getEntity().getUniqueId().toString());
		if (mob == null) return;
		
		if (event.isCancelled())
		{
			if (Config.overrule_healing) event.setCancelled(false);
			else return;
		}		
		
		// end setup
		
		if (mob.getCan_heal() != null && !mob.getCan_heal())
		{
			event.setCancelled(true);
			return;
		}

		if (mob.getHp() == null) return;
		
		int i = mob.getHp() + event.getAmount();
		if (mob.getCan_overheal()) mob.setHp(i);
		else mob.setHp(i > mob.getMax_hp() ? mob.getMax_hp() : i);
	}
	
	@EventHandler(priority = EventPriority.HIGHEST)
	public void onEntityCombust(EntityCombustEvent event)
	{
		Mob mob = Main.all_mobs.get(event.getEntity().getUniqueId().toString());
		if (mob == null) return;
	
		if (event.isCancelled())
		{
			if (Config.overrule_burning) event.setCancelled(false);
			else return;
		}
		
		if (mob.getCan_burn() != null && !mob.getCan_burn())
		{
			event.setCancelled(true);
			return;
		}
		
		if (mob.getBurn_duration() != null) event.setDuration(mob.getBurn_duration() * 20);
	}
	
	@EventHandler(priority = EventPriority.HIGHEST)
	public void onEntityTarget(EntityTargetLivingEntityEvent event)
	{
		Mob mob = Main.all_mobs.get(event.getEntity().getUniqueId().toString());
		if (mob == null) return;
		
		if (event.isCancelled())
		{
			if (Config.overrule_targeting) event.setCancelled(false);
			else return;
		}

		// end setup
		
		if (mob.getSafe() != null) event.setCancelled(mob.getSafe());
	}

	@EventHandler(priority = EventPriority.HIGHEST)
	public void onExplosionPrime(ExplosionPrimeEvent event)
	{
		if (event.isCancelled())
		{
			if (Config.overrule_exploding) event.setCancelled(false);
			else return;
		}

		Entity entity = event.getEntity();
		if (entity instanceof Fireball) entity = ((Fireball)entity).getShooter();
		
		Mob mob = Main.all_mobs.get(entity.getUniqueId().toString());
		if (mob == null) return;
		
		// end setup
		
		if(mob.getFiery_explosion() != null) event.setFire(mob.getFiery_explosion());
		if (mob.getSafe() != null) event.setCancelled(mob.getSafe());
	}
	
	@EventHandler(priority = EventPriority.HIGHEST)
	public void onEntityExplode(EntityExplodeEvent event)
	{
		Entity entity = event.getEntity();
		
		if (entity == null) return;
		
		if (entity instanceof Fireball) entity = ((Fireball)entity).getShooter();
		
		Mob mob = Main.all_mobs.get(entity.getUniqueId().toString());
		if (mob == null) return;
		
		if (event.isCancelled())
		{
			if (Config.overrule_exploding) event.setCancelled(false);
			else return;
		}
		
		// end setup

		if (mob.getSafe() != null && mob.getSafe()) event.setCancelled(true);
		else
		{			
			if (mob.getCan_destroy_blocks() != null && !mob.getCan_destroy_blocks()) event.blockList().clear();
			else
			{
				if (mob.getExplosion_size() != null)
				{
					int size = mob.getExplosion_size();
					event.setCancelled(true);
					Location loc = event.getLocation();
					if (mob.getFiery_explosion() != null) loc.getWorld().createExplosion(loc, size, mob.getFiery_explosion());
					else loc.getWorld().createExplosion(loc, size);
				}
			}
		}
		
	}
		
	@EventHandler(priority = EventPriority.HIGHEST)
	public void onEntityChangeBlock(EntityChangeBlockEvent event)
	{
		Mob mob = Main.all_mobs.get(event.getEntity().getUniqueId().toString());
		if (mob == null) return;
		
		if (event.isCancelled())
		{
			if (Config.overrule_changing_block) event.setCancelled(false);
			else return;
		}
		
		// end setup
		
		if ((mob.getCan_move_blocks() != null && !mob.getCan_move_blocks()) 
				|| (mob.getCan_graze() != null && !mob.getCan_graze())) event.setCancelled(true);
	}

	public void onChunkLoad(ChunkLoadEvent event)
	{
		if (!L.ignore_world(event.getWorld())) L.convert_chunk(event.getChunk());
	}
}

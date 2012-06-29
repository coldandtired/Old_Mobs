package me.coldandtired.mobs.listeners;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import me.coldandtired.mobs.L;
import me.coldandtired.mobs.Main;
import me.coldandtired.mobs.Mob;
import me.coldandtired.mobs.data.Autospawn;
import me.coldandtired.mobs.data.Bounty;
import me.coldandtired.mobs.data.Config;
import me.coldandtired.mobs.data.Creature_data;
import me.coldandtired.mobs.data.Damage_value;
import me.coldandtired.mobs.data.Death_message;
import me.coldandtired.mobs.data.Drops;
import me.coldandtired.mobs.data.Exp;
import me.coldandtired.mobs.data.Item;
import me.coldandtired.mobs.data.Item_enchantment;
import me.coldandtired.mobs.data.Outcome;

import org.bukkit.Effect;
import org.bukkit.Location;
import org.bukkit.enchantments.Enchantment;
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
		
		L.setup_mob(cd, le, spawn_reason, random, autospawn_id, null);		
		
		autospawn = null;			
	}	

	@EventHandler(priority = EventPriority.HIGHEST)
	public void onEntityDeath(EntityDeathEvent event)
	{	
		LivingEntity le = (LivingEntity)event.getEntity();
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
	
		mob_name = mob_name.toLowerCase();
		
		if (Config.log_level > 1) L.log("Tracked mob " + mob_name + " died");
		
		Player p = le.getKiller();
		
		Drops drops = null;		

		// check drops == death or both
		if ((cd.gen_drops_check_type > 0 && !mob.spawn_reason.equalsIgnoreCase("autospawned")) || (cd.as_drops_check_type > 0 && mob.spawn_reason.equalsIgnoreCase("autospawned")))
		{
			if (Config.log_level > 1) L.log("Checking conditions for drops");
			Outcome o = L.get_drops_outcome(cd, le, mob.spawn_reason, p, mob.random, mob.autospawn_id);
			drops = L.merge_drops(o, cd, mob.spawn_reason);
		} 
		else
		{
			if (Config.log_level > 1) L.log("Using spawn conditions");
			drops = mob.drops;
		}
		
		if (drops != null)
		{
			if (Config.log_level > 1) L.log("Drops found");
			if (drops.items != null)
			{
				List<ItemStack> dropped = new ArrayList<ItemStack>();
				boolean replaced = false;
				for (Item item : drops.items)
				{
					if (!replaced && L.matches_number_condition(item.chances, mob.random))
					{
						if (item.replace)
						{
							event.getDrops().clear();
							replaced = true;
						}
						int quantity = L.get_quantity(item.quantity);
						ItemStack is = new ItemStack(item.id, quantity, item.data);
						if (item.enchantments != null)
						{
							for (Item_enchantment ie : item.enchantments)
							{
								is.addUnsafeEnchantment(Enchantment.getByName(ie.name), L.get_quantity(ie.level));
							}
						}
						dropped.add(is);
					}
				}
				for (ItemStack is : dropped) event.getDrops().add(is);
			}
			
			int given_bounty = 0;
			if (drops.bounties != null && p != null)
			{
				for (Bounty b : drops.bounties)
				{
					if (L.matches_number_condition(b.chances, mob.random))
					{
						double amount = L.get_bounty(b.amount);
						given_bounty += amount;
						Main.economy.depositPlayer(p.getName(), amount);
					}
				}
			
			}
			
			if (drops.exps != null)
			{
				boolean replaced = false;
				int xp = 0;
				for (Exp exp : drops.exps)
				{
					if (!replaced && L.matches_number_condition(exp.chances, mob.random))
					{
						if (exp.replace)
						{
							xp = 0;
							replaced = true;
						}
						xp += L.get_quantity(exp.amount);
					}
				}
				event.setDroppedExp(xp);

				if (p != null && Main.heroes != null)
				{
					Hero hero = Main.heroes.getCharacterManager().getHero(p);
					hero.gainExp(Double.parseDouble(Integer.toString(xp)) , HeroClass.ExperienceType.KILLING);
					event.setDroppedExp(0);
				}
			}
			
			if (drops.messages != null)
			{
				for (Death_message dm : drops.messages) L.send_death_message(dm, mob_name, event.getDroppedExp(), p, event.getDrops(), given_bounty);				
			}
			else if (Config.messages != null)
			{
				for (Death_message dm : Config.messages) L.send_death_message(dm, mob_name, event.getDroppedExp(), p, event.getDrops(), given_bounty);
			}
		}
		
		Main.all_mobs.remove(le.getUniqueId().toString());
	}

	@EventHandler(priority = EventPriority.HIGH)
	public void onEntityDamage(EntityDamageEvent event)
	{
		//Monster monster = Main.heroes.getCharacterManager().getMonster((LivingEntity)event.getEntity());
		
		//L.log(11);
		//CharacterDamageEvent other_damage = new CharacterDamageEvent(event.getEntity(), event.getCause(), event.getDamage());
	    //  Main.plugin.getServer().getPluginManager().callEvent(other_damage);
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
				if (attacker.damage != null) damage = attacker.damage;
			}
			else if (damager instanceof Projectile)
			{
				LivingEntity le2 = ((Projectile)damager).getShooter();
				if (le2 != null)
				{
					attacker = Main.all_mobs.get(le2.getUniqueId().toString());
					if (attacker != null && attacker.damage != null) damage = attacker.damage;	
				}
			}
			event.setDamage(damage);
		}				
		
		Mob mob = Main.all_mobs.get(event.getEntity().getUniqueId().toString());
		if (mob == null) return;	
		
		if (mob.boss_mob != null && mob.boss_mob) le.getWorld().playEffect(le.getLocation(), Effect.MOBSPAWNER_FLAMES, 100);
		
		Creature_data cd = Main.tracked_mobs.get(le.getType().name());	
		if (cd == null) return; // not tracked
		
		// end setup
		
		Player p = null;
		if (damager instanceof Player) p = (Player)damager;
		
		HashMap<String, Damage_value> damage_values = null;	
		
		// check damages == damage or both
		if ((cd.gen_damages_check_type > 0 && !mob.spawn_reason.equalsIgnoreCase("autospawned")) || (cd.as_damages_check_type > 0 && mob.spawn_reason.equalsIgnoreCase("autospawned")))
		{
			if (Config.log_level > 1) L.log("Checking conditions for damages");
			Outcome o = L.get_damages_outcome(cd, le, mob.spawn_reason, p, mob.random, mob.autospawn_id);
			damage_values = L.merge_damage_properties(o, cd, mob.spawn_reason);
		} 
		else 
		{
			if (Config.log_level > 1) L.log("Using spawn conditions");
			damage_values = mob.damage_properties;
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
		
		if (mob.hp != null)
		{
			mob.hp -= damage;
			if (mob.hp < 1) event.setDamage(10000); else event.setDamage(-1);
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
		
		if (mob.can_heal != null && !mob.can_heal)
		{
			event.setCancelled(true);
			return;
		}

		if (mob.hp == null) return;
		
		int i = mob.hp + event.getAmount();
		if (mob.can_overheal) mob.hp = i;
		else mob.hp = i > mob.max_hp ? mob.max_hp : i;
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
		
		if (mob.can_burn != null && !mob.can_burn)
		{
			event.setCancelled(true);
			return;
		}
		
		if (mob.burn_duration != null) event.setDuration(mob.burn_duration * 20);
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
		
		if (mob.safe != null) event.setCancelled(mob.safe);
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
		
		if(mob.fiery_explosion != null) event.setFire(mob.fiery_explosion);
		if (mob.safe != null) event.setCancelled(mob.safe);
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

		if (mob.safe != null && mob.safe) event.setCancelled(true);
		else
		{			
			if (mob.can_destroy_blocks != null && !mob.can_destroy_blocks) event.blockList().clear();
			else
			{
				if (mob.explosion_size != null)
				{
					int size = mob.explosion_size;
					event.setCancelled(true);
					Location loc = event.getLocation();
					if (mob.fiery_explosion != null) loc.getWorld().createExplosion(loc, size, mob.fiery_explosion);
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
		
		if ((mob.can_move_blocks != null && !mob.can_move_blocks) 
				|| (mob.can_graze != null && !mob.can_graze)) event.setCancelled(true);
	}
			
	@EventHandler
	public void onChunkLoad(ChunkLoadEvent event)
	{
		if (L.ignore_world(event.getWorld())) return;
		
		L.convert_chunk(event.getChunk());
	}
}

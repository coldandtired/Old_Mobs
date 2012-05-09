package me.coldandtired.mobs.listeners;

import java.text.DecimalFormat;
import java.util.Collection;
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
import me.coldandtired.mobs.data.Drops;
import me.coldandtired.mobs.data.Exp;
import me.coldandtired.mobs.data.Item;
import me.coldandtired.mobs.data.Item_enchantment;
import me.coldandtired.mobs.data.Mob_properties;
import org.bukkit.DyeColor;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Animals;
import org.bukkit.entity.Creeper;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Fireball;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Ocelot;
import org.bukkit.entity.Pig;
import org.bukkit.entity.PigZombie;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.entity.Sheep;
import org.bukkit.entity.Slime;
import org.bukkit.entity.Villager;
import org.bukkit.entity.Wolf;
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
import org.bukkit.event.entity.EntityTargetEvent;
import org.bukkit.event.entity.ExplosionPrimeEvent;
import org.bukkit.event.world.ChunkLoadEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;

public class Main_listener implements Listener 
{	
	public static Autospawn autospawn = null;
	DecimalFormat f = new DecimalFormat("#,###.##");
	
	@EventHandler(priority = EventPriority.HIGHEST)
	public void onCreatureSpawn(CreatureSpawnEvent event)
	{
		if (L.ignore_world(event.getEntity().getWorld()))
		{
			autospawn = null;
			return;
		};

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
		
		String mob_name = le.getType().name().toLowerCase();		
		String spawn_reason = event.getSpawnReason().name();		
		int random = L.rng.nextInt(100) + 1;
		
		Boolean can_spawn = L.merge_spawning(cd, le, spawn_reason, random, autospawn);

		if (can_spawn != null && !can_spawn)
		{
			if (Config.log_level > 1) L.log(mob_name + " was blocked from spawning!");
			event.setCancelled(true);
			autospawn = null;
			return;
		}
		
		Mob_properties props = L.merge_mob_properties(cd, le, spawn_reason, random, autospawn);		
		Collection<PotionEffect> potion_effects = L.merge_potion_effects(cd, le, spawn_reason, random, autospawn);
		Drops drops = null;
		HashMap<String, Damage_value> damage_properties = null;
		
		if ((cd.gen_drops_check_type != 1 && autospawn == null) || cd.as_drops_check_type != 1 && autospawn != null)
			drops = L.merge_drops(cd, le, spawn_reason, null, random, autospawn);
		
		if ((cd.gen_damages_check_type != 1 && autospawn == null) || cd.as_damages_check_type != 1 && autospawn != null)
			damage_properties = L.merge_damage_properties(cd, le, spawn_reason, null, random, autospawn);
		
		if (props == null && potion_effects == null && drops == null && damage_properties == null) 
		{
			if (Config.log_level > 1) L.log("No default outcome for " + mob_name + "  - vanilla mob spawned!");
			autospawn = null;
			return;
		}
		
		Mob mob = new Mob(props, drops, damage_properties, spawn_reason, random);
		
		if (potion_effects != null) le.addPotionEffects(potion_effects);		
		
		Main.mobs.put(le.getUniqueId(), mob);
		autospawn = null;
		
		if (props == null)
		{
			autospawn = null;
			return;
		} // no properties left, only drops etc.
		
		
		if (le instanceof Slime)
		{
			Slime slime = (Slime)le;
			if (spawn_reason.equalsIgnoreCase("SLIME_SPLIT") && props.size != null) slime.setSize(L.return_int_from_array(props.size));
			
			if (props.hp_per_size != null) mob.hp = slime.getSize() * L.return_int_from_array(props.hp_per_size);
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
				if (wolf.isTamed() && props.tamed_hp != null) mob.hp = L.return_int_from_array(props.tamed_hp);
			}
			/*else 
				{wolf.setTarget(L.get_nearby_player(wolf));
				wolf.setAngry(true);
				}
			L.log(wolf.getTarget().toString());*/
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

	@EventHandler
	public void onEntityDeath(EntityDeathEvent event)
	{
		if (L.ignore_world(event.getEntity().getWorld())) return;
		
		LivingEntity le = L.return_le(event.getEntity());
		if (le == null) return;		
		
		Creature_data cd = Main.tracked_mobs.get(le.getType().name());	
		if (cd == null) return; // not tracked
		
		Mob mob = Main.mobs.get(le.getUniqueId());
		if (mob == null) return;
		
		if (Config.log_level > 1) L.log("Tracked mob " + le.getType().name().toLowerCase() + " died");
		
		Player p = le.getKiller();
		
		Drops drops = null;		
		
		// check drops == death or both
		if ((cd.gen_drops_check_type > 0 && autospawn == null) || (cd.as_drops_check_type > 0 && autospawn != null))
		{
			if (Config.log_level > 1) L.log("Checking conditions for drops");
			drops = L.merge_drops(cd, le, mob.spawn_reason, p, mob.random, autospawn);
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
				List<ItemStack> dropped = event.getDrops();
				boolean replaced = false;
				for (Item item : drops.items)
				{
					if (!replaced && L.matches_number_condition(item.chances, mob.random))
					{
						if (item.replace)
						{
							dropped.clear();
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
			}
			
			if (drops.bounties != null)
			{
				for (Bounty b : drops.bounties)
				{
					if (L.matches_number_condition(b.chances, mob.random))
					{
						Main.economy.depositPlayer(p.getName(), L.get_quantity(b.amount));
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
			}
			
			/*if (da.death_messages != null && p != null)
					{
						String s = Utils.get_string_value(da.death_messages.messages);
						String money = "";
						String player_money = "";
						String item_names = "";
						String item_amounts = "";
						
						for (ItemStack is : event.getDrops())
						{
							String ss = is.getType().name().toLowerCase();
							item_names += ", " + ss;
							item_amounts += ", " + is.getAmount() + " x " + ss;
						}
						item_names = item_names.replaceFirst(", ", "");
						item_amounts = item_amounts.replaceFirst(", ", "");
						
						if (Main.economy != null)
						{
							money = Integer.toString(new_money);
							player_money = f.format(Main.economy.getBalance(p.getName()));
						}
						
						s = s.replace("^player^", p.getDisplayName());
						s = s.replace("^exp^", Integer.toString(event.getDroppedExp()));
						s = s.replace("^mob^", entity.getType().getName());
						s = s.replace("^money^", money);
						s = s.replace("^total_money^", player_money);
						s = s.replace("^item_names^", item_names);
						s = s.replace("^item_amounts^", item_amounts);
						
						if (da.death_messages.global) p.getServer().broadcastMessage(s);
						else
						{
							p.sendMessage(s);
							if (da.death_messages.log) L.log(s);
						}
						global_message = false;
					}
				}
			}
		}
		
		if (p != null && global_message)
		{		
			if (death_messages != null && death_messages.messages != null)
			{
				String s = Utils.get_string_value(death_messages.messages);
				
				String money = "";
				String player_money = "";
				String item_names = "";
				String item_amounts = "";
				
				for (ItemStack is : event.getDrops())
				{
					String ss = is.getType().name().toLowerCase();
					item_names += ", " + ss;
					item_amounts += ", " + is.getAmount() + " x " + ss;
				}
				item_names = item_names.replaceFirst(", ", "");
				item_amounts = item_amounts.replaceFirst(", ", "");
				
				if (Main.economy != null)
				{
					money = Integer.toString(new_money);
					player_money = f.format(Main.economy.getBalance(p.getName()));
				}
				
				s = s.replace("^player^", p.getDisplayName());
				s = s.replace("^exp^", Integer.toString(event.getDroppedExp()));
				s = s.replace("^mob^", entity.getType().getName());
				s = s.replace("^money^", money);
				s = s.replace("^total_money^", player_money);
				s = s.replace("^item_names^", item_names);
				s = s.replace("^item_amounts^", item_amounts);
				
				if (death_messages.global) p.getServer().broadcastMessage(s);
				else
				{
					p.sendMessage(s);
					if (death_messages.log) L.log(s);
				}
			}
		}*/
		}
	}

	@EventHandler(priority = EventPriority.HIGHEST)
	public void onEntityDamage(EntityDamageEvent event)
	{
		if (L.ignore_world(event.getEntity().getWorld())) return;

		if (event.isCancelled())
		{
			if (Config.overrule_damaging) event.setCancelled(false);
			else return;
		}

		LivingEntity le = L.return_le(event.getEntity());
		if (le == null) return;		
		
		Creature_data cd = Main.tracked_mobs.get(le.getType().name());	
		if (cd == null) return; // not tracked
		
		Mob mob = Main.mobs.get(le.getUniqueId());
		if (mob == null) return;
		
		// end setup
		
		int damage = event.getDamage();
		Entity damager = null;
		if (event instanceof EntityDamageByEntityEvent) damager = ((EntityDamageByEntityEvent)event).getDamager();
		
		if (damager != null)
		{		
			if (Main.tracked_mobs.get(damager.getType().name()) != null)
			{
				Mob attacker = Main.mobs.get(damager.getUniqueId());
				if (attacker != null && attacker.damage != null) damage = attacker.damage;	
			}
			else if (damager instanceof Projectile)
			{
				LivingEntity le2 = ((Projectile)damager).getShooter();
				if (le2 != null && Main.tracked_mobs.get(le2.getType().name()) != null)
				{
					Mob attacker = Main.mobs.get(le2.getUniqueId());
					if (attacker != null && attacker.damage != null) damage = attacker.damage;	
				}
			}
		}
		
		Player p = null;
		if (damager instanceof Player) p = (Player)damager;
		
		HashMap<String, Damage_value> damage_values = null;	
		
		// check damages == damage or both
		if ((cd.gen_damages_check_type > 0 && autospawn == null) || (cd.as_damages_check_type > 0 && autospawn != null))
		{
			if (Config.log_level > 1) L.log("Checking conditions for damages");
			damage_values = L.merge_damage_properties(cd, le, mob.spawn_reason, p, mob.random, autospawn);
		} 
		else 
		{
			if (Config.log_level > 1) L.log("Using spawn conditions");
			damage_values = mob.damage_properties;
		}
		
		if (damage_values == null) return;
		
		Damage_value dv = damage_values.get(event.getCause().name());
		if (dv != null)
		{
			int hurt = L.get_quantity(dv.amount);
			if (dv.use_percent) hurt = hurt / 100;
			damage = damage * hurt;
		}			
		
		if (mob.hp != null)
		{
			mob.hp -= damage;		
			if (mob.hp < 1) event.setDamage(5000); else event.setDamage(-1);
		} else event.setDamage(damage);
	}
	
	@EventHandler(priority = EventPriority.HIGHEST)
	public void onEntityRegainHealth(EntityRegainHealthEvent event)
	{
		if (L.ignore_world(event.getEntity().getWorld())) return;

		if (event.isCancelled())
		{
			if (Config.overrule_healing) event.setCancelled(false);
			else return;
		}

		LivingEntity le = L.return_le(event.getEntity());
		if (le == null) return;		
		
		Creature_data cd = Main.tracked_mobs.get(le.getType().name());	
		if (cd == null) return; // not tracked
		
		Mob mob = Main.mobs.get(le.getUniqueId());
		if (mob == null) return;
		
		// end setup
		
		if (!mob.can_heal)
		{
			event.setCancelled(true);
			return;
		}
		if (mob.max_hp > -1 || mob.can_overheal)
		{
			int i = mob.hp + event.getAmount();
			if (mob.can_overheal) mob.hp = i;
			else 
			{
				if (i > mob.max_hp) mob.hp = mob.max_hp; else mob.hp = i;
			}
		}
	}
	
	@EventHandler(priority = EventPriority.HIGHEST)
	public void onEntityCombust(EntityCombustEvent event)
	{
		if (L.ignore_world(event.getEntity().getWorld())) return;

		if (event.isCancelled())
		{
			if (Config.overrule_burning) event.setCancelled(false);
			else return;
		}

		LivingEntity le = L.return_le(event.getEntity());
		if (le == null) return;		
		
		Creature_data cd = Main.tracked_mobs.get(le.getType().name());	
		if (cd == null) return; // not tracked
		
		Mob mob = Main.mobs.get(le.getUniqueId());
		if (mob == null) return;
		
		// end setup
		
		if (!mob.can_burn)
		{
			event.setCancelled(true);
			return;
		}
		
		if (mob.burn_duration != null) event.setDuration(mob.burn_duration * 20);
	}
	
	@EventHandler(priority = EventPriority.HIGHEST)
	public void onEntityTarget(EntityTargetEvent event)
	{
		if (L.ignore_world(event.getEntity().getWorld())) return;

		if (event.isCancelled())
		{
			if (Config.overrule_targeting) event.setCancelled(false);
			else return;
		}

		LivingEntity le = L.return_le(event.getEntity());
		if (le == null) return;		
		
		Creature_data cd = Main.tracked_mobs.get(le.getType().name());	
		if (cd == null) return; // not tracked
		
		Mob mob = Main.mobs.get(le.getUniqueId());
		if (mob == null) return;
		
		// end setup
		
		if (mob.safe != null) event.setCancelled(mob.safe); 
	}

	@EventHandler(priority = EventPriority.HIGHEST)
	public void onExplosionPrime(ExplosionPrimeEvent event)
	{
		if (L.ignore_world(event.getEntity().getWorld())) return;

		if (event.isCancelled())
		{
			if (Config.overrule_exploding) event.setCancelled(false);
			else return;
		}

		Entity entity = event.getEntity();
		if (entity instanceof Fireball) entity = ((Fireball)entity).getShooter();
		LivingEntity le = L.return_le(entity);
		if (le == null) return;		
		
		Creature_data cd = Main.tracked_mobs.get(le.getType().name());	
		if (cd == null) return; // not tracked
		
		Mob mob = Main.mobs.get(le.getUniqueId());
		if (mob == null) return;
		
		// end setup
		
		if(mob.fiery_explosion != null) event.setFire(mob.fiery_explosion);
		if (mob.safe != null) event.setCancelled(mob.safe);
	}
	
	@EventHandler(priority = EventPriority.HIGHEST)
	public void onEntityExplode(EntityExplodeEvent event)
	{
		if (L.ignore_world(event.getLocation().getWorld())) return;

		if (event.isCancelled())
		{
			if (Config.overrule_exploding) event.setCancelled(false);
			else return;
		}

		LivingEntity le = L.return_le(event.getEntity());
		if (le == null) return;		
		
		Creature_data cd = Main.tracked_mobs.get(le.getType().name());	
		if (cd == null) return; // not tracked
		
		Mob mob = Main.mobs.get(le.getUniqueId());
		if (mob == null) return;
		
		// end setup
		
		if (mob.can_destroy_blocks != null && !mob.can_destroy_blocks) event.blockList().clear();
		if (mob.safe != null) event.setCancelled(mob.safe);
	}
		
	@EventHandler(priority = EventPriority.HIGHEST)
	public void onEntityChangeBlock(EntityChangeBlockEvent event)
	{
		if (L.ignore_world(event.getEntity().getWorld())) return;

		if (event.isCancelled())
		{
			if (Config.overrule_changing_block) event.setCancelled(false);
			else return;
		}

		LivingEntity le = L.return_le(event.getEntity());
		if (le == null) return;		
		
		Creature_data cd = Main.tracked_mobs.get(le.getType().name());	
		if (cd == null) return; // not tracked
		
		Mob mob = Main.mobs.get(le.getUniqueId());
		if (mob == null) return;
		
		// end setup
		
		if ((mob.can_move_blocks != null && !mob.can_move_blocks) 
				|| (mob.can_graze != null && !mob.can_graze)) event.setCancelled(true);
	}
			
	@EventHandler
	public void onChunkLoad(ChunkLoadEvent event)
	{
		//Utils.log("gen listener");
		/*if (L.ignore_world(event.getWorld())) return;
		
		for (Entity ee: event.getChunk().getEntities()) 
		{					
			String s = ee.getType().name();
			if (ee instanceof LivingEntity)
			{	
				Creature_data cd = tracked_mobs.get(s);
				if (cd != null)
				{
					s = s.toLowerCase();
					if (Config.log_level > 1) Utils.log("Found a vanilla " + s + " and converted to a Mobs " + s + "!");
					mobs.put(ee.getUniqueId(), );
				}
			}
		}*/
	}
}

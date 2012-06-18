package me.coldandtired.mobs;


import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

import org.bukkit.Chunk;
import org.bukkit.DyeColor;
import org.bukkit.Effect;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
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
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import com.khorn.terraincontrol.bukkit.BukkitWorld;

import me.coldandtired.mobs.conditions.Number_condition;
import me.coldandtired.mobs.data.Autospawn;
import me.coldandtired.mobs.data.Autospawn_location;
import me.coldandtired.mobs.data.Config;
import me.coldandtired.mobs.data.Creature_data;
import me.coldandtired.mobs.data.Damage_value;
import me.coldandtired.mobs.data.Death_message;
import me.coldandtired.mobs.data.Drops;
import me.coldandtired.mobs.data.Mob_properties;
import me.coldandtired.mobs.data.Outcome;
import me.coldandtired.mobs.data.Potion_effect;
import me.coldandtired.mobs.data.Selected_outcomes;

public class L 
{
	public static Random rng = new Random();
	static DecimalFormat f = new DecimalFormat("#,###.##");
	
	// general helpers
	
	public static void log(Object message)
	{
		Main.logger.info("" + message);
	}
	
	public static void warn(Object message)
	{
		Main.logger.warning("" + message);
	}
	
	public static boolean ignore_world(World world)
	{
		return Config.ignored_worlds != null && Config.ignored_worlds.contains(world.getName().toUpperCase());
	}
	
	public static LivingEntity return_le(Entity entity)
	{
		return entity instanceof LivingEntity ? (LivingEntity)entity : null;
	}
	
	static boolean use_outcome(Outcome o, Autospawn as)
	{
		if (o == null) return false;
		return o.affected_mobs == 0 || (o.affected_mobs == 1 && as == null) || (o.affected_mobs == 2 && as != null);
	}
	
	public static int get_quantity(List<Integer> choices)
	{
		 return choices.get(get_random_choice(choices.size()));
	}
	
	public static double get_bounty(List<Double> choices)
	{
		 return choices.get(get_random_choice(choices.size()));
	}
	
	public static int get_random_choice(int size)
	{
		return rng.nextInt(size);
	}
	
	// turns "5 to 10" into an array 5, 6, 7, 8, 9, 10 etc.
	public static List<Integer> fill_int_properties(String property)
	{
		if (property.equalsIgnoreCase("")) return null;
		
		property = property.replaceAll(" ", "").toLowerCase();
		
		List<Integer> temp = new ArrayList<Integer>();
		String[] temp1 = property.split(",");
		for (String s : temp1)
		{
			if (s.contains("to"))
			{
				String[] temp2 = s.split("to");
				int low = Math.min(Integer.parseInt(temp2[0]), Integer.parseInt(temp2[1]));
				int high = Math.max(Integer.parseInt(temp2[0]), Integer.parseInt(temp2[1]));
				for (int i = low; i <= high; i++) temp.add(i);
			}
			else temp.add(Integer.parseInt(s));
		}		
		
		return temp;
	}
	
	public static List<Double> fill_double_properties(String property)
	{
		if (property.equalsIgnoreCase("")) return null;
		
		property = property.replaceAll(" ", "").toLowerCase();
		
		List<Double> temp = new ArrayList<Double>();
		String[] temp1 = property.split(",");
		for (String s : temp1)
		{
			if (s.contains("to"))
			{
				String[] temp2 = s.split("to");
				double low = Math.min(Double.parseDouble(temp2[0]), Double.parseDouble(temp2[1]));
				double high = Math.max(Double.parseDouble(temp2[0]), Double.parseDouble(temp2[1]));
				for (double i = low; i <= high; i++) temp.add(i);
			}
			else temp.add(Double.parseDouble(s));
		}			
		return temp;
	}
	
	// turns "above 5" into a number condition array
	public static List<Number_condition> fill_number_values(String number_condition)
	{
		List<Number_condition> values = new ArrayList<Number_condition>();
		String[] temp = number_condition.split(",");
		for (String s : temp) values.add(new Number_condition(s));
		return values;
	}
	
	// checks if the number condition ("above 5") is true for the value
	public static boolean matches_number_condition(List<Number_condition> values, int value)
	{
		if (values == null) return true;
		for (Number_condition nc : values) if (nc.matches_number(value)) return true;
		
		return false;
	}
	
	public static int return_int_from_array(List<Integer> values)
	{
		return values.get(get_random_choice(values.size()));
	}
	
	public static Boolean return_bool_from_string(String s)
	{
		if (s.equalsIgnoreCase("default")) return null;
		if (s.equalsIgnoreCase("yes")) return true;
		if (s.equalsIgnoreCase("no")) return false;
		if (s.equalsIgnoreCase("random")) return rng.nextBoolean();
		return true;
	}
	
	// turns multiple values in one line into an array
	public static List<String> fill_string_values(String string_condition)
	{
		List<String> values = new ArrayList<String>();
		string_condition = string_condition.toUpperCase();
		String[] temp = string_condition.split(",");
		for (String s : temp) values.add(s);
		return values;
	}
	
	public static boolean matches_string(List<String> values, String value)
	{
		if (values == null) return true;
		
		if (values.contains(value.toUpperCase())) return true;
		return false;
	}
	
	public static void setup_mob(Creature_data cd, LivingEntity le, String spawn_reason, int random, Autospawn autospawn, Selected_outcomes previous)
	{
		Selected_outcomes so = new Selected_outcomes();
		if (previous != null)
		{
			spawn_reason = previous.spawn_reason;
			random = previous.random;
		}
		
		Outcome o = previous != null ? get_previous_outcome(cd, previous.properties) : get_mob_properties_outcome(cd, le, spawn_reason, random, autospawn);
		Mob_properties props = merge_mob_properties(o, cd, autospawn);
		if (o != null) so.properties = o.outcome_id;
		
		o = previous != null ? get_previous_outcome(cd, previous.potions) : get_potion_effects_outcome(cd, le, spawn_reason, random, autospawn);
		Collection<PotionEffect> potion_effects = merge_potion_effects(o, cd, autospawn);
		if (o != null) so.potions = o.outcome_id;
		
		Drops drops = null;
		HashMap<String, Damage_value> damage_properties = null;		
				
		if ((cd.gen_drops_check_type != 1 && autospawn == null) || cd.as_drops_check_type != 1 && autospawn != null)
		{
			o = previous != null ? get_previous_outcome(cd, previous.drops) : get_drops_outcome(cd, le, spawn_reason, null, random, autospawn);			
			drops = merge_drops(o, cd, autospawn);
			if (o != null) so.drops = o.outcome_id;
		}
		
		if ((cd.gen_damages_check_type != 1 && autospawn == null) || cd.as_damages_check_type != 1 && autospawn != null)
		{
			o = previous != null ? get_previous_outcome(cd, previous.damages) : get_damages_outcome(cd, le, spawn_reason, null, random, autospawn);
			damage_properties = merge_damage_properties(o, cd, autospawn);
			if (o != null) so.damages = o.outcome_id;
		}
		
		if (props == null && potion_effects == null && drops == null && damage_properties == null) 
		{
			if (Config.log_level > 1) log("No default outcome for " + le.getType().name() + "  - vanilla mob spawned!");
			Mob mob = new Mob(props, drops, damage_properties, spawn_reason, random, so);
			Main.all_mobs.put(le, mob);
			autospawn = null;
			return;
		}

		Mob mob = new Mob(props, drops, damage_properties, spawn_reason, random, so);
		
		if (potion_effects != null) le.addPotionEffects(potion_effects);		
		
		Main.all_mobs.put(le, mob);	
		
		if (mob.boss_mob != null && mob.boss_mob) le.getWorld().playEffect(le.getLocation(), Effect.MOBSPAWNER_FLAMES, 100);
		
		if (props == null) return;			
		
		if (props.max_lifetime != null)
		{
			int i = return_int_from_array(props.max_lifetime);
			Calendar cal = Calendar.getInstance();
			cal.add(Calendar.SECOND, i);
			Main.mobs_with_lifetimes.put(le, cal);
		}
		
		if (le instanceof Slime)
		{
			Slime slime = (Slime)le;
			if (!spawn_reason.equalsIgnoreCase("SLIME_SPLIT") && props.size != null) slime.setSize(return_int_from_array(props.size));
			if (props.hp_per_size != null) mob.hp = slime.getSize() * return_int_from_array(props.hp_per_size);
		}	
			
		if (le instanceof Animals && props.adult != null)
		{
			Animals animal = (Animals)le;
			boolean b = return_bool_from_string(props.adult);
		
			if (b == true) animal.setAdult(); else animal.setBaby();
		}
			
		if (le instanceof Pig && props.saddled != null)
		{
			((Pig)le).setSaddle(return_bool_from_string(props.saddled));
		}
		else if (le instanceof PigZombie && props.angry != null)
		{
			((PigZombie)le).setAngry(return_bool_from_string(props.angry));
		}
		else if (le instanceof Wolf)
		{
			Wolf wolf = (Wolf)le;			
			if (props.angry != null) wolf.setAngry(return_bool_from_string(props.angry));
			if (!wolf.isAngry())
			{
				if (props.tamed != null) wolf.setTamed(return_bool_from_string(props.tamed));
				if (props.can_be_tamed != null && !return_bool_from_string(props.can_be_tamed)) wolf.setTamed(false);
				if (wolf.isTamed() && props.tamed_hp != null) mob.hp = return_int_from_array(props.tamed_hp);
			}
			else wolf.damage(0, get_nearby_player(wolf));
		}
		else if (le instanceof Sheep)
		{
			Sheep sheep = (Sheep)le;
			if (props.sheared != null) sheep.setSheared(return_bool_from_string(props.sheared));
			if (props.can_grow_wool != null && !return_bool_from_string(props.can_grow_wool)) sheep.setSheared(false);
			
			DyeColor dc = set_wool_colour(props.wool_colours);
			if (dc != null) sheep.setColor(dc);
		}
		else if (le instanceof Ocelot && props.ocelot_types != null)
		{
			Ocelot.Type ot = set_ocelot_type(props.ocelot_types);
			if (ot != null) ((Ocelot)le).setCatType(ot);
		}
		else if (le instanceof Villager && props.villager_types != null)
		{
			Villager.Profession prof = set_villager_type(props.villager_types);
			if (prof != null) ((Villager)le).setProfession(prof);			
		}
		else if (le instanceof Creeper && props.powered != null) 
		{
			((Creeper)le).setPowered(return_bool_from_string(props.powered));		
		}
	}
	
	public static void convert_chunk(Chunk c)
	{
		for (Entity e : c.getEntities())
		{
			if (e instanceof LivingEntity)
			{				
				Creature_data cd = Main.tracked_mobs.get(e.getType().name());
				if (cd == null || cd.reload_behaviour == 2) continue;
				// not tracked or admin wants vanilla
				
				LivingEntity le = (LivingEntity)e;
				
				if (Main.all_mobs.containsKey(le)) continue;
				// already tracked
				
				if (cd.reload_behaviour == 0) // remove the mob
				{					
					le.remove();
					continue;
				}
				else if (cd.reload_behaviour == 1) // kill the mob
				{					
					le.damage(1000);
					continue;
				}
				else if (cd.reload_behaviour == 3) // find the previous outcomes used
				{
					Selected_outcomes so = Main.previous_mobs.get(le.getUniqueId());
					if (so == null) continue;
					setup_mob(cd, le, so.spawn_reason, so.random, null, so);
					
				}
				else if (cd.reload_behaviour == 4) // recalculate conditions using current values
				{
					int random = rng.nextInt(100) + 1;						
					setup_mob(cd, le, "NATURAL", random, null, null);
				}
			}
		}
	}
	
	// end general helpers
	
	// creature spawn helpers
	
	public static Outcome get_previous_outcome(Creature_data cd, int outcome_id)
	{
		if (cd.outcomes != null && outcome_id != -1)
		{
			for (Outcome o : cd.outcomes) if (o.outcome_id == outcome_id) return o;
		}
		return null;
	}
	
	public static boolean merge_spawning(Creature_data cd, LivingEntity le, String spawn_reason, int random, Autospawn as)
	{
		int fallthrough = as != null ? cd.as_fallthrough_type : cd.gen_fallthrough_type;
		Boolean b = can_spawn(cd, le, spawn_reason, random, as);
		if (b != null) return b;
		
		if (fallthrough > 0 && use_outcome(cd.default_outcome, as)) return cd.default_outcome.spawn;
		
		return true;
	}
	
	static Boolean can_spawn(Creature_data cd, LivingEntity le, String spawn_reason, int random, Autospawn as)
	{
		if (cd.outcomes != null)
		{
			for (Outcome o : cd.outcomes)
			{
				if (!o.spawn && use_outcome(o, as))
				{
					boolean found = true;
					for (Condition c : o.conditions) if (!c.check(le, le.getWorld(), le.getLocation(), spawn_reason, null, random, as)) found = false;
					if (found) return o.spawn;
				}
			}
		}
		return null;
	}

	public static Mob_properties merge_mob_properties(Outcome o, Creature_data cd, Autospawn as)
	{
		int fallthrough = as != null ? cd.as_fallthrough_type : cd.gen_fallthrough_type;
		
		if (o == null)
		{
			if (fallthrough == 0) return null;
			
			if (use_outcome(cd.default_outcome, as)) return cd.default_outcome.mob_properties; else return null;
		}
		else
		{
			if (fallthrough == 2 && use_outcome(cd.default_outcome, as) && cd.default_outcome.mob_properties != null)
			{
				if (o.mob_properties != null)
				{
					if (o.mob_properties.hp == null) o.mob_properties.hp = cd.default_outcome.mob_properties.hp;
					if (o.mob_properties.damage == null) o.mob_properties.damage = cd.default_outcome.mob_properties.damage;
					if (o.mob_properties.villager_types == null) o.mob_properties.villager_types = cd.default_outcome.mob_properties.villager_types;
					if (o.mob_properties.ocelot_types == null) o.mob_properties.ocelot_types = cd.default_outcome.mob_properties.ocelot_types;
					if (o.mob_properties.burn_duration == null) o.mob_properties.burn_duration = cd.default_outcome.mob_properties.burn_duration;
					if (o.mob_properties.can_burn == null) o.mob_properties.can_burn = cd.default_outcome.mob_properties.can_burn;
					if (o.mob_properties.safe == null) o.mob_properties.safe = cd.default_outcome.mob_properties.safe;
					if (o.mob_properties.adult == null) o.mob_properties.adult = cd.default_outcome.mob_properties.adult;
					if (o.mob_properties.can_breed == null) o.mob_properties.can_breed = cd.default_outcome.mob_properties.can_breed;
					if (o.mob_properties.can_heal == null) o.mob_properties.can_heal = cd.default_outcome.mob_properties.can_heal;
					if (o.mob_properties.can_overheal == null) o.mob_properties.can_overheal = cd.default_outcome.mob_properties.can_overheal;
					if (o.mob_properties.size == null) o.mob_properties.size = cd.default_outcome.mob_properties.size;
					if (o.mob_properties.hp_per_size == null) o.mob_properties.hp_per_size = cd.default_outcome.mob_properties.hp_per_size;
					if (o.mob_properties.split_into == null) o.mob_properties.split_into = cd.default_outcome.mob_properties.split_into;
					if (o.mob_properties.wool_colours == null) o.mob_properties.wool_colours = cd.default_outcome.mob_properties.wool_colours;
					if (o.mob_properties.can_be_dyed == null) o.mob_properties.can_be_dyed = cd.default_outcome.mob_properties.can_be_dyed;
					if (o.mob_properties.sheared == null) o.mob_properties.sheared = cd.default_outcome.mob_properties.sheared;
					if (o.mob_properties.can_grow_wool == null) o.mob_properties.can_grow_wool = cd.default_outcome.mob_properties.can_grow_wool;
					if (o.mob_properties.can_graze == null) o.mob_properties.can_graze = cd.default_outcome.mob_properties.can_graze;
					if (o.mob_properties.can_be_sheared == null) o.mob_properties.can_be_sheared = cd.default_outcome.mob_properties.can_be_sheared;
					if (o.mob_properties.tamed == null) o.mob_properties.tamed = cd.default_outcome.mob_properties.tamed;
					if (o.mob_properties.angry == null) o.mob_properties.angry = cd.default_outcome.mob_properties.angry;
					if (o.mob_properties.tamed_hp == null) o.mob_properties.tamed_hp = cd.default_outcome.mob_properties.tamed_hp;
					if (o.mob_properties.can_be_tamed == null) o.mob_properties.can_be_tamed = cd.default_outcome.mob_properties.can_be_tamed;
					if (o.mob_properties.saddled == null) o.mob_properties.saddled = cd.default_outcome.mob_properties.saddled;
					if (o.mob_properties.can_be_saddled == null) o.mob_properties.can_be_saddled = cd.default_outcome.mob_properties.can_be_saddled;
					if (o.mob_properties.can_become_pig_zombie == null) o.mob_properties.can_become_pig_zombie = cd.default_outcome.mob_properties.can_become_pig_zombie;
					if (o.mob_properties.powered == null) o.mob_properties.powered = cd.default_outcome.mob_properties.powered;
					if (o.mob_properties.can_become_powered_creeper == null) o.mob_properties.can_become_powered_creeper = cd.default_outcome.mob_properties.can_become_powered_creeper;
					if (o.mob_properties.fiery_explosion == null) o.mob_properties.fiery_explosion = cd.default_outcome.mob_properties.fiery_explosion;
					if (o.mob_properties.can_move_blocks == null) o.mob_properties.can_move_blocks = cd.default_outcome.mob_properties.can_move_blocks;
					if (o.mob_properties.can_teleport == null) o.mob_properties.can_teleport = cd.default_outcome.mob_properties.can_teleport;
					if (o.mob_properties.can_create_portal == null) o.mob_properties.can_create_portal = cd.default_outcome.mob_properties.can_create_portal;
					if (o.mob_properties.can_destroy_blocks == null) o.mob_properties.can_destroy_blocks = cd.default_outcome.mob_properties.can_destroy_blocks;
				}
				else o.mob_properties = cd.default_outcome.mob_properties;
				
				return o.mob_properties;
			}
			else return o.mob_properties;
		}
	}

	public static Outcome get_mob_properties_outcome(Creature_data cd, LivingEntity le, String spawn_reason, int random, Autospawn as)
	{
		if (cd.outcomes != null)
		{
			for (Outcome o : cd.outcomes)
			{
				if (o.conditions != null && o.mob_properties != null && use_outcome(o, as))
				{
					boolean found = true;
					for (Condition c : o.conditions) if (!c.check(le, le.getWorld(), le.getLocation(), spawn_reason, null, random, as)) found = false;
					if (found) return o;
				}
			}
		}
		return null;
	}
	
	public static Collection<PotionEffect> merge_potion_effects(Outcome o, Creature_data cd, Autospawn as)
	{
		int fallthrough = as != null ? cd.as_fallthrough_type : cd.gen_fallthrough_type;
		
		if (o != null) return set_potion_effects(o.potion_effects, o.all_potion_effects);
		
		if (fallthrough > 0 && use_outcome(cd.default_outcome, as)) return set_potion_effects(cd.default_outcome.potion_effects, cd.default_outcome.all_potion_effects);
		
		return null;
	}
	
	public static Outcome get_potion_effects_outcome(Creature_data cd, LivingEntity le, String spawn_reason, int random, Autospawn as)
	{
		if (cd.outcomes != null)
		{
			for (Outcome o : cd.outcomes)
			{
				if (o.conditions != null && o.potion_effects != null && use_outcome(o, as))
				{
					boolean found = true;
					for (Condition c : o.conditions) if (!c.check(le, le.getWorld(), le.getLocation(), spawn_reason, null, random, as)) found = false;
					if (found) return o;
				}
			}
		}
		return null;
	}
	
	public static Drops merge_drops(Outcome o, Creature_data cd, Autospawn as)
	{
		int fallthrough = as != null ? cd.as_fallthrough_type : cd.gen_fallthrough_type;
		
		if (o == null)
		{
			if (fallthrough == 0) return null;
			
			if (use_outcome(cd.default_outcome, as)) return cd.default_outcome.drops; else return null;
		}
		else
		{
			if (fallthrough == 2 && use_outcome(cd.default_outcome, as) && cd.default_outcome.drops != null)
			{
				if (o.drops != null)
				{
					if (o.drops.items == null) o.drops.items = cd.default_outcome.drops.items;
					if (o.drops.exps == null) o.drops.exps = cd.default_outcome.drops.exps;
					if (o.drops.bounties == null) o.drops.bounties = cd.default_outcome.drops.bounties;
				}
				else o.drops = cd.default_outcome.drops;
				return o.drops;
			}
			else return o.drops;
		}
	}
	
	public static Outcome get_drops_outcome(Creature_data cd, LivingEntity le, String spawn_reason, Player p, int random, Autospawn as)
	{
		if (cd.outcomes != null)
		{
			for (Outcome o : cd.outcomes)
			{
				if (o.conditions != null && o.drops != null && use_outcome(o, as))
				{
					boolean found = true;
					for (Condition c : o.conditions) if (!c.check(le, le.getWorld(), le.getLocation(), spawn_reason, p, random, as)) found = false;
					if (found) return o;
				}
			}
		}
		return null;
	}
	
	public static HashMap<String, Damage_value> merge_damage_properties(Outcome o, Creature_data cd, Autospawn as)
	{
		int fallthrough = as != null ? cd.as_fallthrough_type : cd.gen_fallthrough_type;
		
		if (o == null)
		{
			if (fallthrough == 0) return null;
			
			if (use_outcome(cd.default_outcome, as)) return cd.default_outcome.damage_properties; else return null;
		}
		else
		{
			if (fallthrough == 2 && use_outcome(cd.default_outcome, as) && cd.default_outcome.damage_properties != null)
			{
				if (o.damage_properties != null)
				{
					Damage_value dv;
					if (o.damage_properties.get("BLOCK_EXPLOSION") == null)
					{
						dv = cd.default_outcome.damage_properties.get("BLOCK_EXPLOSION");
						if (dv != null) o.damage_properties.put("BLOCK_EXPLOSION", dv);
					}
					if (o.damage_properties.get("CONTACT") == null)
					{
						dv = cd.default_outcome.damage_properties.get("CONTACT");
						if (dv != null) o.damage_properties.put("CONTACT", dv);
					}
					if (o.damage_properties.get("CUSTOM") == null)
					{
						dv = cd.default_outcome.damage_properties.get("CUSTOM");
						if (dv != null) o.damage_properties.put("CUSTOM", dv);
					}
					if (o.damage_properties.get("DROWNING") == null)
					{
						dv = cd.default_outcome.damage_properties.get("DROWNING");
						if (dv != null) o.damage_properties.put("DROWNING", dv);
					}
					if (o.damage_properties.get("ENTITY_ATTACK") == null)
					{
						dv = cd.default_outcome.damage_properties.get("ENTITY_ATTACK");
						if (dv != null) o.damage_properties.put("ENTITY_ATTACK", dv);
					}
					if (o.damage_properties.get("ENTITY_EXPLOSION") == null)
					{
						dv = cd.default_outcome.damage_properties.get("ENTITY_EXPLOSION");
						if (dv != null) o.damage_properties.put("ENTITY_EXPLOSION", dv);
					}
					if (o.damage_properties.get("FALL") == null)
					{
						dv = cd.default_outcome.damage_properties.get("FALL");
						if (dv != null) o.damage_properties.put("FALL", dv);
					}
					if (o.damage_properties.get("FIRE") == null)
					{
						dv = cd.default_outcome.damage_properties.get("FIRE");
						if (dv != null) o.damage_properties.put("FIRE", dv);
					}
					if (o.damage_properties.get("FIRE_TICK") == null)
					{
						dv = cd.default_outcome.damage_properties.get("FIRE_TICK");
						if (dv != null) o.damage_properties.put("FIRE_TICK", dv);
					}
					if (o.damage_properties.get("LAVA") == null)
					{
						dv = cd.default_outcome.damage_properties.get("LAVA");
						if (dv != null) o.damage_properties.put("LAVA", dv);
					}
					if (o.damage_properties.get("LIGHTNING") == null)
					{
						dv = cd.default_outcome.damage_properties.get("LIGHTNING");
						if (dv != null) o.damage_properties.put("LIGHTNING", dv);
					}
					if (o.damage_properties.get("MAGIC") == null)
					{
						dv = cd.default_outcome.damage_properties.get("MAGIC");
						if (dv != null) o.damage_properties.put("MAGIC", dv);
					}
					if (o.damage_properties.get("MELTING") == null)
					{
						dv = cd.default_outcome.damage_properties.get("MELTING");
						if (dv != null) o.damage_properties.put("MELTING", dv);
					}
					if (o.damage_properties.get("POISON") == null)
					{
						dv = cd.default_outcome.damage_properties.get("POISON");
						if (dv != null) o.damage_properties.put("POISON", dv);
					}
					if (o.damage_properties.get("PROJECTILE") == null)
					{
						dv = cd.default_outcome.damage_properties.get("PROJECTILE");
						if (dv != null) o.damage_properties.put("PROJECTILE", dv);
					}
					if (o.damage_properties.get("STARVATION") == null)
					{
						dv = cd.default_outcome.damage_properties.get("STARVATION");
						if (dv != null) o.damage_properties.put("STARVATION", dv);
					}
					if (o.damage_properties.get("SUFFOCATION") == null)
					{
						dv = cd.default_outcome.damage_properties.get("SUFFOCATION");
						if (dv != null) o.damage_properties.put("SUFFOCATION", dv);
					}
					if (o.damage_properties.get("SUICIDE") == null)
					{
						dv = cd.default_outcome.damage_properties.get("SUICIDE");
						if (dv != null) o.damage_properties.put("SUICIDE", dv);
					}
					if (o.damage_properties.get("VOID") == null)
					{
						dv = cd.default_outcome.damage_properties.get("VOID");
						if (dv != null) o.damage_properties.put("VOID", dv);
					}
				}
				else o.damage_properties = cd.default_outcome.damage_properties;
				
				return o.damage_properties;
			}
			else return o.damage_properties;
		}
	}
	
	public static Outcome get_damages_outcome(Creature_data cd, LivingEntity le, String spawn_reason, Player p, int random, Autospawn as)
	{
		if (cd.outcomes != null)
		{
			for (Outcome o : cd.outcomes)
			{
				if (o.conditions != null && o.damage_properties != null && use_outcome(o, as))
				{
					boolean found = true;
					for (Condition c : o.conditions) if (!c.check(le, le.getWorld(), le.getLocation(), spawn_reason, p, random, as)) found = false;
					if (found) return o;
				}
			}
		}
		return null;
	}
	
	static Collection<PotionEffect> set_potion_effects(ArrayList<Potion_effect> potion_effects, boolean all_potion_effects)
	{
		if (potion_effects == null) return null;
		
		List<PotionEffect> col = new ArrayList<PotionEffect>();
		if (all_potion_effects)
		{
			for (Potion_effect pe : potion_effects) 
				col.add(new PotionEffect(PotionEffectType.getByName(pe.name), 
						pe.duration.get(get_random_choice(pe.duration.size())), pe.level.get(get_random_choice(pe.level.size()))));
		}
		else
		{
			Potion_effect pe = potion_effects.get(get_random_choice(potion_effects.size()));
			col.add(new PotionEffect(PotionEffectType.getByName(pe.name), 
					pe.duration.get(get_random_choice(pe.duration.size())), pe.level.get(get_random_choice(pe.level.size()))));
		}
		return col;
	}
	
	public static DyeColor set_wool_colour(String[] wool_colours)
	{
		if (wool_colours == null) return null;
		
		String s = wool_colours[get_random_choice(wool_colours.length)].trim().toUpperCase();
		if (s.equalsIgnoreCase("default")) return null;
		else if (s.equalsIgnoreCase("random")) return DyeColor.values()[get_random_choice(DyeColor.values().length)];
		else return DyeColor.valueOf(s);
	}
	
	public static Ocelot.Type set_ocelot_type(String[] ocelot_types)
	{
		String s = ocelot_types[get_random_choice(ocelot_types.length)].trim().toUpperCase();
		if (s.equalsIgnoreCase("default")) return null;
		else if (s.equalsIgnoreCase("random")) return Ocelot.Type.values()[get_random_choice(Ocelot.Type.values().length)];
		else return Ocelot.Type.valueOf(s);
	}
	
	public static Villager.Profession set_villager_type(String[] villager_types)
	{
		String s = villager_types[get_random_choice(villager_types.length)].trim().toUpperCase();
		if (s.equalsIgnoreCase("default")) return null;
		else if (s.equalsIgnoreCase("random")) return Villager.Profession.values()[get_random_choice(Villager.Profession.values().length)];
		else return Villager.Profession.valueOf(s);
	}
	
	public static LivingEntity get_nearby_player(Entity entity)
	{
		List<Entity> temp = new ArrayList<Entity>();
		for (Entity e : entity.getNearbyEntities(20, 20, 20)) if (e instanceof Player) temp.add(e);
		
		if (temp.size() > 0) return (LivingEntity)temp.get(get_random_choice(temp.size()));
		return null;
	}
	
	// end creature spawn helpers
	
	// creature death helpers
	
	public static void send_death_message(Death_message dm, String mob_name, int exp, Player p, List<ItemStack> drops, int given_bounty)
	{
		String s = dm.message;					

		s = s.replace("^mob^", mob_name);
		s = s.replace("^exp^", Integer.toString(exp));
		
		if (s.contains("^player^") && p != null) s = s.replace("^player^", p.getDisplayName());
		
		if ((s.contains("^money^") || s.contains("^total_money^")) && Main.economy != null && p != null)
		{
			s = s.replace("^total_money^", f.format(Main.economy.getBalance(p.getName())));
			s = s.replace("^money^", Integer.toString(given_bounty));
		}
		
		if (s.contains("^item_names^") || s.contains("^item_amounts^"))
		{
			String item_names = "";
			String item_amounts = "";
			
			for (ItemStack is : drops)
			{
				String ss = is.getType().name().toLowerCase();
				item_names += ", " + ss;
				item_amounts += ", " + is.getAmount() + " x " + ss;
			}
			s = s.replace("^item_names^", item_names);
			s = s.replace("^item_amounts^", item_amounts);
		}
		
		if (p != null)
		{
			if (dm.announce_messages) p.getServer().broadcastMessage(s);
			else
			{
				p.sendMessage(s);
				if (dm.log_messages) log(s);
			}
		}
	}
	
	// end creature death helpers
	
	// autospawner helpers
	
	public static void check_above_ground_block(int xb, int yb, int zb, World w, Autospawn_location sl, List<String> temp_biomes, List<String> temp_regions, List<String> safe_blocks)
	{
		for (int x = xb - sl.xrange; x <= xb + sl.xrange; x++)
		{
			if (x <= xb - sl.min_xrange || x >= xb + sl.min_xrange)
			{
				for (int z = zb - sl.zrange; z <= zb + sl.zrange; z++)
				{
					if (z <= zb - sl.min_zrange || z >= zb + sl.min_zrange)
					{
						for (int y = yb - sl.yrange; y <= yb + sl.yrange; y++)
						{
							if ((y <= yb - sl.min_yrange || y >= yb + sl.min_yrange) && y < w.getMaxHeight())
							{
								if (is_safe_above_ground_block(w.getBlockAt(x, y, z), sl.loaded_chunks_only,
										temp_biomes, temp_regions, w)) safe_blocks.add(x + "," + y + "," + z);
							}
						}
					}
				}
			}
		}
	}
	
	public static void check_below_ground_block(int xb, int yb, int zb, World w, Autospawn_location sl, List<String> temp_biomes, List<String> temp_regions, List<String> safe_blocks)
	{
		for (int x = xb - sl.xrange; x <= xb + sl.xrange; x++)
		{
			if (x <= xb - sl.min_xrange || x >= xb + sl.min_xrange)
			{
				for (int z = zb - sl.zrange; z <= zb + sl.zrange; z++)
				{
					if (z <= zb - sl.min_zrange || z >= zb + sl.min_zrange)
					{
						for (int y = yb - sl.yrange; y <= yb + sl.yrange; y++)
						{
							if ((y <= yb - sl.min_yrange || y >= yb + sl.min_yrange) && y < w.getMaxHeight())
							{
								if (is_safe_below_ground_block(w.getBlockAt(x, y, z), sl.loaded_chunks_only,
										temp_biomes, temp_regions, w)) safe_blocks.add(x + "," + y + "," + z);
							}
						}
					}
				}
			}
		}
	}
	
	public static boolean is_safe_above_ground_block(Block b, boolean loaded_chunks_only, List<String> biomes, List<String> regions, World w)
	{
		if (biomes != null)
		{
			String biome = null;
			if (Main.tc != null)
			{
				BukkitWorld bw = Main.tc.worlds.get(w.getUID());
				if (bw != null)
				{
					int id = bw.getBiome(b.getX(), b.getZ());
					biome = bw.getBiomeById(id).getName().toUpperCase();
				}
			}
		
			if (biome == null) biome = b.getBiome().name();
		
			if (!biomes.contains(biome)) return false;
		}
		
		if (regions != null && Main.world_guard != null)
		{
			for (String s : regions)
			{
				if (Main.world_guard.getRegionManager(w).getRegions().get(s) == null) return false;
			}
		}
		
		if (b.getLightFromSky() > 13 && b.getType() == Material.AIR && b.getRelative(BlockFace.UP).getType() == Material.AIR
				&& b.getRelative(BlockFace.DOWN).getType() != Material.AIR)
		{
			if (loaded_chunks_only)
			{
				if (b.getChunk().isLoaded()) return true;
			}
			else return true;
		}
		return false;
	}
		
	public static boolean is_safe_below_ground_block(Block b, boolean chunk_loaded_only, List<String> biomes, List<String> regions, World w)
	{
		if (biomes != null)
		{
			String biome = null;
			if (Main.tc != null)
			{
				BukkitWorld bw = Main.tc.worlds.get(w.getUID());
				if (bw != null)
				{
					int id = bw.getBiome(b.getX(), b.getZ());
					biome = bw.getBiomeById(id).getName().toUpperCase();
				}
			}
		
			if (biome == null) biome = b.getBiome().name();
		
			if (!biomes.contains(biome)) return false;
		}
		
		if (Main.world_guard != null && regions != null)
		{
			for (String s : regions)
			{
				if (Main.world_guard.getRegionManager(w).getRegions().get(s) == null) return false;
			}
		}
		if (b.getLightFromSky() < 14 && b.getType() == Material.AIR && b.getRelative(BlockFace.UP).getType() == Material.AIR
				&& b.getRelative(BlockFace.DOWN).getType() != Material.AIR)
		{
			if (chunk_loaded_only)
			{
				if (b.getChunk().isLoaded()) return true;
			}
			else return true;
		}
		return false;
	}
	
	// end autospawner helpers
}

/*
	
public static boolean matches_quantity(Old_item item, int amount)
{
	if (item.quantities == null) return true;
	if (item.quantities.contains(amount)) return true;
	return false;
}


public static boolean matches_enchantments(ItemStack is, Old_item i)
{
	if (is.getEnchantments().size() == 0) return false;

	boolean validtemp = false;
	for (Enchantment e : i.enchantments.keySet())
	{									
		int ench_level = is.getEnchantmentLevel(Enchantment.getByName(e.getName().toUpperCase()));
		validtemp = i.enchantments.get(e).contains(ench_level);
		
		if (!i.match_all_enchantments)
		{
			if (validtemp) return true;
		}
		else
		{
			if (!validtemp) return false;
		}
	}	
	return validtemp;
}*/

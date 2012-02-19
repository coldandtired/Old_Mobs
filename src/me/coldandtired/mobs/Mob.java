package me.coldandtired.mobs;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Map;

public class Mob 
{
	
	Date spawned_at;
	int hp;
	int tamed_hp;
	int damage;
	String spawn_reason;
	int random = Utils.rng.nextInt(100) + 1;
	boolean can_be_dyed;
	boolean can_be_sheared;
	boolean can_be_tamed;
	boolean can_become_pig_zombie;
	boolean can_become_powered = true;
	boolean can_burn;
	boolean can_create_portal;
	boolean can_destroy_blocks = true;
	boolean can_move_blocks;
	boolean can_grow_wool;
	boolean can_remove_grass;
	boolean can_split;	
	boolean safe;
	ArrayList<Con_group> burn_rules;
	ArrayList<Death_action> death_actions;
	int burn_duration;
	boolean fire_explosion;
	//teleport enderman

	Mob(Map<String, Object> general, Map<String, Object> unique, String spawn_reason)
	{
		this.spawn_reason = spawn_reason;
		spawned_at = Calendar.getInstance().getTime();
		
		hp = Utils.set_int_property(-1, general, unique, "hp");
		tamed_hp = Utils.set_int_property(-1, general, unique, "tamed_hp");		
		damage = Utils.set_int_property(-1, general, unique, "damages");
		safe = Utils.set_boolean_property(false, general, unique, "safe");
		can_be_dyed = Utils.set_boolean_property(true, general, unique, "can_be_dyed");
		can_be_sheared = Utils.set_boolean_property(true, general, unique, "can_be_sheared");
		can_be_tamed = Utils.set_boolean_property(true, general, unique, "can_be_tamed");
		can_become_pig_zombie = Utils.set_boolean_property(true, general, unique, "can_become_pig_zombie");
		can_become_powered = Utils.set_boolean_property(true, general, unique, "can_become_powered");
		can_burn = Utils.set_burn_property(true, general, unique);
		can_create_portal = Utils.set_boolean_property(true, general, unique, "can_create_portal");
		can_destroy_blocks = Utils.set_boolean_property(true, general, unique, "can_destroy_blocks");
		can_move_blocks = Utils.set_boolean_property(true, general, unique, "can_move_blocks");	
		can_grow_wool = Utils.set_boolean_property(true, general, unique, "can_grow_wool");		
		can_remove_grass = Utils.set_boolean_property(true, general, unique, "can_remove_grass");		
		can_split = Utils.set_boolean_property(true, general, unique, "can_split");		
		fire_explosion = Utils.set_boolean_property(false, general, unique, "fire_explosion");		
		death_actions = Utils.set_death_actions(general, unique, random);
		burn_rules = Utils.set_burn_rules(general, unique, random);
		burn_duration = Utils.set_burn_ticks(8, general, unique);		
	}
}

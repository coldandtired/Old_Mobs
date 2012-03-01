package me.coldandtired.mobs;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Map;

public class Mob 
{
	
	Date spawned_at;
	public int hp;
	public int max_hp;
	public int size;
	public int split_into;
	public int tamed_hp;
	public int damage;
	public String spawn_reason;
	public int random = Utils.rng.nextInt(100) + 1;
	public boolean can_be_dyed;
	public boolean can_be_sheared;
	public boolean can_be_tamed;
	public boolean can_become_pig_zombie;
	public boolean can_become_powered = true;
	public boolean can_burn;
	public boolean can_heal;
	public boolean can_overheal;
	public boolean can_create_portal;
	public boolean can_destroy_blocks = true;
	public boolean can_move_blocks;
	public boolean can_grow_wool;
	public boolean can_remove_grass;
	public boolean can_split;
	public boolean can_teleport;
	public boolean safe;
	public ArrayList<Con_group> burn_rules;
	public ArrayList<Death_action> death_actions;
	public int burn_duration;
	public boolean fire_explosion;
	//slime split count

	public Mob(Map<String, Object> general, Map<String, Object> unique, String spawn_reason)
	{
		this.spawn_reason = spawn_reason;
		spawned_at = Calendar.getInstance().getTime();
		
		hp = Utils.set_int_property(-1, general, unique, "hp");
		max_hp = hp;
		size = Utils.set_int_property(-1, general, unique, "size");
		split_into = Utils.set_int_property(-1, general, unique, "split_into");
		tamed_hp = Utils.set_int_property(-1, general, unique, "tamed_hp");		
		damage = Utils.set_int_property(-1, general, unique, "damages");
		safe = Utils.set_boolean_property(false, general, unique, "safe");
		can_be_dyed = Utils.set_boolean_property(true, general, unique, "can_be_dyed");
		can_be_sheared = Utils.set_boolean_property(true, general, unique, "can_be_sheared");
		can_be_tamed = Utils.set_boolean_property(true, general, unique, "can_be_tamed");
		can_become_pig_zombie = Utils.set_boolean_property(true, general, unique, "can_become_pig_zombie");
		can_become_powered = Utils.set_boolean_property(true, general, unique, "can_become_powered");
		can_heal = Utils.set_boolean_property(true, general, unique, "can_heal");
		can_overheal = Utils.set_boolean_property(false, general, unique, "can_overheal");
		can_burn = Utils.set_burn_property(true, general, unique);
		can_create_portal = Utils.set_boolean_property(true, general, unique, "can_create_portal");
		can_destroy_blocks = Utils.set_boolean_property(true, general, unique, "can_destroy_blocks");
		can_move_blocks = Utils.set_boolean_property(true, general, unique, "can_move_blocks");	
		can_grow_wool = Utils.set_boolean_property(true, general, unique, "can_grow_wool");		
		can_remove_grass = Utils.set_boolean_property(true, general, unique, "can_remove_grass");		
		can_split = Utils.set_boolean_property(true, general, unique, "can_split");	
		can_teleport = Utils.set_boolean_property(true, general, unique, "can_teleport");	
		fire_explosion = Utils.set_boolean_property(false, general, unique, "fire_explosion");		
		death_actions = Utils.set_death_actions(general, unique, random);
		burn_rules = Utils.set_burn_rules(general, unique, random);
		burn_duration = Utils.set_burn_ticks(8, general, unique);		
	}
}

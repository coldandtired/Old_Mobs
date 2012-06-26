package me.coldandtired.mobs;

import java.util.HashMap;

import me.coldandtired.mobs.data.Damage_value;
import me.coldandtired.mobs.data.Drops;
import me.coldandtired.mobs.data.Mob_properties;
import me.coldandtired.mobs.data.Selected_outcomes;

public class Mob
{	
	public Integer hp;
	public Integer max_hp;
	public Integer tamed_hp;
	public Integer damage;
	public Integer explosion_size;
	public Integer split_into;
	public String spawn_reason;
	public int random;
	public Boolean can_be_dyed;
	public Boolean can_be_sheared;
	public Boolean can_be_tamed;
	public Boolean can_become_pig_zombie;
	public Boolean can_become_powered_creeper;
	public Boolean can_burn;
	public Boolean can_heal;
	public Boolean can_overheal;
	public Boolean can_create_portal;
	public Boolean can_destroy_blocks;
	public Boolean can_move_blocks;
	public Boolean can_grow_wool;
	public Boolean can_graze;
	public Boolean can_teleport;
	public Boolean safe;
	public Boolean boss_mob;
	public Integer burn_duration;
	public Boolean fiery_explosion;
	public HashMap<String, Damage_value> damage_properties = null;
	public Drops drops = null;
	public Selected_outcomes selected_outcomes = null;
	public String autospawn_id = null;
	
	public Mob(Mob_properties props, Drops drops, HashMap<String, Damage_value> damage_properties, String spawn_reason, int random, Selected_outcomes so, String autospawn_id)
	{
		selected_outcomes = so;
		so.spawn_reason = spawn_reason;
		so.random = random;
		
		if (props != null)
		{
			if (props.hp != null)
			{
				hp = L.return_int_from_array(props.hp);
				max_hp = hp;
			}
			
			if (props.tamed_hp != null) tamed_hp = L.return_int_from_array(props.tamed_hp);
			if (props.damage != null) damage = L.return_int_from_array(props.damage);
			if (props.explosion_size != null) explosion_size = L.return_int_from_array(props.explosion_size);
			if (props.split_into != null) split_into = L.return_int_from_array(props.split_into);
			if (props.burn_duration != null) burn_duration = L.return_int_from_array(props.burn_duration);
			
			if (props.boss_mob != null) boss_mob = L.return_bool_from_string(props.boss_mob);
			if (props.can_be_dyed != null) can_be_dyed = L.return_bool_from_string(props.can_be_dyed);
			if (props.can_be_sheared != null) can_be_sheared = L.return_bool_from_string(props.can_be_sheared);
			if (props.can_be_tamed != null) can_be_tamed = L.return_bool_from_string(props.can_be_tamed);
			if (props.can_become_pig_zombie != null) can_become_pig_zombie = L.return_bool_from_string(props.can_become_pig_zombie);
			if (props.can_become_powered_creeper != null) can_become_powered_creeper = L.return_bool_from_string(props.can_become_powered_creeper);
			if (props.can_burn != null) can_burn = L.return_bool_from_string(props.can_burn);
			if (props.can_heal != null) can_heal = L.return_bool_from_string(props.can_heal);
			if (props.can_overheal != null) can_overheal = L.return_bool_from_string(props.can_overheal);
			if (props.can_create_portal != null) can_create_portal = L.return_bool_from_string(props.can_create_portal);
			if (props.can_destroy_blocks != null) can_destroy_blocks = L.return_bool_from_string(props.can_destroy_blocks);
			if (props.can_move_blocks != null) can_move_blocks = L.return_bool_from_string(props.can_move_blocks);
			if (props.can_grow_wool != null) can_grow_wool = L.return_bool_from_string(props.can_grow_wool);
			if (props.can_graze != null) can_graze = L.return_bool_from_string(props.can_graze);
			if (props.can_teleport != null) can_teleport = L.return_bool_from_string(props.can_teleport);
			if (props.safe != null) safe = L.return_bool_from_string(props.safe);
			if (props.fiery_explosion != null) fiery_explosion = L.return_bool_from_string(props.fiery_explosion);
		}
		this.drops = drops;
		this.damage_properties = damage_properties;
		this.spawn_reason = spawn_reason;
		this.autospawn_id = autospawn_id;
		this.random = random;
	}
}

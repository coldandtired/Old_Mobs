package me.coldandtired.mobs.conditions;

import java.util.List;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;

import me.coldandtired.mobs.Condition;
import me.coldandtired.mobs.L;

public class Coordinate implements Condition
{
	private List<Number_condition> values;
	private String subvalue;
	private boolean reversed = false;
	
	public Coordinate(String s, String sub, boolean reversed)
	{
		values = L.fill_number_values(s);
		subvalue = sub;
		this.reversed = reversed;
	}
	
	@Override
	public boolean check(LivingEntity entity, World world, Location loc, String spawn_reason, Player player, int random, String autospawn_id) 
	{
		boolean b = false;
		if (subvalue.equals("x")) b = L.matches_number_condition(values, loc.getBlockX());
		else if (subvalue.equals("y")) b = L.matches_number_condition(values, loc.getBlockY());
		else if (subvalue.equals("z")) b = L.matches_number_condition(values, loc.getBlockZ());
		if (reversed) return !b; else return b; 
	}	
}

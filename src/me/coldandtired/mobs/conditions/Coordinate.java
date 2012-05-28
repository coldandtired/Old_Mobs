package me.coldandtired.mobs.conditions;

import java.util.List;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;

import me.coldandtired.mobs.Condition;
import me.coldandtired.mobs.data.Autospawn;
import me.coldandtired.mobs.L;

public class Coordinate implements Condition
{
	private List<Number_condition> values;
	private String subvalue;
	
	public Coordinate(String s, String sub)
	{
		values = L.fill_number_values(s);
		subvalue = sub;
	}
	
	@Override
	public boolean check(LivingEntity entity, World world, Location loc, String spawn_reason, Player player, int random, Autospawn as) 
	{
		if (subvalue.equals("x")) return L.matches_number_condition(values, loc.getBlockX());
		else if (subvalue.equals("y")) return L.matches_number_condition(values, loc.getBlockY());
		else if (subvalue.equals("z")) return L.matches_number_condition(values, loc.getBlockZ());
		return false;
	}	
}

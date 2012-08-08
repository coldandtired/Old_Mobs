package me.coldandtired.mobs.conditions;

import java.util.List;

import me.coldandtired.mobs.Condition;
import me.coldandtired.mobs.L;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;

public class Lunar_phases implements Condition
{
	private List<Number_condition> values;
	private boolean reversed = false;
	
	public Lunar_phases(String s, boolean reversed)
	{
		values = L.fill_number_values(s);
		this.reversed = reversed;
	}
	
	@Override
	public boolean check(LivingEntity entity, World world, Location loc, String spawn_reason, Player player, int random, String autospawn_id) 
	{
		long days = world.getFullTime()/24000;
		long phase= days % 8;
		boolean b = L.matches_number_condition(values, (int)phase);
		if (reversed) return !b; else return b; 
	}	
}

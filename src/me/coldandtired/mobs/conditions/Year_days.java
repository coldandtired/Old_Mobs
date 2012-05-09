package me.coldandtired.mobs.conditions;

import java.util.List;
import java.util.Calendar;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;

import me.coldandtired.mobs.Condition;
import me.coldandtired.mobs.data.Autospawn;
import me.coldandtired.mobs.L;

public class Year_days implements Condition
{
	private List<Number_condition> values;

	public Year_days(String s)
	{
		values = L.fill_number_values(s);
	}
	
	@Override
	public boolean check(LivingEntity entity, World world, Location loc, String spawn_reason, Player player, int random, Autospawn as) 
	{
		Calendar cal = Calendar.getInstance();
		return L.matches_number_condition(values, cal.get(Calendar.DAY_OF_YEAR));
	}	
}
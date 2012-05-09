package me.coldandtired.mobs.conditions;

import java.util.Calendar;
import java.util.List;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import me.coldandtired.mobs.Condition;
import me.coldandtired.mobs.L;
import me.coldandtired.mobs.data.Autospawn;

public class Dates implements Condition
{
	private List<Number_condition> values;
	
	public Dates(String s)
	{
		values = L.fill_number_values(s);
	}
	
	@Override
	public boolean check(LivingEntity entity, World world, Location loc, String spawn_reason, Player player, int random, Autospawn as) 
	{
		Calendar cal = Calendar.getInstance();
		return L.matches_number_condition(values, cal.get(Calendar.DATE));
	}	
}
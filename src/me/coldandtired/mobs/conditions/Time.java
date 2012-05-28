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

public class Time implements Condition
{
	private List<Number_condition> values;
	private String subvalue;
	
	public Time(String s, String sub)
	{
		values = L.fill_number_values(s);
		subvalue = sub;
	}
	
	@Override
	public boolean check(LivingEntity entity, World world, Location loc, String spawn_reason, Player player, int random, Autospawn as) 
	{
		Calendar cal = Calendar.getInstance();
		
		if (subvalue.equalsIgnoreCase("seconds")) return L.matches_number_condition(values, cal.get(Calendar.SECOND));
		else if (subvalue.equalsIgnoreCase("minutes")) return L.matches_number_condition(values, cal.get(Calendar.MINUTE));
		else if (subvalue.equalsIgnoreCase("hours")) return L.matches_number_condition(values, cal.get(Calendar.HOUR_OF_DAY));
		else if (subvalue.equalsIgnoreCase("days")) return L.matches_number_condition(values, cal.get(Calendar.DAY_OF_WEEK));
		else if (subvalue.equalsIgnoreCase("dates")) return L.matches_number_condition(values, cal.get(Calendar.DATE));
		else if (subvalue.equalsIgnoreCase("months")) return L.matches_number_condition(values, cal.get(Calendar.MONTH) + 1);
		else if (subvalue.equalsIgnoreCase("years")) return L.matches_number_condition(values, cal.get(Calendar.YEAR));
		else if (subvalue.equalsIgnoreCase("month_weeks")) return L.matches_number_condition(values, cal.get(Calendar.WEEK_OF_MONTH));
		else if (subvalue.equalsIgnoreCase("year_days")) return L.matches_number_condition(values, cal.get(Calendar.DAY_OF_YEAR));
		else if (subvalue.equalsIgnoreCase("year_weeks")) return L.matches_number_condition(values, cal.get(Calendar.WEEK_OF_YEAR));
		return false;
	}

}

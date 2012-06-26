package me.coldandtired.mobs.conditions;

import java.util.Calendar;
import java.util.List;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;

import me.coldandtired.mobs.Condition;
import me.coldandtired.mobs.L;

public class Time implements Condition
{
	private List<Number_condition> values;
	private String subvalue;
	private boolean reversed = false;
	
	public Time(String s, String sub, boolean reversed)
	{
		values = L.fill_number_values(s);
		subvalue = sub;
		this.reversed = reversed;
	}
	
	@Override
	public boolean check(LivingEntity entity, World world, Location loc, String spawn_reason, Player player, int random, String autospawn_id) 
	{
		Calendar cal = Calendar.getInstance();
		boolean b = false;
		if (subvalue.equalsIgnoreCase("seconds")) b = L.matches_number_condition(values, cal.get(Calendar.SECOND));
		else if (subvalue.equalsIgnoreCase("minutes")) b = L.matches_number_condition(values, cal.get(Calendar.MINUTE));
		else if (subvalue.equalsIgnoreCase("hours")) b = L.matches_number_condition(values, cal.get(Calendar.HOUR_OF_DAY));
		else if (subvalue.equalsIgnoreCase("days")) b = L.matches_number_condition(values, cal.get(Calendar.DAY_OF_WEEK));
		else if (subvalue.equalsIgnoreCase("dates")) b = L.matches_number_condition(values, cal.get(Calendar.DATE));
		else if (subvalue.equalsIgnoreCase("months")) b = L.matches_number_condition(values, cal.get(Calendar.MONTH) + 1);
		else if (subvalue.equalsIgnoreCase("years")) b = L.matches_number_condition(values, cal.get(Calendar.YEAR));
		else if (subvalue.equalsIgnoreCase("month_weeks")) b = L.matches_number_condition(values, cal.get(Calendar.WEEK_OF_MONTH));
		else if (subvalue.equalsIgnoreCase("year_days")) b = L.matches_number_condition(values, cal.get(Calendar.DAY_OF_YEAR));
		else if (subvalue.equalsIgnoreCase("year_weeks")) b = L.matches_number_condition(values, cal.get(Calendar.WEEK_OF_YEAR));

		if (reversed) return !b; else return b; 
	}

}

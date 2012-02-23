package me.coldandtired.mobs.conditions;

import java.util.ArrayList;
import java.util.Calendar;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;

import me.coldandtired.mobs.Condition;
import me.coldandtired.mobs.Utils;

public class Month_weeks implements Condition
{
	private ArrayList<Number_condition> values;
	
	public Month_weeks(Object ob)
	{
		values = Utils.fill_number_condition_array(ob);
	}
	
	@Override
	public boolean check(LivingEntity entity, World world, Location loc, String spawn_reason, Player player, int random) 
	{
		Calendar cal = Calendar.getInstance();
		return Utils.matches_number_condition(values, cal.get(Calendar.WEEK_OF_MONTH));
	}	
}
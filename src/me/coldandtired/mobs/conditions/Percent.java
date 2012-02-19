package me.coldandtired.mobs.conditions;

import java.util.ArrayList;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;

import me.coldandtired.mobs.Condition;
import me.coldandtired.mobs.Utils;

public class Percent implements Condition
{
	private ArrayList<Number_condition> values;
	
	public Percent(Object ob)
	{
		values = Utils.fill_number_condition_array(ob);
	}
	
	@Override
	public boolean check(LivingEntity entity, World world, Location loc, String spawn_reason, Player player, int random) 
	{
		return Utils.matches_number_condition(values, random);
	}	
}

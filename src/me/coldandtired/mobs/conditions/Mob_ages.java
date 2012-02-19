package me.coldandtired.mobs.conditions;

import java.util.ArrayList;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Animals;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;

import me.coldandtired.mobs.Condition;
import me.coldandtired.mobs.Utils;

public class Mob_ages implements Condition
{
	private ArrayList<Number_condition> values;
	
	public Mob_ages(Object ob)
	{
		values = Utils.fill_number_condition_array(ob);
	}
	
	@Override
	public boolean check(LivingEntity entity, World world, Location loc, String spawn_reason, Player player, int random) 
	{
		if (!(entity instanceof Animals)) return true;
		for (Number_condition nc : values)
		{
			if (nc.matches_number(((Animals)entity).getAge())) return true;
		}
		return false;
	}	
}
package me.coldandtired.mobs.conditions;

import java.util.List;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Animals;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;

import me.coldandtired.mobs.Condition;
import me.coldandtired.mobs.data.Autospawn;
import me.coldandtired.mobs.L;

public class Mob_ages implements Condition
{
	private List<Number_condition> values;

	public Mob_ages(String s)
	{
		values = L.fill_number_values(s);
	}
	
	@Override
	public boolean check(LivingEntity entity, World world, Location loc, String spawn_reason, Player player, int random, Autospawn as) 
	{
		if (!(entity instanceof Animals)) return true;
		for (Number_condition nc : values)
		{
			if (nc.matches_number(((Animals)entity).getAge())) return true;
		}
		return false;
	}	
}
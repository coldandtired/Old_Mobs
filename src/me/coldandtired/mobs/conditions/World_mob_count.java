package me.coldandtired.mobs.conditions;

import java.util.List;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;

import me.coldandtired.mobs.Condition;
import me.coldandtired.mobs.data.Autospawn;
import me.coldandtired.mobs.L;

public class World_mob_count implements Condition
{
	private List<Number_condition> values;
	private String subvalue;

	public World_mob_count(String s, String sub)
	{
		values = L.fill_number_values(s);
		subvalue = sub;
	}
	
	@Override
	public boolean check(LivingEntity entity, World world, Location loc, String spawn_reason, Player player, int random, Autospawn as) 
	{
		int i = 0;
		if (subvalue.equalsIgnoreCase("all"))
		{
			for (Entity e : world.getEntities()) if (e instanceof LivingEntity) i++;
		}
		else
		{
			if (subvalue.equalsIgnoreCase("class")) subvalue = entity.getType().name();
			for (Entity e : world.getEntities()) if (e.getType().name().equals(subvalue)) i++;
		}
		
		return L.matches_number_condition(values, i);
	}	
}

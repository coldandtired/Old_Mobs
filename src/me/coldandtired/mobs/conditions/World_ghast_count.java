package me.coldandtired.mobs.conditions;

import java.util.List;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Ghast;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;

import me.coldandtired.mobs.Condition;
import me.coldandtired.mobs.data.Autospawn;
import me.coldandtired.mobs.L;

public class World_ghast_count implements Condition
{
	private List<Number_condition> values;

	public World_ghast_count(String s)
	{
		values = L.fill_number_values(s);
	}
	
	@Override
	public boolean check(LivingEntity entity, World world, Location loc, String spawn_reason, Player player, int random, Autospawn as) 
	{
		return L.matches_number_condition(values, world.getEntitiesByClass(Ghast.class).size());
	}	
}

package me.coldandtired.mobs.conditions;

import java.util.List;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.IronGolem;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;

import me.coldandtired.mobs.Condition;
import me.coldandtired.mobs.data.Autospawn;
import me.coldandtired.mobs.L;

public class Chunk_iron_golem_count implements Condition
{
	private List<Number_condition> values;

	public Chunk_iron_golem_count(String s)
	{
		values = L.fill_number_values(s);
	}
	
	@Override
	public boolean check(LivingEntity entity, World world, Location loc, String spawn_reason, Player player, int random, Autospawn as)
	{
		int i = 0;
		for (Entity e : loc.getChunk().getEntities()) if (e instanceof IronGolem) i++;
				
		return L.matches_number_condition(values, i);
	}	
}
package me.coldandtired.mobs.conditions;

import java.util.List;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;

import me.coldandtired.mobs.Condition;
import me.coldandtired.mobs.L;

public class Chunk_mob_count implements Condition
{
	private List<Number_condition> values;
	private String subvalue;
	private boolean reversed = false;

	public Chunk_mob_count(String s, String sub, boolean reversed)
	{
		values = L.fill_number_values(s);
		subvalue = sub;
		this.reversed = reversed;
	}
	
	@Override
	public boolean check(LivingEntity entity, World world, Location loc, String spawn_reason, Player player, int random, String autospawn_id) 
	{
		int i = 0;
		if (subvalue.equalsIgnoreCase("all"))
		{
			for (Entity e : loc.getChunk().getEntities()) if (e instanceof LivingEntity && !(e instanceof Player)) i++;
		}
		else
		{
			if (subvalue.equalsIgnoreCase("class")) subvalue = entity.getType().name();
			for (Entity e : loc.getChunk().getEntities()) if (e.getType().name().equals(subvalue)) i++;
		}
				
		boolean b = L.matches_number_condition(values, i);
		if (reversed) return !b; else return b; 
	}	
}

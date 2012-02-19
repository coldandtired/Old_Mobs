package me.coldandtired.mobs.conditions;

import java.util.ArrayList;
import org.bukkit.DyeColor;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Sheep;

import me.coldandtired.mobs.Condition;

public class Wool_colours  implements Condition
{
	ArrayList<DyeColor> values = new ArrayList<DyeColor>();
	
	@SuppressWarnings("unchecked")
	public Wool_colours(Object ob) 
	{
		for (String s : (ArrayList<String>)ob)
		{
			values.add(DyeColor.valueOf(s.toUpperCase()));
		}
	}

	@Override
	public boolean check(LivingEntity entity, World world, Location loc, String spawn_reason, Player player, int random)
	{
		if (!(entity instanceof Sheep)) return false;
		
		if (values.contains(((Sheep)entity).getColor())) return true;
		return false;
	}
}

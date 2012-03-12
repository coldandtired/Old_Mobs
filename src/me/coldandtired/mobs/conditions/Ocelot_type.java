package me.coldandtired.mobs.conditions;

import java.util.ArrayList;

import me.coldandtired.mobs.Condition;
import me.coldandtired.mobs.Utils;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Ocelot;
import org.bukkit.entity.Player;

public class Ocelot_type  implements Condition
{
	private ArrayList<String> values;
	
	public Ocelot_type(Object ob)
	{
		values = Utils.fill_string_array(ob);
	}
	
	@Override
	public boolean check(LivingEntity entity, World world, Location loc, String spawn_reason, Player player, int random) 
	{
		if (!(entity instanceof Ocelot)) return true;
		
		Ocelot ocelot = (Ocelot)entity;
		return Utils.matches_string(values, ocelot.getCatType().name());
	}
}

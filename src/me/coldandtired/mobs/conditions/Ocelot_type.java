package me.coldandtired.mobs.conditions;

import java.util.List;

import me.coldandtired.mobs.Condition;
import me.coldandtired.mobs.data.Autospawn;
import me.coldandtired.mobs.L;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Ocelot;
import org.bukkit.entity.Player;

public class Ocelot_type  implements Condition
{
	private List<String> values;

	public Ocelot_type(String s)
	{
		values = L.fill_string_values(s);
	}
	
	@Override
	public boolean check(LivingEntity entity, World world, Location loc, String spawn_reason, Player player, int random, Autospawn as) 
	{
		if (!(entity instanceof Ocelot)) return true;
		
		Ocelot ocelot = (Ocelot)entity;
		return L.matches_string(values, ocelot.getCatType().name());
	}
}

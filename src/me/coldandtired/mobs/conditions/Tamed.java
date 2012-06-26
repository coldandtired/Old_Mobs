package me.coldandtired.mobs.conditions;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Wolf;

import me.coldandtired.mobs.Condition;

public class Tamed implements Condition
{
	private boolean value;

	public Tamed(String s)
	{
		value = s.equalsIgnoreCase("yes") ? true : false;
	}

	@Override
	public boolean check(LivingEntity entity, World world, Location loc, String spawn_reason, Player player, int random, String autospawn_id) 
	{
		if (!(entity instanceof Wolf)) return true;
		return  ((Wolf)entity).isTamed() == value;
	}
}
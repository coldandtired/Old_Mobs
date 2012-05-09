package me.coldandtired.mobs.conditions;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Wolf;

import me.coldandtired.mobs.Condition;
import me.coldandtired.mobs.data.Autospawn;

public class Tamed implements Condition
{
	private boolean value;

	public Tamed(boolean b)
	{
		value = b;
	}

	@Override
	public boolean check(LivingEntity entity, World world, Location loc, String spawn_reason, Player player, int random, Autospawn as) 
	{
		if (!(entity instanceof Wolf)) return true;
		return  ((Wolf)entity).isTamed() == value;
	}
}
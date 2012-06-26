package me.coldandtired.mobs.conditions;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Pig;
import org.bukkit.entity.Player;

import me.coldandtired.mobs.Condition;

public class Saddled implements Condition
{
	private boolean value;

	public Saddled(String s)
	{
		value = s.equalsIgnoreCase("yes") ? true : false;
	}
	
	@Override
	public boolean check(LivingEntity entity, World world, Location loc, String spawn_reason, Player player, int random, String autospawn_id) 
	{
		if (!(entity instanceof Pig)) return true;
		return ((Pig)entity).hasSaddle() == value;
	}
}
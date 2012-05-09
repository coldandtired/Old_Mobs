package me.coldandtired.mobs.conditions;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Sheep;

import me.coldandtired.mobs.Condition;
import me.coldandtired.mobs.data.Autospawn;

public class Sheared implements Condition
{
	private boolean value;

	public Sheared(boolean b)
	{
		value = b;
	}
	
	@Override
	public boolean check(LivingEntity entity, World world, Location loc, String spawn_reason, Player player, int random, Autospawn as) 
	{
		if (!(entity instanceof Sheep)) return true;
		return ((Sheep)entity).isSheared() == value;
	}
}

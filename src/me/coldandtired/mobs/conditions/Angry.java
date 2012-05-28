package me.coldandtired.mobs.conditions;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.PigZombie;
import org.bukkit.entity.Player;
import org.bukkit.entity.Wolf;

import me.coldandtired.mobs.Condition;
import me.coldandtired.mobs.data.Autospawn;

public class Angry implements Condition
{
	private boolean value;
	
	public Angry(String s)
	{
		value = s.equalsIgnoreCase("yes") ? true : false;
	}
	
	@Override
	public boolean check(LivingEntity entity, World world, Location loc, String spawn_reason, Player player, int random, Autospawn as)
	{
		if (!(entity instanceof Wolf || entity instanceof PigZombie)) return false;
		
		if (entity instanceof Wolf) return ((Wolf)entity).isAngry() == value;
		else if (entity instanceof PigZombie) return ((PigZombie)entity).isAngry() == value;

		return false;
	}
}
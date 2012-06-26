package me.coldandtired.mobs.conditions;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Animals;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;

import me.coldandtired.mobs.Condition;

public class Can_breed implements Condition
{
	private boolean value;
	
	public Can_breed(String s)
	{
		value = s.equalsIgnoreCase("yes") ? true : false;
	}
	
	@Override
	public boolean check(LivingEntity entity, World world, Location loc, String spawn_reason, Player player, int random, String autospawn_id) 
	{
		if (!(entity instanceof Animals)) return true;
		return ((Animals)entity).canBreed() == value;
	}

}

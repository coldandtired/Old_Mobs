package me.coldandtired.mobs.conditions;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Wolf;

import me.coldandtired.mobs.Condition;
import me.coldandtired.mobs.Utils;

public class Tamed implements Condition
{
	private int value = -1;
	
	public Tamed(Object ob)
	{
		value = Utils.fill_boolean(ob);
	}
	
	@Override
	public boolean check(LivingEntity entity, World world, Location loc, String spawn_reason, Player player, int random) 
	{
		if (value == -1 || !(entity instanceof Wolf)) return true;
		boolean cond = ((Wolf)entity).isTamed();
		if ((cond && value == 1) || (!cond && value == 0)) return true;
		return false;
	}
}
package me.coldandtired.mobs.conditions;

import java.util.Map;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;

import me.coldandtired.mobs.Area;
import me.coldandtired.mobs.Condition;
import me.coldandtired.mobs.data.Autospawn;

public class Player_within implements Condition
{
	Area value;
	
	@SuppressWarnings("unchecked")
	public Player_within(Object ob) 
	{
		value = new Area((Map<String, Integer>)ob);
	}

	@Override
	public boolean check(LivingEntity entity, World world, Location loc, String spawn_reason, Player player, int random, Autospawn as)
	{
		for (Entity e : entity.getNearbyEntities(value.x, value.y, value.z))
		{
			if (e instanceof Player) return true;
		}
		return false;
	}
}

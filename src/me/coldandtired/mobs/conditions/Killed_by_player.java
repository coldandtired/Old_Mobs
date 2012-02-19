package me.coldandtired.mobs.conditions;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;

import me.coldandtired.mobs.Condition;
import me.coldandtired.mobs.Utils;

public class Killed_by_player implements Condition
{
	private int value = -1;
	
	public Killed_by_player(Object ob)
	{
		value = Utils.fill_boolean(ob);
	}
	
	@Override
	public boolean check(LivingEntity entity, World world, Location loc, String spawn_reason, Player player, int random) 
	{
		boolean temp = entity.getKiller() instanceof Player ? true : false;
		if ((temp && value == 1) || (!temp && value == 0)) return true;
		return false;
	}
}

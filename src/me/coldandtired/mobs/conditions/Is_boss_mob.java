package me.coldandtired.mobs.conditions;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;

import me.coldandtired.mobs.Condition;
import me.coldandtired.mobs.Main;
import me.coldandtired.mobs.Mob;

public class Is_boss_mob implements Condition
{
private boolean value;
	
	public Is_boss_mob(String s)
	{
		value = s.equalsIgnoreCase("yes") ? true : false;
	}
	
	@Override
	public boolean check(LivingEntity entity, World world, Location loc, String spawn_reason, Player player, int random, String autospawn_id) 
	{
		Mob m = Main.all_mobs.get(entity);
		if (m == null) return true;
		
		return m.boss_mob == value;
	}
}
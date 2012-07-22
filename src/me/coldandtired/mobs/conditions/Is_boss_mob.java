package me.coldandtired.mobs.conditions;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;

import me.coldandtired.api.Mob;
import me.coldandtired.mobs.Condition;
import me.coldandtired.mobs.Main;

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
		Mob m = Main.db.find(Mob.class, entity.getUniqueId().toString());
		if (m == null) return true;
		
		return m.getBoss_mob() == value;
	}
}

package me.coldandtired.mobs.conditions;

import java.util.ArrayList;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;

import me.coldandtired.mobs.Condition;
import me.coldandtired.mobs.Utils;

public class World_types implements Condition
{
	private ArrayList<String> values;
	
	public World_types(Object ob)
	{
		values = Utils.fill_string_array(ob);
	}
	
	@Override
	public boolean check(LivingEntity entity, World world, Location loc, String spawn_reason, Player player, int random) 
	{
		return Utils.matches_string(values, world.getEnvironment().name());
	}
}

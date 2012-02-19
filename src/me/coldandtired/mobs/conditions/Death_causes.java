package me.coldandtired.mobs.conditions;

import java.util.ArrayList;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;

import me.coldandtired.mobs.Condition;
import me.coldandtired.mobs.Utils;

public class Death_causes implements Condition
{
	private ArrayList<String> values;
	
	public Death_causes(Object ob)
	{
		values = Utils.fill_string_array(ob);
	}
	
	@Override
	public boolean check(LivingEntity entity, World world, Location loc, String spawn_reason, Player player, int random) 
	{
		if (entity.getLastDamageCause()== null) return false;
		return Utils.matches_string(values, entity.getLastDamageCause().getCause().name());
	}
}

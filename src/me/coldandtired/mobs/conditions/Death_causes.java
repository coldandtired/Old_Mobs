package me.coldandtired.mobs.conditions;

import java.util.List;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;

import me.coldandtired.mobs.Condition;
import me.coldandtired.mobs.data.Autospawn;
import me.coldandtired.mobs.L;

public class Death_causes implements Condition
{
	private List<String> values;

	public Death_causes(String s)
	{
		values = L.fill_string_values(s);
	}
	
	@Override
	public boolean check(LivingEntity entity, World world, Location loc, String spawn_reason, Player player, int random, Autospawn as) 
	{
		if (entity.getLastDamageCause()== null) return false;
		return L.matches_string(values, entity.getLastDamageCause().getCause().name());
	}
}

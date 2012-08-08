package me.coldandtired.mobs.conditions;

import java.util.List;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;

import me.coldandtired.api.Mob;
import me.coldandtired.mobs.Condition;
import me.coldandtired.mobs.L;
import me.coldandtired.mobs.Main;

public class Remaining_lifetime implements Condition
{
	private List<Number_condition> values;
	private boolean reversed = false;
	
	public Remaining_lifetime(String s, boolean reversed)
	{
		values = L.fill_number_values(s);
		this.reversed = reversed;
	}
	
	@Override
	public boolean check(LivingEntity entity, World world, Location loc, String spawn_reason, Player player, int random, String autospawn_id) 
	{
		Mob m = Main.all_mobs.get(entity.getUniqueId().toString());
		
		if (m == null || m.getDeath_time() == null) return false;
		
		int i = (int)(m.getDeath_time() - System.currentTimeMillis()) / 1000;
		boolean b = L.matches_number_condition(values, i);
		if (reversed) return !b; else return b;
	}	
}

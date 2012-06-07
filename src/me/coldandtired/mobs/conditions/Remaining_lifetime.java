package me.coldandtired.mobs.conditions;

import java.util.Calendar;
import java.util.List;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;

import me.coldandtired.mobs.Condition;
import me.coldandtired.mobs.Main;
import me.coldandtired.mobs.data.Autospawn;
import me.coldandtired.mobs.L;

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
	public boolean check(LivingEntity entity, World world, Location loc, String spawn_reason, Player player, int random, Autospawn as) 
	{
		if (Main.mobs_with_lifetimes == null) return false;
		boolean b = false;
		Calendar temp = Main.mobs_with_lifetimes.get(entity);
		if (temp != null)
		{
			Calendar cal = Calendar.getInstance();
			long l = temp.getTimeInMillis() - cal.getTimeInMillis();
			b = L.matches_number_condition(values, (int)l);
			if (reversed) return !b; else return b; 
		}
		else return false;
	}	
}

package me.coldandtired.mobs.conditions;

import java.util.List;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Animals;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;

import me.coldandtired.mobs.Condition;
import me.coldandtired.mobs.L;

public class Mob_ages implements Condition
{
	private List<Number_condition> values;
	private boolean reversed = false;

	public Mob_ages(String s, boolean reversed)
	{
		values = L.fill_number_values(s);
		this.reversed = reversed;
	}
	
	@Override
	public boolean check(LivingEntity entity, World world, Location loc, String spawn_reason, Player player, int random, String autospawn_id) 
	{
		if (!(entity instanceof Animals)) return true;
		boolean b = L.matches_number_condition(values, ((Animals)entity).getAge());
		if (reversed) return !b; else return b;
	}	
}
package me.coldandtired.mobs.conditions;

import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;

import me.coldandtired.mobs.Condition;
import me.coldandtired.mobs.L;

public class Online_player_count implements Condition
{
	private List<Number_condition> values;
	private boolean reversed = false;

	public Online_player_count(String s, boolean reversed)
	{
		values = L.fill_number_values(s);
		this.reversed = reversed;
	}
	
	@Override
	public boolean check(LivingEntity entity, World world, Location loc, String spawn_reason, Player player, int random, String autospawn_id) 
	{
		boolean b = L.matches_number_condition(values, Bukkit.getOnlinePlayers().length);
		if (reversed) return !b; else return b; 
	}	
}

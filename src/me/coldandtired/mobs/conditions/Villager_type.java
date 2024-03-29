package me.coldandtired.mobs.conditions;

import java.util.List;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Villager;

import me.coldandtired.mobs.Condition;
import me.coldandtired.mobs.L;

public class Villager_type implements Condition
{
	private List<String> values;
	private boolean reversed = false;

	public Villager_type(String s, boolean reversed)
	{
		values = L.fill_string_values(s);
		this.reversed = reversed;
	}
	
	@Override
	public boolean check(LivingEntity entity, World world, Location loc, String spawn_reason, Player player, int random, String autospawn_id) 
	{
		if (!(entity instanceof Villager)) return true;
		
		Villager villager = (Villager)entity;
		boolean b = L.matches_string(values, villager.getProfession().name());
		if (reversed) return !b; else return b; 
	}
}

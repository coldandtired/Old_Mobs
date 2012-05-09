package me.coldandtired.mobs.conditions;

import java.util.List;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Villager;

import me.coldandtired.mobs.Condition;
import me.coldandtired.mobs.data.Autospawn;
import me.coldandtired.mobs.L;

public class Villager_type implements Condition
{
	private List<String> values;

	public Villager_type(String s)
	{
		values = L.fill_string_values(s);
	}
	
	@Override
	public boolean check(LivingEntity entity, World world, Location loc, String spawn_reason, Player player, int random, Autospawn as) 
	{
		if (!(entity instanceof Villager)) return true;
		
		Villager villager = (Villager)entity;
		return L.matches_string(values, villager.getProfession().name());
	}
}

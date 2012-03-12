package me.coldandtired.mobs.conditions;

import java.util.ArrayList;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Villager;

import me.coldandtired.mobs.Condition;
import me.coldandtired.mobs.Utils;

public class Villager_type implements Condition
{
	private ArrayList<String> values;
	
	public Villager_type(Object ob)
	{
		values = Utils.fill_string_array(ob);
	}
	
	@Override
	public boolean check(LivingEntity entity, World world, Location loc, String spawn_reason, Player player, int random) 
	{
		if (!(entity instanceof Villager)) return true;
		
		Villager villager = (Villager)entity;
		return Utils.matches_string(values, villager.getProfession().name());
	}
}
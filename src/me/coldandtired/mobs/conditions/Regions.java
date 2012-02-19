package me.coldandtired.mobs.conditions;

import java.util.ArrayList;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;

import com.sk89q.worldguard.protection.regions.ProtectedRegion;

import me.coldandtired.mobs.Condition;
import me.coldandtired.mobs.Main;
import me.coldandtired.mobs.Utils;

public class Regions implements Condition
{
	private ArrayList<String> values;
	
	public Regions(Object ob)
	{
		values = Utils.fill_string_array(ob);
	}
	
	@Override
	public boolean check(LivingEntity entity, World world, Location loc, String spawn_reason, Player player, int random) 
	{
		if (Main.world_guard == null) return true;

		for (String r : values)
		{
			ProtectedRegion pr = Main.world_guard.getRegionManager(loc.getWorld()).getRegion(r);
			if (pr != null && pr.contains(loc.getBlockX(), loc.getBlockY(), loc.getBlockZ())) return true;
		}
		return false;
	}
}
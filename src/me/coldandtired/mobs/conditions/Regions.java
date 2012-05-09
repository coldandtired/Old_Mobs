package me.coldandtired.mobs.conditions;

import java.util.List;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;

import com.sk89q.worldguard.protection.regions.ProtectedRegion;

import me.coldandtired.mobs.Condition;
import me.coldandtired.mobs.Main;
import me.coldandtired.mobs.data.Autospawn;
import me.coldandtired.mobs.L;

public class Regions implements Condition
{
	private List<String> values;

	public Regions(String s)
	{
		values = L.fill_string_values(s);
	}
	
	@Override
	public boolean check(LivingEntity entity, World world, Location loc, String spawn_reason, Player player, int random, Autospawn as) 
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
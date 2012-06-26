package me.coldandtired.mobs.conditions;

import java.util.List;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;

import com.sk89q.worldguard.protection.regions.ProtectedRegion;

import me.coldandtired.mobs.Condition;
import me.coldandtired.mobs.Main;
import me.coldandtired.mobs.L;

public class Regions implements Condition
{
	private List<String> values;
	private boolean reversed = false;

	public Regions(String s, boolean reversed)
	{
		values = L.fill_string_values(s);
		this.reversed = reversed;
	}
	
	@Override
	public boolean check(LivingEntity entity, World world, Location loc, String spawn_reason, Player player, int random, String autospawn_id) 
	{
		if (Main.world_guard == null) return true;
		boolean b = false;
		for (String r : values)
		{
			ProtectedRegion pr = Main.world_guard.getRegionManager(world).getRegion(r);
			if (pr != null && pr.contains(loc.getBlockX(), loc.getBlockY(), loc.getBlockZ())) b = true;
		}
		if (reversed) return !b; else return b; 
	}
}
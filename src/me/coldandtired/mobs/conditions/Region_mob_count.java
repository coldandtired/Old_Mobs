package me.coldandtired.mobs.conditions;

import java.util.List;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;

import com.sk89q.worldguard.protection.regions.ProtectedRegion;

import me.coldandtired.mobs.Condition;
import me.coldandtired.mobs.L;
import me.coldandtired.mobs.Main;

public class Region_mob_count implements Condition 
{
	private List<Number_condition> values;
	private String subvalue;
	private boolean reversed = false;
	private List<String> regions;
	
	public Region_mob_count(String s, String sub, String regions, boolean reversed)
	{
		values = L.fill_number_values(s);
		subvalue = sub;
		this.regions = L.fill_string_values(regions);
		this.reversed = reversed;
	}
	
	@Override
	public boolean check(LivingEntity entity, World world, Location loc, String spawn_reason, Player player, int random, String autospawn_id) 
	{
		if (Main.world_guard == null) return true;
		boolean b = false;
		if (subvalue.equalsIgnoreCase("class")) subvalue = entity.getType().name();
		for (String r : regions)
		{
			ProtectedRegion pr = Main.world_guard.getRegionManager(world).getRegion(r);
			if (pr != null)
			{
				int i = 0;
				if (subvalue.equalsIgnoreCase("all"))
				{
					for (Entity e : world.getEntities()) if (e instanceof LivingEntity && 
							!(e instanceof Player) && pr.contains(loc.getBlockX(), loc.getBlockY(), loc.getBlockZ())) i++;
				}
				else
				{					
					for (Entity e : world.getEntities()) if (e.getType().name().equals(subvalue) && 
							pr.contains(loc.getBlockX(), loc.getBlockY(), loc.getBlockZ())) i++;
				}
				if (L.matches_number_condition(values, i)) b = true;
			}
		}	
		if (reversed) return !b; else return b; 
	}

}

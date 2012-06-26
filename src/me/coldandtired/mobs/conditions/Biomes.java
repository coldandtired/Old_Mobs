package me.coldandtired.mobs.conditions;

import java.util.List;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;

import com.khorn.terraincontrol.bukkit.BukkitWorld;

import me.coldandtired.mobs.Condition;
import me.coldandtired.mobs.Main;
import me.coldandtired.mobs.L;

public class Biomes implements Condition
{
	private List<String> values;
	private boolean reversed = false;
	
	public Biomes(String s, boolean reversed)
	{
		values = L.fill_string_values(s);
		this.reversed = reversed;
	}
	
	@Override
	public boolean check(LivingEntity entity, World world, Location loc, String spawn_reason, Player player, int random, String autospawn_id) 
	{
		String biome = null;
		if (Main.tc != null)
		{
			BukkitWorld bw = Main.tc.worlds.get(world.getUID());
			if (bw != null)
			{
				int id = bw.getBiome(loc.getBlockX(), loc.getBlockZ());
				biome = bw.getBiomeById(id).getName().toUpperCase();
			}
		}
	
		if (biome == null) biome = world.getBiome(loc.getBlockX(), loc.getBlockZ()).name();
		
		boolean b = L.matches_string(values, biome);
		if (reversed) return !b; else return b; 
	}
}

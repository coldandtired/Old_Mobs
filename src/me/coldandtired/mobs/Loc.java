package me.coldandtired.mobs;

import java.util.ArrayList;
import java.util.Map;
import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;

import com.sk89q.worldedit.BlockVector;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;

public class Loc 
{
	public Area base;
	public Area range;
	World world;
	boolean all_world = false;
	Random rng = new Random();
	ArrayList<String> players;
	public String region_name = "";
	
	public Loc(String region_name)
	{
		this.region_name = region_name;
	}
	
	public Loc(World world, String region)
	{		//if (world == null) this.world = Bukkit.getWorlds().get(0); else	
		this.world = world;
		this.region_name = region;
		ProtectedRegion pr = Main.world_guard.getRegionManager(world).getRegions().get(region);
		BlockVector min = pr.getMinimumPoint();
		BlockVector max = pr.getMaximumPoint();
		int x = (Math.abs(max.getBlockX() - min.getBlockX()) / 2);
		int y = (Math.abs(max.getBlockY() - min.getBlockY()) / 2);
		int z = (Math.abs(max.getBlockZ() - min.getBlockZ()) / 2);
		range = new Area(x, y, z);
		base = new Area(min.getBlockX() + x, min.getBlockY() + y, min.getBlockZ() + z);		
	}
	
	@SuppressWarnings("unchecked")
	Loc(World world, Map<String, Object> loc)
	{
		this.world = world;
		if (loc.containsKey("base")) base = new Area((Map<String, Integer>)loc.get("base"));
		else base = new Area(0, 120, 0);
		if (loc.containsKey("range")) range = new Area((Map<String, Integer>)loc.get("range"));
		else range = new Area(1000, 0, 1000);
	}
	
	@SuppressWarnings("unchecked")
	public
	Loc(Map<String, Object> loc)
	{
		if (loc.containsKey("base")) base = new Area((Map<String, Integer>)loc.get("base"));
		else base = new Area(0, 120, 0);
		if (loc.containsKey("range")) range = new Area((Map<String, Integer>)loc.get("range"));
		else range = new Area(1000, 0, 1000);
	}
	
	@SuppressWarnings("unchecked")
	Loc(World world, Map<String, Object> loc, String s)
	{
		if (players == null) players = new ArrayList<String>();
		players.add(s);
		this.world = world;
		if (loc.containsKey("range")) range = new Area((Map<String, Integer>)loc.get("range"));
		else range = new Area(5, 5, 5);
	}
	
	Loc(World world)
	{
		this.world = world;
		all_world = true;
	}
	
	Location get_location()
	{
		int x = 0;
		int y = 0;
		int z = 0;
		if (!all_world)
		{
			x = rng.nextInt((range.x * 2) + 1);
			x = base.x + (x - range.x);
			y = rng.nextInt((range.y * 2) + 1);
			y = base.y + (y - range.y);
			if (y < 4) y = 4;
			z = rng.nextInt((range.z * 2) + 1);
			z = base.z + (z - range.z);
		}
		else
		{
			Chunk [] chunks = world.getLoadedChunks();
			//Bukkit.getLogger().info("size = " + chunks.length);
			Chunk chunk = chunks[rng.nextInt(chunks.length)];
			x = chunk.getX() + rng.nextInt(16);
			y = rng.nextInt(world.getMaxHeight() - 4) + 4;
			z = chunk.getZ() + rng.nextInt(16);
			//Bukkit.getLogger().info("x = " + x);
			//Bukkit.getLogger().info("z = " + z);
		}
		return Utils.get_safe_block(world.getBlockAt(x, y, z));		
	}
	
	Location get_player_location(String name)
	{
		Player p = Bukkit.getPlayer(name);
		if (p == null) return null;
		
		Location loc = p.getLocation();
		int x = 0;
		int y = 0;
		int z = 0;
		
		x = rng.nextInt((range.x * 2) + 1);
		x = (int) (loc.getX() + (x - range.x));
		y = rng.nextInt((range.y * 2) + 1);
		y = (int) (loc.getY() + (y - range.y));
		if (y < 4) y = 4;
		z = rng.nextInt((range.z * 2) + 1);
		z = (int) (loc.getZ() + (z - range.z));
		
		return new Location(p.getWorld(), x, y, z);
	}
}

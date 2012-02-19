package me.coldandtired.mobs;

import java.util.Map;

import org.bukkit.Location;

public class Area 
{
	public int x = 0;
	public int y = 0;
	public int z = 0;
	
	@SuppressWarnings("unchecked")
	public
	Area (Object o)
	{
		Map<String, Integer> a = (Map<String, Integer>)o;
		
		x = a.get("x");
		y = a.get("y");
		z = a.get("z");
	}
	
	Area (int x, int y, int z)
	{
		this.x = x;
		this.y = y;
		this.z = z;
	}
	
	Area (Location loc)
	{
		x = loc.getBlockX();
		y = loc.getBlockY();
		z = loc.getBlockZ();
	}
}

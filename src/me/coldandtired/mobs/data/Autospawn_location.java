package me.coldandtired.mobs.data;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import me.coldandtired.mobs.L;
import me.coldandtired.mobs.Main;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.w3c.dom.Element;

public class Autospawn_location 
{
	public List<World> worlds = null;
	public List<String> regions= null;
	public List<String> biomes = null;
	public boolean all_values;
	public Integer xbase;
	public Integer ybase;
	public Integer zbase;
	public Integer xrange;
	public Integer yrange;
	public Integer zrange;
	public int min_xrange;
	public int min_yrange;
	public int min_zrange;
	public int autospawn_placement;
	public String location_type = "";	
	
	Autospawn_location(Element el)
	{
		String s = "";
		location_type = el.getAttributeNode("location_type").getValue();
		all_values = Boolean.parseBoolean(el.getAttributeNode("all_values").getValue());
		
		s = el.getAttributeNode("worlds").getValue();
		if (!s.equalsIgnoreCase(""))
		{
			worlds = new ArrayList<World>();
			for (String ss : s.split(","))
			{
				World w = Bukkit.getWorld(ss.trim());
				if (w != null) worlds.add(w);
				else if (Config.log_level > 0) L.log("No world called " + ss + " exists - check the spelling and case!");
			}
			if (worlds.size() == 0) worlds = null;
		}
		
		s = el.getAttributeNode("regions").getValue();
		if (!s.equalsIgnoreCase(""))
		{
			if (Main.world_guard != null) regions = Arrays.asList(s.split(","));
			else L.log("No worldguard found - the regions will be ignored!");
		}
		
		s = el.getAttributeNode("biomes").getValue();
		if (!s.equalsIgnoreCase(""))
		{
			s = s.toUpperCase();
			biomes = Arrays.asList(s.split(","));
		}
		
		autospawn_placement = Integer.parseInt(el.getAttributeNode("autospawn_placement").getValue());
		
		if (location_type.equalsIgnoreCase("near_players") || location_type.equalsIgnoreCase("range"))
		{			
			s = el.getAttributeNode("xbase").getValue();
			xbase = !s.equalsIgnoreCase("") ? Integer.parseInt(s) : null;
	
			s = el.getAttributeNode("ybase").getValue();
			ybase = !s.equalsIgnoreCase("") ? Integer.parseInt(s) : null;
	
			s = el.getAttributeNode("zbase").getValue();
			zbase = !s.equalsIgnoreCase("") ? Integer.parseInt(s) : null;
			
			s = el.getAttributeNode("xrange").getValue();
			xrange = !s.equalsIgnoreCase("") ? Integer.parseInt(s) : null;
			
			s = el.getAttributeNode("yrange").getValue();
			yrange = !s.equalsIgnoreCase("") ? Integer.parseInt(s) : null;
	
			s = el.getAttributeNode("zrange").getValue();
			zrange = !s.equalsIgnoreCase("") ? Integer.parseInt(s) : null;

			s = el.getAttributeNode("min_xrange").getValue();
			min_xrange = !s.equalsIgnoreCase("") ? Integer.parseInt(s) : 0;
			
			s = el.getAttributeNode("min_yrange").getValue();
			min_yrange = !s.equalsIgnoreCase("") ? Integer.parseInt(s) : 0;
	
			s = el.getAttributeNode("min_zrange").getValue();
			min_zrange = !s.equalsIgnoreCase("") ? Integer.parseInt(s) : 0;
		}
	}
}

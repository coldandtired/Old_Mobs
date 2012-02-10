package me.coldandtired.mobs;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.bukkit.World;

public class Auto_spawn 
{
	String name;
	ArrayList<Integer> quantities;
	ArrayList<Loc> locations = new ArrayList<Loc>();
	Map<String, Object> source;
	String shortcut = "";
	boolean manual = false;
	
	@SuppressWarnings("unchecked")
	Auto_spawn(String name, Map<String, Object> m, Main plugin)
	{
		m = (Map<String, Object>) m.get("spawn_event");
		source = m;
		this.name = name;
		List<World> all_worlds = plugin.getServer().getWorlds();
		
		if (m.containsKey("quantities")) quantities = Utils.fill_int_array(m.get("quantities"));
		if (m.containsKey("manual")) manual = (Boolean)m.get("manual");
		if (m.containsKey("where_to_spawn"))
		{
			List<Map<String, Object>> t = (List<Map<String, Object>>)m.get("where_to_spawn");
			for (Map<String, Object> o : t)
			{
				Map<String, Object> loc = (Map<String, Object>)o.get("location");
				ArrayList<World> worlds = new ArrayList<World>();
				
				if (loc.containsKey("worlds"))
				{
					for (String s : (ArrayList<String>)loc.get("worlds"))
					{
						for (World w : all_worlds)
						{
							if (w.getName().equalsIgnoreCase(s))
							{
								worlds.add(w);
								break;
							}
						}
					}
				}
				else worlds.add(all_worlds.get(0));
				
				for (World w : worlds)
				{
					if (loc.containsKey("regions") && Main.world_guard != null)
						for (String s : (ArrayList<String>)loc.get("regions")) locations.add(new Loc(w, s));
					else if (loc.containsKey("players"))
						for (String s : (ArrayList<String>)loc.get("players")) locations.add(new Loc(w, loc, s));
					else if (loc.containsKey("base") || loc.containsKey("range")) locations.add(new Loc(w, loc));
					else locations.add(new Loc(w)); 							
				}
			}
		}
		else locations.add(new Loc(all_worlds.get(0)));
		
		Object o = m.get("shortcut");
		if (o != null)
		{
			if (o instanceof String) shortcut = (String)o;
			else if (o instanceof Integer) shortcut = Integer.toString((Integer)o); 
		}
	}
}

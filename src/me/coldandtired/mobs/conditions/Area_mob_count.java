package me.coldandtired.mobs.conditions;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;

import me.coldandtired.mobs.Condition;
import me.coldandtired.mobs.Loc;
import me.coldandtired.mobs.data.Autospawn;

public class Area_mob_count  implements Condition
{
	Map<Loc, ArrayList<Number_condition>> values = new HashMap<Loc, ArrayList<Number_condition>>();
	
	public Area_mob_count(Object ob) 
	{
		/*if (ob instanceof ArrayList)
		{
			for (Map<String, Object> a : (ArrayList<Map<String, Object>>)ob)
			{
				Map<String, Object> temp = (Map<String, Object>) a.get("location");
				Loc loc;
				if (temp.containsKey("region")) loc = new Loc((String)temp.get("region"));
				else loc = new Loc(temp);
				values.put(loc, Utils.fill_number_condition_array(temp.get("count")));
			}
		}
		else
		{
			Map<String, Object> temp = (Map<String, Object>) ob;
			temp = (Map<String, Object>) temp.get("location");
			Loc loc;
			if (temp.containsKey("region")) loc = new Loc((String)temp.get("region"));
			else loc = new Loc(temp);
			values.put(loc, Utils.fill_number_condition_array(temp.get("count")));
		}	*/
	}

	@Override
	public boolean check(LivingEntity entity, World world, Location loc, String spawn_reason, Player player, int random, Autospawn as) 
	{
		/*for (Loc l : values.keySet())
		{
			int i = 0;
			Loc temp = null;
			Location ll = null;
			if (!l.region_name.equalsIgnoreCase(""))
			{
				if (Main.world_guard == null) return true;
				if (Utils.check_region(world.getName(), l.region_name)) temp = new Loc(world, l.region_name);
				else
				{
					L.warn("The world called " + world.getName() + " has no region called " + l.region_name + "!");
					return true;
				}
			}
			
			if (temp == null) temp = l;
			for (Entity e : world.getEntities())
			{
				if (e instanceof LivingEntity && !(e instanceof Player))
				{
					ll = e.getLocation();
					if (ll.getBlockX() >= (temp.base.x - temp.range.x) && ll.getBlockX() <= (temp.base.x + temp.range.x) 
							&& ll.getBlockY() >= (temp.base.y - temp.range.y) && ll.getBlockY() <= (temp.base.y + temp.range.y)
							&& ll.getBlockZ() >= (temp.base.z - temp.range.z) && ll.getBlockZ() <= (temp.base.z + temp.range.z)) i++;
				}
			}
			return L.matches_number_condition(values.get(l), i);
		}*/
		return false;
	}
}

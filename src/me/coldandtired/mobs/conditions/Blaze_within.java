package me.coldandtired.mobs.conditions;

import java.util.ArrayList;
import java.util.Map;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Blaze;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;

import me.coldandtired.mobs.Condition;
import me.coldandtired.mobs.data.Autospawn;
import me.coldandtired.mobs.L;

public class Blaze_within implements Condition
{
	ArrayList<Mob_within> values = new ArrayList<Mob_within>();
	
	@SuppressWarnings("unchecked")
	public Blaze_within(Object ob) 
	{	
		for (Map<String, Object> m : (ArrayList<Map<String, Object>>)ob)
		{
			values.add(new Mob_within(m.get("range")));
			//Utils.log("blaze");
		}
	}

	@Override
	public boolean check(LivingEntity entity, World world, Location loc, String spawn_reason, Player player, int random, Autospawn as) 
	{
		for (Mob_within mw : values)
		{
			int i = 0;
			for (Entity e : entity.getNearbyEntities(mw.area.x, mw.area.y, mw.area.z)) if (e instanceof Blaze) i++;
			if (L.matches_number_condition(mw.count, i)) return true;
		}
		return false;
	}
}

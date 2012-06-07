package me.coldandtired.mobs.conditions;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Sheep;

import me.coldandtired.mobs.Condition;
import me.coldandtired.mobs.L;
import me.coldandtired.mobs.data.Autospawn;

public class Wool_colours  implements Condition
{
	List<String> values = new ArrayList<String>();
	private boolean reversed = false;

	public Wool_colours(String s, boolean reversed)
	{
		values = L.fill_string_values(s);
		this.reversed = reversed;
	}

	@Override
	public boolean check(LivingEntity entity, World world, Location loc, String spawn_reason, Player player, int random, Autospawn as)
	{
		if (!(entity instanceof Sheep)) return false;
		boolean b = false;
		if (values.contains(Byte.toString(((Sheep)entity).getColor().getData()))) b = true;
		if (reversed) return !b; else return b;
	}
}

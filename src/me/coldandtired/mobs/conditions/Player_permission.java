package me.coldandtired.mobs.conditions;

import java.util.List;

import me.coldandtired.mobs.Condition;
import me.coldandtired.mobs.L;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;

public class Player_permission  implements Condition
{
	private List<String> values;
	private boolean reversed = false;

	public Player_permission(String s, boolean reversed)
	{
		values = L.fill_string_values(s);
		this.reversed = reversed;
	}
	
	@Override
	public boolean check(LivingEntity entity, World world, Location loc, String spawn_reason, Player player, int random, String autospawn_id) 
	{
		if (player == null) return true;
		
		boolean b = false;
		for (String s : values) if (player.hasPermission(s)) b = true;
		if (reversed) return !b; else return b; 
	}
}

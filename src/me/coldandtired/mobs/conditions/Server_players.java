package me.coldandtired.mobs.conditions;

import java.util.List;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;

import me.coldandtired.mobs.Condition;
import me.coldandtired.mobs.L;

public class Server_players implements Condition
{
	private List<String> values;
	private boolean reversed = false;

	public Server_players(String s, boolean reversed)
	{
		values = L.fill_string_values(s);
		this.reversed = reversed;
	}
	
	@Override
	public boolean check(LivingEntity entity, World world, Location loc, String spawn_reason, Player player, int random, String autospawn_id) 
	{
		boolean b = false;
		for (Player p : entity.getServer().getOnlinePlayers()) if (values.contains(p.getName().toUpperCase())) b = true;
		if (reversed) return !b; else return b;
	}
}

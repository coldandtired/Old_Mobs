package me.coldandtired.mobs.conditions;

import java.util.List;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;

import me.coldandtired.mobs.Condition;
import me.coldandtired.mobs.data.Autospawn;
import me.coldandtired.mobs.L;

public class Server_players implements Condition
{
	private List<String> values;

	public Server_players(String s)
	{
		values = L.fill_string_values(s);
	}
	
	@Override
	public boolean check(LivingEntity entity, World world, Location loc, String spawn_reason, Player player, int random, Autospawn as) 
	{
		for (Player p : entity.getServer().getOnlinePlayers()) if (values.contains(p.getName().toUpperCase())) return true;
		return false;
	}
}

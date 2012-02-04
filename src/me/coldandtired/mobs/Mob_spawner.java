package me.coldandtired.mobs;

import java.util.ArrayList;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;

public class Mob_spawner implements Runnable
{
	ArrayList<Auto_spawn> spawns;
	Main plugin;
	
	public Mob_spawner(Main plugin)
	{
		this.plugin = plugin;
		spawns = new ArrayList<Auto_spawn>();
	}
	
	void spawn_mobs(Auto_spawn a, Location loc)
	{
		int quantity = a.quantities != null ? Utils.get_quantity(a.quantities) : 1;
		for (int i = 0; i < quantity; i++)
		{
			for (Loc l : a.locations)
			{
				if (l.players == null || l.players.size() == 0)
				{
					loc = l.get_location();
					if (loc.getY() > loc.getWorld().getMaxHeight()) loc.setY(loc.getWorld().getMaxHeight() - 2);
					if (!loc.getChunk().isLoaded()) return;
				
					plugin.listener.unique = a.source;
					loc.getWorld().spawnCreature(loc, a.mob);
				}
				else
				{
					if (l.players.contains("all_players")) 
					{
						for (Player p : l.world.getPlayers())
						{
							plugin.listener.unique = a.source;
							loc = l.get_player_location(p.getName());
							if (loc != null) l.world.spawnCreature(loc, a.mob);
						}
					}
					else
					{
						for (String s : l.players)
						{
							plugin.listener.unique = a.source;
							loc = l.get_player_location(s);
							if (loc != null) l.world.spawnCreature(loc, a.mob);
						}
					}
				}
			}
		}
	}
	
	public void run() 
	{
		int player_count = plugin.config.contains("auto_spawn_min_player_count") ? plugin.config.getInt("auto_spawn_min_player_count") : 1;
		if (Bukkit.getServer().getOnlinePlayers().length < player_count) return;	
		for (Auto_spawn s : spawns) 
		{
			if (!s.manual) spawn_mobs(s, null);
		}
	}
}

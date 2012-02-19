package me.coldandtired.mobs;

import java.util.ArrayList;
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
		if (Main.debug && !a.shortcut.equalsIgnoreCase("")) Utils.log("Auto spawning shortcut" + a.shortcut);
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
					if (loc != null)
					{
						plugin.listener.unique = a.source;
						loc.getWorld().spawnCreature(loc, Utils.get_creature_type(a.name));
					}
				}				
				else
				{
					if (l.players.contains("all_players")) 
					{
						for (Player p : l.world.getPlayers())
						{							
							loc = l.get_player_location(p.getName());
							if (loc != null)
							{
								plugin.listener.unique = a.source;
								loc.getWorld().spawnCreature(loc, Utils.get_creature_type(a.name));
							}
						}
					}
					else
					{
						for (String s : l.players)
						{
							plugin.listener.unique = a.source;
							loc = l.get_player_location(s);
							if (loc != null)
							{
								plugin.listener.unique = a.source;
								loc.getWorld().spawnCreature(loc, Utils.get_creature_type(a.name));
							}
						}
					}
				}
			}
			if (Main.debug && !a.shortcut.equalsIgnoreCase(""))Utils.log("Spawned shortcut" + a.shortcut);
		}
	}
	
	public void run() 
	{
		int player_count = Main.config.containsKey("auto_spawn_min_player_count") ? (Integer)Main.config.get("auto_spawn_min_player_count") : 1;
		if (plugin.getServer().getOnlinePlayers().length < player_count) return;	
		for (Auto_spawn s : spawns) 
		{
			if (!s.manual) spawn_mobs(s, null);
		}
	}
}

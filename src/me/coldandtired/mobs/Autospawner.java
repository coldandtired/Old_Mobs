package me.coldandtired.mobs;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

import me.coldandtired.mobs.data.Autospawn;
import me.coldandtired.mobs.data.Autospawn_location;
import me.coldandtired.mobs.data.Autospawn_time;
import me.coldandtired.mobs.data.Config;
import me.coldandtired.mobs.listeners.Main_listener;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;

import com.khorn.terraincontrol.bukkit.BukkitWorld;
import com.sk89q.worldedit.BlockVector;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;

public class Autospawner implements Runnable
{
	ArrayList<Autospawn> spawns;
	Main plugin;
	int interval = Config.check_interval;
	
	public static HashMap<String, Biome_data> chunks = new HashMap<String, Biome_data>();
	
	public Autospawner(Main plugin, ArrayList<Autospawn> spawns)
	{
		this.plugin = plugin;
		this.spawns = spawns;
	}
	
	public static void add_chunk(Chunk chunk)
	{
		Block b = chunk.getBlock(7, 0, 7);
		
		String world_name = chunk.getWorld().getName();
		Biome_data bd = chunks.get(world_name);
		if (bd == null) bd = new Biome_data();
		
		String biome = null;
		
		if (Main.tc != null)
		{
			BukkitWorld bw = Main.tc.worlds.get(chunk.getWorld().getUID());
			if (bw != null)
			{
				int id = bw.getBiome(b.getX(), b.getZ());
				biome = bw.getBiomeById(id).getName().toUpperCase();
			}
		}
		
		if (biome == null) biome = b.getBiome().name();
		
		Integer[] loc = { chunk.getX(), chunk.getZ() };
		List<Integer[]> locs = bd.chunks.get(biome);
		
		if (locs == null)
		{
			locs = new ArrayList<Integer[]>();
			locs.add(loc);
			bd.chunks.put(biome, locs);
			chunks.put(world_name, bd);
			if (Config.log_level > 1) L.log("added new biome " + biome + " to " + world_name);
		}
		else if (!locs.contains(loc)) 
		{
			locs.add(loc);
			bd.chunks.put(biome, locs);
			chunks.put(world_name, bd);

			if (biome.equalsIgnoreCase("gold")) L.log(loc[0] * 16 + loc[1] * 16);
		}
	}	
	
	void spawn_mobs(Autospawn as, Autospawn_location sl, World w, boolean from_command)
	{
		if (as.spawn_time != null && as.spawn_time.has_mc_time && (!from_command || (from_command && as.command_time_check)))
		{
			long mc_time = w.getTime();
			if (mc_time < as.spawn_time.mc_start && mc_time > as.spawn_time.mc_end) return;
		}
		
		List<String> safe_blocks = new ArrayList<String>();
		
		boolean above_ground = true;
		switch (sl.autospawn_placement)
		{			
			case 0:
				above_ground = true;
				break;
			case 1:
				above_ground = false;
				break;
			case 2:
				above_ground = L.rng.nextBoolean();
				break;
		}
		
		List<String> temp_biomes = null;
		if (sl.biomes != null)
		{
			if (sl.all_values) temp_biomes = sl.biomes;
			else
			{
				temp_biomes = new ArrayList<String>();
				temp_biomes.add(sl.biomes.get(L.get_random_choice(sl.biomes.size())));			
			}
		}
		
		List<String> temp_regions = null;
		if (sl.regions != null)
		{
			if (sl.all_values) temp_regions = sl.regions;
			else
			{
				temp_regions = new ArrayList<String>();
				temp_regions.add(sl.regions.get(L.get_random_choice(sl.regions.size())));
			}
		}
		
		if (sl.location_type.equalsIgnoreCase(""))
		{					
			if (temp_regions != null)
			{
				for (String s : temp_regions)
				{
					safe_blocks.clear();
					ProtectedRegion pr = Main.world_guard.getRegionManager(w).getRegions().get(s);
					if (pr != null)
					{
						BlockVector min = pr.getMinimumPoint();
						BlockVector max = pr.getMaximumPoint();
						if (above_ground)
						{
							for (int x = min.getBlockX(); x < max.getBlockX(); x++)
							{
								for (int z = min.getBlockZ(); z < max.getBlockZ(); z++)
								{
									for (int y = min.getBlockZ(); y < max.getBlockY(); y++)
									{
										if (y < w.getMaxHeight())
										{
											if (L.is_safe_above_ground_block(w.getBlockAt(x, y, z), sl.loaded_chunks_only,
													temp_biomes, null, w)) safe_blocks.add(x + "," + y + "," + z);
										}
									}
								}
							}
						}
						else
						{
							for (int x = min.getBlockX(); x <= max.getBlockX(); x++)
							{
								for (int z = min.getBlockZ(); z <= max.getBlockZ(); z++)
								{
									for (int y = min.getBlockZ(); y <= max.getBlockY(); y++)
									{
										if (y < w.getMaxHeight())
										{
											if (L.is_safe_below_ground_block(w.getBlockAt(x, y, z), sl.loaded_chunks_only,
													temp_biomes, null, w)) safe_blocks.add(x + "," + y + "," + z);
										}
									}
								}
							}
						}
						if (safe_blocks.size() > 0)
						{			
							int amount = as.spawn_time != null ? as.spawn_time.amount.get(L.get_random_choice(as.spawn_time.amount.size())) : 1;
							for (int i = 0; i < amount; i++)
							{
								String[] temp = safe_blocks.get(L.get_random_choice(safe_blocks.size())).split(",");

								Main_listener.autospawn = as;
								w.spawnCreature(w.getBlockAt(Integer.parseInt(temp[0]), Integer.parseInt(temp[1]), Integer.parseInt(temp[2])).getLocation(), EntityType.valueOf(as.mob_name));
								if (Config.log_level > 1) L.log("Autospawned " + as.mob_name);
							}
						}
					}
					else L.log("No region called " + s + " in world " + " world!");
				}
			}
			else if (temp_biomes != null)
			{
				Biome_data bd = chunks.get(w.getName());
				for (String s : temp_biomes)
				{
					List<Integer[]> locs = bd.chunks.get(s);
					if (locs == null) continue;
					for (Integer[] ints : locs)
					{
						Chunk c = w.getChunkAt(ints[0], ints[1]);
						if (sl.loaded_chunks_only && !c.isLoaded()) continue;
						int cx = L.get_random_choice(16);
						int cz = L.get_random_choice(16);
						if (above_ground)
						{			
							int amount = as.spawn_time != null ? as.spawn_time.amount.get(L.get_random_choice(as.spawn_time.amount.size())) : 1;
							for (int i = 0; i < amount; i++)
							{								
								Main_listener.autospawn = as;
								w.spawnCreature(c.getBlock(cx, w.getHighestBlockYAt(cx, cz), cz).getLocation(), EntityType.valueOf(as.mob_name));
								if (Config.log_level > 1) L.log("Autospawned " + as.mob_name);								
							}
						}
						else
						{
							for (int x = 0; x < 16; x++)
							{
								for (int z = 0; z < 16; z++)
								{
									for (int y = 0; y < w.getMaxHeight(); y++)
									{
										Block b = c.getBlock(x, y, z);
										if (b.getLightFromSky() < 14 && b.getType() == Material.AIR && b.getRelative(BlockFace.UP).getType() == Material.AIR
												&& b.getRelative(BlockFace.DOWN).getType() != Material.AIR) safe_blocks.add(x + "," + y + "," + z);
									}
								}
							}
							if (safe_blocks.size() > 0)
							{						
								int amount = as.spawn_time != null ? as.spawn_time.amount.get(L.get_random_choice(as.spawn_time.amount.size())) : 1;				
								for (int i = 0; i < amount; i++)
								{
									String[] temp = safe_blocks.get(L.get_random_choice(safe_blocks.size())).split(",");

									Main_listener.autospawn = as;
									w.spawnCreature(w.getBlockAt(Integer.parseInt(temp[0]), Integer.parseInt(temp[1]), Integer.parseInt(temp[2])).getLocation(), EntityType.valueOf(as.mob_name));
									if (Config.log_level > 1) L.log("Autospawned " + as.mob_name);									
								}
							}
						}
					}
				}
			}			
		}		
		else if (sl.location_type.equalsIgnoreCase("near_players") || sl.location_type.equalsIgnoreCase("range"))
		{	
			if (above_ground)
			{
				if (sl.location_type.equalsIgnoreCase("near_players"))
				{
					for (Player p : w.getPlayers())
					{
						Location loc = p.getLocation();
						L.check_above_ground_block((int)loc.getX(), (int)loc.getY(), (int)loc.getZ(), w, sl, temp_biomes, temp_regions, safe_blocks);
					}
				}
				else L.check_above_ground_block(sl.xbase, sl.ybase, sl.zbase, w, sl, temp_biomes, temp_regions, safe_blocks);
			}
			else
			{
				if (sl.location_type.equalsIgnoreCase("near_players"))
				{
					for (Player p : w.getPlayers())
					{
						Location loc = p.getLocation();
						L.check_below_ground_block((int)loc.getX(), (int)loc.getY(), (int)loc.getZ(), w, sl, temp_biomes, temp_regions, safe_blocks);
					}
				}
				else L.check_below_ground_block(sl.xbase, sl.ybase, sl.zbase, w, sl, temp_biomes, temp_regions, safe_blocks);
			}
			if (safe_blocks.size() > 0)
			{				
				int amount = as.spawn_time != null ? as.spawn_time.amount.get(L.get_random_choice(as.spawn_time.amount.size())) : 1;
				for (int i = 0; i < amount; i++)
				{
					String[] temp = safe_blocks.get(L.get_random_choice(safe_blocks.size())).split(",");

					Main_listener.autospawn = as;
					w.spawnCreature(w.getBlockAt(Integer.parseInt(temp[0]), Integer.parseInt(temp[1]), Integer.parseInt(temp[2])).getLocation(), EntityType.valueOf(as.mob_name));
					if (Config.log_level > 1) L.log("Autospawned " + as.mob_name);					
				}
			}
		}
	}	
	
	public void activate_autospawn(Autospawn as, boolean from_command)
	{
		Autospawn_time st = as.spawn_time;
		if (st != null && st.has_real_time && (!from_command || (from_command && as.command_time_check)))
		{
			Calendar cal = Calendar.getInstance();
			cal.set(Calendar.YEAR, 0);
			cal.set(Calendar.MONTH, 0);
			cal.set(Calendar.DATE, 0);
			cal.set(Calendar.MILLISECOND, 0);
			if (st.real_start != null && cal.compareTo(st.real_start) < 0) return;
			if (st.real_end != null && cal.compareTo(st.real_end) > 0) return;
		}
		
		for (Autospawn_location sl : as.spawn_locations)
		{
			if (sl.all_values) for (World w : sl.worlds)
			{
				if (!L.ignore_world(w) || (from_command && !as.command_time_check)) spawn_mobs(as, sl, w, from_command);
			}
			else
			{
				World w = sl.worlds.get(L.get_random_choice(sl.worlds.size()));
				if (!L.ignore_world(w) || (from_command && !as.command_time_check)) spawn_mobs(as, sl, w, from_command);						
			}
		}		
	}
	
	public void run() 
	{
		int player_count = Config.min_player_count;
		if (plugin.getServer().getOnlinePlayers().length < player_count) return;
		
		for (Autospawn as : spawns) 
		{
			if (as.manual) continue;
			
			as.timer_value -= interval;
			if (as.timer_value <= 0)
			{
				activate_autospawn(as, false);		
				as.timer_value = as.spawn_interval;
			}
		}
	}
}

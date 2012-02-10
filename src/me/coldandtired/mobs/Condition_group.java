package me.coldandtired.mobs;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.milkbowl.vault.economy.Economy;

import org.bukkit.DyeColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Animals;
import org.bukkit.entity.Creeper;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Pig;
import org.bukkit.entity.PigZombie;
import org.bukkit.entity.Player;
import org.bukkit.entity.Sheep;
import org.bukkit.entity.Wolf;
import org.bukkit.inventory.ItemStack;

import com.sk89q.worldguard.protection.regions.ProtectedRegion;

public class Condition_group 
{
	//world
	int raining = -1;
	int thundering = -1;
	int killed_by_player = -1;
	ArrayList<Number_condition> heights;
	ArrayList<Number_condition> game_times;
	Area player_within;
	ArrayList<String> biomes;
	ArrayList<String> world_types;
	ArrayList<String> spawn_reasons;
	ArrayList<String> death_causes;
	ArrayList<Number_condition> light_levels;
	ArrayList<Number_condition> server_player_count;
	ArrayList<Number_condition> world_player_count;
	ArrayList<String> world_players;
	ArrayList<String> server_players;
	ArrayList<String> regions;
	ArrayList<String> world_names;
	ArrayList<Number_condition> percent;
	ArrayList<Number_condition> world_mob_count;
	ArrayList<Number_condition> world_mob_class_count;
	//end world
	ArrayList<Number_condition> years;
	ArrayList<Number_condition> months;
	ArrayList<Number_condition> days;
	ArrayList<Number_condition> year_weeks;
	ArrayList<Number_condition> month_weeks;
	ArrayList<Number_condition> year_days;
	ArrayList<Number_condition> dates;
	ArrayList<Number_condition> hours;
	ArrayList<Number_condition> minutes;
	ArrayList<Number_condition> seconds;
	//end time
	ArrayList<Number_condition> mob_ages;
	int saddled = -1;
	int tamed = -1;
	int sheared = -1;
	int powered = -1;
	int angry = -1;
	ArrayList<DyeColor> wool_colours;
	ArrayList<String> killed_player_names;	
	ArrayList<String> mob_standing_on;
	//end mob
	//player
	ArrayList<String> player_names;
	ArrayList<Item> player_holding;
	ArrayList<Item> player_wearing;
	ArrayList<Item> player_items;
	ArrayList<Number_condition> player_money;
	ArrayList<String> player_standing_on;
	boolean match_all_items;	
	boolean match_all_wearing;
	HashMap<Loc, ArrayList<Number_condition>> area_mob_count;
	HashMap<Loc, ArrayList<Number_condition>> area_mob_class_count;
	int random;
	
	@SuppressWarnings("unchecked")
	//end player
	
	ArrayList<String> fill_string_array(Object o)
	{
		ArrayList<String> temp = new ArrayList<String>();
		for (String s : (ArrayList<String>)o) temp.add(s.replaceAll(" ", "").toUpperCase());
		return temp;
	}
	
	int fill_boolean(Object o)
	{
		if ((Boolean)o) return 1; else return 0;
	}
	
	@SuppressWarnings("unchecked")
	public Condition_group(Object ob, int random)
	{
		this.random = random;
		Map<String, Object> conditional = (Map<String, Object>)ob;
		conditional = (Map<String, Object>) conditional.get("condition_group");
		
		if (conditional.containsKey("spawn_reasons")) spawn_reasons = fill_string_array(conditional.get("spawn_reasons"));	
		if (conditional.containsKey("death_causes")) death_causes = fill_string_array(conditional.get("death_causes"));
		if (conditional.containsKey("world_names")) world_names = fill_string_array(conditional.get("world_names"));		
		if (conditional.containsKey("biomes")) biomes = fill_string_array(conditional.get("biomes"));	
		if (conditional.containsKey("world_players")) world_players = fill_string_array(conditional.get("world_players"));
		if (conditional.containsKey("server_players")) server_players = fill_string_array(conditional.get("server_players"));
		if (conditional.containsKey("regions")) regions = fill_string_array(conditional.get("regions"));		
		if (conditional.containsKey("world_types")) world_types = fill_string_array(conditional.get("world_types"));		
		if (conditional.containsKey("raining")) raining = fill_boolean(conditional.get("raining"));		
		if (conditional.containsKey("thundering")) thundering = fill_boolean(conditional.get("thundering"));
		if (conditional.containsKey("killed_by_player")) killed_by_player = fill_boolean(conditional.get("killed_by_player"));

		if (conditional.containsKey("area_mob_count"))
		{
			area_mob_count = new HashMap<Loc, ArrayList<Number_condition>>();
			
			for (Map<String, Object> a : (ArrayList<Map<String, Object>>)conditional.get("area_mob_count"))
			{
				a = (Map<String, Object>) a.get("location");
				Loc loc;
				if (a.containsKey("region")) loc = new Loc((String)a.get("region"));
				else loc = new Loc(a);
				area_mob_count.put(loc, Utils.fill_number_condition_array(a.get("count")));
			}
		} 
		
		if (conditional.containsKey("area_mob_class_count"))
		{
			area_mob_class_count = new HashMap<Loc, ArrayList<Number_condition>>();
			
			for (Map<String, Object> a : (ArrayList<Map<String, Object>>)conditional.get("area_mob_class_count"))
			{
				a = (Map<String, Object>) a.get("location");
				Loc loc;
				if (a.containsKey("region")) loc = new Loc((String)a.get("region"));
				else loc = new Loc(a);
				area_mob_class_count.put(loc, Utils.fill_number_condition_array(a.get("count")));
			}
		} 
		
		if (conditional.containsKey("player_within"))
		{
			player_within = new Area((Map<String, Integer>)conditional.get("player_within"));
		}

		if (conditional.containsKey("game_times")) game_times = Utils.fill_number_condition_array(conditional.get("game_times"));
		if (conditional.containsKey("server_player_count")) server_player_count = Utils.fill_number_condition_array(conditional.get("server_player_count"));
		if (conditional.containsKey("world_player_count")) world_player_count = Utils.fill_number_condition_array(conditional.get("world_player_count"));
		if (conditional.containsKey("light_levels")) light_levels = Utils.fill_number_condition_array(conditional.get("light_levels"));
		if (conditional.containsKey("world_mob_count")) world_mob_count = Utils.fill_number_condition_array(conditional.get("world_mob_count"));
		if (conditional.containsKey("world_mob_class_count")) world_mob_class_count = Utils.fill_number_condition_array(conditional.get("world_mob_class_count"));
 		if (conditional.containsKey("heights")) heights = Utils.fill_number_condition_array(conditional.get("heights"));
 		if (conditional.containsKey("years")) years = Utils.fill_number_condition_array(conditional.get("years"));
 		if (conditional.containsKey("months")) months = Utils.fill_number_condition_array(conditional.get("months"));
 		if (conditional.containsKey("days")) days = Utils.fill_number_condition_array(conditional.get("days"));
 		if (conditional.containsKey("hours")) hours = Utils.fill_number_condition_array(conditional.get("hours"));
 		if (conditional.containsKey("minutes")) minutes = Utils.fill_number_condition_array(conditional.get("minutes"));
 		if (conditional.containsKey("seconds")) seconds = Utils.fill_number_condition_array(conditional.get("seconds"));
 		if (conditional.containsKey("year_weeks")) year_weeks = Utils.fill_number_condition_array(conditional.get("year_weeks"));
 		if (conditional.containsKey("month_weeks")) month_weeks = Utils.fill_number_condition_array(conditional.get("month_weeks"));
 		if (conditional.containsKey("year_days")) year_days = Utils.fill_number_condition_array(conditional.get("year_daysv"));
 		if (conditional.containsKey("dates")) dates = Utils.fill_number_condition_array(conditional.get("dates"));
		//end time
		if (conditional.containsKey("killed_player_names")) killed_player_names = fill_string_array(conditional.get("killed_player_names"));		
		if (conditional.containsKey("mob_standing_on")) mob_standing_on = fill_string_array(conditional.get("mob_standing_on"));
		
		if (conditional.containsKey("wool_colors"))
		{
			wool_colours = new ArrayList<DyeColor>();
			for (String s : (ArrayList<String>)conditional.get("wool_colors"))
			{
				wool_colours.add(DyeColor.valueOf(s.toUpperCase()));
			}
		}
		
		if (conditional.containsKey("mob_ages")) mob_ages = Utils.fill_number_condition_array(conditional.get("mob_ages"));
		
		if (conditional.containsKey("percent")) percent = Utils.fill_number_condition_array(conditional.get("percent"));
		
		if (conditional.containsKey("angry")) angry = fill_boolean(conditional.get("angry"));		
		if (conditional.containsKey("saddled")) saddled = fill_boolean(conditional.get("saddled"));		
		if (conditional.containsKey("sheared")) sheared = fill_boolean(conditional.get("sheared"));		
		if (conditional.containsKey("powered")) powered = fill_boolean(conditional.get("powered"));		
		if (conditional.containsKey("tamed")) tamed = fill_boolean(conditional.get("tamed"));
		//end mob
		if (conditional.containsKey("player_names")) player_names = fill_string_array(conditional.get("player_names"));		
		if (conditional.containsKey("player_standing_on")) player_standing_on = fill_string_array(conditional.get("player_standing_on"));		
		if (conditional.containsKey("player_money")) player_money = Utils.fill_number_condition_array(conditional.get("player_money"));
		
		match_all_items = (Boolean)(conditional.containsKey("match_all_items") ? conditional.get("match_all_items") : false);
		match_all_wearing = (Boolean)(conditional.containsKey("match_all_wearing") ? conditional.get("match_all_wearing") : false);
		
		if (conditional.containsKey("player_holding"))
		{
			player_holding = new ArrayList<Item>();
			for (Map<String, Object> w : (ArrayList<Map<String, Object>>)conditional.get("player_holding"))
			{
				Matlist groups = Utils.groups.get((String)w.get("name"));
				if (groups == null) for (String s : ((String)w.get("name")).split(","))	player_holding.add(new Item(w, s));
				else for (String s : groups.names) player_holding.add(new Item(w, s));
			}
		} 
		
		if (conditional.containsKey("player_wearing"))
		{
			player_wearing = new ArrayList<Item>();
			for (Map<String, Object> w : (ArrayList<Map<String, Object>>)conditional.get("player_wearing"))
			{
				Matlist groups = Utils.groups.get((String)w.get("name"));
				if (groups == null) for (String s : ((String)w.get("name")).split(","))	player_wearing.add(new Item(w, s));
				else 
				{
					for (String s : groups.names) player_wearing.add(new Item(w, s));
					match_all_wearing = false;
				}
			}
		}
		
		if (conditional.containsKey("player_items"))
		{
			player_items = new ArrayList<Item>();
			for (Map<String, Object> i : (ArrayList<Map<String, Object>>)conditional.get("player_items"))
			{
				Matlist groups = Utils.groups.get((String)i.get("group"));
				if (groups == null) for (String s : ((String)i.get("name")).split(","))	player_items.add(new Item(i, s));
				else
				{
					for (String s : groups.names) player_items.add(new Item(i, s));
					match_all_items = false;
				}
			}
		}
		//end player
	}
	
	@SuppressWarnings("unchecked")
	public Boolean matches_all_conditions(Entity entity, String spawn_reason, Player player)
	{
		LivingEntity le = (LivingEntity)entity;
		World world = le.getWorld();
		Location loc = le.getLocation();
		Calendar cal = Calendar.getInstance();
		
		boolean validmob =  matches_killed_player_name(le)				
				&& matches_mob_age(le)
				&& matches_saddled(le)
				&& matches_tame(le)
				&& matches_sheared(le)
				&& matches_powered(le)
				&& matches_angry(le)
				&& matches_wool_colour(le) 
				&& matches_mob_standing_on(loc)
				&& matches_area_mob_class_count(world, le)
				&& matches_killed_by_player(le)
				&& matches_area_mob_count(world);
		if (le.getLastDamageCause()!= null) validmob = validmob && matches_string(death_causes, le.getLastDamageCause().getCause().name());
		
		boolean validworld =  matches_string(world_types, world.getEnvironment().name()) 
				&& matches_string(biomes, world.getBiome(loc.getBlockX(), loc.getBlockZ()).name()) 
				&& matches_raining(world.hasStorm())
				&& matches_thundering(world.isThundering())
				&& matches_string(spawn_reasons, spawn_reason)
				&& matches_number_condition(heights, loc.getBlockY())
				&& matches_number_condition(game_times, (int)world.getTime())
				&& matches_number_condition(light_levels, loc.getBlock().getLightLevel())
				&& matches_number_condition(server_player_count, le.getServer().getOnlinePlayers().length)
				&& matches_world_players(world)
				&& matches_server_players(le)
				&& matches_number_condition(world_player_count, world.getPlayers().size())
				&& matches_number_condition(world_mob_count, world.getEntities().size())
				&& matches_number_condition(world_mob_class_count, world.getEntitiesByClass(le.getClass()).size())
				&& matches_player_within(le)
				&& matches_region(loc)
				&& matches_string(world_names, world.getName());		
		
		boolean validtime = matches_number_condition(years, cal.get(Calendar.YEAR)) 
				&& matches_number_condition(months, cal.get(Calendar.MONTH) + 1) 
				&& matches_number_condition(year_weeks, cal.get(Calendar.WEEK_OF_YEAR))
				&& matches_number_condition(month_weeks, cal.get(Calendar.WEEK_OF_MONTH)) 
				&& matches_number_condition(year_days, cal.get(Calendar.DAY_OF_YEAR))
				&& matches_number_condition(days, cal.get(Calendar.DAY_OF_WEEK)) 
				&& matches_number_condition(hours, cal.get(Calendar.HOUR_OF_DAY))
				&& matches_number_condition(minutes, cal.get(Calendar.MINUTE)) 
				&& matches_number_condition(seconds, cal.get(Calendar.SECOND))
				&& matches_number_condition(dates, cal.get(Calendar.DATE));
		
		boolean validplayer;
		if (player != null) validplayer =  matches_string(player_names, player.getName())
				&& matches_holding(player)
				&& matches_wearing(player)
				&& matches_items(player)
				&& matches_money(Main.economy, player)
				&& matches_player_standing_on(player.getLocation());
		else validplayer = true;
		
		return matches_number_condition(percent, random) && validmob && validworld && validtime && validplayer;
	}
	
	boolean matches_number_condition(ArrayList<Number_condition> temp, int value)
	{
		if (temp == null) return true;
		for (Number_condition nc : temp)
		{
			if (nc.matches_number(value)) return true;
		}
		return false;
	}
	
	boolean matches_string(ArrayList<String> temp, String value)
	{
		if (temp == null) return true;
		
		if (temp.contains(value.toUpperCase())) return true;
		return false;
	}
	
	boolean matches_server_players(LivingEntity le)
	{
		if (server_players == null) return true;
		
		for (Player p : le.getServer().getOnlinePlayers()) if (server_players.contains(p.getName().toUpperCase())) return true;
		return false;
	}
	
	boolean matches_world_players(World world)
	{
		if (world_players == null) return true;
		
		for (Player p : world.getPlayers()) if (world_players.contains(p.getName().toUpperCase())) return true;
		return false;
	}
	
	//world matches
	boolean matches_raining(boolean stormy)
	{
		if (raining == -1) return true;
		
		if ((stormy && raining == 1) || (!stormy && raining == 0)) return true;
		return false;
	}
	
	boolean matches_killed_by_player(LivingEntity le)
	{
		if (killed_by_player == -1) return true;
		
		boolean temp = le.getKiller() instanceof Player ? true : false;
		if ((temp && killed_by_player == 1) || (!temp && killed_by_player == 0)) return true;
		return false;
	}
	
	boolean matches_thundering(boolean thunder)
	{
		if (thundering == -1) return true;
		
		if ((thunder && thundering == 1) || (!thunder && thundering == 0)) return true;
		return false;
	}
	
	@SuppressWarnings("unchecked")
	boolean matches_area_mob_class_count(World world, LivingEntity le)
	{
		if (area_mob_class_count == null) return true;		
		
		for (Loc loc : area_mob_class_count.keySet())
		{
			int i = 0;
			Loc temp = null;
			Location ll = null;
			if (!loc.region_name.equalsIgnoreCase(""))
			{
				if (Main.world_guard != null) temp = new Loc(world, loc.region_name); else return true;
			}
			
			if (temp == null) temp = loc;
			for (Entity e : world.getEntitiesByClass(le.getClass()))
			{
				ll = e.getLocation();
				if (ll.getBlockX() >= (temp.base.x - temp.range.x) && ll.getBlockX() <= (temp.base.x + temp.range.x) 
						&& ll.getBlockY() >= (temp.base.y - temp.range.y) && ll.getBlockY() <= (temp.base.y + temp.range.y)
						&& ll.getBlockZ() >= (temp.base.z - temp.range.z) && ll.getBlockZ() <= (temp.base.z + temp.range.z)) i++;
			}
			return matches_number_condition(area_mob_class_count.get(loc), i);
		}
		return false;
	}	
	
	boolean matches_area_mob_count(World world)
	{
		if (area_mob_count == null) return true;		
		
		for (Loc loc : area_mob_count.keySet())
		{
			int i = 0;
			Loc temp = null;
			Location ll = null;
			if (!loc.region_name.equalsIgnoreCase(""))
			{
				if (Main.world_guard != null) temp = new Loc(world, loc.region_name); else return true;
			}
			
			if (temp == null) temp = loc;
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
			return matches_number_condition(area_mob_count.get(loc), i);
		}
		return false;
	}	
	
	boolean matches_region(Location loc)
	{
		if (regions == null || Main.world_guard == null) return true;

		for (String r : regions)
		{
			ProtectedRegion pr = Main.world_guard.getRegionManager(loc.getWorld()).getRegion(r);
			if (pr != null && pr.contains(loc.getBlockX(), loc.getBlockY(), loc.getBlockZ())) return true;
		}
		return false;
	}
	
	boolean matches_player_within(Entity entity)
	{
		if (player_within == null) return true;
		
		for (Entity e : entity.getNearbyEntities(player_within.x, player_within.y, player_within.z))
		{
			if (e instanceof Player) return true;
		}
		return false;
	}
	
	//end world matches

	boolean matches_mob_age(LivingEntity animal)
	{
		if (mob_ages == null || !(animal instanceof Animals)) return true;
		for (Number_condition nc : mob_ages)
		{
			if (nc.matches_number(((Animals)animal).getAge())) return true;
		}
		return false;
	}
	
	boolean matches_mob_standing_on(Location loc)
	{
		if (mob_standing_on == null) return true;
		
		Block block = loc.getBlock().getRelative(BlockFace.DOWN);
		if (mob_standing_on.contains(block.getType().name())) return true;
		
		for (String s : mob_standing_on)
		for (Matlist m : Utils.matlists)
		{
			if (m.names.contains(s))
			{
				if (block.getTypeId() == m.id && ((short)m.names.indexOf(s)) == block.getData()) return true;
			}			
		}
		return false;
	}
	
	boolean matches_killed_player_name(LivingEntity player)
	{
		if (killed_player_names == null || !(player instanceof Player)) return true;
		
		if (killed_player_names.contains(((Player)player).getName().toUpperCase())) return true;
		return false;
	}
	
	boolean matches_saddled(LivingEntity pig)
	{
		if (saddled == -1) return true;
		if (!(pig instanceof Pig)) return false;
		
		if ((((Pig)pig).hasSaddle() && saddled == 1) || (!((Pig)pig).hasSaddle() && saddled == 0)) return true;
		return false;
	}
	
	boolean matches_sheared(LivingEntity sheep)
	{
		if (sheared == -1) return true;
		if (!(sheep instanceof Sheep)) return false;
				
		if ((((Sheep)sheep).isSheared() && sheared == 1) || (!((Sheep)sheep).isSheared() && sheared == 0)) return true;
		return false;
	}

	boolean matches_wool_colour(LivingEntity sheep)
	{
		if (wool_colours == null) return true;
		if (!(sheep instanceof Sheep)) return false;
		
		if (wool_colours.contains(((Sheep)sheep).getColor())) return true;
		return false;
	}
	
	boolean matches_powered(LivingEntity creeper)
	{
		if (powered == -1) return true;
		if (!(creeper instanceof Creeper)) return false;
		
		if ((((Creeper)creeper).isPowered() && powered == 1) || (!((Creeper)creeper).isPowered() && powered == 0)) return true;
		return false;
	}
	
	boolean matches_angry(LivingEntity animal)
	{
		if (angry == -1) return true;
		if (!(animal instanceof Wolf || animal instanceof PigZombie)) return false;
		
		if (animal instanceof Wolf)
		{
			if ((((Wolf)animal).isAngry() && angry == 1) || (!((Wolf)animal).isAngry() && angry == 0)) return true;
		}
		else if (animal instanceof PigZombie)
		{
			if ((((PigZombie)animal).isAngry() && angry == 1) || (!((PigZombie)animal).isAngry() && angry == 0)) return true;
		}
		return false;
	}
	
	boolean matches_tame(LivingEntity wolf)
	{
		if (tamed == -1 || !(wolf instanceof Wolf)) return true;
		if (!(wolf instanceof Wolf)) return false;
		
		if ((((Wolf)wolf).isTamed() && tamed == 1) || (!((Wolf)wolf).isTamed() && tamed == 0)) return true;
		return false;
	}

	//end mob matches

	boolean matches_enchantments(ItemStack is, Item i)
	{
		if (is.getEnchantments().size() == 0) return false;

		boolean validtemp = false;
		for (Enchantment e : i.enchantments.keySet())
		{									
			int ench_level = is.getEnchantmentLevel(Enchantment.getByName(e.getName().toUpperCase()));
			validtemp = i.enchantments.get(e).contains(ench_level);
			
			if (!i.match_all_enchantments)
			{
				if (validtemp) return true;
			}
			else
			{
				if (!validtemp) return false;
			}
		}	
		return validtemp;
	}
	
	boolean matches_player_standing_on(Location loc)
	{
		if (player_standing_on == null) return true;
		
		Block block = loc.getBlock().getRelative(BlockFace.DOWN);
		if (player_standing_on.contains(block.getType().name())) return true;
		
		for (String s : player_standing_on)
		for (Matlist m : Utils.matlists)
		{
			if (m.names.contains(s))
			{
				if (block.getTypeId() == m.id && ((short)m.names.indexOf(s)) == block.getData()) return true;
			}			
		}
		return false;
	}
	
	boolean matches_wearing(Player player)
	{
		if (player_wearing == null) return true;
		
		boolean matches = false;
		List<ItemStack> temp = new ArrayList<ItemStack>();
		temp.add(player.getInventory().getHelmet());
		temp.add(player.getInventory().getChestplate());
		temp.add(player.getInventory().getLeggings());
		temp.add(player.getInventory().getBoots());
		if (temp.size() != 0)
		{
			for (Item w : player_wearing)
			{
				for (ItemStack is : temp)
				if (is != null)
				{
					matches = is.getType() == Material.matchMaterial(w.name);
					
					if (matches)
					{
						if (w.enchantments != null) // enchants match requested
						{
							matches = matches_enchantments(is, w);
						}
					}	
				
					if (match_all_wearing)
					{
						if (!matches) return false;
					}
					else
					{
						if (matches) return true;
					}
				}
			}
		} else return false;			
		return matches;
	}
	
	boolean matches_money(Economy economy, Player player)
	{
		if (player_money == null || economy == null) return true;
		
		double d = economy.getBalance(player.getName());		
		for (Number_condition nc : player_money)
		{
			if (nc.matches_number((int) Math.round(d))) return true;
		}
		return false;
	}
	
	boolean matches_quantity(Item item, int amount)
	{
		if (item.quantities == null) return true;
		if (item.quantities.contains(amount)) return true;
		return false;
	}
	
	boolean matches_holding(Player player)
	{		
		if (player_holding == null) return true;
		
		boolean validholding = false;
		ItemStack is = player.getItemInHand();
		int amount = is.getAmount();
		for (Item w : player_holding)
		{
			Material material = Material.matchMaterial(w.name);
			boolean matches = false;
			if (material != null)
			{
				if (is.getType() == material) matches = true;
			}
			else
			{
				String s = w.name.toUpperCase();
				for (Matlist m : Utils.matlists)
				{
					if (m.names.contains(s))
					{
						if (is.getTypeId() == m.id && is.getData().getData() == (short)m.names.indexOf(s)) 
						{
							matches = true;
							break;
						}
					}
				}
			}
			
			if (matches && matches_quantity(w, amount))
			{
				if (w.enchantments != null)
				{
					validholding = matches_enchantments(is, w);
					if (w.match_all_enchantments)
					{
						if (!validholding) return false;
					}
					else
					{
						if (validholding) return true;
					}
				}
				else return true;
			}	
		}
		return validholding;
	}
	
	boolean matches_items(Player player)
	{
		if (player_items == null) return true;
		
		boolean validitems = false;
		for (Item i : player_items)
		{
			int count = 0;
			for (ItemStack is : player.getInventory().getContents())
			if (is != null)
			{
				int amount = is.getAmount();
				Material material = Material.matchMaterial(i.name);
				boolean matches = false;
				if (material != null)
				{
					if (is.getType() == material) matches = true;
				}
				else
				{
					String s = i.name.toUpperCase();
					for (Matlist m : Utils.matlists)
					{
						if (m.names.contains(s))
						{
							if (is.getTypeId() == m.id && is.getData().getData() == (short)m.names.indexOf(s)) 
							{
								matches = true;
								break;
							}
						}
					}
				}
				if (matches)
				{
					if (i.enchantments != null) // enchants match requested
					{
						if (matches_enchantments(is, i)) count += amount;
					} else count += amount; // items match, no enchants to worry about
				}					
			}
			validitems = matches_quantity(i, count);
			
			if (match_all_items)
			{
				if (!validitems) return false;
			}
			else
			{
				if (validitems) return true;
			}
		}
		return validitems;
	}
}

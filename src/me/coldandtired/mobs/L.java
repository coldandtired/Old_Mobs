package me.coldandtired.mobs;


import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.DyeColor;
import org.bukkit.Effect;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Animals;
import org.bukkit.entity.Creeper;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Ocelot;
import org.bukkit.entity.Pig;
import org.bukkit.entity.PigZombie;
import org.bukkit.entity.Player;
import org.bukkit.entity.Sheep;
import org.bukkit.entity.Slime;
import org.bukkit.entity.Villager;
import org.bukkit.entity.Wolf;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import com.herocraftonline.heroes.characters.Monster;
import com.khorn.terraincontrol.bukkit.BukkitWorld;
import com.sk89q.worldedit.BlockVector;
import com.sk89q.worldguard.protection.managers.RegionManager;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;

import me.coldandtired.api.Mob;
import me.coldandtired.mobs.api.events.Mob_created_event;
import me.coldandtired.mobs.conditions.Number_condition;
import me.coldandtired.mobs.data.Autospawn;
import me.coldandtired.mobs.data.Autospawn_location;
import me.coldandtired.mobs.data.Bounty;
import me.coldandtired.mobs.data.Config;
import me.coldandtired.mobs.data.Creature_data;
import me.coldandtired.mobs.data.Damage_value;
import me.coldandtired.mobs.data.Death_message;
import me.coldandtired.mobs.data.Drops;
import me.coldandtired.mobs.data.Exp;
import me.coldandtired.mobs.data.Item;
import me.coldandtired.mobs.data.Item_enchantment;
import me.coldandtired.mobs.data.Mob_properties;
import me.coldandtired.mobs.data.Outcome;
import me.coldandtired.mobs.data.Potion_effect;
import me.coldandtired.mobs.listeners.Main_listener;

public class L 
{
	public static Random rng = new Random();
	static DecimalFormat f = new DecimalFormat("#,###.##");
    static List<String> unsafe_blocks = Arrays.asList("AIR", "LAVA", "WATER", "STATIONARY_WATER", "STATIONARY_LAVA");

    // general helpers
	
	public static void spawn_mob(List<Block> safe_blocks, Autospawn as, boolean from_command, World w)
	{
		if (safe_blocks == null) return;
				
		int amount = 1;
		if (as.manual && from_command && as.manual_amount != null) amount = as.manual_amount.get(get_random_choice(as.manual_amount.size()));
		else if (as.amount != null) amount = as.amount.get(get_random_choice(as.amount.size()));
		
		for (int i = 0; i < amount; i++)
		{
			Block b = safe_blocks.get(get_random_choice(safe_blocks.size()));
			Main_listener.autospawn = as;
			w.spawnEntity(b.getLocation(), EntityType.valueOf(as.mob_name));
			if (Config.log_level > 1) log("Autospawned " + as.mob_name);
		}
	}
	
	public static boolean is_above_ground(Block b)
	{
		return b.getLightFromSky() > 13;
	}
	
	public static boolean is_safe_to_spawn(Block b, Boolean above_ground, String safe_type, Material material)
	{	
		if (!b.getChunk().isLoaded()) return false;
		
		if (b.getType() != material) return false;
		
		if (above_ground != null && above_ground != is_above_ground(b)) return false;			
			
		Material m = b.getRelative(BlockFace.NORTH).getType();
		if (m != material) return false;
			
		m = b.getRelative(BlockFace.EAST).getType();
		if (m != material) return false;
			
		m = b.getRelative(BlockFace.SOUTH).getType();
		if (m != material) return false;
			
		m = b.getRelative(BlockFace.WEST).getType();
		if (m != material) return false;
		
		m = b.getRelative(BlockFace.NORTH_EAST).getType();
		if (m != material) return false;
			
		m = b.getRelative(BlockFace.NORTH_WEST).getType();
		if (m != material) return false;
			
		m = b.getRelative(BlockFace.SOUTH_EAST).getType();
		if (m != material) return false;
			
		m = b.getRelative(BlockFace.SOUTH_WEST).getType();
		if (m != material) return false;
		
		m = b.getRelative(BlockFace.DOWN).getType();
		
		if (safe_type.equalsIgnoreCase("land"))
		{
			if (!m.isBlock() || unsafe_blocks.contains(m.name())) return false;
		}
		else if (m != material) return false;

		b = b.getRelative(BlockFace.UP);
		if (b.getType() != material) return false;
		
		m = b.getRelative(BlockFace.NORTH).getType();
		if (m != material) return false;
			
		m = b.getRelative(BlockFace.EAST).getType();
		if (m != material) return false;
			
		m = b.getRelative(BlockFace.SOUTH).getType();
		if (m != material) return false;
			
		m = b.getRelative(BlockFace.WEST).getType();
		if (m != material) return false;
		
		m = b.getRelative(BlockFace.NORTH_EAST).getType();
		if (m != material) return false;
			
		m = b.getRelative(BlockFace.NORTH_WEST).getType();
		if (m != material) return false;
			
		m = b.getRelative(BlockFace.SOUTH_EAST).getType();
		if (m != material) return false;
			
		m = b.getRelative(BlockFace.SOUTH_WEST).getType();
		if (m != material) return false;
		
		return true;
	}
	
	public static String get_biome_name(BukkitWorld bw, Block b)
	{		
		String temp = null;
		if (bw != null)
		{
			int id = bw.getBiome(b.getX(), b.getZ());
			temp = bw.getBiomeById(id).getName();
		}
		if (temp == null) temp = b.getBiome().name();
		return temp;
	}
	
	public static boolean is_block_in_regions(Block b, List<String> regions)
	{
		if (Main.world_guard == null) return false;
		RegionManager rm = Main.world_guard.getRegionManager(b.getWorld());
		if (rm == null) return false;
		for (String region : regions)
		{
			ProtectedRegion pr = rm.getRegion(region);
			if (pr != null && pr.contains(b.getX(), b.getY(), b.getZ())) return true;
		}
		return false;
	}
	
	/** Gets a list of all blocks in the biome */
	public static List<Block> get_blocks_in_biome(BukkitWorld bw, String mob_name, World world, String biome, Boolean above_ground)
	{
		List<Chunk> chunks = new ArrayList<Chunk>();
		for (Chunk c : world.getLoadedChunks())
		{
			Block b = c.getBlock(7, 0, 7);			
			if (get_biome_name(bw, b).equalsIgnoreCase(biome)) chunks.add(c);
		}
		
		if (chunks.size() == 0) return null;
		
		String safe_type = "land";
		Material material = Material.AIR;
		
		if (mob_name.equalsIgnoreCase("blaze") || mob_name.equalsIgnoreCase("ghast")
				|| mob_name.equalsIgnoreCase("enderdragon")) safe_type = "air";
			
		if (mob_name.equalsIgnoreCase("squid"))
		{
			safe_type = "water";
			material = Material.WATER;
		}
		
		List<Block> temp = new ArrayList<Block>();
		
		for (Chunk c : chunks)
		{			
			for (int x = 0; x < 15; x++)
			{
				for (int y = 0; y < world.getMaxHeight(); y++)
				{
					for (int z = 0; z < 15; z++)
					{
						Block b = c.getBlock(x, y, z);
						if (is_safe_to_spawn(b, above_ground, safe_type, material)) temp.add(b);
					}
				}
			}			
		}
		if (temp.size() == 0) temp = null;
		return temp;
	}
	
	/** Gets a list of all blocks in the region */
	public static List<Block> get_blocks_in_region(String mob_name, World world, String region, Boolean above_ground, List<String> temp_biomes)
	{
		if (Main.world_guard == null) return null;
		RegionManager rm = Main.world_guard.getRegionManager(world);
		if (rm == null) return null;
		ProtectedRegion pr = rm.getRegion(region);
		if (pr == null) return null;
		
		String safe_type = "land";
		Material material = Material.AIR;
		
		if (mob_name.equalsIgnoreCase("blaze") || mob_name.equalsIgnoreCase("ghast")
				|| mob_name.equalsIgnoreCase("enderdragon")) safe_type = "air";
			
		if (mob_name.equalsIgnoreCase("squid"))
		{
			safe_type = "water";
			material = Material.WATER;
		}
		
		List<Block> temp = new ArrayList<Block>();
		BlockVector min = pr.getMinimumPoint();
		BlockVector max = pr.getMaximumPoint();
		for (int x = min.getBlockX(); x < max.getBlockX(); x++)
		{
			for (int y = min.getBlockY(); y < max.getBlockY(); y++)
			{
				for (int z = min.getBlockZ(); z < max.getBlockZ(); z++)
				{
					Block b = world.getBlockAt(x, y, z);
					if (is_safe_to_spawn(b, above_ground, safe_type, material))
					{
						if (temp_biomes == null || temp_biomes.contains(b.getBiome().name())) temp.add(b);
					}
				}
			}
		}
		if (temp.size() == 0) temp = null;
		return temp;
	}
	
	public static List<Block> get_blocks_in_range(BukkitWorld bw, String mob_name, int xbase, int ybase, int zbase, Autospawn_location sl, World world, Boolean above_ground, List<String> temp_biomes, List<String> temp_regions)
	{
		String safe_type = "land";
		Material material = Material.AIR;
		
		if (mob_name.equalsIgnoreCase("blaze") || mob_name.equalsIgnoreCase("ghast")
				|| mob_name.equalsIgnoreCase("enderdragon")) safe_type = "air";
			
		if (mob_name.equalsIgnoreCase("squid"))
		{
			safe_type = "water";
			material = Material.WATER;
		}
		
		List<Block> temp = new ArrayList<Block>();
		
		for (int x = xbase - sl.xrange; x <= xbase + sl.xrange; x++)
		{
			if (x <= xbase - sl.min_xrange || x >= xbase + sl.min_xrange)
			{
				for (int z = zbase - sl.zrange; z <= zbase + sl.zrange; z++)
				{
					if (z <= zbase - sl.min_zrange || z >= zbase + sl.min_zrange)
					{
						for (int y = ybase - sl.yrange; y <= ybase + sl.yrange; y++)
						{
							if ((y <= ybase - sl.min_yrange || y >= ybase + sl.min_yrange) && y < world.getMaxHeight())
							{
								Block b = world.getBlockAt(x, y, z);
								if (is_safe_to_spawn(b, above_ground, safe_type, material))
								{
									if (temp_biomes == null || temp_biomes.contains(get_biome_name(bw, b)))
									{
										if (temp_regions == null || is_block_in_regions(b, temp_regions)) temp.add(b);
									}
								}
							}
						}
					}
				}
			}
		}
		if (temp.size() == 0) temp = null;
		return temp;
	}
	
	public static LivingEntity get_mob_from_id(String id)
	{
		for (World w : Bukkit.getWorlds())
		{
			if (!ignore_world(w))
			{
				for (LivingEntity le : w.getLivingEntities())
				{
					if (le.getUniqueId().toString().equalsIgnoreCase(id)) return le;
				}
			}
		}
		return null;
	}
	
	public static int get_fallthrough(Creature_data cd, String spawn_reason)
	{
		return spawn_reason.equalsIgnoreCase("autospawned") ? cd.as_fallthrough_type : cd.gen_fallthrough_type;
	}
	
	public static void log(Object message)
	{
		Main.logger.info("" + message);
	}
	
	public static void warn(Object message)
	{
		Main.logger.warning("" + message);
	}
	
	public static boolean ignore_world(World world)
	{
		return Config.ignored_worlds != null && Config.ignored_worlds.contains(world.getName().toUpperCase());
	}
	
	public static LivingEntity return_le(Entity entity)
	{
		return entity instanceof LivingEntity ? (LivingEntity)entity : null;
	}
	
	static boolean use_outcome(Outcome o, String spawn_reason)
	{
		if (o == null) return false;
		boolean b = spawn_reason.equalsIgnoreCase("autospawned");
		return o.affected_mobs == 0 || (o.affected_mobs == 1 && !b) || (o.affected_mobs == 2 && b);
	}
	
	public static int get_quantity(List<Integer> choices)
	{
		 return choices.get(get_random_choice(choices.size()));
	}
	
	public static double get_bounty(List<Double> choices)
	{
		 return choices.get(get_random_choice(choices.size()));
	}
	
	public static int get_random_choice(int size)
	{
		return rng.nextInt(size);
	}
	
	// turns "5 to 10" into an array 5, 6, 7, 8, 9, 10 etc.
	public static List<Integer> fill_int_properties(String property)
	{
		if (property.equalsIgnoreCase("")) return null;
		
		property = property.replaceAll(" ", "").toLowerCase();
		
		List<Integer> temp = new ArrayList<Integer>();
		String[] temp1 = property.split(",");
		for (String s : temp1)
		{
			if (s.contains("to"))
			{
				String[] temp2 = s.split("to");
				int low = Math.min(Integer.parseInt(temp2[0]), Integer.parseInt(temp2[1]));
				int high = Math.max(Integer.parseInt(temp2[0]), Integer.parseInt(temp2[1]));
				for (int i = low; i <= high; i++) temp.add(i);
			}
			else temp.add(Integer.parseInt(s));
		}		
		
		return temp;
	}
	
	public static List<Double> fill_double_properties(String property)
	{
		if (property.equalsIgnoreCase("")) return null;
		
		property = property.replaceAll(" ", "").toLowerCase();
		
		List<Double> temp = new ArrayList<Double>();
		String[] temp1 = property.split(",");
		for (String s : temp1)
		{
			if (s.contains("to"))
			{
				String[] temp2 = s.split("to");
				double low = Math.min(Double.parseDouble(temp2[0]), Double.parseDouble(temp2[1]));
				double high = Math.max(Double.parseDouble(temp2[0]), Double.parseDouble(temp2[1]));
				for (double i = low; i <= high; i++) temp.add(i);
			}
			else temp.add(Double.parseDouble(s));
		}			
		return temp;
	}
	
	// turns "above 5" into a number condition array
	public static List<Number_condition> fill_number_values(String number_condition)
	{
		List<Number_condition> values = new ArrayList<Number_condition>();
		String[] temp = number_condition.split(",");
		for (String s : temp) values.add(new Number_condition(s));
		return values;
	}
	
	// checks if the number condition ("above 5") is true for the value
	public static boolean matches_number_condition(List<Number_condition> values, int value)
	{
		if (values == null) return true;
		for (Number_condition nc : values) if (nc.matches_number(value)) return true;
		
		return false;
	}
	
	public static int return_int_from_array(List<Integer> values)
	{
		return values.get(get_random_choice(values.size()));
	}
	
	public static Boolean return_bool_from_string(String s)
	{
		if (s.equalsIgnoreCase("default")) return null;
		if (s.equalsIgnoreCase("yes")) return true;
		if (s.equalsIgnoreCase("no")) return false;
		if (s.equalsIgnoreCase("random")) return rng.nextBoolean();
		return true;
	}
	
	// turns multiple values in one line into an array
	public static List<String> fill_string_values(String string_condition)
	{
		List<String> values = new ArrayList<String>();
		string_condition = string_condition.toUpperCase();
		String[] temp = string_condition.split(",");
		for (String s : temp) values.add(s);
		return values;
	}
	
	public static boolean matches_string(List<String> values, String value)
	{
		if (values == null) return true;
		
		if (values.contains(value.toUpperCase())) return true;
		return false;
	}
	
	public static Mob make_mob(LivingEntity le, Mob_properties props, Drops drops, Map<String, Damage_value> damage_properties, String spawn_reason, int random, String autospawn_id)
	{
		Mob m = new Mob();
		
		m.setMob_id(le.getUniqueId().toString());
		m.setName(le.getType().name());
		
		if (props != null)
		{
			if (props.hp != null)
			{
				m.setHp(L.return_int_from_array(props.hp));
				m.setMax_hp(m.getHp());
			}
			
			if (props.hp_per_size != null) m.setHp_per_size(L.return_int_from_array(props.hp_per_size));
			if (props.size != null) m.setSize(L.return_int_from_array(props.size));
			if (props.tamed_hp != null) m.setTamed_hp(L.return_int_from_array(props.tamed_hp));
			if (props.damage != null) m.setDamage(L.return_int_from_array(props.damage));
			if (props.explosion_size != null) m.setExplosion_size(L.return_int_from_array(props.explosion_size));
			if (props.split_into != null) m.setSplit_into(L.return_int_from_array(props.split_into));
			if (props.burn_duration != null) m.setBurn_duration(L.return_int_from_array(props.burn_duration));
			if (props.max_lifetime != null)	m.setDeath_time(System.currentTimeMillis() + (L.return_int_from_array(props.max_lifetime) * 1000));
			if (props.invincibility_ticks != null) m.setInvincibility_ticks(L.return_int_from_array(props.invincibility_ticks));
			
			if (props.boss_mob != null) m.setBoss_mob(L.return_bool_from_string(props.boss_mob));
			if (props.adult != null) m.setAdult(L.return_bool_from_string(props.adult));
			if (props.saddled != null) m.setSaddled(L.return_bool_from_string(props.saddled));
			if (props.angry != null) m.setAngry(L.return_bool_from_string(props.angry));
			if (props.tamed != null) m.setTamed(L.return_bool_from_string(props.tamed));
			if (props.sheared != null) m.setSheared(L.return_bool_from_string(props.sheared));
			if (props.powered != null) m.setPowered(L.return_bool_from_string(props.powered));
			if (props.can_be_dyed != null) m.setCan_be_dyed(L.return_bool_from_string(props.can_be_dyed));
			if (props.can_be_sheared != null) m.setCan_be_sheared(L.return_bool_from_string(props.can_be_sheared));
			if (props.can_be_tamed != null) m.setCan_be_tamed(L.return_bool_from_string(props.can_be_tamed));
			if (props.can_become_pig_zombie != null) m.setCan_become_pig_zombie(L.return_bool_from_string(props.can_become_pig_zombie));
			if (props.can_become_powered_creeper != null) m.setCan_become_powered_creeper(L.return_bool_from_string(props.can_become_powered_creeper));
			if (props.can_burn != null) m.setCan_burn(L.return_bool_from_string(props.can_burn));
			if (props.can_heal != null) m.setCan_heal(L.return_bool_from_string(props.can_heal));
			if (props.can_overheal != null) m.setCan_overheal(L.return_bool_from_string(props.can_overheal));
			if (props.can_create_portal != null) m.setCan_create_portal(L.return_bool_from_string(props.can_create_portal));
			if (props.can_destroy_blocks != null) m.setCan_destroy_blocks(L.return_bool_from_string(props.can_destroy_blocks));
			if (props.can_move_blocks != null) m.setCan_move_blocks(L.return_bool_from_string(props.can_move_blocks));
			if (props.can_grow_wool != null) m.setCan_grow_wool(L.return_bool_from_string(props.can_grow_wool));
			if (props.can_graze != null) m.setCan_graze(L.return_bool_from_string(props.can_graze));
			if (props.can_teleport != null) m.setCan_teleport(L.return_bool_from_string(props.can_teleport));
			if (props.safe != null) m.setSafe(L.return_bool_from_string(props.safe));
			if (props.fiery_explosion != null) m.setFiery_explosion(L.return_bool_from_string(props.fiery_explosion));
			if (props.wool_colours != null) m.setWool_colour(L.set_wool_colour(props.wool_colours));
			if (props.ocelot_types != null) m.setOcelot_type(L.set_ocelot_type(props.ocelot_types));
			if (props.villager_types != null) m.setVillager_type(L.set_villager_type(props.villager_types));
		}
		m.setDrops(drops);
		
		m.setDamage_properties(damage_properties);
		m.setSpawn_reason(spawn_reason);
		m.setAutospawn_id(autospawn_id);
		m.setRandom(random);
		return m;
	}
	
	public static void setup_mob(Creature_data cd, LivingEntity le, String spawn_reason, int random, String autospawn_id)
	{		
		Outcome o = get_mob_properties_outcome(cd, le, spawn_reason, random, autospawn_id);
		Mob_properties props = merge_mob_properties(o, cd, spawn_reason);
		
		o = get_potion_effects_outcome(cd, le, spawn_reason, random, autospawn_id);
		Collection<PotionEffect> potion_effects = merge_potion_effects(o, cd, spawn_reason);
		
		Drops drops = null;
		HashMap<String, Damage_value> damage_properties = null;		
				
		boolean b = spawn_reason.equalsIgnoreCase("autospawned");
		if ((cd.gen_drops_check_type != 1 && !b) || cd.as_drops_check_type != 1 && b)
		{				
			o = get_drops_outcome(cd, le, spawn_reason, null, random, autospawn_id);			
			drops = merge_drops(o, cd, spawn_reason);
		}
		
		if ((cd.gen_damages_check_type != 1 && !b) || cd.as_damages_check_type != 1 && b)
		{
			o = get_damages_outcome(cd, le, spawn_reason, null, random, autospawn_id);
			damage_properties = merge_damage_properties(o, cd, spawn_reason);
		}
		
		//if (props == null && potion_effects == null && drops == null && damage_properties == null) return;	
		
		Mob mob = make_mob(le, props, drops, damage_properties, spawn_reason, random, autospawn_id);	
		
		if (mob.getInvincibility_ticks() != null) le.setMaximumNoDamageTicks(mob.getInvincibility_ticks());
		
		Mob_created_event mob_created_event = new Mob_created_event(mob, le, potion_effects);
		Main.plugin.getServer().getPluginManager().callEvent(mob_created_event);
		
		if (mob_created_event.isCancelled()) return;
		
		mob = mob_created_event.get_mob();
		Main.all_mobs.put(le.getUniqueId().toString(), mob);
		
		potion_effects = mob_created_event.get_potion_effects();
		
		if (Main.heroes != null)
		{
			Monster monster = Main.heroes.getCharacterManager().getMonster(le);
			if (mob.getHp() != null)	monster.setMaxHealth(mob.getHp());
			if (mob.getDamage() != null) monster.setDamage(mob.getDamage());
		}
		
		if (potion_effects != null) le.addPotionEffects(potion_effects);		
	
		if (mob.getBoss_mob() != null && mob.getBoss_mob()) le.getWorld().playEffect(le.getLocation(), Effect.MOBSPAWNER_FLAMES, 100);		
		
		if (le instanceof Slime)
		{
			Slime slime = (Slime)le;
			if (!spawn_reason.equalsIgnoreCase("SLIME_SPLIT") && mob.getSize() != null) slime.setSize(mob.getSize());
			if (mob.getHp_per_size() != null) mob.setHp(slime.getSize() * mob.getHp_per_size());
		}	
			
		if (le instanceof Animals && mob.getAdult() != null)
		{
			Animals animal = (Animals)le;
		
			if (mob.getAdult() == true) animal.setAdult(); else animal.setBaby();
		}
			
		if (le instanceof Pig && mob.getSaddled() != null)
		{
			((Pig)le).setSaddle(mob.getSaddled());
		}
		else if (le instanceof PigZombie && mob.getAngry() != null)
		{
			((PigZombie)le).setAngry(mob.getAngry());
		}
		else if (le instanceof Wolf)
		{
			Wolf wolf = (Wolf)le;			
			if (mob.getAngry() != null) wolf.setAngry(mob.getAngry());
			if (!wolf.isAngry())
			{
				if (mob.getTamed() != null) wolf.setTamed(mob.getTamed());
				if (mob.getCan_be_tamed() != null && !mob.getCan_be_tamed()) wolf.setTamed(false);
				if (wolf.isTamed() && mob.getTamed_hp() != null) mob.setHp(mob.getTamed_hp());
			}
			else wolf.damage(0, get_nearby_player(wolf));
		}
		else if (le instanceof Sheep)
		{
			Sheep sheep = (Sheep)le;
			if (mob.getSheared() != null) sheep.setSheared(mob.getSheared());
			if (mob.getCan_grow_wool() != null && !mob.getCan_grow_wool()) sheep.setSheared(false);
			
			if (mob.getWool_colour() != null) sheep.setColor(mob.getWool_colour());
		}
		else if (le instanceof Ocelot && mob.getOcelot_type() != null)
		{
			((Ocelot)le).setCatType(mob.getOcelot_type());
		}
		else if (le instanceof Villager && mob.getVillager_type() != null)
		{
			((Villager)le).setProfession(mob.getVillager_type());			
		}
		else if (le instanceof Creeper && mob.getPowered() != null) 
		{
			((Creeper)le).setPowered(mob.getPowered());		
		}
	}
	
	public static void convert_chunk(Chunk c)
	{
		for (Entity e : c.getEntities())
		{
			if (e instanceof LivingEntity)
			{		
				LivingEntity le = (LivingEntity)e;
				Creature_data cd = Main.tracked_mobs.get(le.getType().name());
				Mob mob = Main.all_mobs.get(le.getUniqueId().toString());
				
				if (mob != null)
				{
					// is in database
					if (cd == null || cd.load_behaviour_existing == 2)
					{
						// not tracked or admin wants vanilla
						Main.all_mobs.remove(le.getUniqueId().toString());
						continue;
					}
					else if (cd.load_behaviour_existing == 0)
					{
						// remove the mob
						le.remove();
						Main.all_mobs.remove(le.getUniqueId().toString());
						continue;
					}
					else if (cd.load_behaviour_existing == 1)
					{
						// kill the mob
						le.damage(10000);
						continue;
					}
					else if (cd.load_behaviour_existing == 3) 
					{
						// recalculate conditions using current values
						int random = rng.nextInt(100) + 1;						
						setup_mob(cd, le, "NATURAL", random, null);
					}
				}
				
				if (mob == null && cd != null && cd.load_behaviour_new != 2) 
				{
					// not previously tracked
					if (cd.load_behaviour_new == 0)
					{
						// remove the mob
						le.remove();
						continue;
					}
					else if (cd.load_behaviour_new == 1)
					{
						// kill the mob
						le.damage(10000);
						continue;
					}
					else if (cd.load_behaviour_new == 3) 
					{
						// recalculate conditions using current values
						int random = rng.nextInt(100) + 1;						
						setup_mob(cd, le, "NATURAL", random, null);
					}
				}
			}
		}
	}
	
	// end general helpers
	
	// creature spawn helpers
	
	public static Outcome get_previous_outcome(Creature_data cd, int outcome_id)
	{
		if (cd.outcomes != null && outcome_id != -1)
		{
			for (Outcome o : cd.outcomes) if (o.outcome_id == outcome_id) return o;
		}
		return null;
	}
	
	public static boolean merge_spawning(Creature_data cd, LivingEntity le, String spawn_reason, int random, String autospawn_id)
	{
		int fallthrough = get_fallthrough(cd, spawn_reason);
		Boolean b = can_spawn(cd, le, spawn_reason, random, autospawn_id);
		if (b != null) return b;
		
		if (fallthrough > 0 && use_outcome(cd.default_outcome, spawn_reason)) return cd.default_outcome.spawn;
		
		return true;
	}
	
	static Boolean can_spawn(Creature_data cd, LivingEntity le, String spawn_reason, int random, String autospawn_id)
	{
		if (cd.outcomes != null)
		{
			for (Outcome o : cd.outcomes)
			{
				if (!o.spawn && use_outcome(o, spawn_reason))
				{
					boolean found = true;
					for (Condition c : o.conditions) if (!c.check(le, le.getWorld(), le.getLocation(), spawn_reason, null, random, autospawn_id)) found = false;
					if (found) return o.spawn;
				}
			}
		}
		return null;
	}

	public static Mob_properties merge_mob_properties(Outcome o, Creature_data cd, String spawn_reason)
	{
		int fallthrough = get_fallthrough(cd, spawn_reason);
		
		if (o == null)
		{
			if (fallthrough == 0) return null;
			
			if (use_outcome(cd.default_outcome, spawn_reason)) return cd.default_outcome.mob_properties; else return null;
		}
		else
		{
			if (fallthrough == 2 && use_outcome(cd.default_outcome, spawn_reason) && cd.default_outcome.mob_properties != null)
			{
				if (o.mob_properties != null)
				{
					if (o.mob_properties.hp == null) o.mob_properties.hp = cd.default_outcome.mob_properties.hp;
					if (o.mob_properties.damage == null) o.mob_properties.damage = cd.default_outcome.mob_properties.damage;
					if (o.mob_properties.villager_types == null) o.mob_properties.villager_types = cd.default_outcome.mob_properties.villager_types;
					if (o.mob_properties.ocelot_types == null) o.mob_properties.ocelot_types = cd.default_outcome.mob_properties.ocelot_types;
					if (o.mob_properties.burn_duration == null) o.mob_properties.burn_duration = cd.default_outcome.mob_properties.burn_duration;
					if (o.mob_properties.can_burn == null) o.mob_properties.can_burn = cd.default_outcome.mob_properties.can_burn;
					if (o.mob_properties.safe == null) o.mob_properties.safe = cd.default_outcome.mob_properties.safe;
					if (o.mob_properties.adult == null) o.mob_properties.adult = cd.default_outcome.mob_properties.adult;
					if (o.mob_properties.can_breed == null) o.mob_properties.can_breed = cd.default_outcome.mob_properties.can_breed;
					if (o.mob_properties.can_heal == null) o.mob_properties.can_heal = cd.default_outcome.mob_properties.can_heal;
					if (o.mob_properties.can_overheal == null) o.mob_properties.can_overheal = cd.default_outcome.mob_properties.can_overheal;
					if (o.mob_properties.size == null) o.mob_properties.size = cd.default_outcome.mob_properties.size;
					if (o.mob_properties.hp_per_size == null) o.mob_properties.hp_per_size = cd.default_outcome.mob_properties.hp_per_size;
					if (o.mob_properties.split_into == null) o.mob_properties.split_into = cd.default_outcome.mob_properties.split_into;
					if (o.mob_properties.wool_colours == null) o.mob_properties.wool_colours = cd.default_outcome.mob_properties.wool_colours;
					if (o.mob_properties.can_be_dyed == null) o.mob_properties.can_be_dyed = cd.default_outcome.mob_properties.can_be_dyed;
					if (o.mob_properties.sheared == null) o.mob_properties.sheared = cd.default_outcome.mob_properties.sheared;
					if (o.mob_properties.can_grow_wool == null) o.mob_properties.can_grow_wool = cd.default_outcome.mob_properties.can_grow_wool;
					if (o.mob_properties.can_graze == null) o.mob_properties.can_graze = cd.default_outcome.mob_properties.can_graze;
					if (o.mob_properties.can_be_sheared == null) o.mob_properties.can_be_sheared = cd.default_outcome.mob_properties.can_be_sheared;
					if (o.mob_properties.tamed == null) o.mob_properties.tamed = cd.default_outcome.mob_properties.tamed;
					if (o.mob_properties.angry == null) o.mob_properties.angry = cd.default_outcome.mob_properties.angry;
					if (o.mob_properties.tamed_hp == null) o.mob_properties.tamed_hp = cd.default_outcome.mob_properties.tamed_hp;
					if (o.mob_properties.can_be_tamed == null) o.mob_properties.can_be_tamed = cd.default_outcome.mob_properties.can_be_tamed;
					if (o.mob_properties.saddled == null) o.mob_properties.saddled = cd.default_outcome.mob_properties.saddled;
					if (o.mob_properties.can_be_saddled == null) o.mob_properties.can_be_saddled = cd.default_outcome.mob_properties.can_be_saddled;
					if (o.mob_properties.can_become_pig_zombie == null) o.mob_properties.can_become_pig_zombie = cd.default_outcome.mob_properties.can_become_pig_zombie;
					if (o.mob_properties.powered == null) o.mob_properties.powered = cd.default_outcome.mob_properties.powered;
					if (o.mob_properties.can_become_powered_creeper == null) o.mob_properties.can_become_powered_creeper = cd.default_outcome.mob_properties.can_become_powered_creeper;
					if (o.mob_properties.fiery_explosion == null) o.mob_properties.fiery_explosion = cd.default_outcome.mob_properties.fiery_explosion;
					if (o.mob_properties.can_move_blocks == null) o.mob_properties.can_move_blocks = cd.default_outcome.mob_properties.can_move_blocks;
					if (o.mob_properties.can_teleport == null) o.mob_properties.can_teleport = cd.default_outcome.mob_properties.can_teleport;
					if (o.mob_properties.can_create_portal == null) o.mob_properties.can_create_portal = cd.default_outcome.mob_properties.can_create_portal;
					if (o.mob_properties.can_destroy_blocks == null) o.mob_properties.can_destroy_blocks = cd.default_outcome.mob_properties.can_destroy_blocks;
				}
				else o.mob_properties = cd.default_outcome.mob_properties;
				
				return o.mob_properties;
			}
			else return o.mob_properties;
		}
	}

	public static Outcome get_mob_properties_outcome(Creature_data cd, LivingEntity le, String spawn_reason, int random, String autospawn_id)
	{
		if (cd.outcomes != null)
		{
			for (Outcome o : cd.outcomes)
			{
				if (o.conditions != null && o.mob_properties != null && use_outcome(o, spawn_reason))
				{
					boolean found = true;
					for (Condition c : o.conditions) if (!c.check(le, le.getWorld(), le.getLocation(), spawn_reason, null, random, autospawn_id)) found = false;
					if (found) return o;
				}
			}
		}
		return null;
	}
	
	public static Collection<PotionEffect> merge_potion_effects(Outcome o, Creature_data cd, String spawn_reason)
	{
		int fallthrough = get_fallthrough(cd, spawn_reason);
		
		if (o != null) return set_potion_effects(o.potion_effects, o.all_potion_effects);
		
		if (fallthrough > 0 && use_outcome(cd.default_outcome, spawn_reason)) return set_potion_effects(cd.default_outcome.potion_effects, cd.default_outcome.all_potion_effects);
		
		return null;
	}
	
	public static Outcome get_potion_effects_outcome(Creature_data cd, LivingEntity le, String spawn_reason, int random, String autospawn_id)
	{
		if (cd.outcomes != null)
		{
			for (Outcome o : cd.outcomes)
			{
				if (o.conditions != null && o.potion_effects != null && use_outcome(o, spawn_reason))
				{
					boolean found = true;
					for (Condition c : o.conditions) if (!c.check(le, le.getWorld(), le.getLocation(), spawn_reason, null, random, autospawn_id)) found = false;
					if (found) return o;
				}
			}
		}
		return null;
	}
	
	public static Drops merge_drops(Outcome o, Creature_data cd, String spawn_reason)
	{
		int fallthrough = get_fallthrough(cd, spawn_reason);
		
		if (o == null)
		{
			if (fallthrough == 0) return null;
			
			if (use_outcome(cd.default_outcome, spawn_reason)) return cd.default_outcome.drops; else return null;
		}
		else
		{
			if (fallthrough == 2 && use_outcome(cd.default_outcome, spawn_reason) && cd.default_outcome.drops != null)
			{
				if (o.drops != null)
				{
					if (o.drops.items == null) o.drops.items = cd.default_outcome.drops.items;
					if (o.drops.exps == null) o.drops.exps = cd.default_outcome.drops.exps;
					if (o.drops.bounties == null) o.drops.bounties = cd.default_outcome.drops.bounties;
				}
				else o.drops = cd.default_outcome.drops;
				return o.drops;
			}
			else return o.drops;
		}
	}
	
	public static Outcome get_drops_outcome(Creature_data cd, LivingEntity le, String spawn_reason, Player p, int random, String autospawn_id)
	{
		if (cd.outcomes != null)
		{
			for (Outcome o : cd.outcomes)
			{
				if (o.conditions != null && o.drops != null && use_outcome(o, spawn_reason))
				{
					boolean found = true;
					for (Condition c : o.conditions) if (!c.check(le, le.getWorld(), le.getLocation(), spawn_reason, p, random, autospawn_id)) found = false;
					if (found) return o;
				}
			}
		}
		return null;
	}
	
	public static HashMap<String, Damage_value> merge_damage_properties(Outcome o, Creature_data cd, String spawn_reason)
	{
		int fallthrough = get_fallthrough(cd, spawn_reason);
		
		if (o == null)
		{
			if (fallthrough == 0) return null;
			
			if (use_outcome(cd.default_outcome, spawn_reason)) return cd.default_outcome.damage_properties; else return null;
		}
		else
		{
			if (fallthrough == 2 && use_outcome(cd.default_outcome, spawn_reason) && cd.default_outcome.damage_properties != null)
			{
				if (o.damage_properties != null)
				{
					Damage_value dv;
					if (o.damage_properties.get("BLOCK_EXPLOSION") == null)
					{
						dv = cd.default_outcome.damage_properties.get("BLOCK_EXPLOSION");
						if (dv != null) o.damage_properties.put("BLOCK_EXPLOSION", dv);
					}
					if (o.damage_properties.get("CONTACT") == null)
					{
						dv = cd.default_outcome.damage_properties.get("CONTACT");
						if (dv != null) o.damage_properties.put("CONTACT", dv);
					}
					if (o.damage_properties.get("CUSTOM") == null)
					{
						dv = cd.default_outcome.damage_properties.get("CUSTOM");
						if (dv != null) o.damage_properties.put("CUSTOM", dv);
					}
					if (o.damage_properties.get("DROWNING") == null)
					{
						dv = cd.default_outcome.damage_properties.get("DROWNING");
						if (dv != null) o.damage_properties.put("DROWNING", dv);
					}
					if (o.damage_properties.get("ENTITY_ATTACK") == null)
					{
						dv = cd.default_outcome.damage_properties.get("ENTITY_ATTACK");
						if (dv != null) o.damage_properties.put("ENTITY_ATTACK", dv);
					}
					if (o.damage_properties.get("ENTITY_EXPLOSION") == null)
					{
						dv = cd.default_outcome.damage_properties.get("ENTITY_EXPLOSION");
						if (dv != null) o.damage_properties.put("ENTITY_EXPLOSION", dv);
					}
					if (o.damage_properties.get("FALL") == null)
					{
						dv = cd.default_outcome.damage_properties.get("FALL");
						if (dv != null) o.damage_properties.put("FALL", dv);
					}
					if (o.damage_properties.get("FIRE") == null)
					{
						dv = cd.default_outcome.damage_properties.get("FIRE");
						if (dv != null) o.damage_properties.put("FIRE", dv);
					}
					if (o.damage_properties.get("FIRE_TICK") == null)
					{
						dv = cd.default_outcome.damage_properties.get("FIRE_TICK");
						if (dv != null) o.damage_properties.put("FIRE_TICK", dv);
					}
					if (o.damage_properties.get("LAVA") == null)
					{
						dv = cd.default_outcome.damage_properties.get("LAVA");
						if (dv != null) o.damage_properties.put("LAVA", dv);
					}
					if (o.damage_properties.get("LIGHTNING") == null)
					{
						dv = cd.default_outcome.damage_properties.get("LIGHTNING");
						if (dv != null) o.damage_properties.put("LIGHTNING", dv);
					}
					if (o.damage_properties.get("MAGIC") == null)
					{
						dv = cd.default_outcome.damage_properties.get("MAGIC");
						if (dv != null) o.damage_properties.put("MAGIC", dv);
					}
					if (o.damage_properties.get("MELTING") == null)
					{
						dv = cd.default_outcome.damage_properties.get("MELTING");
						if (dv != null) o.damage_properties.put("MELTING", dv);
					}
					if (o.damage_properties.get("POISON") == null)
					{
						dv = cd.default_outcome.damage_properties.get("POISON");
						if (dv != null) o.damage_properties.put("POISON", dv);
					}
					if (o.damage_properties.get("PROJECTILE") == null)
					{
						dv = cd.default_outcome.damage_properties.get("PROJECTILE");
						if (dv != null) o.damage_properties.put("PROJECTILE", dv);
					}
					if (o.damage_properties.get("STARVATION") == null)
					{
						dv = cd.default_outcome.damage_properties.get("STARVATION");
						if (dv != null) o.damage_properties.put("STARVATION", dv);
					}
					if (o.damage_properties.get("SUFFOCATION") == null)
					{
						dv = cd.default_outcome.damage_properties.get("SUFFOCATION");
						if (dv != null) o.damage_properties.put("SUFFOCATION", dv);
					}
					if (o.damage_properties.get("SUICIDE") == null)
					{
						dv = cd.default_outcome.damage_properties.get("SUICIDE");
						if (dv != null) o.damage_properties.put("SUICIDE", dv);
					}
					if (o.damage_properties.get("VOID") == null)
					{
						dv = cd.default_outcome.damage_properties.get("VOID");
						if (dv != null) o.damage_properties.put("VOID", dv);
					}
				}
				else o.damage_properties = cd.default_outcome.damage_properties;
				
				return o.damage_properties;
			}
			else return o.damage_properties;
		}
	}
	
	public static Outcome get_damages_outcome(Creature_data cd, LivingEntity le, String spawn_reason, Player p, int random, String autospawn_id)
	{
		if (cd.outcomes != null)
		{
			for (Outcome o : cd.outcomes)
			{
				if (o.conditions != null && o.damage_properties != null && use_outcome(o, spawn_reason))
				{
					boolean found = true;
					for (Condition c : o.conditions) if (!c.check(le, le.getWorld(), le.getLocation(), spawn_reason, p, random, autospawn_id)) found = false;
					if (found) return o;
				}
			}
		}
		return null;
	}
	
	static Collection<PotionEffect> set_potion_effects(ArrayList<Potion_effect> potion_effects, boolean all_potion_effects)
	{
		if (potion_effects == null) return null;
		
		List<PotionEffect> col = new ArrayList<PotionEffect>();
		if (all_potion_effects)
		{
			for (Potion_effect pe : potion_effects) 
				col.add(new PotionEffect(PotionEffectType.getByName(pe.name), 
						pe.duration.get(get_random_choice(pe.duration.size())), pe.level.get(get_random_choice(pe.level.size()))));
		}
		else
		{
			Potion_effect pe = potion_effects.get(get_random_choice(potion_effects.size()));
			col.add(new PotionEffect(PotionEffectType.getByName(pe.name), 
					pe.duration.get(get_random_choice(pe.duration.size())), pe.level.get(get_random_choice(pe.level.size()))));
		}
		return col;
	}
	
	public static DyeColor set_wool_colour(String[] wool_colours)
	{
		if (wool_colours == null) return null;
		
		String s = wool_colours[get_random_choice(wool_colours.length)].trim().toUpperCase();
		if (s.equalsIgnoreCase("default")) return null;
		else if (s.equalsIgnoreCase("random")) return DyeColor.values()[get_random_choice(DyeColor.values().length)];
		else return DyeColor.valueOf(s);
	}
	
	public static Ocelot.Type set_ocelot_type(String[] ocelot_types)
	{
		String s = ocelot_types[get_random_choice(ocelot_types.length)].trim().toUpperCase();
		if (s.equalsIgnoreCase("default")) return null;
		else if (s.equalsIgnoreCase("random")) return Ocelot.Type.values()[get_random_choice(Ocelot.Type.values().length)];
		else return Ocelot.Type.valueOf(s);
	}
	
	public static Villager.Profession set_villager_type(String[] villager_types)
	{
		String s = villager_types[get_random_choice(villager_types.length)].trim().toUpperCase();
		if (s.equalsIgnoreCase("default")) return null;
		else if (s.equalsIgnoreCase("random")) return Villager.Profession.values()[get_random_choice(Villager.Profession.values().length)];
		else return Villager.Profession.valueOf(s);
	}
	
	public static LivingEntity get_nearby_player(Entity entity)
	{
		List<Entity> temp = new ArrayList<Entity>();
		for (Entity e : entity.getNearbyEntities(50, 50, 50)) if (e instanceof Player) temp.add(e);
		
		if (temp.size() > 0) return (LivingEntity)temp.get(get_random_choice(temp.size()));
		return null;
	}
	
	// end creature spawn helpers
	
	// creature death helpers
	
	public static List<ItemStack> get_drops(Mob mob, Drops drops, List<ItemStack> old_drops)
	{
		if (drops.items == null) return old_drops;
		
		List<ItemStack> dropped = new ArrayList<ItemStack>();
		for (ItemStack is : old_drops) dropped.add(is);
		boolean replaced = false;
		for (Item item : drops.items)
		{
			if (!replaced && L.matches_number_condition(item.chances, mob.getRandom()))
			{
				if (item.replace)
				{
					dropped.clear();
					replaced = true;
				}
				int quantity = L.get_quantity(item.quantity);
				ItemStack is = new ItemStack(item.id, quantity, item.data);
				if (item.enchantments != null)
				{
					for (Item_enchantment ie : item.enchantments)
					{
						is.addUnsafeEnchantment(Enchantment.getByName(ie.name), L.get_quantity(ie.level));
					}
				}
				dropped.add(is);
			}
		}
		return dropped;
	}
	
	public static int get_exp(Mob mob, Drops drops, int old_exp)
	{
		if (drops.exps == null) return old_exp;
		
		boolean replaced = false;
		for (Exp exp : drops.exps)
		{
			if (!replaced && L.matches_number_condition(exp.chances, mob.getRandom()))
			{
				if (exp.replace)
				{
					old_exp = 0;
					replaced = true;
				}
				old_exp += L.get_quantity(exp.amount);
			}
		}
		return old_exp;
	}
	
	public static int get_bounties(Mob mob, Drops drops)
	{
		if (drops.bounties == null) return 0;
		
		int given_bounty = 0;
		
		for (Bounty b : drops.bounties)
		{
			if (L.matches_number_condition(b.chances, mob.getRandom()))
			{
				double amount = L.get_bounty(b.amount);
				given_bounty += amount;
			}
		}		
		return given_bounty;
	}
	
	public static void send_death_message(Death_message dm, String mob_name, int exp, Player p, List<ItemStack> drops, int given_bounty)
	{
		String s = dm.message;					

		s = s.replace("^mob^", mob_name);
		s = s.replace("^exp^", Integer.toString(exp));
		
		if (s.contains("^player^") && p != null) s = s.replace("^player^", p.getDisplayName());
		
		if ((s.contains("^money^") || s.contains("^total_money^")) && Main.economy != null && p != null)
		{
			s = s.replace("^total_money^", f.format(Main.economy.getBalance(p.getName())));
			s = s.replace("^money^", Integer.toString(given_bounty));
		}
		
		if (s.contains("^item_names^") || s.contains("^item_amounts^"))
		{
			String item_names = "";
			String item_amounts = "";
			
			for (ItemStack is : drops)
			{
				String ss = is.getType().name().toLowerCase();
				item_names += ", " + ss;
				item_amounts += ", " + is.getAmount() + " x " + ss;
			}
			s = s.replace("^item_names^", item_names);
			s = s.replace("^item_amounts^", item_amounts);
		}
		
		if (p != null)
		{
			if (dm.announce_messages) p.getServer().broadcastMessage(s);
			else
			{
				p.sendMessage(s);
				if (dm.log_messages) log(s);
			}
		}
	}
}
	// end creature death helpers
/*
	
public static boolean matches_quantity(Old_item item, int amount)
{
	if (item.quantities == null) return true;
	if (item.quantities.contains(amount)) return true;
	return false;
}


public static boolean matches_enchantments(ItemStack is, Old_item i)
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
}*/

package me.coldandtired.mobs;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.logging.Logger;

import me.coldandtired.mobs.conditions.Number_condition;

import org.bukkit.DyeColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.CreatureType;
import org.bukkit.entity.Entity;
import org.bukkit.inventory.ItemStack;

public class Utils 
{
	public static List<Matlist> matlists;
	public static Map<String, Matlist> groups;
	static Random rng = new Random();
	static List<String> mobs = Arrays.asList("blaze", "cavespider", "chicken", "cow", "creeper", "enderdragon", 
			"enderman", "ghast", "giant", "magmacube", "mushroom_cow", "pig", "pigzombie", "sheep", "silverfish", 
			"skeleton", "slime", "snowman", "spider", "squid", "villager", "wolf", "zombie");
	static private Logger logger;
	
	@SuppressWarnings("unchecked")
	public
	static ArrayList<String> fill_string_array(Object o)
	{
		ArrayList<String> temp = new ArrayList<String>();
		if (o instanceof ArrayList) for (String s : (ArrayList<String>)o) temp.add(s.replaceAll(" ", "").toUpperCase());
		else temp.add(((String)o).replaceAll(" ", "").toUpperCase());
		return temp;
	}
	
	public static boolean matches_string(ArrayList<String> values, String value)
	{
		if (values == null) return true;
		
		if (values.contains(value.toUpperCase())) return true;
		return false;
	}
	
	public static int fill_boolean(Object o)
	{
		if ((Boolean)o) return 1; else return 0;
	}
	
	static void setup_utils(Main plugin)
	{			
		logger = plugin.getLogger();
		matlists = new ArrayList<Matlist>();
		Matlist woods = new Matlist(17, "GENERIC_LOG,REDWOOD_LOG,BIRCH_LOG");
		Matlist wools = new Matlist(35, "WHITE_WOOL,ORANGE_WOOL,MAGENTA_WOOL,LIGHT_BLUE_WOOL,YELLOW_WOOL,LIME_WOOL,PINK_WOOL," + 
				"GRAY_WOOL,LIGHT_GRAY_WOOL,CYAN_WOOL,PURPLE_WOOL,BLUE_WOOL,BROWN_WOOL,GREEN_WOOL,RED_WOOL,BLACK_WOOL");
		Matlist leaves = new Matlist(18, "GENERIC_LEAVES,REDWOOD_LEAVES,BIRCH_LEAVES");
		Matlist saplings = new Matlist(6, "GENERIC_SAPLING,REDWOOD_SAPLING,BIRCH_SAPLING");
		Matlist slabs = new Matlist(44, "STONE_SLAB,SANDSTONE_SLAB,WOOD_SLAB,COBBLESTONE_SLAB,BRICK_SLAB,SMOOTH_BRICK_SLAB");
		Matlist bricks = new Matlist(98, "STONE_SMOOTH_BRICK,MOSSY_SMOOTH_BRICK,COBBLESTONE_SMOOTH_BRICK");
		Matlist dyes = new Matlist(351, "INK_SAC,ROSE_RED,CACTUS_GREEN,COCOA_BEANS,LAPIS_LAZULI,PURPLE_DYE,CYAN_DYE,LIGHT_GRAY_DYE,GRAY_DYE"+
		"PINK_DYE,LILME_DYE,DANDELION_YELLOW,LIGHT_BLUE_DYE,MAGENTA_DYE,ORANGE_DYE,BONE_MEAL");
		matlists.add(woods);
		matlists.add(wools);
		matlists.add(leaves);
		matlists.add(saplings);
		matlists.add(slabs);
		matlists.add(bricks);
		matlists.add(dyes);
		
		groups = new HashMap<String, Matlist>();
		Matlist swords = new Matlist(0, "WOOD_SWORD,GOLD_SWORD,STONE_SWORD,IRON_SWORD,DIAMOND_SWORD");
		Matlist pickaxes = new Matlist(1, "WOOD_PICKAXE,GOLD_PICKAXE,STONE_PICKAXE,IRON_PICKAXE,DIAMOND_PICKAXE");
		Matlist axes = new Matlist(2, "WOOD_AXE,GOLD_AXE,STONE_AXE,IRON_AXE,DIAMOND_AXE");
		Matlist spades = new Matlist(3, "WOOD_SPADE,GOLD_SPADE,STONE_SPADE,IRON_SPADE,DIAMOND_SPADE");
		Matlist hoes = new Matlist(4, "WOOD_HOE,GOLD_HOE,STONE_HOE,IRON_HOE,DIAMOND_HOE");
		Matlist weapons = new Matlist(5, "WOOD_SWORD,GOLD_SWORD,STONE_SWORD,IRON_SWORD,DIAMOND_SWORD,BOW");
		Matlist helmets = new Matlist(6, "LEATHER_HELMET,GOLD_HELMET,CHAINMAIL_HELMET,IRON_HELMET,DIAMOND_HELMET");
		Matlist chestplates = new Matlist(7, "LEATHER_CHESTPLATE,GOLD_CHESTPLATE,CHAINMAIL_CHESTPLATE,IRON_CHESTPLATE,DIAMOND_CHESTPLATE");
		Matlist leggings = new Matlist(8, "LEATHER_LEGGINGS,GOLD_LEGGINGS,CHAINMAIL_LEGGINGS,IRON_LEGGINGS,DIAMOND_LEGGINGS");
		Matlist boots = new Matlist(9, "LEATHER_BOOTS,GOLD_BOOTS,CHAINMAIL_BOOTS,IRON_BOOTS,DIAMOND_BOOTS");
		Matlist armor = new Matlist(10, "LEATHER_HELMET,GOLD_HELMET,CHAINMAIL_HELMET,IRON_HELMET,DIAMOND_HELMET,LEATHER_CHESTPLATE,GOLD_CHESTPLATE," +
				"CHAINMAIL_CHESTPLATE,IRON_CHESTPLATE,DIAMOND_CHESTPLATE,LEATHER_LEGGINGS,GOLD_LEGGINGS,CHAINMAIL_LEGGINGS,IRON_LEGGINGS,DIAMOND_LEGGINGS," +
				"LEATHER_BOOTS,GOLD_BOOTS,CHAINMAIL_BOOTS,IRON_BOOTS,DIAMOND_BOOTS");
		groups.put("swords", swords);
		groups.put("pickaxes", pickaxes);
		groups.put("axes", axes);
		groups.put("spades", spades);
		groups.put("hoes", hoes);
		groups.put("weapons", weapons);
		groups.put("helmets", helmets);
		groups.put("chestplates", chestplates);
		groups.put("leggings", leggings);
		groups.put("boots", boots);
		groups.put("armor", armor);
	}
	
	public static boolean check_region(String world_name, String region_name)
	{
		if (Main.found_regions == null) return false;
		ArrayList<String> regions = Main.found_regions.get(world_name);
		if (regions == null) return false;
		for (String s : regions)
		{
			if (s.equals(region_name)) return true;
		}
		return false;
	}
	
	public static boolean matches_quantity(Item item, int amount)
	{
		if (item.quantities == null) return true;
		if (item.quantities.contains(amount)) return true;
		return false;
	}
	
	public static boolean matches_enchantments(ItemStack is, Item i)
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
	
	public static String get_mob(Entity creature)
	{
		String mob = creature.getClass().getName();
		int c = mob.lastIndexOf ("Craft") + 5; 
		if (c > 0) return mob.substring(c).toLowerCase(); else return null;
	}
	
	static CreatureType get_creature_type(String name)
	{
		CreatureType creature_type;
		if (name.equalsIgnoreCase("cavespider")) creature_type = CreatureType.CAVE_SPIDER;
		else if (name.equalsIgnoreCase("enderdragon")) creature_type = CreatureType.ENDER_DRAGON;
		else if (name.equalsIgnoreCase("magmacube")) creature_type = CreatureType.MAGMA_CUBE;
		else if (name.equalsIgnoreCase("mushroomcow")) creature_type = CreatureType.MUSHROOM_COW;
		else if (name.equalsIgnoreCase("pigzombie")) creature_type = CreatureType.PIG_ZOMBIE;		
		else creature_type = CreatureType.valueOf(name.toUpperCase());
		
		return creature_type;
	}
	
	static int get_quantity(ArrayList<Integer> choices)
	{
		 return choices.get(rng.nextInt(choices.size()));
	}
	
	public static void log(Object message)
	{
		logger.info("" + message);
	}
	
	public static void warn(Object message)
	{
		logger.warning("" + message);
	}
	
	static boolean get_random(Object o)
	{
		if (o instanceof Boolean) return (Boolean)o;
		
		if (o instanceof String && rng.nextInt(2) == 1) return true;
		
		return false;
	}
	
	@SuppressWarnings("unchecked")
	public static ArrayList<Number_condition> fill_number_condition_array(Object ints)
	{
		ArrayList<Number_condition> temp = new ArrayList<Number_condition>();
		if (ints instanceof ArrayList)
		{
			for (Object o : (ArrayList<Object>)ints) temp.add(new Number_condition(o));
		}
		else temp.add(new Number_condition(ints));
		return temp;
	}
	
	public static boolean matches_number_condition(ArrayList<Number_condition> values, int value)
	{
		if (values == null) return true;
		for (Number_condition nc : values)
		{
			if (nc.matches_number(value)) return true;
		}
		return false;
	}
	
	@SuppressWarnings("unchecked")
	static byte get_wool_colour(Object o)
	{		
		if (o == null) return DyeColor.WHITE.getData();
		
		ArrayList<String> colours = new ArrayList<String>();
		for (String ss : (ArrayList<String>)o) colours.add(ss.replaceAll(" ", "").toUpperCase());
				
		if (colours.contains("RANDOM"))
		{
			int i = rng.nextInt(DyeColor.values().length);
			return DyeColor.values()[i].getData();
		}
		
		String s = colours.get(rng.nextInt(colours.size()));
		return DyeColor.valueOf(s).getData();
	}
	
	static int get_number(Object o)
	{
		ArrayList<Integer> ints = Utils.fill_int_array(o);
		return get_quantity(ints);
	}
	
	static Location get_safe_block(Block block)
	{
		if (block.getType() == Material.AIR)
		{
			while (block.getType() == Material.AIR && block.getRelative(BlockFace.DOWN).getType() == Material.AIR) 
				block = block.getRelative(BlockFace.DOWN);
		}
		else
		{
			while (block.getType() != Material.AIR || block.getRelative(BlockFace.UP).getType() != Material.AIR)
				block = block.getRelative(BlockFace.UP);
		}
		return block.getLocation();
	}
	
	@SuppressWarnings("unchecked")
	static ArrayList<Integer> fill_int_array(Object ints)
	{
		ArrayList<Integer> temp = new ArrayList<Integer>();
		for (Object o : (ArrayList<Object>)ints)
		{
			if (o instanceof Integer)
			{
				temp.add((Integer)o);
			}
			if (o instanceof String)
			{
				String number;
				number = ((String)o).replaceAll(" ", "").toLowerCase();
				if (number.contains("to"))
				{
					String[] numbers = number.split("to");
					int low = Math.min(Integer.parseInt(numbers[0]), Integer.parseInt(numbers[1]));
					int high = Math.max(Integer.parseInt(numbers[0]), Integer.parseInt(numbers[1]));
					for (int i = low; i <= high; i++) temp.add(i);
				}
			}
		}
		return temp;
	}
	
	@SuppressWarnings("unchecked")
	static int set_int_property(int def, Map<String, Object> general, Map<String, Object> unique, String node)
	{
		if (general == null && unique == null) return def;
		
		Map<String, Object> temp = unique == null ? general : unique;
		if (temp.containsKey("general")) temp = (Map<String, Object>) temp.get("general");
		if (temp != null && temp.containsKey(node)) return get_number(temp.get(node)); else return def;
	}
	
	@SuppressWarnings("unchecked")
	static int set_burn_ticks(int def, Map<String, Object> general, Map<String, Object> unique)
	{
		if (general == null && unique == null) return def;
		
		Map<String, Object> temp = unique == null ? general : unique;
		if (temp.containsKey("burn_rules")) temp = (Map<String, Object>) temp.get("burn_rules");
		if (temp != null && temp.containsKey("burn_ticks")) return get_number(temp.get("burn_ticks")); else return def;
	}
	
	@SuppressWarnings("unchecked")
	static boolean set_burn_property(boolean def, Map<String, Object> general, Map<String, Object> unique)
	{
		if (general == null && unique == null) return def;
		
		Map<String, Object> temp = unique == null ? general : unique;
		if (temp.containsKey("burn_rules")) temp = (Map<String, Object>) temp.get("burn_rules");
		if (temp != null && temp.containsKey("burn")) return get_random(temp.get("burn")); else return def;
	}
	
	@SuppressWarnings("unchecked")
	static boolean set_boolean_property(boolean def, Map<String, Object> general, Map<String, Object> unique, String node)
	{
		if (general == null && unique == null) return def;
		
		Map<String, Object> temp = unique == null ? general : unique;
		if (temp.containsKey("general")) temp = (Map<String, Object>) temp.get("general");
		if (temp != null && temp.containsKey(node)) return get_random(temp.get(node)); else return def;
	}
	
	@SuppressWarnings("unchecked")
	static ArrayList<Death_action> set_death_actions(Map<String, Object> general, Map<String, Object> unique, int random)
	{
		if ((general == null || general.get("death_rules") == null) && (unique == null || unique.get("death_rules") == null)) return null;
		
		Map<String, Object> temp = unique == null ? general : unique;
		Object ob = temp.containsKey("death_rules") ? temp.get("death_rules") : null;
		
		if (ob == null) return null;
		
		if (ob instanceof ArrayList)
		{
			ArrayList<Death_action> da = new ArrayList<Death_action>();			
			for (Map<String, Object> o : (ArrayList<Map<String, Object>>)ob) da.add(new Death_action((Map<String, Object>)o, random));
			return da;
		}
		else
		{
			ArrayList<Death_action> da = new ArrayList<Death_action>();
			da.add(new Death_action((Map<String, Object>) ob, random));
			return da;
		}
	}
	
	@SuppressWarnings("unchecked")
	static ArrayList<Con_group> set_burn_rules(Map<String, Object> general, Map<String, Object> unique, int random)
	{
		if ((general == null || general.get("burn_rules") == null) && (unique == null || unique.get("burn_rules") == null)) return null;
		
		Map<String, Object> temp = unique == null ? general : unique;
		if (temp.containsKey("burn_rules")) temp = (Map<String, Object>) temp.get("burn_rules");
		Object ob = temp.containsKey("unless") ? temp.get("unless") : null;
			
		if (ob == null) return null;
		
		if (ob instanceof ArrayList)
		{
			ArrayList<Con_group> br = new ArrayList<Con_group>();
			for (Object o : (ArrayList<Object>)ob) br.add(new Con_group(((Map<String, Object>)o).get("condition_group")));
			return br;
		}
		else
		{
			ArrayList<Con_group> br = new ArrayList<Con_group>();
			br.add(new Con_group(((Map<String, Object>)ob).get("condition_group")));
			return br;
		}
	}
	
	@SuppressWarnings("unchecked")
	static byte set_byte_property(Map<String, Object> general, Map<String, Object> unique)
	{
		if (general == null && unique == null) return -1;
		
		Map<String, Object> temp = unique == null ? general : unique;
		if (temp.containsKey("general")) temp = (Map<String, Object>) temp.get("general");
		if (temp != null && temp.containsKey("wool_colors")) return get_wool_colour(temp.get("wool_colors")); else return -1;
	}

}

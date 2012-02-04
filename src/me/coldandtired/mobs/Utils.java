package me.coldandtired.mobs;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import org.bukkit.DyeColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;

public class Utils 
{
	static List<Matlist> matlists;
	static Map<String, Matlist> groups;
	static Random rng = new Random();
	
	static void setup_utils()
	{
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
	
	static int get_quantity(ArrayList<Integer> choices)
	{
		 return choices.get(rng.nextInt(choices.size()));
	}
	
	static boolean get_random(Object o)
	{
		if (o instanceof Boolean) return (Boolean)o;
		
		if (o instanceof String && rng.nextInt(2) == 1) return true;
		
		return false;
	}
	
	@SuppressWarnings("unchecked")
	static ArrayList<Number_condition> fill_number_condition_array(Object ints)
	{
		ArrayList<Number_condition> temp = new ArrayList<Number_condition>();
		for (Object o : (ArrayList<Object>)ints)
		{
			temp.add(new Number_condition(o));
		}
		return temp;
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
}

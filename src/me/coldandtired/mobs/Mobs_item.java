package me.coldandtired.mobs;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.bukkit.enchantments.Enchantment;

public class Mobs_item 
{
	String name;
	ArrayList<Integer> quantities;
	boolean match_all_enchantments = false;
	Map<Enchantment, ArrayList<Integer>> enchantments;
	
	@SuppressWarnings("unchecked")
	Mobs_item(Map<String, Object> item, String name)
	{
		this.name = name.toUpperCase().trim();
		
		if (item.containsKey("quantities")) quantities = Utils.fill_int_array(item.get("quantities"));
		if (item.containsKey("match_all_enchantments")) match_all_enchantments = (Boolean)item.get("match_all_enchantments");
		if (item.containsKey("enchantments"))
		{
			enchantments = new HashMap<Enchantment, ArrayList<Integer>>();
			for (Map<String, Object> e : (ArrayList<Map<String, Object>>)item.get("enchantments"))
			{
				ArrayList<Integer> temp = null;
				if (e.containsKey("level")) temp = Utils.fill_int_array(e.get("level"));
				enchantments.put(Enchantment.getByName(((String)e.get("effect")).toUpperCase()), temp);
			}
		}
	}
}

package me.coldandtired.mobs.conditions;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import me.coldandtired.mobs.Condition;
import me.coldandtired.mobs.Item;
import me.coldandtired.mobs.Matlist;
import me.coldandtired.mobs.Utils;

public class Player_wearing_all  implements Condition
{
	ArrayList<Item> values = new ArrayList<Item>();
	
	@SuppressWarnings("unchecked")
	public Player_wearing_all(Object ob) 
	{
		for (Map<String, Object> w : (ArrayList<Map<String, Object>>)ob)
		{
			Matlist groups = Utils.groups.get((String)w.get("name"));
			if (groups == null) for (String s : ((String)w.get("name")).split(","))	values.add(new Item(w, s));
			else 
			{
				for (String s : groups.names) values.add(new Item(w, s));
			}
		}
	}

	@Override
	public boolean check(LivingEntity entity, World world, Location loc, String spawn_reason, Player player, int random)
	{
		boolean matches = false;
		List<ItemStack> temp = new ArrayList<ItemStack>();
		temp.add(player.getInventory().getHelmet());
		temp.add(player.getInventory().getChestplate());
		temp.add(player.getInventory().getLeggings());
		temp.add(player.getInventory().getBoots());
		if (temp.size() != 0)
		{
			for (Item w : values)
			{
				for (ItemStack is : temp)
				if (is != null)
				{
					matches = is.getType() == Material.matchMaterial(w.name);
					
					if (matches)
					{
						if (w.enchantments != null) // enchants match requested
						{
							matches = Utils.matches_enchantments(is, w);
						}
					}	
				
					if (!matches) return false;
				}
			}
		} else return false;			
		return matches;
	}
}

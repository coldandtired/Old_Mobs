package me.coldandtired.mobs.conditions;

import java.util.ArrayList;
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

public class Player_items implements Condition
{
	ArrayList<Item> values = new ArrayList<Item>();
	
	@SuppressWarnings("unchecked")
	public Player_items(Object ob) 
	{
		for (Map<String, Object> i : (ArrayList<Map<String, Object>>)ob)
		{
			Matlist groups = Utils.groups.get((String)i.get("group"));
			if (groups == null) for (String s : ((String)i.get("name")).split(","))	values.add(new Item(i, s));
			else
			{
				for (String s : groups.names) values.add(new Item(i, s));
			}
		}
	}

	@Override
	public boolean check(LivingEntity entity, World world, Location loc, String spawn_reason, Player player, int random)
	{
		boolean validitems = false;
		for (Item i : values)
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
						if (Utils.matches_enchantments(is, i)) count += amount;
					} else count += amount; // items match, no enchants to worry about
				}					
			}
			validitems = Utils.matches_quantity(i, count);
			
			if (validitems) return true;
		}
		return validitems;
	}
}

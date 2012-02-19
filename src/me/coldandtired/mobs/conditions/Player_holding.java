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

public class Player_holding implements Condition 
{
	ArrayList<Item> values = new ArrayList<Item>();
	
	@SuppressWarnings("unchecked")
	public Player_holding(Object ob) 
	{
		for (Map<String, Object> w : (ArrayList<Map<String, Object>>)ob)
		{
			Matlist groups = Utils.groups.get((String)w.get("name"));
			if (groups == null) for (String s : ((String)w.get("name")).split(","))	values.add(new Item(w, s));
			else for (String s : groups.names) values.add(new Item(w, s));
		}
	}

	@Override
	public boolean check(LivingEntity entity, World world, Location loc, String spawn_reason, Player player, int random)
	{
		boolean validholding = false;
		ItemStack is = player.getItemInHand();
		int amount = is.getAmount();
		for (Item w : values)
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
			
			if (matches && Utils.matches_quantity(w, amount))
			{
				if (w.enchantments != null)
				{
					validholding = Utils.matches_enchantments(is, w);
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
}

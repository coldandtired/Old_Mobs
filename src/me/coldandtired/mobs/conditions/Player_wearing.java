package me.coldandtired.mobs.conditions;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import me.coldandtired.mobs.Condition;

public class Player_wearing  implements Condition
{
	boolean match_all_wearing = false;
	
	public Player_wearing(Object ob) 
	{
		/*for (Map<String, Object> w : (ArrayList<Map<String, Object>>)ob)
		{
			Matlist groups = Utils.groups.get((String)w.get("name"));
			if (groups == null) for (String s : ((String)w.get("name")).split(","))	values.add(new Old_item(w, s));
			else 
			{
				for (String s : groups.names) values.add(new Old_item(w, s));
				match_all_wearing = false;
			}
		}*/
	}

	@Override
	public boolean check(LivingEntity entity, World world, Location loc, String spawn_reason, Player player, int random, String autospawn_id)
	{
		/*boolean matches = false;
		List<ItemStack> temp = new ArrayList<ItemStack>();
		temp.add(player.getInventory().getHelmet());
		temp.add(player.getInventory().getChestplate());
		temp.add(player.getInventory().getLeggings());
		temp.add(player.getInventory().getBoots());
		if (temp.size() != 0)
		{
			for (Old_item w : values)
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
				
					if (matches) return true;
				}
			}
		} else return false;			
		return matches;*/
		return true;
	}
}

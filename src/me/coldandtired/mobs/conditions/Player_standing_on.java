package me.coldandtired.mobs.conditions;

import java.util.ArrayList;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;

import me.coldandtired.mobs.Condition;
import me.coldandtired.mobs.Matlist;
import me.coldandtired.mobs.Utils;

public class Player_standing_on implements Condition
{
	private ArrayList<String> values;
	
	public Player_standing_on(Object ob)
	{
		values = Utils.fill_string_array(ob);
	}
	
	@Override
	public boolean check(LivingEntity entity, World world, Location loc, String spawn_reason, Player player, int random) 
	{		
		Block block = player.getLocation().getBlock().getRelative(BlockFace.DOWN);
		if (values.contains(block.getType().name())) return true;
		
		for (String s : values)
		for (Matlist m : Utils.matlists)
		{
			if (m.names.contains(s))
			{
				if (block.getTypeId() == m.id && ((short)m.names.indexOf(s)) == block.getData()) return true;
			}			
		}
		return false;
	}
}

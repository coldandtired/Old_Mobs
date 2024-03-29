package me.coldandtired.mobs.conditions;

import java.util.List;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;

import me.coldandtired.mobs.Condition;
import me.coldandtired.mobs.L;

public class Player_standing_on implements Condition
{
	private List<Number_condition> values;
	private boolean reversed = false;

	public Player_standing_on(String s, boolean reversed)
	{
		values = L.fill_number_values(s);
		this.reversed = reversed;
	}
	
	@Override
	public boolean check(LivingEntity entity, World world, Location loc, String spawn_reason, Player player, int random, String autospawn_id) 
	{		
		Block block = player.getLocation().getBlock().getRelative(BlockFace.DOWN);
		boolean b = L.matches_number_condition(values, block.getType().getId());
		if (reversed) return !b; else return b; 
	}
}

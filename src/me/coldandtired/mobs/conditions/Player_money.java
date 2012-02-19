package me.coldandtired.mobs.conditions;

import java.util.ArrayList;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;

import me.coldandtired.mobs.Condition;
import me.coldandtired.mobs.Main;
import me.coldandtired.mobs.Utils;

public class Player_money implements Condition
{
	private ArrayList<Number_condition> values;
	
	public Player_money(Object ob)
	{
		values = Utils.fill_number_condition_array(ob);
	}
	
	@Override
	public boolean check(LivingEntity entity, World world, Location loc, String spawn_reason, Player player, int random) 
	{
		if (Main.economy == null) return true;
		
		double d = Main.economy.getBalance(player.getName());		
		for (Number_condition nc : values)
		{
			if (nc.matches_number((int) Math.round(d))) return true;
		}
		return false;
	}	
}

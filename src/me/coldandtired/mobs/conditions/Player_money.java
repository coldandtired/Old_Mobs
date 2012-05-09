package me.coldandtired.mobs.conditions;

import java.util.List;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;

import me.coldandtired.mobs.Condition;
import me.coldandtired.mobs.Main;
import me.coldandtired.mobs.data.Autospawn;
import me.coldandtired.mobs.L;

public class Player_money implements Condition
{
	private List<Number_condition> values;

	public Player_money(String s)
	{
		values = L.fill_number_values(s);
	}
	
	@Override
	public boolean check(LivingEntity entity, World world, Location loc, String spawn_reason, Player player, int random, Autospawn as) 
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

package me.coldandtired.mobs.data;

import java.util.List;

import me.coldandtired.mobs.L;

public class Damage_value
{
	public List<Integer> amount;
	public boolean use_percent;
	
	public Damage_value(String s, boolean b)
	{
		amount = L.fill_int_properties(s);
		use_percent = b;
	}
}

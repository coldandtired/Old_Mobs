package me.coldandtired.mobs.conditions;

import java.util.ArrayList;
import java.util.Map;

import me.coldandtired.mobs.Area;

public class Mob_within 
{
	Area area;
	ArrayList<Number_condition> count;
	
	@SuppressWarnings("unchecked")
	Mob_within(Object o)
	{
		Map<String, Object> mw = (Map<String, Object>)o;
		area = new Area(mw.get("radius"));
		//count = Utils.fill_number_condition_array(mw.get("count"));
	}
}

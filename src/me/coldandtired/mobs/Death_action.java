package me.coldandtired.mobs;

import java.util.ArrayList;
import java.util.Map;

public class Death_action 
{
	ArrayList<Condition_group> conditions;
	ArrayList<Item> items;
	ArrayList<Integer> exp;
	ArrayList<Integer> money;
	boolean replace_items = true;
	boolean replace_exp = true;
	
	@SuppressWarnings("unchecked")
	public Death_action(Map<String, Object> da, int random) 
	{
		da = (Map<String, Object>) da.get("action");
		
		if (da.containsKey("will_give_exp")) exp = Utils.fill_int_array(da.get("will_give_exp"));
		if (da.containsKey("will_give_money") && Main.economy != null) money = Utils.fill_int_array(da.get("will_give_money"));
		if (da.containsKey("replace_items")) replace_items = (Boolean)da.get("replace_items");
		if (da.containsKey("replace_exp")) replace_exp = (Boolean)da.get("replace_exp");
		
		ArrayList<Object> conds = (ArrayList<Object>)da.get("if");
		if (conds != null && conds.size() > 0)
		{
			if (conditions == null) conditions = new ArrayList<Condition_group>();
			for (Object o : conds) conditions.add(new Condition_group(o, random));
		}
		
		ArrayList<Map<String, Object>> drops = (ArrayList<Map<String, Object>>)da.get("will_drop");
		if (drops != null && drops.size() > 0)
		{
			if (items == null) items = new ArrayList<Item>();
			for (Map<String, Object> o : drops)
			{
				o = (Map<String, Object>)o.get("item");
				for (String s : (ArrayList<String>)o.get("names"))
				items.add(new Item(o, s));
			}
		}
	}

}

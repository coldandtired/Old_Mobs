package me.coldandtired.mobs;

import java.util.ArrayList;
import java.util.Map;

public class Death_action 
{
	ArrayList<Con_group> conditions;
	ArrayList<Item> items;
	ArrayList<Integer> exp;
	ArrayList<Integer> money;
	boolean replace_items = true;
	boolean replace_exp = true;
	Death_message death_messages;
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public Death_action(Map<String, Object> da, int random) 
	{
		da = (Map<String, Object>) da.get("action");
		if (da.containsKey("will_give_exp")) exp = Utils.fill_int_array(da.get("will_give_exp"));
		if (da.containsKey("will_give_money") && Main.economy != null) money = Utils.fill_int_array(da.get("will_give_money"));
		if (da.containsKey("replace_items")) replace_items = (Boolean)da.get("replace_items");
		if (da.containsKey("replace_exp")) replace_exp = (Boolean)da.get("replace_exp");
		Object ob = da.get("death_messages");
		if (ob != null)
		{			
			Map<String, Object> temp = (Map<String, Object>)ob;
			death_messages = new Death_message(temp);
			if (death_messages.messages == null) death_messages = null;
		}
		ob = da.get("if");

		if (ob != null)
		{
			if (ob instanceof ArrayList)
			{
				if (conditions == null) conditions = new ArrayList<Con_group>();
				for (Object o : (ArrayList)ob) conditions.add(new Con_group(((Map<String, Object>)o).get("condition_group")));
			}
			else
			{
				if (conditions == null) conditions = new ArrayList<Con_group>();
				conditions.add(new Con_group(((Map<String, Object>)ob).get("condition_group")));
			}
		}
		
		ob = da.get("will_drop");
	
		if (ob != null)
		{
			if (ob instanceof ArrayList)
			{
				if (items == null) items = new ArrayList<Item>();
				for (Map<String, Object> o : (ArrayList<Map<String, Object>>)ob)
				{
					o = (Map<String, Object>)o.get("item");
					Object o2 = o.get("names");
					if (o2 != null)
					{
						if (o2 instanceof ArrayList) for (String s : (ArrayList<String>)o2) items.add(new Item(o, s));
						else items.add(new Item(o, (String)o2));
					}
				}				
			}
			else
			{
				if (items == null) items = new ArrayList<Item>();
				Map<String, Object> temp = (Map<String, Object>)ob;
				temp = (Map<String, Object>) temp.get("item");
				Object o2 = temp.get("names");
				if (o2 != null)
				{
					if (o2 instanceof ArrayList) for (String s : (ArrayList<String>)o2) items.add(new Item(temp, s));
					else items.add(new Item(temp, (String)o2));
				}
			}
		}
	}
}

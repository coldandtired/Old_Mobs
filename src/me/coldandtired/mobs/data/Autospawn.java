package me.coldandtired.mobs.data;

import java.util.ArrayList;

import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

public class Autospawn 
{
	public int spawn_interval = Config.check_interval;
	public int timer_value = spawn_interval;
	public String id = "";
	public boolean manual = false;
	public boolean command_time_check = false;
	public Autospawn_time spawn_time = null;
	public ArrayList<Autospawn_location> spawn_locations = null;
	public String mob_name;
	
	public Autospawn(Element el, NodeList locations, String mob_name, Element as_settings)
	{			
		this.mob_name = mob_name;
		if (as_settings != null)
		{
			id = as_settings.getAttributeNode("id").getValue();
			manual = Boolean.parseBoolean(as_settings.getAttributeNode("manual").getValue());
			command_time_check = Boolean.parseBoolean(as_settings.getAttributeNode("command_time_check").getValue());
		}
		
		if (!manual) spawn_time = new Autospawn_time(el);
		
		spawn_locations = new ArrayList<Autospawn_location>();
		for (int i = 0; i < locations.getLength(); i ++)
		{
			Autospawn_location sl = new Autospawn_location((Element)locations.item(i));
			if (sl.worlds != null) spawn_locations.add(sl);
		}
	}
}
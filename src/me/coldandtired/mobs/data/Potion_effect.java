package me.coldandtired.mobs.data;

import java.util.ArrayList;
import java.util.List;

import me.coldandtired.mobs.L;
import org.w3c.dom.Element;

public class Potion_effect 
{
	public List<Integer> duration = new ArrayList<Integer>();
	public List<Integer> level;
	public String name;
	
	Potion_effect(Element el)
	{
		level = L.fill_int_properties(el.getAttributeNode("level").getValue());
		
		List<Integer> temp = L.fill_int_properties(el.getAttributeNode("duration").getValue());
		boolean b = Boolean.parseBoolean(el.getAttributeNode("use_seconds").getValue());
		for (int i : temp)
		{
			int j = i * 20;
			if (!b) duration.add(j * 1200); else duration.add(j);
		}
		name = el.getAttributeNode("name").getValue();
	}
}

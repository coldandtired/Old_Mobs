package me.coldandtired.mobs.data;

import java.util.List;

import me.coldandtired.mobs.L;
import org.w3c.dom.Element;

public class Item_enchantment 
{
	public List<Integer> level;
	public String name;
	
	public Item_enchantment(Element el)
	{
		level = L.fill_int_properties(el.getAttributeNode("level").getValue());
		name = el.getAttributeNode("name").getValue().toUpperCase();
	}
}

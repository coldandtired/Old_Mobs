package me.coldandtired.mobs.data;

import java.util.List;

import me.coldandtired.mobs.L;
import me.coldandtired.mobs.conditions.Number_condition;

import org.w3c.dom.Element;

public class Exp
{
	public boolean replace;
	public List<Integer> amount;
	public List<Number_condition> chances;
	
	public Exp (Element el)
	{
		amount = L.fill_int_properties(el.getAttributeNode("amount").getValue());
		chances = L.fill_number_values(el.getAttributeNode("chance").getValue());
		replace = Boolean.parseBoolean(el.getAttributeNode("replace").getValue());
	}
}

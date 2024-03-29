package me.coldandtired.mobs.data;

import java.io.Serializable;
import java.util.List;

import me.coldandtired.mobs.L;
import me.coldandtired.mobs.conditions.Number_condition;
import org.w3c.dom.Element;

public class Bounty implements Serializable
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public List<Double> amount;
	public List<Number_condition> chances;
	
	public Bounty (Element el)
	{
		amount = L.fill_double_properties(el.getAttributeNode("amount").getValue());
		chances = L.fill_number_values(el.getAttributeNode("chance").getValue());
	}
}

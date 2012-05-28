package me.coldandtired.mobs.data;

import java.util.ArrayList;
import java.util.List;

import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;

import me.coldandtired.mobs.L;
import me.coldandtired.mobs.Main;
import me.coldandtired.mobs.conditions.Number_condition;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

public class Item
{
	public List<Integer> quantity;
	public int id;
	public boolean replace;
	public Short data;
	public List<Number_condition> chances;
	public boolean all_enchantments;
	public List<Item_enchantment> enchantments;
	
	Item (Element element)
	{
		replace = Boolean.parseBoolean(element.getAttributeNode("replace").getValue());
		all_enchantments = Boolean.parseBoolean(element.getAttributeNode("add_all_enchantments").getValue());
		quantity = L.fill_int_properties(element.getAttributeNode("quantity").getValue());
		id = Integer.parseInt(element.getAttributeNode("name").getValue());
		String s = element.getAttributeNode("data").getValue();
		if (s.equalsIgnoreCase("default")) data = 0; else data = Short.parseShort(s);
		chances = L.fill_number_values(element.getAttributeNode("chance").getValue());
		
		if (Boolean.parseBoolean(element.getAttributeNode("has_enchantments").getValue()))
		{
			enchantments = new ArrayList<Item_enchantment>();
			try 
			{
				NodeList list = (NodeList)Main.xpath.evaluate("item_enchantments/enchantments/Enchantment", element, XPathConstants.NODESET);
				
				for (int i = 0; i < list.getLength(); i++) enchantments.add(new Item_enchantment((Element)list.item(i)));
			} 
			catch (XPathExpressionException e) {e.printStackTrace();}
		}
	}
}

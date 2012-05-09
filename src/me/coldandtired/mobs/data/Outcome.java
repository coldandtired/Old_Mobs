package me.coldandtired.mobs.data;

import java.util.ArrayList;
import java.util.HashMap;

import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;


import me.coldandtired.mobs.Main;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

public class Outcome 
{
    public ArrayList<New_condition> conditions = null;
    public ArrayList<Potion_effect> potion_effects = null;
    public Drops drops = null;
    public Mob_properties mob_properties = null;
    public HashMap<String, Damage_value> damage_properties = null;
    public boolean spawn = true;
    public boolean all_potion_effects = true;
    public int affected_mobs = 0;
    
    public Outcome(Element element)
    {
    	if (element == null) return;
    	
    	try
    	{    	
    		all_potion_effects = Boolean.parseBoolean(element.getAttributeNode("all_potion_effects").getValue());	    	
	    	spawn =  Boolean.parseBoolean(element.getAttributeNode("spawn").getValue());
    		affected_mobs = Integer.parseInt(element.getAttributeNode("affected_mobs").getValue());	  		
    	
	    	if (Boolean.parseBoolean(element.getAttributeNode("has_conditions").getValue()))
	    	{
	    		NodeList list = (NodeList)Main.xpath.evaluate("outcome_conditions/conditions/Condition_group", element, XPathConstants.NODESET);   			
    			conditions = new ArrayList<New_condition>();
    			for (int i = 0; i < list.getLength(); i ++)
    			{
    				Element el = (Element)list.item(i);    				
    				conditions.add(new New_condition(el));
    			}
	    	}
    	
	    	if (Boolean.parseBoolean(element.getAttributeNode("has_potions").getValue()))
	    	{
	    		NodeList list = (NodeList)Main.xpath.evaluate("potion_effects/potions/Potion", element, XPathConstants.NODESET);
	    		
	    		potion_effects = new ArrayList<Potion_effect>();
    			for (int i = 0; i < list.getLength(); i ++)
    			{
    				Element el = (Element)list.item(i);
    				potion_effects.add(new Potion_effect(el));
    			}
	    	}
	    	
	    	if (Boolean.parseBoolean(element.getAttributeNode("has_drops").getValue()))
	    	{
	    		drops = new Drops((Element)Main.xpath.evaluate("drops[1]", element, XPathConstants.NODE));
	    	}
	    	
	    	if (Boolean.parseBoolean(element.getAttributeNode("has_mob_properties").getValue()))
	    	{
	    		NodeList list = (NodeList)Main.xpath.evaluate("mob_properties/properties/Property", element, XPathConstants.NODESET);
	    		mob_properties = new Mob_properties(list);
	    	}
    	
	    	if (Boolean.parseBoolean(element.getAttributeNode("has_damage_properties").getValue()))
	    	{
	    		NodeList list = (NodeList)Main.xpath.evaluate("damage_properties/properties/Property[@property_value != \"\"]", element, XPathConstants.NODESET);
	    		damage_properties = new HashMap<String, Damage_value>();
	    		for (int i = 0; i < list.getLength(); i ++)
	    		{
	    			Element el = (Element)list.item(i);
	    			damage_properties.put(el.getAttributeNode("name").getValue().toUpperCase(), 
	    					new Damage_value(el.getAttributeNode("property_value").getValue(),
	    					Boolean.parseBoolean(el.getAttributeNode("damage_is_percentage").getValue())));
	    		}
	    	}
    	}
    	catch (XPathExpressionException e) {e.printStackTrace();}
    }
}

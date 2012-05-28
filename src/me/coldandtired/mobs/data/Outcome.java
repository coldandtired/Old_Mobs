package me.coldandtired.mobs.data;

import java.util.ArrayList;
import java.util.HashMap;

import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;


import me.coldandtired.mobs.Condition;
import me.coldandtired.mobs.Main;
import me.coldandtired.mobs.conditions.Angry;
import me.coldandtired.mobs.conditions.Autospawn_id;
import me.coldandtired.mobs.conditions.Biomes;
import me.coldandtired.mobs.conditions.Can_breed;
import me.coldandtired.mobs.conditions.Chunk_mob_count;
import me.coldandtired.mobs.conditions.Death_causes;
import me.coldandtired.mobs.conditions.Game_times;
import me.coldandtired.mobs.conditions.Coordinate;
import me.coldandtired.mobs.conditions.Killed_by_player;
import me.coldandtired.mobs.conditions.Killed_player_names;
import me.coldandtired.mobs.conditions.Light_levels;
import me.coldandtired.mobs.conditions.Mob_ages;
import me.coldandtired.mobs.conditions.Mob_standing_on;
import me.coldandtired.mobs.conditions.Ocelot_type;
import me.coldandtired.mobs.conditions.Online_player_count;
import me.coldandtired.mobs.conditions.Percent;
import me.coldandtired.mobs.conditions.Player_money;
import me.coldandtired.mobs.conditions.Player_names;
import me.coldandtired.mobs.conditions.Player_standing_on;
import me.coldandtired.mobs.conditions.Powered;
import me.coldandtired.mobs.conditions.Raining;
import me.coldandtired.mobs.conditions.Regions;
import me.coldandtired.mobs.conditions.Remaining_lifetime;
import me.coldandtired.mobs.conditions.Saddled;
import me.coldandtired.mobs.conditions.Server_player_count;
import me.coldandtired.mobs.conditions.Server_players;
import me.coldandtired.mobs.conditions.Sheared;
import me.coldandtired.mobs.conditions.Spawn_reasons;
import me.coldandtired.mobs.conditions.Tamed;
import me.coldandtired.mobs.conditions.Thundering;
import me.coldandtired.mobs.conditions.Time;
import me.coldandtired.mobs.conditions.Villager_type;
import me.coldandtired.mobs.conditions.Wool_colours;
import me.coldandtired.mobs.conditions.World_mob_count;
import me.coldandtired.mobs.conditions.World_names;
import me.coldandtired.mobs.conditions.World_players;
import me.coldandtired.mobs.conditions.World_types;

import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

public class Outcome 
{
    public ArrayList<Condition> conditions = null;
    public ArrayList<Potion_effect> potion_effects = null;
    public Drops drops = null;
    public Mob_properties mob_properties = null;
    public HashMap<String, Damage_value> damage_properties = null;
    public boolean spawn = true;
    public boolean all_potion_effects = true;
    public int affected_mobs = 0;
    public int outcome_id = -1;
    
    public Outcome(Element element)
    {
    	if (element == null) return;
    	
    	try
    	{  	
    		all_potion_effects = Boolean.parseBoolean(element.getAttributeNode("all_potion_effects").getValue());	    	
	    	spawn =  Boolean.parseBoolean(element.getAttributeNode("spawn").getValue());
    		affected_mobs = Integer.parseInt(element.getAttributeNode("affected_mobs").getValue());
    		if (element.hasAttribute("outcome_id"))
    		{
    			String s = element.getAttributeNode("outcome_id").getValue();
    			if (!s.equalsIgnoreCase("")) outcome_id  = Integer.parseInt(s);
    		}
    		
	    	if (Boolean.parseBoolean(element.getAttributeNode("has_conditions").getValue()))
	    	{
	    		NodeList list = (NodeList)Main.xpath.evaluate("conditions/Condition", element, XPathConstants.NODESET);   			
    			conditions = new ArrayList<Condition>();
    			for (int i = 0; i < list.getLength(); i ++)
    			{
    				Element el = (Element)list.item(i);    				
    				String s = el.getAttributeNode("name").getValue();
    				String s2 = el.getAttributeNode("condition_value").getValue();
    				String s3 = el.hasAttribute("subvalue") ? el.getAttributeNode("subvalue").getValue() : "";
    				
    				if (s.equalsIgnoreCase("autospawn_id")) conditions.add(new Autospawn_id(s2));
    				if (s.equalsIgnoreCase("biome_type")) conditions.add(new Biomes(s2));
    				if (s.equalsIgnoreCase("death_cause")) conditions.add(new Death_causes(s2));
    				if (s.equalsIgnoreCase("game_times")) conditions.add(new Game_times(s2));
    				
    				//if (s.equalsIgnoreCase("item_type")) conditions.add(new Item_type(s2));
    				if (s.equalsIgnoreCase("killed_player_names")) conditions.add(new Killed_player_names(s2));
    				if (s.equalsIgnoreCase("light_levels")) conditions.add(new Light_levels(s2));
    				if (s.equalsIgnoreCase("mob_ages")) conditions.add(new Mob_ages(s2));
    				if (s.equalsIgnoreCase("mob_standing_on")) conditions.add(new Mob_standing_on(s2));
    				if (s.equalsIgnoreCase("ocelot_type")) conditions.add(new Ocelot_type(s2));
    				if (s.equalsIgnoreCase("online_player_count")) conditions.add(new Online_player_count(s2));
    				if (s.equalsIgnoreCase("percent")) conditions.add(new Percent(s2));
    				if (s.equalsIgnoreCase("player_money")) conditions.add(new Player_money(s2));
    				if (s.equalsIgnoreCase("player_names")) conditions.add(new Player_names(s2));
    				if (s.equalsIgnoreCase("player_standing_on")) conditions.add(new Player_standing_on(s2));
    				if (s.equalsIgnoreCase("regions")) conditions.add(new Regions(s2));
    				if (s.equalsIgnoreCase("remaining_lifetime")) conditions.add(new Remaining_lifetime(s2));
    				if (s.equalsIgnoreCase("server_player_count")) conditions.add(new Server_player_count(s2));
    				if (s.equalsIgnoreCase("server_players")) conditions.add(new Server_players(s2));
    				if (s.equalsIgnoreCase("spawn_type")) conditions.add(new Spawn_reasons(s2));
    				if (s.equalsIgnoreCase("villager_type")) conditions.add(new Villager_type(s2));
    				if (s.equalsIgnoreCase("wool_colour")) conditions.add(new Wool_colours(s2));
    				
    				if (s.equalsIgnoreCase("chunk_mob_count")) conditions.add(new Chunk_mob_count(s2, s3));    				
    				if (s.equalsIgnoreCase("world_mob_count")) conditions.add(new World_mob_count(s2, s3));	
    				if (s.equalsIgnoreCase("time")) conditions.add(new Time(s2, s3));	
    				if (s.equalsIgnoreCase("coordinate")) conditions.add(new Coordinate(s2, s3));			
    				
    				if (s.equalsIgnoreCase("world_names")) conditions.add(new World_names(s2));
    				if (s.equalsIgnoreCase("world_players")) conditions.add(new World_players(s2));
    				if (s.equalsIgnoreCase("world_type")) conditions.add(new World_types(s2));
    				
    				if (s.equalsIgnoreCase("angry")) conditions.add(new Angry(s2));
    				if (s.equalsIgnoreCase("can_breed")) conditions.add(new Can_breed(s2));
    				if (s.equalsIgnoreCase("killed_by_player")) conditions.add(new Killed_by_player(s2));
    				if (s.equalsIgnoreCase("powered")) conditions.add(new Powered(s2));
    				if (s.equalsIgnoreCase("raining")) conditions.add(new Raining(s2));
    				if (s.equalsIgnoreCase("saddled")) conditions.add(new Saddled(s2));
    				if (s.equalsIgnoreCase("sheared")) conditions.add(new Sheared(s2));
    				if (s.equalsIgnoreCase("tamed")) conditions.add(new Tamed(s2));
    				if (s.equalsIgnoreCase("thundering")) conditions.add(new Thundering(s2));
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

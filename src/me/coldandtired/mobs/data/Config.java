package me.coldandtired.mobs.data;

import java.util.ArrayList;

import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;

import me.coldandtired.mobs.Main;

import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

public class Config 
{
	public static ArrayList<String> ignored_worlds;
	public static int check_interval;
	public static int min_player_count;
	public static int log_level;
	public static boolean overrule_damaging;
	public static boolean overrule_burning;
	public static boolean overrule_spawning;
	public static boolean overrule_targeting;
	public static boolean overrule_exploding;
	public static boolean overrule_creating_portal;
	public static boolean overrule_splitting;
	public static boolean overrule_regrowing_wool;
	public static boolean overrule_dying_wool;
	public static boolean overrule_shearing;
	public static boolean overrule_changing_block;
	public static boolean overrule_taming;
	public static boolean overrule_becoming_powered_creeper;
	public static boolean overrule_becoming_pig_zombie;
	public static boolean overrule_teleporting;
	public static boolean overrule_healing;
	public static boolean tracked_mobs_only;
	public static ArrayList<Death_message> messages = null;
	
	public static void setup_config(Element element)
	{
		String s = element.getAttributeNode("ignored_worlds").getValue();
		if (!s.equalsIgnoreCase(""))
		{
			ignored_worlds = new ArrayList<String>();
			String[] temp = s.split(",");
			for (String ss : temp) ignored_worlds.add(ss.toUpperCase());
		}
		
		if (Boolean.parseBoolean(element.getAttributeNode("has_messages").getValue()))
		{			
			messages = new ArrayList<Death_message>();
			try 
			{
				NodeList list = (NodeList)Main.xpath.evaluate("death_messages/Message", element, XPathConstants.NODESET);
				for (int i = 0; i < list.getLength(); i ++) messages.add(new Death_message((Element)list.item(i)));
			} 
			catch (XPathExpressionException e) {e.printStackTrace();}
		}
		
		check_interval = Integer.parseInt(element.getAttributeNode("check_interval").getValue()) * 20;
		min_player_count = Integer.parseInt(element.getAttributeNode("min_player_count").getValue());
		tracked_mobs_only = Boolean.parseBoolean(element.getAttributeNode("tracked_mobs_only").getValue());
		log_level = Integer.parseInt(element.getAttributeNode("log_level").getValue());
		overrule_damaging = Boolean.parseBoolean(element.getAttributeNode("damaging").getValue());
		overrule_burning = Boolean.parseBoolean(element.getAttributeNode("burning").getValue());
		overrule_spawning = Boolean.parseBoolean(element.getAttributeNode("spawning").getValue());
		overrule_targeting = Boolean.parseBoolean(element.getAttributeNode("targeting").getValue());
		overrule_exploding = Boolean.parseBoolean(element.getAttributeNode("exploding").getValue());
		overrule_creating_portal = Boolean.parseBoolean(element.getAttributeNode("creating_portal").getValue());
		overrule_splitting = Boolean.parseBoolean(element.getAttributeNode("splitting").getValue());
		overrule_regrowing_wool = Boolean.parseBoolean(element.getAttributeNode("regrowing_wool").getValue());
		overrule_dying_wool = Boolean.parseBoolean(element.getAttributeNode("dying_wool").getValue());
		overrule_shearing = Boolean.parseBoolean(element.getAttributeNode("shearing").getValue());
		overrule_changing_block = Boolean.parseBoolean(element.getAttributeNode("changing_block").getValue());
		overrule_taming = Boolean.parseBoolean(element.getAttributeNode("taming").getValue());
		overrule_becoming_powered_creeper = Boolean.parseBoolean(element.getAttributeNode("becoming_powered_creeper").getValue());
		overrule_becoming_pig_zombie = Boolean.parseBoolean(element.getAttributeNode("becoming_pig_zombie").getValue());
		overrule_teleporting = Boolean.parseBoolean(element.getAttributeNode("teleporting").getValue());
		overrule_healing = Boolean.parseBoolean(element.getAttributeNode("healing").getValue());
	}
}

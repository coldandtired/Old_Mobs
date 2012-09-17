package me.coldandtired.mobs.data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;

import me.coldandtired.mobs.Main;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

public class Drops implements Serializable
{	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public List<Exp> exps = null;
	public List<Bounty> bounties = null;
	public List<Item> items = null;
	public List<Death_message> messages = null;
	
	Drops(Element element)
	{
		try 
		{
			if (Boolean.parseBoolean(element.getAttributeNode("has_exp").getValue()))
			{
				exps = new ArrayList<Exp>();
				NodeList list = (NodeList)Main.xpath.evaluate("exps/Exp", element, XPathConstants.NODESET);
				for (int i = 0; i < list.getLength(); i++) exps.add(new Exp((Element)list.item(i)));
			}
			
			if (Boolean.parseBoolean(element.getAttributeNode("has_bounties").getValue()) && Main.economy != null)
			{
				bounties = new ArrayList<Bounty>();
				NodeList list = (NodeList)Main.xpath.evaluate("bounties/Bounty", element, XPathConstants.NODESET);
				for (int i = 0; i < list.getLength(); i++) bounties.add(new Bounty((Element)list.item(i)));
			}
			
			if (Boolean.parseBoolean(element.getAttributeNode("has_items").getValue()))
			{
				items = new ArrayList<Item>();
				NodeList list = (NodeList)Main.xpath.evaluate("items/Item", element, XPathConstants.NODESET);
				for (int i = 0; i < list.getLength(); i++) items.add(new Item((Element)list.item(i)));
			}
			
			if (Boolean.parseBoolean(element.getAttributeNode("has_messages").getValue()))
			{
				messages = new ArrayList<Death_message>();
				NodeList list = (NodeList)Main.xpath.evaluate("death_messages/Message", element, XPathConstants.NODESET);
				for (int i = 0; i < list.getLength(); i ++) messages.add(new Death_message((Element)list.item(i)));
			}
		} 
		catch (XPathExpressionException e) {e.printStackTrace();}
	}
}

package me.coldandtired.mobs.data;

import java.util.ArrayList;

import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;

import me.coldandtired.mobs.Main;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

public class Creature_data 
{
	public ArrayList<Outcome> outcomes = null;
	public Outcome default_outcome = null;
    public int gen_damages_check_type;
    public int gen_drops_check_type;
    public int as_damages_check_type;
    public int as_drops_check_type;
    public int gen_fallthrough_type;
    public int as_fallthrough_type;
    
	public Creature_data(Element element)
	{
		try 
		{
			gen_damages_check_type = Integer.parseInt(element.getAttributeNode("gen_damages_check_type").getValue());
			gen_drops_check_type = Integer.parseInt(element.getAttributeNode("gen_drops_check_type").getValue());
			as_damages_check_type = Integer.parseInt(element.getAttributeNode("as_damages_check_type").getValue());
			as_drops_check_type = Integer.parseInt(element.getAttributeNode("as_drops_check_type").getValue());
			gen_fallthrough_type = Integer.parseInt(element.getAttributeNode("gen_fallthrough_type").getValue());
			as_fallthrough_type = Integer.parseInt(element.getAttributeNode("as_fallthrough_type").getValue());
			
			if (Boolean.parseBoolean(element.getAttributeNode("has_outcomes").getValue()))
			{
				outcomes = new ArrayList<Outcome>();
				NodeList list = (NodeList)Main.xpath.evaluate("outcomes/Outcome", element, XPathConstants.NODESET);
				for (int i = 0; i < list.getLength(); i++) outcomes.add(new Outcome((Element)list.item(i)));
			}
			
			if (gen_fallthrough_type + as_fallthrough_type > 0 && Boolean.parseBoolean(element.getAttributeNode("has_default_outcome").getValue()))
			{
				Element el = (Element)Main.xpath.evaluate("default_outcome", element, XPathConstants.NODE);
				default_outcome = new Outcome(el);
			}
		} 
		catch (XPathExpressionException e) {e.printStackTrace();}
	}
}

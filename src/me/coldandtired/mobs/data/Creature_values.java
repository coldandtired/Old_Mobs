package me.coldandtired.mobs.data;

import java.util.ArrayList;


import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

public class Creature_values 
{
	ArrayList<Outcome> outcomes = null;
	Outcome default_outcome = null;
	
	Creature_values(Element element, boolean has_conditions, boolean has_default)
	{
		if (has_conditions)
		{
			outcomes = new ArrayList<Outcome>();
			Element e = (Element)element.getElementsByTagName("outcomes").item(0);
			NodeList list = e.getElementsByTagName("Outcome");
			for (int i = 0; i < list.getLength(); i++)
			{
				outcomes.add(new Outcome((Element)list.item(i)));
			}
		}
		if (has_default) default_outcome = new Outcome((Element)element.getElementsByTagName("default_outcome").item(0));
	}
}

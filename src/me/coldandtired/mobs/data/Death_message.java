package me.coldandtired.mobs.data;

import org.w3c.dom.Element;

public class Death_message 
{
	public boolean log_messages;
	public boolean announce_messages;                                                                  
	public String message;
	
	Death_message(Element el)
	{
		log_messages = Boolean.parseBoolean(el.getAttributeNode("log").getValue());
		announce_messages = Boolean.parseBoolean(el.getAttributeNode("announce").getValue());
		message = el.getAttributeNode("text").getValue();
	}
}

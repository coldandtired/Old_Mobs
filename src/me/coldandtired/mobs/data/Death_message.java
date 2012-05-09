package me.coldandtired.mobs.data;

import org.w3c.dom.Element;

public class Death_message 
{
	boolean log_messages;
	boolean announce_messages;                                                                  
	String message;
	
	Death_message(Element el)
	{
		log_messages = Boolean.parseBoolean(el.getAttributeNode("log").getValue());
		announce_messages = Boolean.parseBoolean(el.getAttributeNode("announce").getValue());
		message = el.getAttributeNode("text").getValue();
	}
}

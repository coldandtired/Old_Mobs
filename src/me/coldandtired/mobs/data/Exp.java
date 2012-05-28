package me.coldandtired.mobs.data;

import org.w3c.dom.Element;

public class Exp extends Bounty
{
	public boolean replace;
	
	public Exp (Element el)
	{
		super(el);
		replace = Boolean.parseBoolean(el.getAttributeNode("replace").getValue());
	}
}

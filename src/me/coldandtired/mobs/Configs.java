package me.coldandtired.mobs;

import java.util.Map;

public class Configs 
{
	static Map<String, Object> config;
	
	@SuppressWarnings("unchecked")
	public static Map<String, Object> get_section(String s)
	{
		return (Map<String, Object>)config.get(s);
	}
	
	@SuppressWarnings("unchecked")
	public static boolean get_bool(Map<String, Object> parent, String s, boolean def)
	{
		String[] nodes = s.split("\\.");
		if (parent == null) parent = config;
		
		if (nodes != null)
		{
			if (nodes.length == 1 && parent.containsKey(nodes[0])) return (Boolean)parent.get(nodes[0]);
			else if (nodes.length == 2)
			{
				Map<String, Object> temp = (Map<String, Object>) parent.get(nodes[0]);
				if (temp != null && temp.containsKey(nodes[1])) return (Boolean)temp.get(nodes[1]);
			}
		}
		return def;
	}
}

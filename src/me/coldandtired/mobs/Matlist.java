package me.coldandtired.mobs;

import java.util.ArrayList;
import java.util.List;

public class Matlist 
{
	int id;
	List<String> names = new ArrayList<String>();
	
	public Matlist(int id, String values)
	{
		this.id = id;
		for (String s : values.split(","))
		{
			names.add(s);
		}
	}
}

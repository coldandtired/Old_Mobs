package me.coldandtired.mobs.data;

import java.util.Calendar;
import java.util.List;

import me.coldandtired.mobs.L;

import org.w3c.dom.Element;

public class Autospawn_time 
{
	public int interval;
	public List<Integer> amount = null;
	public int mc_start;
	public int mc_end;
	public boolean has_real_time;
	public boolean has_mc_time;
	public Calendar real_start = null;
	public Calendar real_end = null;
	
	Autospawn_time(Element el)
	{
		String s = "";
		s = el.getAttributeNode("interval").getValue();
		interval = Integer.parseInt(s) * 20;
		if (!Boolean.parseBoolean(el.getAttributeNode("use_seconds").getValue())) interval = interval * 60;
		
		s = el.getAttributeNode("amount").getValue();
		if (!s.equalsIgnoreCase("")) amount = L.fill_int_properties(s);
		
		has_mc_time = Boolean.parseBoolean(el.getAttributeNode("has_mc_time").getValue());
		
		if (has_mc_time)
		{
			s = el.getAttributeNode("mc_time_start").getValue();
			mc_start = !s.equalsIgnoreCase("") ? Integer.parseInt(s) : 0;
		
			s = el.getAttributeNode("mc_time_end").getValue();
			mc_end = !s.equalsIgnoreCase("") ? Integer.parseInt(s) : 24000;
		}
		
		has_real_time = Boolean.parseBoolean(el.getAttributeNode("has_real_time").getValue());
		
		if (has_real_time)
		{
			s = el.getAttributeNode("real_time_start").getValue();
			String[] starts = s.split(":");
			int a = Integer.parseInt(starts[0]);
			int b = 0;
			int c = 0;
			switch (starts.length)
			{
				case 2: 
					b = Integer.parseInt(starts[1]);
					break;
				case 3:
					b = Integer.parseInt(starts[1]);
					c = Integer.parseInt(starts[2]);
					break;
			}
			real_start = Calendar.getInstance();
			real_start.clear();
			real_start.set(0, 0, 0, a, b, c);
								
			s = el.getAttributeNode("real_time_end").getValue();
			starts = s.split(":");
			a = Integer.parseInt(starts[0]);
			b = 0;
			c = 0;
			switch (starts.length)
			{
				case 2: 
					b = Integer.parseInt(starts[1]);
					break;
				case 3:
					b = Integer.parseInt(starts[1]);
					c = Integer.parseInt(starts[2]);
					break;
			}
			real_end = Calendar.getInstance();
			real_end.clear();
			real_end.set(0, 0, 0, a, b, c);
		}
	}
}

package me.coldandtired.mobs.conditions;

import java.io.Serializable;

public class Number_condition implements Serializable
{
	private static final long serialVersionUID = 1L;
	String type = "";
	int low;
	int high;
	
	public Number_condition(Object o)
	{
		if (o instanceof Integer)
		{
			low = (Integer)o;
			type = "equals";
			return;
		}
		
		String number;
		number = ((String)o).replaceAll(" ", "").toLowerCase();
		
		if (number.contains("below"))
		{
			number = number.replaceAll("below", "");
			low = Integer.parseInt(number);
			type = "below";
		}
		else if (number.contains("above"))
		{
			number = number.replaceAll("above", "");
			high = Integer.parseInt(number);
			type = "above";
		}
		else if (number.contains("to"))
		{
			String[] numbers = number.split("to");
			low = Integer.parseInt(numbers[0]);
			high = Integer.parseInt(numbers[1]);
			type = "between";
		}
	}
	
	public boolean matches_number(int number)
	{
		if (type.equalsIgnoreCase("equals") && low == number) return true;
		
		if (type.equalsIgnoreCase("below") && number < low) return true;
		
		if (type.equalsIgnoreCase("above") && number > high) return true;
		
		if (type.equalsIgnoreCase("between") && number >= low && number <= high) return true;
		
		return false;
	}
}

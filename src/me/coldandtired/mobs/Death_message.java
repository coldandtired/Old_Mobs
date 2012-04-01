package me.coldandtired.mobs;

import java.util.ArrayList;
import java.util.Map;

public class Death_message 
{
	ArrayList<String> messages =  new ArrayList<String>();
	boolean log = false;
	boolean global = false;
	boolean tracked_mobs_only = true;
	
	@SuppressWarnings("unchecked")
	Death_message(Map<String, Object> dm)
	{
		messages = dm.containsKey("messages") ? (ArrayList<String>)dm.get("messages") : null;
		log = dm.containsKey("log") ? (Boolean)dm.get("log") : false;
		global = dm.containsKey("global") ? (Boolean)dm.get("global") : false;
		tracked_mobs_only = dm.containsKey("tracked_mobs_only") ? (Boolean)dm.get("tracked_mobs_only") : true;
	}
}

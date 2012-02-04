package me.coldandtired.mobs;

import java.util.ArrayList;
import java.util.Map;

import org.bukkit.configuration.MemorySection;

import net.minecraft.server.World;

public class Mobs_villager extends net.minecraft.server.EntityVillager
{
	private int hp = 20;
	ArrayList<Death_action> death_actions;
	String spawn_reason = "";
	int spawn_rate = 0;
	
	public Mobs_villager(World world) 
	{
		super(world);
	}
	
	@Override
	public int getMaxHealth() 
	{
        return hp;
    }
	
	@SuppressWarnings("unchecked")
	public void setup(MemorySection ms, Map<String, Object> unique, String spawn_reason)
	{
		this.spawn_reason = spawn_reason;
		
		if (unique != null)
		{
			if (unique.containsKey("general"))
			{
				Map<String, Object> general = (Map<String, Object>)unique.get("general");
				if (general.containsKey("hp")) hp = Utils.get_number(general.get("hp"));
				if (general.containsKey("spawn_rate")) spawn_rate = Utils.get_number(general.get("spawn_rate"));
			}
			if (unique.containsKey("death_rules"))
			{
				death_actions = new ArrayList<Death_action>();
				for (Map<String, Object> o : (ArrayList<Map<String, Object>>)unique.get("death_rules")) death_actions.add(new Death_action(o));
			}
		}
		else
		{
			if (ms.contains("general.hp")) hp = Utils.get_number(ms.get("general.hp"));
			if (ms.contains("general.spawn_rate")) spawn_rate = Utils.get_number(ms.get("general.spawn_rate"));	
			if (ms.contains("death_rules"))
			{
				death_actions = new ArrayList<Death_action>();
				for (Map<String, Object> o : ms.getMapList("death_rules")) death_actions.add(new Death_action(o));
			}
		}
		health = hp;
	}
}

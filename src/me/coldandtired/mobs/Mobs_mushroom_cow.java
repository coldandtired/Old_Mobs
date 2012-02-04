package me.coldandtired.mobs;

import java.util.ArrayList;
import java.util.Map;

import org.bukkit.configuration.MemorySection;

import net.minecraft.server.World;

public class Mobs_mushroom_cow extends net.minecraft.server.EntityMushroomCow
{
	private int hp = 10;
	ArrayList<Death_action> death_actions;
	String spawn_reason = "";
	int spawn_rate = 0;
	boolean can_be_sheared = true;
	
	public Mobs_mushroom_cow(World world) 
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
				if (general.containsKey("adult")) if (!Utils.get_random(general.get("adult"))) setAge(-24000);
				if (general.containsKey("can_be_sheared")) can_be_sheared = Utils.get_random(general.get("can_be_sheared"));
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
			if (ms.contains("general.adult")) if (!Utils.get_random(ms.get("general.adult"))) setAge(-24000);
			if (ms.contains("general.can_be_sheared")) can_be_sheared = Utils.get_random(ms.get("general.can_be_sheared", true));
			if (ms.contains("death_rules"))
			{
				death_actions = new ArrayList<Death_action>();
				for (Map<String, Object> o : ms.getMapList("death_rules")) death_actions.add(new Death_action(o));
			}
		}
		health = hp;
	}
}

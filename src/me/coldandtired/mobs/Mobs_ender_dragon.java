package me.coldandtired.mobs;

import java.util.ArrayList;
import java.util.Map;

import org.bukkit.configuration.MemorySection;

import net.minecraft.server.World;

public class Mobs_ender_dragon extends net.minecraft.server.EntityEnderDragon
{
	private int hp = 200;
	ArrayList<Death_action> death_actions;
	boolean create_portal = true;
	boolean destroy_blocks = true;
	boolean safe = false;
	int spawn_rate = 0;
	String spawn_reason = "";
	
	public Mobs_ender_dragon(World world) 
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
				if (general.containsKey("create_portal")) create_portal = Utils.get_random(general.get("create_portal")); 
				if (general.containsKey("destroy_blocks")) destroy_blocks = Utils.get_random(general.get("destroy_blocks"));
				if (general.containsKey("safe")) safe = Utils.get_random(general.get("safe"));
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
			if (ms.contains("general.create_portal")) create_portal = Utils.get_random(ms.get("general.create_portal"));
			if (ms.contains("general.destroy_blocks")) destroy_blocks = Utils.get_random(ms.get("general.destroy_blocks"));
			if (ms.contains("general.safe")) safe = Utils.get_random(ms.get("general.safe"));
			if (ms.contains("death_rules"))
			{
				death_actions = new ArrayList<Death_action>();
				for (Map<String, Object> o : ms.getMapList("death_rules")) death_actions.add(new Death_action(o));
			}
		}
		health = hp;		
	}
}

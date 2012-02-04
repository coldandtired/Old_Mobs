package me.coldandtired.mobs;

import java.util.ArrayList;
import java.util.Map;

import org.bukkit.configuration.MemorySection;

import net.minecraft.server.World;

public class Mobs_creeper extends net.minecraft.server.EntityCreeper
{
	private int hp = 20;
	boolean safe = false;
	int spawn_rate = 0;
	String spawn_reason = "";
	boolean can_become_powered = true;
	boolean destroy_blocks = true;
	boolean fire_explosion = false;
	
	ArrayList<Death_action> death_actions;
	
	public Mobs_creeper(World world) 
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
				if (general.containsKey("powered")) setPowered(Utils.get_random(general.get("powered")));
				if (general.containsKey("safe")) safe = Utils.get_random(general.get("safe"));
				if (general.containsKey("destroy_blocks")) destroy_blocks = Utils.get_random(general.get("destroy_blocks"));
				if (general.containsKey("fire_explosion")) fire_explosion = Utils.get_random(general.get("fire_explosion"));
				if (general.containsKey("can_become_powered")) can_become_powered = Utils.get_random(general.get("can_become_powered"));
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
			if (ms.contains("general.powered")) setPowered(Utils.get_random(ms.get("general.powered", false)));
			if (ms.contains("general.safe")) safe = Utils.get_random(ms.get("general.safe"));
			if (ms.contains("general.destroy_blocks")) destroy_blocks = Utils.get_random(ms.get("general.destroy_blocks"));
			if (ms.contains("general.fire_explosion")) fire_explosion = Utils.get_random(ms.get("general.fire_explosion"));
			if (ms.contains("general.can_become_powered")) can_become_powered = Utils.get_random(ms.get("general.can_become_powered", false));
			if (ms.contains("death_rules"))
			{
				death_actions = new ArrayList<Death_action>();
				for (Map<String, Object> o : ms.getMapList("death_rules")) death_actions.add(new Death_action(o));
			}
		}
		health = hp;		
	}
}

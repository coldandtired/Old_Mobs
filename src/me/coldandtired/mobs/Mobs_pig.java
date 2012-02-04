package me.coldandtired.mobs;

import java.util.ArrayList;
import java.util.Map;

import org.bukkit.configuration.MemorySection;

import net.minecraft.server.PathfinderGoalMeleeAttack;
import net.minecraft.server.World;

public class Mobs_pig extends net.minecraft.server.EntityPig
{
	private int hp = 10;
	ArrayList<Death_action> death_actions;
	int spawn_rate = 0;
	String spawn_reason = "";
	boolean can_become_pig_zombie = true;
	
	public Mobs_pig(World world) 
	{
		super(world);
		this.goalSelector.a(2, new PathfinderGoalMeleeAttack(this, world, 16.0F));
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
				if (general.containsKey("saddled")) setSaddle(Utils.get_random(general.get("saddled")));
				if (general.containsKey("adult")) if (!Utils.get_random(general.get("adult"))) setAge(-24000);
				if (general.containsKey("can_become_pig_zombie")) can_become_pig_zombie = Utils.get_random(general.get("can_become_pig_zombie"));
			}
			if (unique.containsKey("death_rules"))
			{
				death_actions = new ArrayList<Death_action>();
				for (Map<String, Object> o : (ArrayList<Map<String, Object>>)unique.get("death_rules")) death_actions.add(new Death_action(o));
			}
		}
		else if (ms != null)
		{
			if (ms.contains("general.hp")) hp = Utils.get_number(ms.get("general.hp"));	
			if (ms.contains("general.spawn_rate")) spawn_rate = Utils.get_number(ms.get("general.spawn_rate"));	
			if (ms.contains("general.adult")) if (!Utils.get_random(ms.get("general.adult"))) setAge(-24000);
			if (ms.contains("general.saddled")) setSaddle(Utils.get_random(ms.get("general.saddled", false)));
			if (ms.contains("general.can_become_pig_zombie")) can_become_pig_zombie = Utils.get_random(ms.get("general.can_become_pig_zombie", false));
			if (ms.contains("death_rules"))
			{
				death_actions = new ArrayList<Death_action>();
				for (Map<String, Object> o : ms.getMapList("death_rules")) death_actions.add(new Death_action(o));
			}
		}
		health = hp;
	}
}

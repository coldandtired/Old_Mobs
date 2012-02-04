package me.coldandtired.mobs;

import java.util.ArrayList;
import java.util.Map;

import org.bukkit.configuration.MemorySection;

import net.minecraft.server.World;

public class Mobs_skeleton extends net.minecraft.server.EntitySkeleton
{
	private int hp = 20;
	boolean burn = true;
	ArrayList<Condition_group> burn_rules;
	ArrayList<Death_action> death_actions;
	boolean safe = false;
	int spawn_rate = 0;
	String spawn_reason = "";
	
	public Mobs_skeleton(World world) 
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
				if (general.containsKey("damages")) damage = Utils.get_number(general.get("damages")); 
				if (general.containsKey("safe")) safe = Utils.get_random(general.get("safe"));
			}
			if (unique.containsKey("death_rules"))
			{
				death_actions = new ArrayList<Death_action>();
				for (Map<String, Object> o : (ArrayList<Map<String, Object>>)unique.get("death_rules")) death_actions.add(new Death_action(o));
			}
			if (unique.containsKey("burn_rules")) unique = (Map<String, Object>) unique.get("burn_rules");
			if (unique.containsKey("burn")) burn = Utils.get_random(unique.get("burn"));
			ArrayList<Object> conds = (ArrayList<Object>)unique.get("unless");
			if (conds != null && conds.size() > 0)
			{
				if (burn_rules == null) burn_rules = new ArrayList<Condition_group>();
				for (Object o : conds) burn_rules.add(new Condition_group(o));
			}
		}
		else
		{
			if (ms.contains("general.hp")) hp = Utils.get_number(ms.get("general.hp"));
			if (ms.contains("general.spawn_rate")) spawn_rate = Utils.get_number(ms.get("general.spawn_rate"));	
			if (ms.contains("general.damages")) damage = Utils.get_number(ms.get("general.damages"));
			if (ms.contains("general.safe")) safe = Utils.get_random(ms.get("general.safe"));
			if (ms.contains("death_rules"))
			{
				death_actions = new ArrayList<Death_action>();
				for (Map<String, Object> o : ms.getMapList("death_rules")) death_actions.add(new Death_action(o));
			}
			if (ms.contains("burn_rules.burn")) burn = Utils.get_random(ms.get("burn_rules.burn"));
			ArrayList<Object> conds = (ArrayList<Object>)ms.get("burn_rules.unless");
			if (conds != null && conds.size() > 0)
			{
				if (burn_rules == null) burn_rules = new ArrayList<Condition_group>();
				for (Object o : conds) burn_rules.add(new Condition_group(o));
			}
		}
		health = hp;		
	}
}

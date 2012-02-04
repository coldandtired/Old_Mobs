package me.coldandtired.mobs;

import java.util.ArrayList;
import java.util.Map;

import org.bukkit.configuration.MemorySection;

import net.minecraft.server.World;

public class Mobs_wolf extends net.minecraft.server.EntityWolf
{
	private int hp = 8;
	private int tamed_hp = 20;
	ArrayList<Death_action> death_actions;
	boolean safe = false;
	int spawn_rate = 0;
	String spawn_reason = "";
	boolean can_be_tamed = true;
	
	public Mobs_wolf(World world) 
	{
		super(world);
	}
	
	@Override
	public int getMaxHealth() 
	{
		return this.isTamed() ? tamed_hp : hp;
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
				if (general.containsKey("tamed_hp")) tamed_hp = Utils.get_number(general.get("tamed_hp")); 
				if (general.containsKey("safe")) safe = Utils.get_random(general.get("safe"));
				if (general.containsKey("adult")) if (!Utils.get_random(general.get("adult"))) setAge(-24000);
				if (general.containsKey("tamed")) if (Utils.get_random(general.get("tamed"))) setTamed(true);
				if (general.containsKey("angry")) if (Utils.get_random(general.get("angry"))) setAngry(true);
				if (general.containsKey("can_be_tamed")) can_be_tamed = Utils.get_random(general.get("can_be_tamed"));
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
			if (ms.contains("general.tamed_hp")) tamed_hp = Utils.get_number(ms.get("general.tamed_hp"));
			if (ms.contains("general.adult")) if (!Utils.get_random(ms.get("general.adult"))) setAge(-24000);
			if (ms.contains("general.safe")) safe = Utils.get_random(ms.get("general.safe"));
			if (ms.contains("general.tamed")) setTamed(Utils.get_random(ms.get("general.tamed", false)));
			if (ms.contains("general.angry")) setAngry(Utils.get_random(ms.get("general.angry", false)));
			if (ms.contains("general.can_be_tamed")) can_be_tamed = Utils.get_random(ms.get("general.can_be_tamed", true));
			if (ms.contains("death_rules"))
			{
				death_actions = new ArrayList<Death_action>();
				for (Map<String, Object> o : ms.getMapList("death_rules")) death_actions.add(new Death_action(o));
			}
		}
		health = hp;
		if (!can_be_tamed) setTamed(false);
	}
}

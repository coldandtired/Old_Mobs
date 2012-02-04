package me.coldandtired.mobs;

import java.util.ArrayList;
import java.util.Map;

import org.bukkit.configuration.MemorySection;

import net.minecraft.server.World;

public class Mobs_slime extends net.minecraft.server.EntitySlime
{	
	ArrayList<Death_action> death_actions;	
	boolean split = true;
	private int hp = 0;
	int hp_per_size = 0;
	int spawn_rate = 0;
	String spawn_reason = "";
	
	public Mobs_slime(World world) 
	{
		super(world);
	}
	
	@Override
	public int getMaxHealth() 
	{
		int i = this.getSize();
		if (hp > 0) return hp;
		if (hp_per_size > 0) return hp_per_size * i;		

		return i * i;
    }
	
	@SuppressWarnings("unchecked")
	public void setup(MemorySection ms, Map<String, Object> unique, String spawn_reason)
	{
		this.spawn_reason = spawn_reason;
		
		int size = 1 << this.random.nextInt(3);
		
		if (unique != null)
		{
			if (unique.containsKey("general"))
			{
				Map<String, Object> general = (Map<String, Object>)unique.get("general");
				if (general.containsKey("hp")) hp = Utils.get_number(general.get("hp"));
				if (general.containsKey("spawn_rate")) spawn_rate = Utils.get_number(general.get("spawn_rate"));
				if (general.containsKey("hp_per_size")) hp_per_size = Utils.get_number(general.get("hp_per_size"));
				if (general.containsKey("size")) size = Utils.get_number(general.get("size"));
				if (general.containsKey("split")) split = Utils.get_random(general.get("split"));
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
			if (ms.contains("general.size")) size = Utils.get_number(ms.get("general.size"));
			if (ms.contains("general.hp_per_size")) hp_per_size = Utils.get_number(ms.get("general.hp_per_size"));
			if (ms.contains("general.split")) split = Utils.get_random(ms.get("general.split"));
			if (ms.contains("death_rules"))
			{
				death_actions = new ArrayList<Death_action>();
				for (Map<String, Object> o : ms.getMapList("death_rules")) death_actions.add(new Death_action(o));
			}
		}
		setSize(size);	
	}
}

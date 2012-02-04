package me.coldandtired.mobs;

import java.util.ArrayList;
import java.util.Map;

import org.bukkit.configuration.MemorySection;

import net.minecraft.server.World;

public class Mobs_sheep extends net.minecraft.server.EntitySheep
{
	private int hp = 8;
	ArrayList<Death_action> death_actions;
	boolean always_sheared = false;
	byte colour;
	int spawn_rate = 0;
	String spawn_reason = "";
	boolean can_be_sheared = true;
	boolean can_be_dyed = true;
	boolean can_remove_grass;
	
	public Mobs_sheep(World world) 
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
				if (general.containsKey("sheared")) setSheared(Utils.get_random(general.get("sheared")));
				if (general.containsKey("always_sheared")) always_sheared = Utils.get_random(general.get("always_sheared"));
				if (general.containsKey("can_be_sheared")) can_be_sheared = Utils.get_random(general.get("can_be_sheared"));
				if (general.containsKey("can_remove_grass")) can_remove_grass = Utils.get_random(general.get("can_remove_grass"));
				if (general.containsKey("can_be_dyed")) can_be_dyed = Utils.get_random(general.get("can_be_dyed"));
				colour = Utils.get_wool_colour(general.get("wool_colors"));
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
			if (ms.contains("general.sheared")) setSheared(Utils.get_random(ms.get("general.sheared", false)));
			colour = Utils.get_wool_colour(ms.get("general.wool_colors"));
			if (ms.contains("general.always_sheared")) always_sheared = Utils.get_random(ms.get("general.always_sheared", false));
			if (ms.contains("general.can_be_sheared")) can_be_sheared = Utils.get_random(ms.get("general.can_be_sheared", true));
			if (ms.contains("general.can_remove_grass")) can_remove_grass = Utils.get_random(ms.get("general.can_remove_grass", true));
			if (ms.contains("general.can_be_dyed")) can_be_dyed = Utils.get_random(ms.get("general.can_be_dyed", true));
			if (ms.contains("death_rules"))
			{
				death_actions = new ArrayList<Death_action>();
				for (Map<String, Object> o : ms.getMapList("death_rules")) death_actions.add(new Death_action(o));
			}
		}
		
		this.setColor(colour);
		if (!can_be_sheared) setSheared(false);
		if (always_sheared) setSheared(true);
		health = hp;		
	}
}

package me.coldandtired.mobs;

import java.util.ArrayList;
import java.util.Map;

import org.bukkit.configuration.MemorySection;

import net.minecraft.server.World;

public class Mobs_cave_spider extends net.minecraft.server.EntityCaveSpider
{
	private int hp = 12;
	ArrayList<Death_action> death_actions;
	boolean safe = false;
	int spawn_rate = 0;
	String spawn_reason = "";
	
	public Mobs_cave_spider(World world) 
	{
		super(world);
	}
	
	@Override
	public int getMaxHealth() 
	{
        return hp;
    }
	
	@SuppressWarnings("unchecked")
	public void setup(MemorySection general, Map<String, Object> unique, String spawn_reason)
	{
		this.spawn_reason = spawn_reason;
		
		Map<String, Object> u_general = null;
		if (unique != null && unique.containsKey("general")) u_general = (Map<String, Object>)unique.get("general");

		hp = Utils.set_int_property(12, general, u_general, "hp");
		damage = Utils.set_int_property(7, general, u_general, "damages");
		safe = Utils.set_boolean_property(false, general, u_general, "safe");
		spawn_rate= Utils.set_int_property(0, general, u_general, "spawn_rate");
		death_actions = Utils.set_death_actions(general, unique);
		health = hp;	
	}
}

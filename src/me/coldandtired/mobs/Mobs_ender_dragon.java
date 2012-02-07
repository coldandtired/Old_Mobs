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
	public void setup(MemorySection general, Map<String, Object> unique, String spawn_reason)
	{
		this.spawn_reason = spawn_reason;
		
		Map<String, Object> u_general = null;
		if (unique != null && unique.containsKey("general")) u_general = (Map<String, Object>)unique.get("general");

		hp = Utils.set_int_property(200, general, u_general, "hp");
		safe = Utils.set_boolean_property(false, general, u_general, "safe");
		create_portal = Utils.set_boolean_property(true, general, u_general, "create_portal");
		destroy_blocks = Utils.set_boolean_property(true, general, u_general, "destroy_blocks");
		spawn_rate= Utils.set_int_property(0, general, u_general, "spawn_rate");
		death_actions = Utils.set_death_actions(general, unique);
		health = hp;	
	}
}

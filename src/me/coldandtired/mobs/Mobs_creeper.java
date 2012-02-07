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
	public void setup(MemorySection general, Map<String, Object> unique, String spawn_reason)
	{
		this.spawn_reason = spawn_reason;
		
		Map<String, Object> u_general = null;
		if (unique != null && unique.containsKey("general")) u_general = (Map<String, Object>)unique.get("general");

		hp = Utils.set_int_property(20, general, u_general, "hp");
		damage = Utils.set_int_property(6, general, u_general, "damages");
		safe = Utils.set_boolean_property(false, general, u_general, "safe");
		setPowered(Utils.set_boolean_property(false, general, u_general, "powered"));
		destroy_blocks = Utils.set_boolean_property(true, general, u_general, "destroy_blocks");
		fire_explosion = Utils.set_boolean_property(false, general, u_general, "fire_explosion");
		can_become_powered = Utils.set_boolean_property(true, general, u_general, "can_become_powered");
		spawn_rate= Utils.set_int_property(0, general, u_general, "spawn_rate");
		death_actions = Utils.set_death_actions(general, unique);
		health = hp;		
	}
}

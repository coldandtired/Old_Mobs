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
	public void setup(MemorySection general, Map<String, Object> unique, String spawn_reason)
	{
		this.spawn_reason = spawn_reason;
		
		Map<String, Object> u_general = null;
		if (unique != null && unique.containsKey("general")) u_general = (Map<String, Object>)unique.get("general");

		hp = Utils.set_int_property(8, general, u_general, "hp");
		tamed_hp = Utils.set_int_property(20, general, u_general, "tamed_hp");
		safe = Utils.set_boolean_property(false, general, u_general, "safe");
		can_be_tamed = Utils.set_boolean_property(true, general, u_general, "can_be_tamed");
		spawn_rate= Utils.set_int_property(0, general, u_general, "spawn_rate");
		if (Utils.set_boolean_property(false, general, u_general, "adult")) setAge(-24000);
		setTamed(Utils.set_boolean_property(false, general, u_general, "tamed"));
		setAngry(Utils.set_boolean_property(false, general, u_general, "angry"));
		death_actions = Utils.set_death_actions(general, unique);
		health = hp;
		if (!can_be_tamed) setTamed(false);
	}
}

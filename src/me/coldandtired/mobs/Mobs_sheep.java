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
	boolean can_remove_grass = true;
	
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
	public void setup(MemorySection general, Map<String, Object> unique, String spawn_reason)
	{
		this.spawn_reason = spawn_reason;
		
		Map<String, Object> u_general = null;
		if (unique != null && unique.containsKey("general")) u_general = (Map<String, Object>)unique.get("general");

		hp = Utils.set_int_property(8, general, u_general, "hp");
		if (Utils.set_boolean_property(false, general, u_general, "adult")) setAge(-24000);
		always_sheared = Utils.set_boolean_property(false, general, u_general, "always_sheared");
		can_be_sheared = Utils.set_boolean_property(true, general, u_general, "can_be_sheared");
		setSheared(Utils.set_boolean_property(false, general, u_general, "sheared"));
		can_remove_grass = Utils.set_boolean_property(true, general, u_general, "can_remove_grass");
		can_be_dyed = Utils.set_boolean_property(true, general, u_general, "can_be_dyed");
		spawn_rate= Utils.set_int_property(0, general, u_general, "spawn_rate");
		death_actions = Utils.set_death_actions(general, unique);
		colour = Utils.set_byte_property(general, unique);
		health = hp;				
		
		this.setColor(colour);
		if (!can_be_sheared) setSheared(false);
		if (always_sheared) setSheared(true);
		health = hp;		
	}
}

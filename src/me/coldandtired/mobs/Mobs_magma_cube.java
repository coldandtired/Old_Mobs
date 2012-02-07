package me.coldandtired.mobs;

import java.util.ArrayList;
import java.util.Map;

import org.bukkit.configuration.MemorySection;

import net.minecraft.server.World;

public class Mobs_magma_cube extends net.minecraft.server.EntityMagmaCube
{
	ArrayList<Death_action> death_actions;
	boolean split = true;
	private int hp = 0;
	int hp_per_size = 0;
	int spawn_rate = 0;
	String spawn_reason = "";
	
	public Mobs_magma_cube(World world) 
	{
		super(world);
	}
	
	@Override
	public int getMaxHealth() 
	{
		int i = this.getSize();
		if (hp > 0)
		{
			if (hp_per_size == 0) return hp;
			else return (hp * i);
		}
		return i * i;
    }
	
	@SuppressWarnings("unchecked")
	public void setup(MemorySection general, Map<String, Object> unique, String spawn_reason)
	{
		this.spawn_reason = spawn_reason;
		
		Map<String, Object> u_general = null;
		if (unique != null && unique.containsKey("general")) u_general = (Map<String, Object>)unique.get("general");
		int size = 1 << this.random.nextInt(3);
		
		hp = Utils.set_int_property(size * size, general, u_general, "hp");
		size = Utils.set_int_property(size, general, u_general, "size");
		hp_per_size = Utils.set_int_property(size, general, u_general, "hp_per_size");
		split = Utils.set_boolean_property(true, general, u_general, "split");
		spawn_rate= Utils.set_int_property(0, general, u_general, "spawn_rate");
		death_actions = Utils.set_death_actions(general, unique);
		health = hp;			
		setSize(size);		
	}
}

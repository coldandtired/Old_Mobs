package me.coldandtired.mobs;

import java.util.ArrayList;
import java.util.Map;

import org.bukkit.configuration.MemorySection;
import net.minecraft.server.World;

public class Mobs_pig extends net.minecraft.server.EntityPig
{
	private int hp;
	ArrayList<Death_action> death_actions;
	int spawn_rate;
	String spawn_reason;
	boolean can_become_pig_zombie;
	//boolean burn = false;
	//ArrayList<Condition_group> burn_rules;
	
	public Mobs_pig(World world) 
	{
		super(world);
		//this.goalSelector.a(2, new PathfinderGoalMeleeAttack(this, world, 16.0F));
	}	
	
	@Override
	public int getMaxHealth() 
	{
        return hp;
    }
	
	/*@Override
	public void d() 
	{
		boolean temp_burn = burn;
		if (General_listener.matches_condition(burn_rules, (LivingEntity)((CraftEntity)this), spawn_reason, null)) temp_burn = !temp_burn;
		
		if (!temp_burn) return;
		
        if (this.world.e() && !this.world.isStatic) 
        {
            float f = this.a(1.0F);

            if (f > 0.5F && this.world.isChunkLoaded(MathHelper.floor(this.locX), MathHelper.floor(this.locY), 
            		MathHelper.floor(this.locZ)) && this.random.nextFloat() * 30.0F < (f - 0.4F) * 2.0F) 
            {
                this.setOnFire(8);
            }
        }
        super.d();
    }*/
	
	@SuppressWarnings("unchecked")
	public void setup(MemorySection general, Map<String, Object> unique, String spawn_reason)
	{
		this.spawn_reason = spawn_reason;

		Map<String, Object> u_general = null;
		if (unique != null && unique.containsKey("general")) u_general = (Map<String, Object>)unique.get("general");

		hp = Utils.set_int_property(10, general, u_general, "hp");
		spawn_rate= Utils.set_int_property(0, general, u_general, "spawn_rate");
		setSaddle(Utils.set_boolean_property(false, general, u_general, "saddled"));
		can_become_pig_zombie = Utils.set_boolean_property(true, general, u_general, "can_become_pig_zombie");
		if (Utils.set_boolean_property(false, general, u_general, "adult")) setAge(-24000);
		death_actions = Utils.set_death_actions(general, unique);
		//burn_rules = Utils.set_burn_rules(general, unique);
		//burn = Utils.set_burn_property(false, general, unique);
		health = hp;
	}
}

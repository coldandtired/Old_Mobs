package me.coldandtired.mobs;

import java.util.ArrayList;
import java.util.Map;

import org.bukkit.configuration.MemorySection;

import net.minecraft.server.Block;
import net.minecraft.server.Entity;
import net.minecraft.server.MathHelper;
import net.minecraft.server.World;

public class Mobs_enderman extends net.minecraft.server.EntityEnderman
{
	private int hp = 40;
	ArrayList<Death_action> death_actions;
	boolean safe = false;
	boolean move_blocks = true;
	int spawn_rate = 0;
	String spawn_reason = "";
	boolean can_teleport = true;
	
	public Mobs_enderman(World world) 
	{
		super(world);
	}
	
	@Override
	public int getMaxHealth() 
	{
        return hp;
    }
	
	@Override
	protected boolean b(double d0, double d1, double d2)
	{
		if (!can_teleport) return false;
		
		double d3 = this.locX;
        double d4 = this.locY;
        double d5 = this.locZ;

        this.locX = d0;
        this.locY = d1;
        this.locZ = d2;
        boolean flag = false;
        int i = MathHelper.floor(this.locX);
        int j = MathHelper.floor(this.locY);
        int k = MathHelper.floor(this.locZ);
        int l;

        if (this.world.isLoaded(i, j, k)) {
            boolean flag1 = false;

            while (!flag1 && j > 0) {
                l = this.world.getTypeId(i, j - 1, k);
                if (l != 0 && Block.byId[l].material.isSolid()) {
                    flag1 = true;
                } else {
                    --this.locY;
                    --j;
                }
            }

            if (flag1) {
                this.setPosition(this.locX, this.locY, this.locZ);
                if (this.world.a((Entity) this, this.boundingBox).size() == 0 && !this.world.c(this.boundingBox)) {
                    flag = true;
                }
            }
        }

        if (!flag) {
            this.setPosition(d3, d4, d5);
            return false;
        } else {
            short short1 = 128;

            for (l = 0; l < short1; ++l) {
                double d6 = (double) l / ((double) short1 - 1.0D);
                float f = (this.random.nextFloat() - 0.5F) * 0.2F;
                float f1 = (this.random.nextFloat() - 0.5F) * 0.2F;
                float f2 = (this.random.nextFloat() - 0.5F) * 0.2F;
                double d7 = d3 + (this.locX - d3) * d6 + (this.random.nextDouble() - 0.5D) * (double) this.width * 2.0D;
                double d8 = d4 + (this.locY - d4) * d6 + this.random.nextDouble() * (double) this.length;
                double d9 = d5 + (this.locZ - d5) * d6 + (this.random.nextDouble() - 0.5D) * (double) this.width * 2.0D;

                this.world.a("portal", d7, d8, d9, (double) f, (double) f1, (double) f2);
            }

            this.world.makeSound(d3, d4, d5, "mob.endermen.portal", 1.0F, 1.0F);
            this.world.makeSound(this, "mob.endermen.portal", 1.0F, 1.0F);
            return true;
        }		
	}
	
	@SuppressWarnings("unchecked")
	public void setup(MemorySection general, Map<String, Object> unique, String spawn_reason)
	{
		this.spawn_reason = spawn_reason;
		
		Map<String, Object> u_general = null;
		if (unique != null && unique.containsKey("general")) u_general = (Map<String, Object>)unique.get("general");

		hp = Utils.set_int_property(40, general, u_general, "hp");
		damage = Utils.set_int_property(7, general, u_general, "damages");
		safe = Utils.set_boolean_property(false, general, u_general, "safe");
		move_blocks = Utils.set_boolean_property(true, general, u_general, "move_blocks");
		can_teleport = Utils.set_boolean_property(true, general, u_general, "can_teleport");
		spawn_rate= Utils.set_int_property(0, general, u_general, "spawn_rate");
		death_actions = Utils.set_death_actions(general, unique);
		health = hp;			
	}
}

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
				if (general.containsKey("damages")) damage = Utils.get_number(general.get("damages")); 
				if (general.containsKey("safe")) safe = Utils.get_random(general.get("safe"));
				if (general.containsKey("move_blocks")) move_blocks = Utils.get_random(general.get("move_blocks")); 
				if (general.containsKey("can_teleport")) can_teleport = Utils.get_random(general.get("can_teleport"));
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
			if (ms.contains("general.damages")) damage = Utils.get_number(ms.get("general.damages"));
			if (ms.contains("general.safe")) safe = Utils.get_random(ms.get("general.safe"));
			if (ms.contains("general.move_blocks")) move_blocks = Utils.get_random(ms.get("general.move_blocks"));
			if (ms.contains("general.can_teleport")) can_teleport = Utils.get_random(ms.get("general.can_teleport"));
			if (ms.contains("death_rules"))
			{
				death_actions = new ArrayList<Death_action>();
				for (Map<String, Object> o : ms.getMapList("death_rules")) death_actions.add(new Death_action(o));
			}
		}
		health = hp;		
	}
}

package me.coldandtired.mobs.listeners;

import java.util.Map;

import me.coldandtired.api.Mob;
import me.coldandtired.mobs.L;
import me.coldandtired.mobs.Main;
import me.coldandtired.mobs.data.Config;
import me.coldandtired.mobs.data.Creature_data;
import me.coldandtired.mobs.data.Damage_value;
import me.coldandtired.mobs.data.Outcome;

import org.bukkit.Effect;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;

import com.herocraftonline.heroes.api.events.WeaponDamageEvent;

public class Heroes_listener implements Listener
{
	@EventHandler(priority = EventPriority.HIGHEST)
	public void hero_damaged(WeaponDamageEvent event)
	{
		int damage = event.getDamage();		
		LivingEntity le = (LivingEntity)event.getEntity();
		LivingEntity damager = event.getDamager().getEntity();

		if (damager != null)
		{
			Mob attacker = Main.all_mobs.get(damager.getUniqueId().toString());
			if (attacker != null)
			{
				if (attacker.damage != null) damage = attacker.damage;
			}
			else if (damager instanceof Projectile)
			{
				LivingEntity le2 = ((Projectile)damager).getShooter();
				if (le2 != null)
				{
					attacker = Main.all_mobs.get(le2.getUniqueId().toString());
					if (attacker != null && attacker.damage != null) damage = attacker.damage;	
				}
			}
			event.setDamage(damage);
		}				
		
		Mob mob = Main.all_mobs.get(event.getEntity().getUniqueId().toString());
		if (mob == null) return;	
		
		if (mob.boss_mob != null && mob.boss_mob) le.getWorld().playEffect(le.getLocation(), Effect.MOBSPAWNER_FLAMES, 100);
		
		Creature_data cd = Main.tracked_mobs.get(le.getType().name());	
		if (cd == null) return; // not tracked
		
		// end setup
		
		Player p = null;
		if (damager instanceof Player) p = (Player)damager;
		
		Map<String, Damage_value> damage_values = null;	
		
		// check damages == damage or both
		if ((cd.gen_damages_check_type > 0 && !mob.spawn_reason.equalsIgnoreCase("autospawned")) || (cd.as_damages_check_type > 0 && mob.spawn_reason.equalsIgnoreCase("autospawned")))
		{
			if (Config.log_level > 1) L.log("Checking conditions for damages");
			Outcome o = L.get_damages_outcome(cd, le, mob.spawn_reason, p, mob.random, mob.autospawn_id);
			damage_values = L.merge_damage_properties(o, cd, mob.spawn_reason);
		} 
		else 
		{
			if (Config.log_level > 1) L.log("Using spawn conditions");
			damage_values = mob.damage_properties;
		}
		
		if (damage_values != null)
		{
			Damage_value dv = damage_values.get(event.getCause().name());
			if (dv != null)
			{
				double hurt = L.get_quantity(dv.amount);
				if (dv.use_percent)
				{
					hurt = hurt / 100;
					damage = (int) Math.ceil(damage * hurt);
				}
				else damage = (int) hurt;
			}	
		}
		event.setDamage(damage);
	}
	
	//@EventHandler
	//public void other_damage(CharacterDamageEvent event)
	//{
	//	L.log("H event = " + event.getDamage());
	//}
}

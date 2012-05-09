package me.coldandtired.mobs.listeners;

import me.coldandtired.mobs.L;
import me.coldandtired.mobs.Main;
import me.coldandtired.mobs.Mob;
import me.coldandtired.mobs.data.Config;
import me.coldandtired.mobs.data.Creature_data;

import org.bukkit.entity.LivingEntity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.SheepDyeWoolEvent;
import org.bukkit.event.entity.SheepRegrowWoolEvent;
import org.bukkit.event.player.PlayerShearEntityEvent;

public class Sheep_listener implements Listener
{
	@EventHandler(priority = EventPriority.HIGHEST)
	public void onSheepRegrowWool(SheepRegrowWoolEvent event)
	{	
		if (L.ignore_world(event.getEntity().getWorld())) return;

		if (event.isCancelled())
		{
			if (Config.overrule_regrowing_wool) event.setCancelled(false);
			else return;
		}

		LivingEntity le = L.return_le(event.getEntity());
		if (le == null) return;		
		
		Creature_data cd = Main.tracked_mobs.get(le.getType().name());	
		if (cd == null) return; // not tracked
		
		Mob mob = Main.mobs.get(le.getUniqueId());
		if (mob == null) return;
		
		// end setup
		
		if (mob.can_grow_wool != null && !mob.can_grow_wool) event.setCancelled(true);
	}
	
	@EventHandler(priority = EventPriority.HIGHEST)
	public void onSheepDyeWool(SheepDyeWoolEvent event)
	{		
		if (L.ignore_world(event.getEntity().getWorld())) return;

		if (event.isCancelled())
		{
			if (Config.overrule_dying_wool) event.setCancelled(false);
			else return;
		}

		LivingEntity le = L.return_le(event.getEntity());
		if (le == null) return;		
		
		Creature_data cd = Main.tracked_mobs.get(le.getType().name());	
		if (cd == null) return; // not tracked
		
		Mob mob = Main.mobs.get(le.getUniqueId());
		if (mob == null) return;
		
		// end setup
		
		if (mob.can_be_dyed != null && !mob.can_be_dyed) event.setCancelled(true);
	}
	
	@EventHandler(priority = EventPriority.HIGHEST)
	public void onPlayerShearEntity(PlayerShearEntityEvent event)
	{
		if (L.ignore_world(event.getEntity().getWorld())) return;

		if (event.isCancelled())
		{
			if (Config.overrule_shearing) event.setCancelled(false);
			else return;
		}

		LivingEntity le = L.return_le(event.getEntity());
		if (le == null) return;		
		
		Creature_data cd = Main.tracked_mobs.get(le.getType().name());	
		if (cd == null) return; // not tracked
		
		Mob mob = Main.mobs.get(le.getUniqueId());
		if (mob == null) return;
		
		// end setup
		
		if (mob.can_be_sheared != null && !mob.can_be_sheared) event.setCancelled(true);
	}
}

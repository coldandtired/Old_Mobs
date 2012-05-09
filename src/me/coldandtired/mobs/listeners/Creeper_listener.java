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
import org.bukkit.event.entity.CreeperPowerEvent;

public class Creeper_listener implements Listener
{
	@EventHandler(priority = EventPriority.HIGHEST)
	public void onCreeperPower(CreeperPowerEvent event)
	{
		if (L.ignore_world(event.getEntity().getWorld())) return;

		if (event.isCancelled())
		{
			if (Config.overrule_becoming_powered_creeper) event.setCancelled(false);
			else return;
		}

		LivingEntity le = L.return_le(event.getEntity());
		if (le == null) return;		
		
		Creature_data cd = Main.tracked_mobs.get(le.getType().name());	
		if (cd == null) return; // not tracked
		
		Mob mob = Main.mobs.get(le.getUniqueId());
		if (mob == null) return;
		
		// end setup
		
		if (mob.can_become_powered_creeper != null && !mob.can_become_powered_creeper) event.setCancelled(true);
	}
}

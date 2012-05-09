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
import org.bukkit.event.entity.SlimeSplitEvent;

public class Slime_listener implements Listener
{
	@EventHandler(priority = EventPriority.HIGHEST)
	public void onSlimeSplit(SlimeSplitEvent event)
	{
		if (L.ignore_world(event.getEntity().getWorld())) return;

		if (event.isCancelled())
		{
			if (Config.overrule_splitting) event.setCancelled(false);
			else return;
		}

		LivingEntity le = L.return_le(event.getEntity());
		if (le == null) return;		
		
		Creature_data cd = Main.tracked_mobs.get(le.getType().name());	
		if (cd == null) return; // not tracked
		
		Mob mob = Main.mobs.get(le.getUniqueId());
		if (mob == null) return;
		
		// end setup

		if (mob.split_into != null) event.setCount(mob.split_into);
	}
}

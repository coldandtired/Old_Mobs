package me.coldandtired.mobs.listeners;

import me.coldandtired.api.Mob;
import me.coldandtired.mobs.Main;
import me.coldandtired.mobs.data.Config;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.SlimeSplitEvent;

public class Slime_listener implements Listener
{
	@EventHandler(priority = EventPriority.HIGHEST)
	public void onSlimeSplit(SlimeSplitEvent event)
	{
		Mob mob = Main.db.find(Mob.class, event.getEntity().getUniqueId().toString());
		if (mob == null) return;

		if (event.isCancelled())
		{
			if (Config.overrule_splitting) event.setCancelled(false);
			else return;
		}
		
		// end setup
	
		if (mob.getSplit_into() != null) event.setCount(mob.getSplit_into());
	}
}

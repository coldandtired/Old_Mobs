package me.coldandtired.mobs.listeners;

import me.coldandtired.mobs.Main;
import me.coldandtired.mobs.Mob;
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
		if (!Main.all_mobs.containsKey(event.getEntity())) return;

		if (event.isCancelled())
		{
			if (Config.overrule_splitting) event.setCancelled(false);
			else return;
		}

		Mob mob = Main.all_mobs.get(event.getEntity());
		
		// end setup

		if (mob.split_into != null) event.setCount(mob.split_into);
	}
}

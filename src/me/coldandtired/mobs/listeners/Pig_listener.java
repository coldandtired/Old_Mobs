package me.coldandtired.mobs.listeners;

import me.coldandtired.api.Mob;
import me.coldandtired.mobs.Main;
import me.coldandtired.mobs.data.Config;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PigZapEvent;

public class Pig_listener implements Listener
{
	@EventHandler(priority = EventPriority.HIGHEST)
	public void onPigZap(PigZapEvent event)
	{		
		Mob mob = Main.all_mobs.get(event.getEntity().getUniqueId().toString());
		if (mob == null) return;

		if (event.isCancelled())
		{
			if (Config.overrule_becoming_pig_zombie) event.setCancelled(false);
			else return;
		}
		
		// end setup
		
		if (mob.can_become_pig_zombie != null && !mob.can_become_pig_zombie) event.setCancelled(true);
	}
}

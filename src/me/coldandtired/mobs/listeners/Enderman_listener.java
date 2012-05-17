package me.coldandtired.mobs.listeners;

import me.coldandtired.mobs.Mob;
import me.coldandtired.mobs.data.Config;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityTeleportEvent;

public class Enderman_listener implements Listener
{
	@EventHandler(priority = EventPriority.HIGHEST)
	public void onEntityTeleport(EntityTeleportEvent event)
	{
		if (!event.getEntity().hasMetadata("mobs_data")) return;

		if (event.isCancelled())
		{
			if (Config.overrule_teleporting) event.setCancelled(false);
			else return;
		}

		Object o = event.getEntity().getMetadata("mobs_data").get(0).value();
		Mob mob = (Mob)o;
		
		// end setup
		
		if (mob.can_teleport != null && !mob.can_teleport) event.setCancelled(true);
	}
}

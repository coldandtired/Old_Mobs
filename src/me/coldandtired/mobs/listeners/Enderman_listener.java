package me.coldandtired.mobs.listeners;

import me.coldandtired.api.Mob;
import me.coldandtired.mobs.Main;
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
		Mob mob = Main.all_mobs.get(event.getEntity().getUniqueId().toString());
		if (mob == null) return;

		if (event.isCancelled())
		{
			if (Config.overrule_teleporting) event.setCancelled(false);
			else return;
		}
		
		// end setup
		
		if (mob.getCan_teleport() != null && !mob.getCan_teleport()) event.setCancelled(true);
	}
}

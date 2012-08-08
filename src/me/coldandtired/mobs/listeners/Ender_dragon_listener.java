package me.coldandtired.mobs.listeners;

import me.coldandtired.api.Mob;
import me.coldandtired.mobs.Main;
import me.coldandtired.mobs.data.Config;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityCreatePortalEvent;

public class Ender_dragon_listener implements Listener
{
	@EventHandler(priority = EventPriority.HIGHEST)
	public void onEntityCreatePortal(EntityCreatePortalEvent event)
	{
		Mob mob = Main.all_mobs.get(event.getEntity().getUniqueId().toString());
		if (mob == null) return;

		if (event.isCancelled())
		{
			if (Config.overrule_creating_portal) event.setCancelled(false);
			else return;
		}
		
		// end setup
		
		if (mob.getCan_create_portal() != null && !mob.getCan_create_portal()) event.setCancelled(true);
	}
}

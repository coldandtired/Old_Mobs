package me.coldandtired.mobs.listeners;

import me.coldandtired.api.Mob;
import me.coldandtired.mobs.Main;
import me.coldandtired.mobs.data.Config;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.CreeperPowerEvent;

public class Creeper_listener implements Listener
{
	@EventHandler(priority = EventPriority.HIGHEST)
	public void onCreeperPower(CreeperPowerEvent event)
	{
		Mob mob = Main.db.find(Mob.class, event.getEntity().getUniqueId().toString());
		if (mob == null) return;
		
		if (event.isCancelled())
		{
			if (Config.overrule_becoming_powered_creeper) event.setCancelled(false);
			else return;
		}		
				
		// end setup
		
		if (mob.getCan_become_powered_creeper() != null && !mob.getCan_become_powered_creeper()) event.setCancelled(true);
	}
}

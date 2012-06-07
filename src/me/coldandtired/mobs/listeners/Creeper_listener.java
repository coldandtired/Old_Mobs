package me.coldandtired.mobs.listeners;

import me.coldandtired.mobs.Main;
import me.coldandtired.mobs.Mob;
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
		if (!Main.all_mobs.containsKey(event.getEntity())) return;
		
		if (event.isCancelled())
		{
			if (Config.overrule_becoming_powered_creeper) event.setCancelled(false);
			else return;
		}		
		
		Mob mob = Main.all_mobs.get(event.getEntity());
		
		// end setup
		
		if (mob.can_become_powered_creeper != null && !mob.can_become_powered_creeper) event.setCancelled(true);
	}
}

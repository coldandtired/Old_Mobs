package me.coldandtired.mobs.listeners;

import me.coldandtired.mobs.Main;
import me.coldandtired.mobs.Mob;
import me.coldandtired.mobs.data.Config;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityTameEvent;

public class Wolf_listener implements Listener
{
	@EventHandler(priority = EventPriority.HIGHEST)
	public void onEntityTame(EntityTameEvent event)
	{		
		//if (!event.getEntity().hasMetadata("mobs_data")) return;
		if (!Main.all_mobs.containsKey(event.getEntity())) return;

		if (event.isCancelled())
		{
			if (Config.overrule_taming) event.setCancelled(false);
			else return;
		}

		//Object o = event.getEntity().getMetadata("mobs_data").get(0).value();
		//Mob mob = (Mob)o;
		Mob mob = Main.all_mobs.get(event.getEntity());
		
		// end setup
		
		if (mob.can_be_tamed != null && !mob.can_be_tamed) 
		{
			event.setCancelled(true);
			return;
		}
		if (mob.tamed_hp != null) mob.hp = mob.tamed_hp;
	}
}

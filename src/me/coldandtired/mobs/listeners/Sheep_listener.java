package me.coldandtired.mobs.listeners;

import me.coldandtired.mobs.Main;
import me.coldandtired.mobs.Mob;
import me.coldandtired.mobs.data.Config;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.SheepDyeWoolEvent;
import org.bukkit.event.entity.SheepRegrowWoolEvent;
import org.bukkit.event.player.PlayerShearEntityEvent;

public class Sheep_listener implements Listener
{
	@EventHandler(priority = EventPriority.HIGHEST)
	public void onSheepRegrowWool(SheepRegrowWoolEvent event)
	{	
		//if (!event.getEntity().hasMetadata("mobs_data")) return;
		if (!Main.all_mobs.containsKey(event.getEntity())) return;

		if (event.isCancelled())
		{
			if (Config.overrule_regrowing_wool) event.setCancelled(false);
			else return;
		}

		//Object o = event.getEntity().getMetadata("mobs_data").get(0).value();
		//Mob mob = (Mob)o;
		Mob mob = Main.all_mobs.get(event.getEntity());
		
		// end setup
		
		if (mob.can_grow_wool != null && !mob.can_grow_wool) event.setCancelled(true);
	}
	
	@EventHandler(priority = EventPriority.HIGHEST)
	public void onSheepDyeWool(SheepDyeWoolEvent event)
	{		
		//if (!event.getEntity().hasMetadata("mobs_data")) return;
		if (!Main.all_mobs.containsKey(event.getEntity())) return;

		if (event.isCancelled())
		{
			if (Config.overrule_dying_wool) event.setCancelled(false);
			else return;
		}

		//Object o = event.getEntity().getMetadata("mobs_data").get(0).value();
		//Mob mob = (Mob)o;
		Mob mob = Main.all_mobs.get(event.getEntity());
		
		// end setup
		
		if (mob.can_be_dyed != null && !mob.can_be_dyed) event.setCancelled(true);
	}
	
	@EventHandler(priority = EventPriority.HIGHEST)
	public void onPlayerShearEntity(PlayerShearEntityEvent event)
	{
		//if (!event.getEntity().hasMetadata("mobs_data")) return;
		if (!Main.all_mobs.containsKey(event.getEntity())) return;

		if (event.isCancelled())
		{
			if (Config.overrule_shearing) event.setCancelled(false);
			else return;
		}

		//Object o = event.getEntity().getMetadata("mobs_data").get(0).value();
		//Mob mob = (Mob)o;
		Mob mob = Main.all_mobs.get(event.getEntity());
		
		// end setup
		
		if (mob.can_be_sheared != null && !mob.can_be_sheared) event.setCancelled(true);
	}
}

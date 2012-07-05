package me.coldandtired.mobs.api.events;

import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

public class Mob_damaged_event extends Event implements Cancellable
{
	private static final HandlerList handlers = new HandlerList();
	  private boolean cancelled = false;
	//  private int damage;
	  //private final CharacterTemplate damager;
	//  private final Entity entity;
	 // private final EntityDamageEvent.DamageCause cause;
	//  private final boolean projectile;

	public Mob_damaged_event(int damage, EntityDamageByEntityEvent event) 
	{
	//  this.damage = damage;
	 // this.damager = damager;
	 //this.entity = event.getEntity();
	  //this.cause = event.getCause();
	  //this.projectile = (event.getDamager() instanceof Projectile);
	}
	  
	@Override
	public boolean isCancelled() 
	{
		return cancelled;
	}

	@Override
	public void setCancelled(boolean arg0) 
	{
		cancelled = arg0;
	}

	@Override
	public HandlerList getHandlers() 
	{
		return handlers;
	}

}

package me.coldandtired.mobs.api.events;

import java.util.Collection;

import me.coldandtired.api.Mob;
import org.bukkit.entity.LivingEntity;
import org.bukkit.potion.PotionEffect;

public class Mob_created_event extends Base_event
{
	private final Mob mob;
	private final LivingEntity original_entity;
	private Collection<PotionEffect> potion_effects;
	
	public Mob_created_event(Mob mob, LivingEntity original_entity, Collection<PotionEffect> potion_effects)
	{
		this.mob = mob;
		this.original_entity = original_entity;
		this.potion_effects = potion_effects;
	}
	
	public Mob get_mob()
	{
		return mob;
	}
	
	public LivingEntity get_original_entity()
	{
		return original_entity;
	}
	
	public Collection<PotionEffect> get_potion_effects()
	{
		return potion_effects;
	}
	
	public void set_potion_effects(Collection<PotionEffect> new_potion_effects)
	{
		potion_effects = new_potion_effects;
	}
}

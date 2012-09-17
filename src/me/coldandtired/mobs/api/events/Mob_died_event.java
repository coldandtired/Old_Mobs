package me.coldandtired.mobs.api.events;

import java.util.ArrayList;
import java.util.List;

import me.coldandtired.api.Mob;
import me.coldandtired.mobs.data.Death_message;

import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class Mob_died_event extends Base_event
{
	private final Mob mob;
	private final LivingEntity original_entity;
	private final Player killer;
	private List<ItemStack> drops;
	private int exp;
	private int bounty;
	private List<Death_message> messages;
	
	public Mob_died_event(Mob mob, LivingEntity original_entity, Player killer, List<ItemStack> drops,
			int exp, int bounty, List<Death_message> messages)
	{
		this.mob = mob;
		this.original_entity = original_entity;
		this.killer = killer;
		if (drops != null)
		{
			this.drops = new ArrayList<ItemStack>();
			for (ItemStack is : drops) this.drops.add(is);
		}
		else this.drops = null;
		this.exp = exp;
		this.bounty = bounty;
		this.messages = messages;
	}
	
	public Mob get_mob()
	{
		return mob;
	}
	
	public LivingEntity get_original_entity()
	{
		return original_entity;
	}
	
	public Player get_killer()
	{
		return killer;
	}
	
	public List<ItemStack> get_drops()
	{
		return drops;
	}
	
	public void set_drops(List<ItemStack> new_drops)
	{
		drops = new_drops;
	}
	
	public int get_exp()
	{
		return exp;
	}
	
	public void set_exp(int new_exp)
	{
		exp = new_exp;
	}
	
	public int get_bounty()
	{
		return bounty;
	}
	
	public void set_bounty(int new_bounty)
	{
		bounty = new_bounty;
	}
	
	public List<Death_message> get_death_messages()
	{
		return messages;
	}
	
	public void set_death_messages(List<Death_message> new_death_messages)
	{
		messages = new_death_messages;
	}
}

package me.coldandtired.mobs.listeners;

import me.coldandtired.mobs.L;
import me.coldandtired.mobs.Main;
import me.coldandtired.mobs.data.Creature_data;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerRespawnEvent;

public class Player_listener implements Listener
{
	@EventHandler(priority = EventPriority.HIGHEST)
	public void onPlayerRespawn(PlayerRespawnEvent event)
	{
		if (L.ignore_world(event.getPlayer().getWorld())) return;
		LivingEntity le = event.getPlayer();
		
		Creature_data cd = Main.tracked_mobs.get(le.getType().name());
		// end intro		
		
		String spawn_reason = "NATURAL";
		int random = L.rng.nextInt(100) + 1;
		
		L.setup_mob(cd, le, spawn_reason, random, null);	
	}
	
	@EventHandler(priority = EventPriority.HIGHEST)
	public void player_died(PlayerDeathEvent event)
	{
		/*LivingEntity le = event.getEntity();
		String mob_name = le.getType().name();
	
		Creature_data cd = Main.tracked_mobs.get(mob_name);	
		if (cd == null)
		{
			if (Config.messages != null && !Config.tracked_mobs_only)
			{
				for (Death_message dm : Config.messages) L.send_death_message(dm, mob_name, event.getDroppedExp(), le.getKiller(), event.getDrops(), 0);
			}
			else return; // not tracked
		}
		
		Mob mob = Main.all_mobs.get(le.getUniqueId().toString());
		if (mob == null) return;
	
		Main.all_mobs.remove(le.getUniqueId().toString());
		//if (Main.mobs_with_lifetimes != null) Main.mobs_with_lifetimes.remove(le.getUniqueId().toString());
		
		mob_name = mob_name.toLowerCase();
		
		if (Config.log_level > 1) L.log("Tracked mob " + mob_name + " died");
		
		Player p = le.getKiller();
		
		Drops drops = null;		

		// check drops == death or both
		if ((cd.gen_drops_check_type > 0 && !mob.getSpawn_reason().equalsIgnoreCase("autospawned")) || (cd.as_drops_check_type > 0 && mob.getSpawn_reason().equalsIgnoreCase("autospawned")))
		{
			if (Config.log_level > 1) L.log("Checking conditions for drops");
			Outcome o = L.get_drops_outcome(cd, le, mob.getSpawn_reason(), p, mob.getRandom(), mob.getAutospawn_id());
			drops = L.merge_drops(o, cd, mob.getSpawn_reason());
		} 
		else
		{
			if (Config.log_level > 1) L.log("Using spawn conditions");
			drops = mob.getDrops();
		}
		
		List<ItemStack> old_drops = drops != null ? L.get_drops(mob, drops, event.getDrops()) : event.getDrops();
		int old_exp = drops != null ? L.get_exp(mob, drops, event.getDroppedExp()) : event.getDroppedExp();
		int old_bounties = drops != null ? L.get_bounties(mob, drops) : 0;
		List<Death_message> old_messages = drops != null ? drops.messages : Config.messages;
		
		Mob_died_event mob_died_event = new Mob_died_event(mob, le, p, old_drops, old_exp, old_bounties,
				old_messages);
		Main.plugin.getServer().getPluginManager().callEvent(mob_died_event);
		
		if (mob_died_event.isCancelled()) return;
		
		if (mob_died_event.get_drops() != null)
		{
			event.getDrops().clear();
			Iterator<ItemStack> iter = mob_died_event.get_drops().iterator();
			while (iter.hasNext()) event.getDrops().add(iter.next());
		}
		
		event.setDroppedExp(mob_died_event.get_exp());
		
		if (Main.economy != null) Main.economy.depositPlayer(p.getName(), mob_died_event.get_bounty());
		
		if (p != null && Main.heroes != null)
		{
			Hero hero = Main.heroes.getCharacterManager().getHero(p);
			hero.gainExp(Double.parseDouble(Integer.toString(mob_died_event.get_exp())) , HeroClass.ExperienceType.KILLING);
			event.setDroppedExp(0);
		}
			
		if (mob_died_event.get_death_messages() != null)
		{
			for (Death_message dm : mob_died_event.get_death_messages()) L.send_death_message(dm, mob_name, mob_died_event.get_exp(), p, mob_died_event.get_drops(), mob_died_event.get_bounty());				
		}*/
	}
}

package me.coldandtired.mobs.listeners;

import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerRespawnEvent;

public class Player_listener implements Listener
{
	//@EventHandler(priority = EventPriority.HIGHEST)
	public void onPlayerRespawn(PlayerRespawnEvent event)
	{
		/*Player p = event.getPlayer();

		if (!Main.all_mobs.containsKey(p)) return;
		
		if (L.ignore_world(p.getWorld())) return;
		
		int random = L.rng.nextInt(100) + 1;
		
		Creature_data cd = Main.tracked_mobs.get("PLAYER");
		Mob_properties props = L.merge_mob_properties(cd, p, "NATURAL", random, null);		
		Collection<PotionEffect> potion_effects = L.merge_potion_effects(cd, p, "NATURAL", random, null);
		Drops drops = null;
		HashMap<String, Damage_value> damage_properties = null;		
		
		if (cd.gen_drops_check_type != 1) drops = L.merge_drops(cd, p, "NATURAL", null, random, null);
		
		if (cd.gen_damages_check_type != 1) damage_properties = L.merge_damage_properties(cd, p, "NATURAL", null, random, null);
		
		if (props == null && potion_effects == null && drops == null && damage_properties == null) 
		{
			Mob mob = new Mob(props, drops, damage_properties, "NATURAL", random);
			p.setMetadata("mobs_data", new FixedMetadataValue(Main.plugin, mob));
			return;
		}

		Mob mob = new Mob(props, drops, damage_properties, "NATURAL", random);
		
		if (potion_effects != null) p.addPotionEffects(potion_effects);
		Main.all_mobs.put(p, mob);
		
		L.log("respawned!");*/
	}
}

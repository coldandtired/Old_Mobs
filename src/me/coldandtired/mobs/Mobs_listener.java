package me.coldandtired.mobs;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.UUID;

import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Animals;
import org.bukkit.entity.Creeper;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Fireball;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Pig;
import org.bukkit.entity.PigZombie;
import org.bukkit.entity.Player;
import org.bukkit.entity.Sheep;
import org.bukkit.entity.Slime;
import org.bukkit.entity.Wolf;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.entity.CreatureSpawnEvent.SpawnReason;
import org.bukkit.event.entity.CreeperPowerEvent;
import org.bukkit.event.entity.EntityChangeBlockEvent;
import org.bukkit.event.entity.EntityCombustEvent;
import org.bukkit.event.entity.EntityCreatePortalEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.event.entity.EntityTameEvent;
import org.bukkit.event.entity.EntityTargetEvent;
import org.bukkit.event.entity.ExplosionPrimeEvent;
import org.bukkit.event.entity.PigZapEvent;
import org.bukkit.event.entity.SheepDyeWoolEvent;
import org.bukkit.event.entity.SheepRegrowWoolEvent;
import org.bukkit.event.entity.SlimeSplitEvent;
import org.bukkit.event.player.PlayerShearEntityEvent;
import org.bukkit.event.world.ChunkLoadEvent;
import org.bukkit.inventory.ItemStack;

public class Mobs_listener implements Listener
{
	Main plugin;
	Map<String, Object> unique = null;
	ArrayList<String> tracked_mobs;
	Random rng = new Random();
	boolean overrule_damage = false;
	boolean overrule_burn = false;
	boolean overrule_spawn = false;
	boolean overrule_target = false;
	boolean overrule_explode = false;
	boolean overrule_create_portal = false;
	boolean overrule_split = false;
	boolean overrule_regrow_wool = false;
	boolean overrule_dye_wool = false;
	boolean overrule_shear = false;
	boolean overrule_change_block = false;
	boolean overrule_become_powered_creeper = false;
	boolean overrule_tame = false;
	boolean overrule_become_pig_zombie = false;
	Map<UUID, Mob> mobs = new HashMap<UUID, Mob>();
	
	@SuppressWarnings("unchecked")
	boolean setup(Main plugin)
	{
		this.plugin = plugin;
		tracked_mobs = new ArrayList<String>();
		overrule_damage = Configs.get_bool(null, "overrule.damaging", false);
		overrule_burn = Configs.get_bool(null, "overrule.burning", false);
		overrule_spawn = Configs.get_bool(null, "overrule.spawning", false);
		overrule_target = Configs.get_bool(null, "overrule.targeting", false);
		overrule_explode = Configs.get_bool(null, "overrule.exploding", false);
		overrule_create_portal = Configs.get_bool(null, "overrule.creating_portal", false);
		overrule_split = Configs.get_bool(null, "overrule.splitting", false);
		overrule_regrow_wool = Configs.get_bool(null, "overrule.regrowing_wool", false);
		overrule_dye_wool = Configs.get_bool(null, "overrule.dying_wool", false);
		overrule_shear = Configs.get_bool(null, "overrule.shearing", false);
		overrule_change_block = Configs.get_bool(null, "overrule.changing_block", false);
		overrule_tame = Configs.get_bool(null, "overrule.taming", false);
		overrule_become_powered_creeper = Configs.get_bool(null, "overrule.becoming_powered_creeper", false);	
		overrule_become_pig_zombie = Configs.get_bool(null, "overrule.becoming_pig_zombie", false);
		Map<String, String> temp = new HashMap<String, String>();
		for (String s : Utils.mobs)
		{
			Map<String, Object> ms = Configs.get_section(s);
			if ( ms != null)
			{
				if (!(ms.get("general") == null && ms.get("auto_spawn") == null && ms.get("spawn_rules") == null
						&& ms.get("burn_rules") == null && ms.get("death_rules") == null))
				{
					tracked_mobs.add(s);
					String ss = s;
					if (ms.get("auto_spawn") != null)
					{
						ss = s + " (auto_spawn)";
						for (Map<String, Object> map : (ArrayList<Map<String, Object>>)ms.get("auto_spawn"))
						{	
							map = (Map<String, Object>) map.get("spawn_event");		
							if (!(map.get("general") == null && map.get("spawn_rules") == null
								&& map.get("burn_rules") == null && map.get("death_rules") == null)) ss = s + " (custom auto_spawn)";
						}
					}
					temp.put(s, ss);
				}
			}
		}
		
		if (tracked_mobs.size() > 0)
		{
			Utils.log("Tracked mobs:");
			for (String s : tracked_mobs) Utils.log(temp.get(s));
			return true;
		} else return false;
	}
	
	@EventHandler
	public void onEntityDeath(EntityDeathEvent event)
	{
		Entity entity = event.getEntity();
		if (!tracked_mobs.contains(Utils.get_mob(entity))) return;

		LivingEntity le = (LivingEntity)entity;
		Player p = le.getKiller();
		 
		Mob mob = mobs.get(entity.getUniqueId());if (mob != null)
		if (mob != null && mob.death_actions != null)
		{
			for (Death_action da : mob.death_actions)
			{
				if (matches_condition(da.conditions, le, mob.spawn_reason, p, mob.random))
				{
					if (da.exp != null)
					{
						int exp = Utils.get_quantity(da.exp);						
						if (!da.replace_exp) exp += event.getDroppedExp();
						event.setDroppedExp(exp);
					}
					
					if (da.money != null && Main.economy != null && p != null)
					{
						Main.economy.depositPlayer(p.getName(), Utils.get_quantity(da.money));
					}
					
					if (da.items != null)
					{
						if (da.replace_items) event.getDrops().clear();
						for (Item i : da.items)
						{
							if (!i.name.equalsIgnoreCase("nothing"))
							{									
								int quantity = i.quantities != null ? Utils.get_quantity(i.quantities) : 1;	
								Material material = Material.matchMaterial(i.name);
								for (int count = 0; count < quantity; count++)
								{
									ItemStack is = null;
									if (material == null)
									{
										String s = i.name.toUpperCase();
										for (Matlist m : Utils.matlists)
										{
											if (m.names.contains(s))
											{
												is = new ItemStack(m.id, 1, (short)m.names.indexOf(s));	
												break;
											}			
										}
									} 
									else is = new ItemStack(material, 1);
									if (i.enchantments != null)
									{
										for (Enchantment e : i.enchantments.keySet())
										{
											ArrayList<Integer> tempi = i.enchantments.get(e);
											int level = tempi != null ? Utils.get_quantity(tempi) : 1;
											is.addUnsafeEnchantment(e, level);
										}
									}
									event.getDrops().add(is);
								}
							} 
							else
							{
								event.getDrops().clear();
								break;
							}
						}
					}
				}
			}
		}
	}
	
	@SuppressWarnings("unchecked")
	boolean can_spawn(Map<String, Object> ms, LivingEntity entity, SpawnReason spawn_reason, Player player, int random)
	{
		if (ms == null) return true;

		ArrayList<Con_group> conditions = null;
		boolean def = true;
		if (unique != null)
		{
			Map<String, Object> temp = (Map<String, Object>)unique.get("spawn_rules");
			if (temp != null)
			{
				if (temp.containsKey("spawn")) def = (Boolean)temp.get("spawn");
				Object ob = temp.get("unless");
				if (ob instanceof ArrayList)
				{
					ArrayList<Object> conds = (ArrayList<Object>)ob;
					if (conds != null && conds.size() > 0)
					{
						if (conditions == null) conditions = new ArrayList<Con_group>();
						for (Object o : conds) conditions.add(new Con_group(((Map<String, Object>)o).get("condition_group")));
					}
				}
				else
				{
					if (conditions == null) conditions = new ArrayList<Con_group>();
					conditions.add(new Con_group(((Map<String, Object>)ob).get("condition_group")));
				}					
			}
		}
		else
		{
			def = Configs.get_bool(ms, "spawn_rules.spawn", true);
			//def = ms.getBoolean("spawn_rules.spawn", true);
			Object ob = ms.get("spawn_rules.unless");
			if (ob != null)
			{
				if (ob instanceof ArrayList)
				{
					ArrayList<Object> conds = (ArrayList<Object>)ob;
					if (conds != null && conds.size() > 0)
					{
						if (conditions == null) conditions = new ArrayList<Con_group>();
						for (Object o : conds) conditions.add(new Con_group(((Map<String, Object>)o).get("condition_group")));
					}
				}
				else
				{
					if (conditions == null) conditions = new ArrayList<Con_group>();
					conditions.add(new Con_group(((Map<String, Object>)ob).get("condition_group")));
				}
			}
		}
		
		if (conditions == null || conditions.size() == 0) return def;

		for (Con_group c : conditions) if (c.check(entity, entity.getWorld(), entity.getLocation(), spawn_reason.name(), player, random)) return !def;
		return def;
	}
	
	@EventHandler(priority = EventPriority.HIGHEST)
	public void onCreatureSpawn(CreatureSpawnEvent event)
	{
		if (event.isCancelled())
		{
			if (overrule_spawn) event.setCancelled(false); else return;
		}
		try {
		Entity entity = event.getEntity();
		String s = Utils.get_mob(entity);
		if (!tracked_mobs.contains(s)) return;
		
		Map<String, Object> ms = Configs.get_section(s);
		
		if (unique == null && ms.get("spawn_rules") == null && ms.get("burn_rules") == null && ms.get("death_rules") == null) return;
		
		SpawnReason spawn_reason = event.getSpawnReason();
		
		Mob mob = new Mob(ms, unique, spawn_reason.name());
		if (!can_spawn(ms, (LivingEntity)entity, spawn_reason, null, mob.random))
    	{
    		event.setCancelled(true);
    		return;
    	}
		
		if (entity instanceof Slime)
		{
			int hp_per_size = Utils.set_int_property(-1, ms, unique, "hp_per_size");
			if (hp_per_size > -1) mob.hp = ((Slime)entity).getSize() * hp_per_size;
		}
		
		mobs.put(entity.getUniqueId(), mob);
		if (entity instanceof Animals)
		{
			Animals animal = (Animals)entity;
			if (Utils.set_boolean_property(animal.isAdult(), ms, unique, "adult")) animal.setAdult(); else animal.setBaby();
		}
		if (entity instanceof Pig) ((Pig)entity).setSaddle(Utils.set_boolean_property(((Pig)entity).hasSaddle(), ms, unique, "saddled"));
		else if (entity instanceof PigZombie) ((PigZombie)entity).setAngry(Utils.set_boolean_property(((PigZombie)entity).isAngry(), ms, unique, "angry"));
		else if (entity instanceof Wolf)
		{
			Wolf wolf = (Wolf)entity;
			wolf.setAngry(Utils.set_boolean_property(wolf.isAngry(), ms, unique, "angry"));
			wolf.setTamed(Utils.set_boolean_property(wolf.isTamed(), ms, unique, "tamed"));
			if (!mob.can_be_tamed) wolf.setTamed(false);
		}
		else if (entity instanceof Sheep)
		{
			Sheep sheep = (Sheep)entity;
			sheep.setSheared(Utils.set_boolean_property(sheep.isSheared(), ms, unique, "sheared"));
			if (!mob.can_grow_wool) sheep.setSheared(true);
			byte colour = Utils.set_byte_property(ms, unique);
			if (colour > -1) sheep.setColor(DyeColor.getByData(colour));
		}
		else if (entity instanceof Creeper) ((Creeper)entity).setPowered(Utils.set_boolean_property(((Creeper)entity).isPowered(), ms, unique, "powered"));
        }
		catch (Exception e)
		{
			Utils.warn("Problem with " + Utils.get_mob(event.getEntity()) + " in world " + event.getEntity().getWorld().getName());
		}
		unique = null;
	}
		
	@EventHandler(priority = EventPriority.HIGHEST)
	public void onEntityDamage(EntityDamageEvent event)
	{
		Entity entity = event.getEntity();
		if (!(entity instanceof LivingEntity)) return;
		
		if (event.isCancelled())
		{
			if (overrule_damage) event.setCancelled(false); else return;
		}		
		
		int damage = event.getDamage();
		Entity damager = null;
		if (event instanceof EntityDamageByEntityEvent) damager = ((EntityDamageByEntityEvent)event).getDamager();
		
		if (damager != null && tracked_mobs.contains(Utils.get_mob(damager)))
		{
			Mob attacker = mobs.get(damager.getUniqueId());
			if (attacker != null && attacker.damage > -1) damage = attacker.damage;	
		}
		
		Mob mob = mobs.get(entity.getUniqueId());
		if (mob != null && mob.hp > -1)
		{
			mob.hp -= damage;		
			if (mob.hp < 1) event.setDamage(2000); else event.setDamage(-1);
		} else event.setDamage(damage);
	}
		
	static boolean matches_condition(ArrayList<Con_group> conditions, LivingEntity entity, String spawn_reason, Player player, int random)
	{
		if (conditions == null || conditions.size() == 0) return true;
		
		for (Con_group c : conditions) if (c.check(entity, entity.getWorld(), entity.getLocation(), spawn_reason, player, random)) return true;
		return false;
	}
		
	@EventHandler(priority = EventPriority.HIGHEST)
	public void onEntityCombust(EntityCombustEvent event)
	{		
		if (event.isCancelled())
		{
			if (overrule_burn) event.setCancelled(false); else return;
		}
		
		Entity entity = event.getEntity();
		if (!tracked_mobs.contains(Utils.get_mob(entity))) return;
		
		Mob mob = mobs.get(entity.getUniqueId());
		if (mob != null)
		{
			boolean burn = mob.can_burn;
			if (mob.burn_rules != null)
			{
				if (matches_condition(mob.burn_rules, (LivingEntity)entity, mob.spawn_reason, null, mob.random)) burn = !burn;
			}
			if (!burn) event.setCancelled(true);
		}
	}
		
	@EventHandler(priority = EventPriority.HIGHEST)
	public void onEntityTarget(EntityTargetEvent event)
	{
		if (event.isCancelled())
		{
			if (overrule_target) event.setCancelled(false); else return;
		}
		
		Entity entity = event.getEntity();
		if (!tracked_mobs.contains(Utils.get_mob(entity))) return;
		
		Mob mob = mobs.get(entity.getUniqueId());
		if (mob != null && mob.safe) event.setCancelled(true);
	}
		
	@EventHandler(priority = EventPriority.HIGHEST)
	public void onExplosionPrime(ExplosionPrimeEvent event)
	{
		if (event.isCancelled())
		{
			if (overrule_explode) event.setCancelled(false); else return;
		}
		
		Entity entity = event.getEntity();
		if (entity instanceof Fireball) entity = ((Fireball)entity).getShooter();
		if (!tracked_mobs.contains(Utils.get_mob(entity))) return;
		
		Mob mob = mobs.get(entity.getUniqueId());
		if (mob != null)
		{
			event.setFire(mob.fire_explosion);
        	if (mob.safe) event.setCancelled(true);
		}
	}
	
	@EventHandler(priority = EventPriority.HIGHEST)
	public void onEntityExplode(EntityExplodeEvent event)
	{try{
		if (event.isCancelled())
		{
			if (overrule_explode) event.setCancelled(false); else return;
		}
		
		Entity entity = event.getEntity();
		if (!tracked_mobs.contains(Utils.get_mob(entity))) return;
		
		Mob mob = mobs.get(entity.getUniqueId());
		if (mob != null)
		{
			if (!mob.can_destroy_blocks) event.blockList().clear();
        	if (mob.safe) event.setCancelled(true);
		}
	}
	catch (Exception e) {Utils.log("exploding problem with " + event.getEntity().toString());}
	}
	
	@EventHandler(priority = EventPriority.HIGHEST)
	public void onEntityCreatePortal(EntityCreatePortalEvent event)
	{
		if (event.isCancelled())
		{
			if (overrule_create_portal) event.setCancelled(false); else return;
		}
		
		Entity entity = event.getEntity();
		if (!tracked_mobs.contains(Utils.get_mob(entity))) return;
		
		Mob mob = mobs.get(entity.getUniqueId());
		if (mob != null && !mob.can_create_portal) event.setCancelled(true);
	}

	@EventHandler(priority = EventPriority.HIGHEST)
	public void onSlimeSplit(SlimeSplitEvent event)
	{
		if (event.isCancelled())
		{
			if (overrule_split) event.setCancelled(false); else return;
		}
		
		Entity entity = event.getEntity();
		if (!tracked_mobs.contains(Utils.get_mob(entity))) return;
		
		Mob mob = mobs.get(entity.getUniqueId());
		if (mob != null && !mob.can_split) event.setCancelled(true);
	}
	
	@EventHandler(priority = EventPriority.HIGHEST)
	public void onSheepRegrowWool(SheepRegrowWoolEvent event)
	{		
		if (event.isCancelled())
		{
			if (overrule_regrow_wool) event.setCancelled(false); else return;
		}
		
		Entity entity = event.getEntity();
		if (!tracked_mobs.contains(Utils.get_mob(entity))) return;
		
		Mob mob = mobs.get(entity.getUniqueId());
		if (mob != null && !mob.can_grow_wool) event.setCancelled(true);
	}
	
	@EventHandler(priority = EventPriority.HIGHEST)
	public void onSheepDyeWool(SheepDyeWoolEvent event)
	{		
		if (event.isCancelled())
		{
			if (overrule_dye_wool) event.setCancelled(false); else return;
		}
		
		Entity entity = event.getEntity();
		if (!tracked_mobs.contains(Utils.get_mob(entity))) return;
		
		Mob mob = mobs.get(entity.getUniqueId());
		if (mob != null && !mob.can_be_dyed) event.setCancelled(true);
	}
	
	@EventHandler(priority = EventPriority.HIGHEST)
	public void onPlayerShearEntity(PlayerShearEntityEvent event)
	{
		if (event.isCancelled())
		{
			if (overrule_shear) event.setCancelled(false); else return;
		}
		
		Entity entity = event.getEntity();
		if (!tracked_mobs.contains(Utils.get_mob(entity))) return;
		
		Mob mob = mobs.get(entity.getUniqueId());
		if (mob != null && !mob.can_be_sheared) event.setCancelled(true);
	}
	
	@EventHandler(priority = EventPriority.HIGHEST)
	public void onEntityChangeBlock(EntityChangeBlockEvent event)
	{
		if (event.isCancelled())
		{
			if (overrule_change_block) event.setCancelled(false); else return;
		}
		
		Entity entity = event.getEntity();
		if (!tracked_mobs.contains(Utils.get_mob(entity))) return;
		
		Mob mob = mobs.get(entity.getUniqueId());
		if (mob != null && (!mob.can_remove_grass || !mob.can_move_blocks)) event.setCancelled(true);
	}
	
	@EventHandler(priority = EventPriority.HIGHEST)
	public void onCreeperPower(CreeperPowerEvent event)
	{
		if (event.isCancelled())
		{
			if (overrule_become_powered_creeper) event.setCancelled(false); else return;
		}
		
		Entity entity = event.getEntity();
		if (!tracked_mobs.contains(Utils.get_mob(entity))) return;
		
		Mob mob = mobs.get(entity.getUniqueId());
		if (mob != null && !mob.can_become_powered) event.setCancelled(true);
	}
	
	@EventHandler(priority = EventPriority.HIGHEST)
	public void onEntityTame(EntityTameEvent event)
	{		
		if (event.isCancelled())
		{
			if (overrule_tame) event.setCancelled(false); else return;
		}
		
		Entity entity = event.getEntity();
		if (!tracked_mobs.contains(Utils.get_mob(entity))) return;
		
		Mob mob = mobs.get(entity.getUniqueId());
		if (mob != null)
		{			
			if (mob.can_be_tamed) event.setCancelled(true);
			else if (mob.tamed_hp > -1) mob.hp = mob.tamed_hp;
		}
	}
	
	@EventHandler(priority = EventPriority.HIGHEST)
	public void onPigZap(PigZapEvent event)
	{		
		if (event.isCancelled())
		{
			if (overrule_become_pig_zombie) event.setCancelled(false); else return;
		}
		
		Entity entity = event.getEntity();
		if (!tracked_mobs.contains(Utils.get_mob(entity))) return;
		
		Mob mob = mobs.get(entity.getUniqueId());
		if (mob != null && !mob.can_become_pig_zombie) event.setCancelled(true);
	}
	
	@EventHandler
	public void onChunkLoad(ChunkLoadEvent event)
	{
		for (Entity ee: event.getChunk().getEntities()) 
		{
			if (ee instanceof LivingEntity && tracked_mobs.contains(Utils.get_mob(ee)))
			{
				String s = Utils.get_mob(ee);
				if (Main.debug) Utils.log("Found a vanilla " + s + " and converted to a Mobs " + s + "!");
				mobs.put(ee.getUniqueId(), new Mob(Configs.get_section(s), null, SpawnReason.NATURAL.name()));
			}
		}
	}
}

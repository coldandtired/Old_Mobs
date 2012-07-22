package me.coldandtired.api;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import org.bukkit.DyeColor;
import org.bukkit.entity.Ocelot;
import org.bukkit.entity.Villager;

@Entity
@Table(name = "all_mobs")
public class Mob
{	
	// most mobs
	@Id private String mob_id;
	private String name;
	@Column private Integer hp;
	@Column private Integer max_hp;
	@Column private Integer invincibility_ticks;
	@Column private int random;
	@Column private Integer burn_duration;
	@Column private Boolean can_burn;
	@Column private Boolean can_heal;
	@Column private Boolean can_overheal;
	@Column private String spawn_reason;
	//@Column private Drops drops = null;
	@Column private String autospawn_id = null;
	@Column private Boolean boss_mob;
	//@Embedded private Map<String, Damage_value> damage_properties = null;
	@Column private Long death_time;

	// hostile mobs
	@Column private Boolean safe;
	@Column private Integer damage;
	
	// creepers
	@Column private Boolean fiery_explosion;
	@Column private Boolean powered;
	@Column private Boolean can_become_powered_creeper;
	@Column private Integer explosion_size;
		
	// wolves
	@Column private Integer tamed_hp;
	@Column private Boolean tamed;
	@Column private Boolean can_be_tamed;
	@Column private Boolean angry;
	//can_become_angry?
		
	// endermen
	@Column private Boolean can_move_blocks;
	@Column private Boolean can_teleport;
		
	//ender dragons
	@Column private Boolean can_create_portal;
	@Column private Boolean can_destroy_blocks;
		
	// ocelots
	@Column private Ocelot.Type ocelot_type = null;
			
	// animals
	@Column private Boolean adult;
	
	// sheep
	@Column private Boolean sheared;
	@Column private Boolean can_be_dyed;
	@Column private Boolean can_be_sheared;
	@Column private Boolean can_grow_wool;
	@Column private Boolean can_graze;
	@Column private DyeColor wool_colour = null;
	
	// slimes
	@Column private Integer hp_per_size;
	@Column private Integer size;
	@Column private Integer split_into;
	
	// pigs
	@Column private Boolean saddled;
	@Column private Boolean can_become_pig_zombie;
		
	// villagers
	@Column private Villager.Profession villager_type = null;

	
	// getters and setters

	// most mobs
	public String getMob_id() 
	{
		return mob_id;
	}

	public void setMob_id(String mob_id)
	{
		this.mob_id = mob_id;
	}

	public String getName()
	{
		return name;
	}

	public void setName(String name)
	{
		this.name = name;
	}

	public Integer getHp()
	{ 
		return hp; 
	}
	
	public void setHp(Integer hp)
	{
		this.hp = hp;
	}
	
	public Integer getHp_per_size()
	{
		return hp_per_size;
	}
	
	public void setHp_per_size(Integer hp_per_size)
	{
		this.hp_per_size = hp_per_size;
	}

	public Integer getMax_hp() {
		return max_hp;
	}

	public void setMax_hp(Integer max_hp)
	{
		this.max_hp = max_hp;
	}
/*
	public Integer getMax_lifetime()
	{
		return max_lifetime;
	}

	public void setMax_lifetime(Integer max_lifetime)
	{
		this.max_lifetime = max_lifetime;
	}*/

	public Integer getInvincibility_ticks()
	{
		return invincibility_ticks;
	}

	public void setInvincibility_ticks(Integer invincibility_ticks)
	{
		this.invincibility_ticks = invincibility_ticks;
	}

	public int getRandom()
	{
		return random;
	}
	
	public void setRandom(int random)
	{
		this.random = random;
	}

	public Integer getBurn_duration()
	{
		return burn_duration;
	}

	public void setBurn_duration(Integer burn_duration)
	{
		this.burn_duration = burn_duration;
	}

	public Boolean getCan_burn()
	{
		return can_burn;
	}

	public void setCan_burn(Boolean can_burn)
	{
		this.can_burn = can_burn;
	}

	public Boolean getCan_heal()
	{
		return can_heal;
	}

	public void setCan_heal(Boolean can_heal)
	{
		this.can_heal = can_heal;
	}

	public Boolean getCan_overheal()
	{
		return can_overheal;
	}

	public void setCan_overheal(Boolean can_overheal)
	{
		this.can_overheal = can_overheal;
	}

	public String getSpawn_reason()
	{
		return spawn_reason;
	}

	public void setSpawn_reason(String spawn_reason)
	{
		this.spawn_reason = spawn_reason;
	}

	/*public Drops getDrops()
	{
		return drops;
	}

	public void setDrops(Drops drops)
	{
		this.drops = drops;
	}
/*
	public Selected_outcomes getSelected_outcomes() {
		return selected_outcomes;
	}

	public void setSelected_outcomes(Selected_outcomes selected_outcomes) {
		this.selected_outcomes = selected_outcomes;
	}*/

	public String getAutospawn_id()
	{
		return autospawn_id;
	}

	public void setAutospawn_id(String autospawn_id)
	{
		this.autospawn_id = autospawn_id;
	}

	public Boolean getBoss_mob()
	{
		return boss_mob;
	}

	public void setBoss_mob(Boolean boss_mob)
	{
		this.boss_mob = boss_mob;
	}

	/*public Map<String, Damage_value> getDamage_properties()
	{
		return damage_properties;
	}

	public void setDamage_properties(Map<String, Damage_value> damage_properties)
	{
		this.damage_properties = damage_properties;
	}*/

	public Long getDeath_time()
	{
		return death_time;
	}

	public void setDeath_time(Long death_time)
	{
		this.death_time = death_time;
	}

	public Boolean getSafe()
	{
		return safe;
	}

	public void setSafe(Boolean safe)
	{
		this.safe = safe;
	}

	public Integer getDamage()
	{
		return damage;
	}

	public void setDamage(Integer damage)
	{
		this.damage = damage;
	}

	public Boolean getFiery_explosion()
	{
		return fiery_explosion;
	}

	public void setFiery_explosion(Boolean fiery_explosion)
	{
		this.fiery_explosion = fiery_explosion;
	}

	public Boolean getPowered()
	{
		return powered;
	}

	public void setPowered(Boolean powered)
	{
		this.powered = powered;
	}

	public Boolean getCan_become_powered_creeper()
	{
		return can_become_powered_creeper;
	}

	public void setCan_become_powered_creeper(Boolean can_become_powered_creeper)
	{
		this.can_become_powered_creeper = can_become_powered_creeper;
	}

	public Integer getExplosion_size()
	{
		return explosion_size;
	}

	public void setExplosion_size(Integer explosion_size)
	{
		this.explosion_size = explosion_size;
	}

	public Integer getTamed_hp()
	{
		return tamed_hp;
	}

	public void setTamed_hp(Integer tamed_hp)
	{
		this.tamed_hp = tamed_hp;
	}

	public Boolean getTamed()
	{
		return tamed;
	}

	public void setTamed(Boolean tamed)
	{
		this.tamed = tamed;
	}

	public Boolean getCan_be_tamed()
	{
		return can_be_tamed;
	}

	public void setCan_be_tamed(Boolean can_be_tamed)
	{
		this.can_be_tamed = can_be_tamed;
	}

	public Boolean getAngry()
	{
		return angry;
	}

	public void setAngry(Boolean angry)
	{
		this.angry = angry;
	}

	public Boolean getCan_move_blocks()
	{
		return can_move_blocks;
	}

	public void setCan_move_blocks(Boolean can_move_blocks)
	{
		this.can_move_blocks = can_move_blocks;
	}

	public Boolean getCan_teleport()
	{
		return can_teleport;
	}

	public void setCan_teleport(Boolean can_teleport)
	{
		this.can_teleport = can_teleport;
	}

	public Boolean getCan_create_portal()
	{
		return can_create_portal;
	}

	public void setCan_create_portal(Boolean can_create_portal)
	{
		this.can_create_portal = can_create_portal;
	}

	public Boolean getCan_destroy_blocks()
	{
		return can_destroy_blocks;
	}

	public void setCan_destroy_blocks(Boolean can_destroy_blocks)
	{
		this.can_destroy_blocks = can_destroy_blocks;
	}

	public Ocelot.Type getOcelot_type()
	{
		return ocelot_type;
	}

	public void setOcelot_type(Ocelot.Type ocelot_type)
	{
		this.ocelot_type = ocelot_type;
	}

	public Boolean getAdult()
	{
		return adult;
	}

	public void setAdult(Boolean adult)
	{
		this.adult = adult;
	}

	public Boolean getSheared()
	{
		return sheared;
	}

	public void setSheared(Boolean sheared)
	{
		this.sheared = sheared;
	}

	public Boolean getCan_be_dyed()
	{
		return can_be_dyed;
	}

	public void setCan_be_dyed(Boolean can_be_dyed) 
	{
		this.can_be_dyed = can_be_dyed;
	}

	public Boolean getCan_be_sheared()
	{
		return can_be_sheared;
	}

	public void setCan_be_sheared(Boolean can_be_sheared)
	{
		this.can_be_sheared = can_be_sheared;
	}

	public Boolean getCan_grow_wool() 
	{
		return can_grow_wool;
	}

	public void setCan_grow_wool(Boolean can_grow_wool)
	{
		this.can_grow_wool = can_grow_wool;
	}

	public Boolean getCan_graze()
	{
		return can_graze;
	}

	public void setCan_graze(Boolean can_graze)
	{
		this.can_graze = can_graze;
	}

	public DyeColor getWool_colour()
	{
		return wool_colour;
	}

	public void setWool_colour(DyeColor wool_colour)
	{
		this.wool_colour = wool_colour;
	}

	public Integer getSize()
	{
		return size;
	}

	public void setSize(Integer size)
	{
		this.size = size;
	}

	public Integer getSplit_into() 
	{
		return split_into;
	}

	public void setSplit_into(Integer split_into)
	{
		this.split_into = split_into;
	}

	public Boolean getSaddled()
	{
		return saddled;
	}

	public void setSaddled(Boolean saddled)
	{
		this.saddled = saddled;
	}

	public Boolean getCan_become_pig_zombie()
	{
		return can_become_pig_zombie;
	}

	public void setCan_become_pig_zombie(Boolean can_become_pig_zombie)
	{
		this.can_become_pig_zombie = can_become_pig_zombie;
	}

	public Villager.Profession getVillager_type()
	{
		return villager_type;
	}

	public void setVillager_type(Villager.Profession villager_type)
	{
		this.villager_type = villager_type;
	}
}

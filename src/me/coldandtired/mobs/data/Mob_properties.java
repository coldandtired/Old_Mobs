package me.coldandtired.mobs.data;

import java.util.List;

import me.coldandtired.mobs.L;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class Mob_properties
{
	public List<Integer> hp = null;
	public List<Integer> damage = null;
	public String[] villager_types = null;
	public String[] ocelot_types = null;
	public List<Integer> burn_duration = null;
	public List<Integer> max_lifetime = null;
	
	public String boss_mob = null;
	public String can_burn = null;
	public String safe = null;
	public String adult = null;
	public String can_breed = null;
	public String can_heal = null;
	public String can_overheal = null;
	
	// slime properties
	public List<Integer> size = null;
	public List<Integer> hp_per_size = null;
	public List<Integer> split_into = null;
	
	// sheep properties
	public String[] wool_colours = null;
	public String can_be_dyed = null;
	public String sheared = null;
	public String can_grow_wool = null;
	public String can_graze = null;
	public String can_be_sheared = null;
	
	// wolf properties
	public List<Integer> tamed_hp = null;
	public String angry = null;
	public String tamed = null;
	public String can_be_tamed = null;
	
	// pig properties
	public String saddled = null;
	public String can_be_saddled = null;
	public String can_become_pig_zombie = null;
	
	// creeper properties
	public String powered = null;
	public String can_become_powered_creeper = null;
	public String fiery_explosion = null;
	public List<Integer> explosion_size = null;
	
	// enderman properties
	public String can_move_blocks = null;
	public String can_teleport = null;
	
	// ender dragon properties
	public String can_create_portal = null;
	public String can_destroy_blocks = null;	
	
	public Mob_properties(NodeList list)
	{
		if (list == null) return;
		
		for (int i = 0; i < list.getLength(); i ++)
		{
			if (list.item(i).getNodeType() != Node.ELEMENT_NODE) continue;
			Element e = (Element)list.item(i);
			String s = e.getAttributeNode("name").getValue();
			String value = e.getAttributeNode("property_value").getValue();
			String bool_value = e.getAttributeNode("bool_property_value").getValue();
			
			if (!value.equalsIgnoreCase(""))
			{
				if (s.equalsIgnoreCase("hp")) hp = L.fill_int_properties(value);
				if (s.equalsIgnoreCase("max_lifetime")) max_lifetime = L.fill_int_properties(value);
				if (s.equalsIgnoreCase("size")) size = L.fill_int_properties(value);
				if (s.equalsIgnoreCase("hp_per_size")) hp_per_size = L.fill_int_properties(value);
				if (s.equalsIgnoreCase("split_into")) split_into = L.fill_int_properties(value);
				if (s.equalsIgnoreCase("tamed_hp")) tamed_hp = L.fill_int_properties(value);
				if (s.equalsIgnoreCase("damage")) damage = L.fill_int_properties(value);
				if (s.equalsIgnoreCase("explosion_size")) explosion_size = L.fill_int_properties(value);
				if (s.equalsIgnoreCase("wool_colours")) wool_colours = value.split(",");
				if (s.equalsIgnoreCase("villager_types")) villager_types = value.split(",");
				if (s.equalsIgnoreCase("ocelot_types")) ocelot_types = value.split(",");
			}
			if (!bool_value.equalsIgnoreCase("default"))
			{
				if (s.equalsIgnoreCase("boss_mob")) boss_mob = bool_value;
				if (s.equalsIgnoreCase("can_heal")) can_heal = bool_value;
				if (s.equalsIgnoreCase("can_overheal")) can_overheal = bool_value;
				if (s.equalsIgnoreCase("safe")) safe = bool_value;
				if (s.equalsIgnoreCase("sheared")) sheared = bool_value;
				if (s.equalsIgnoreCase("can_be_dyed")) can_be_dyed = bool_value;
				if (s.equalsIgnoreCase("can_be_sheared")) can_be_sheared = bool_value;
				if (s.equalsIgnoreCase("can_grow_wool")) can_grow_wool = bool_value;
				if (s.equalsIgnoreCase("can_graze")) can_graze = bool_value;
				if (s.equalsIgnoreCase("angry")) angry = bool_value;
				if (s.equalsIgnoreCase("tamed")) tamed = bool_value;
				if (s.equalsIgnoreCase("can_be_tamed")) can_be_tamed = bool_value;
				if (s.equalsIgnoreCase("saddled")) saddled = bool_value;
				if (s.equalsIgnoreCase("can_be_saddled")) can_be_saddled = bool_value;
				if (s.equalsIgnoreCase("can_become_pig_zombie")) can_become_pig_zombie = bool_value;
				if (s.equalsIgnoreCase("fiery_explosion")) fiery_explosion = bool_value;
				if (s.equalsIgnoreCase("powered")) powered = bool_value;
				if (s.equalsIgnoreCase("can_become_powered_creeper")) can_become_powered_creeper = bool_value;
				if (s.equalsIgnoreCase("can_create_portal")) can_create_portal = bool_value;
				if (s.equalsIgnoreCase("can_destroy_blocks")) can_destroy_blocks = bool_value;
				if (s.equalsIgnoreCase("can_teleport")) can_teleport = bool_value;
				if (s.equalsIgnoreCase("can_move_blocks")) can_move_blocks = bool_value;
				if (s.equalsIgnoreCase("adult")) adult = bool_value;
				if (s.equalsIgnoreCase("can_breed")) can_breed = bool_value;
				if (s.equalsIgnoreCase("can_burn")) can_burn = bool_value;
			}
		}
	}
}

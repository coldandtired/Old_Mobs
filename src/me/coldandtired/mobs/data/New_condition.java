package me.coldandtired.mobs.data;

import java.util.ArrayList;

import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;

import me.coldandtired.mobs.Condition;
import me.coldandtired.mobs.Main;
import me.coldandtired.mobs.conditions.*;

import org.w3c.dom.Element;
import org.w3c.dom.NodeList;


public class New_condition 
{
	public ArrayList<Condition> conditions = new ArrayList<Condition>();
	
	New_condition(Element element)
	{
		try 
		{
			NodeList list = (NodeList)Main.xpath.evaluate("properties/Property[@property_value != \"\"]", element, XPathConstants.NODESET);
			for (int i = 0; i < list.getLength(); i++)
			{
				Element el = (Element)list.item(i);
				String s = el.getAttributeNode("name").getValue();
				String s2 = el.getAttributeNode("property_value").getValue();

				if (s.equalsIgnoreCase("autospawn_id")) conditions.add(new Autospawn_id(s2));
				if (s.equalsIgnoreCase("biome_type")) conditions.add(new Biomes(s2));
				if (s.equalsIgnoreCase("dates")) conditions.add(new Dates(s2));
				if (s.equalsIgnoreCase("days")) conditions.add(new Days(s2));
				if (s.equalsIgnoreCase("death_cause")) conditions.add(new Death_causes(s2));
				if (s.equalsIgnoreCase("game_times")) conditions.add(new Game_times(s2));
				if (s.equalsIgnoreCase("heights")) conditions.add(new Heights(s2));
				if (s.equalsIgnoreCase("hours")) conditions.add(new Hours(s2));
				//if (s.equalsIgnoreCase("item_type")) conditions.add(new Item_type(s2));
				if (s.equalsIgnoreCase("killed_player_names")) conditions.add(new Killed_player_names(s2));
				if (s.equalsIgnoreCase("light_levels")) conditions.add(new Light_levels(s2));
				if (s.equalsIgnoreCase("minutes")) conditions.add(new Minutes(s2));
				if (s.equalsIgnoreCase("mob_ages")) conditions.add(new Mob_ages(s2));
				if (s.equalsIgnoreCase("mob_standing_on")) conditions.add(new Mob_standing_on(s2));
				if (s.equalsIgnoreCase("months")) conditions.add(new Months(s2));
				if (s.equalsIgnoreCase("month_weeks")) conditions.add(new Month_weeks(s2));
				if (s.equalsIgnoreCase("ocelot_type")) conditions.add(new Ocelot_type(s2));
				if (s.equalsIgnoreCase("online_player_count")) conditions.add(new Online_player_count(s2));
				if (s.equalsIgnoreCase("percent")) conditions.add(new Percent(s2));
				if (s.equalsIgnoreCase("player_money")) conditions.add(new Player_money(s2));
				if (s.equalsIgnoreCase("player_names")) conditions.add(new Player_names(s2));
				if (s.equalsIgnoreCase("player_standing_on")) conditions.add(new Player_standing_on(s2));
				if (s.equalsIgnoreCase("regions")) conditions.add(new Regions(s2));
				if (s.equalsIgnoreCase("remaining_lifetime")) conditions.add(new Remaining_lifetime(s2));
				if (s.equalsIgnoreCase("seconds")) conditions.add(new Seconds(s2));
				if (s.equalsIgnoreCase("server_player_count")) conditions.add(new Server_player_count(s2));
				if (s.equalsIgnoreCase("server_players")) conditions.add(new Server_players(s2));
				if (s.equalsIgnoreCase("spawn_type")) conditions.add(new Spawn_reasons(s2));
				if (s.equalsIgnoreCase("villager_type")) conditions.add(new Villager_type(s2));
				if (s.equalsIgnoreCase("wool_colour")) conditions.add(new Wool_colours(s2));
				
				if (s.equalsIgnoreCase("chunk_mob_class_count")) conditions.add(new Chunk_mob_class_count(s2));
				if (s.equalsIgnoreCase("chunk_mob_count")) conditions.add(new Chunk_mob_count(s2));					
				if (s.equalsIgnoreCase("chunk_blaze_count")) conditions.add(new Chunk_blaze_count(s2));	
				if (s.equalsIgnoreCase("chunk_cave_spider_count")) conditions.add(new Chunk_cave_spider_count(s2));
				if (s.equalsIgnoreCase("chunk_chicken_count")) conditions.add(new Chunk_chicken_count(s2));
				if (s.equalsIgnoreCase("chunk_cow_count")) conditions.add(new Chunk_cow_count(s2));
				if (s.equalsIgnoreCase("chunk_creeper_count")) conditions.add(new Chunk_creeper_count(s2));
				if (s.equalsIgnoreCase("chunk_ender_dragon_count")) conditions.add(new Chunk_ender_dragon_count(s2));
				if (s.equalsIgnoreCase("chunk_enderman_count")) conditions.add(new Chunk_enderman_count(s2));
				if (s.equalsIgnoreCase("chunk_ghast_count")) conditions.add(new Chunk_ghast_count(s2));
				if (s.equalsIgnoreCase("chunk_giant_count")) conditions.add(new Chunk_giant_count(s2));
				if (s.equalsIgnoreCase("chunk_iron_golem_count")) conditions.add(new Chunk_iron_golem_count(s2));
				if (s.equalsIgnoreCase("chunk_magma_cube_count")) conditions.add(new Chunk_magma_cube_count(s2));
				if (s.equalsIgnoreCase("chunk_mushroom_cow_count")) conditions.add(new Chunk_mushroom_cow_count(s2));
				if (s.equalsIgnoreCase("chunk_ocelot_count")) conditions.add(new Chunk_ocelot_count(s2));
				if (s.equalsIgnoreCase("chunk_pig_count")) conditions.add(new Chunk_pig_count(s2));
				if (s.equalsIgnoreCase("chunk_pig_zombie_count")) conditions.add(new Chunk_pig_zombie_count(s2));
				if (s.equalsIgnoreCase("chunk_sheep_count")) conditions.add(new Chunk_sheep_count(s2));
				if (s.equalsIgnoreCase("chunk_silverfish_count")) conditions.add(new Chunk_silverfish_count(s2));
				if (s.equalsIgnoreCase("chunk_skeleton_count")) conditions.add(new Chunk_skeleton_count(s2));
				if (s.equalsIgnoreCase("chunk_slime_count")) conditions.add(new Chunk_slime_count(s2));
				if (s.equalsIgnoreCase("chunk_snowman_count")) conditions.add(new Chunk_snowman_count(s2));
				if (s.equalsIgnoreCase("chunk_spider_count")) conditions.add(new Chunk_spider_count(s2));
				if (s.equalsIgnoreCase("chunk_squid_count")) conditions.add(new Chunk_squid_count(s2));
				if (s.equalsIgnoreCase("chunk_villager_count")) conditions.add(new Chunk_villager_count(s2));
				if (s.equalsIgnoreCase("chunk_wolf_count")) conditions.add(new Chunk_wolf_count(s2));
				if (s.equalsIgnoreCase("chunk_zombie_count")) conditions.add(new Chunk_zombie_count(s2));
				
				if (s.equalsIgnoreCase("world_mob_class_count")) conditions.add(new World_mob_class_count(s2));
				if (s.equalsIgnoreCase("world_mob_count")) conditions.add(new World_mob_count(s2));					
				if (s.equalsIgnoreCase("world_blaze_count")) conditions.add(new World_blaze_count(s2));	
				if (s.equalsIgnoreCase("world_cave_spider_count")) conditions.add(new World_cave_spider_count(s2));
				if (s.equalsIgnoreCase("world_chicken_count")) conditions.add(new World_chicken_count(s2));
				if (s.equalsIgnoreCase("world_cow_count")) conditions.add(new World_cow_count(s2));
				if (s.equalsIgnoreCase("world_creeper_count")) conditions.add(new World_creeper_count(s2));
				if (s.equalsIgnoreCase("world_ender_dragon_count")) conditions.add(new World_ender_dragon_count(s2));
				if (s.equalsIgnoreCase("world_enderman_count")) conditions.add(new World_enderman_count(s2));
				if (s.equalsIgnoreCase("world_ghast_count")) conditions.add(new World_ghast_count(s2));
				if (s.equalsIgnoreCase("world_giant_count")) conditions.add(new World_giant_count(s2));
				if (s.equalsIgnoreCase("world_iron_golem_count")) conditions.add(new World_iron_golem_count(s2));
				if (s.equalsIgnoreCase("world_magma_cube_count")) conditions.add(new World_magma_cube_count(s2));
				if (s.equalsIgnoreCase("world_mushroom_cow_count")) conditions.add(new World_mushroom_cow_count(s2));
				if (s.equalsIgnoreCase("world_ocelot_count")) conditions.add(new World_ocelot_count(s2));
				if (s.equalsIgnoreCase("world_pig_count")) conditions.add(new World_pig_count(s2));
				if (s.equalsIgnoreCase("world_pig_zombie_count")) conditions.add(new World_pig_zombie_count(s2));
				if (s.equalsIgnoreCase("world_sheep_count")) conditions.add(new World_sheep_count(s2));
				if (s.equalsIgnoreCase("world_silverfish_count")) conditions.add(new World_silverfish_count(s2));
				if (s.equalsIgnoreCase("world_skeleton_count")) conditions.add(new World_skeleton_count(s2));
				if (s.equalsIgnoreCase("world_slime_count")) conditions.add(new World_slime_count(s2));
				if (s.equalsIgnoreCase("world_snowman_count")) conditions.add(new World_snowman_count(s2));
				if (s.equalsIgnoreCase("world_spider_count")) conditions.add(new World_spider_count(s2));
				if (s.equalsIgnoreCase("world_squid_count")) conditions.add(new World_squid_count(s2));
				if (s.equalsIgnoreCase("world_villager_count")) conditions.add(new World_villager_count(s2));
				if (s.equalsIgnoreCase("world_wolf_count")) conditions.add(new World_wolf_count(s2));
				if (s.equalsIgnoreCase("world_zombie_count")) conditions.add(new World_zombie_count(s2));
				
				if (s.equalsIgnoreCase("world_names")) conditions.add(new World_names(s2));
				if (s.equalsIgnoreCase("world_player_count")) conditions.add(new World_player_count(s2));
				if (s.equalsIgnoreCase("world_players")) conditions.add(new World_players(s2));
				if (s.equalsIgnoreCase("world_type")) conditions.add(new World_types(s2));
				if (s.equalsIgnoreCase("years")) conditions.add(new Years(s2));
				if (s.equalsIgnoreCase("year_days")) conditions.add(new Year_days(s2));
				if (s.equalsIgnoreCase("year_weeks")) conditions.add(new Year_weeks(s2));
			}
			
			list = (NodeList)Main.xpath.evaluate("properties/Property[@bool_property_value != \"Default\"]", element, XPathConstants.NODESET);
			for (int i = 0; i < list.getLength(); i++)
			{
				Element el = (Element)list.item(i);
				String s = el.getAttributeNode("name").getValue();
				boolean b = el.getAttributeNode("bool_property_value").getValue().equalsIgnoreCase("yes") ? true : false;
				
				if (s.equalsIgnoreCase("angry")) conditions.add(new Angry(b));
				if (s.equalsIgnoreCase("angry")) conditions.add(new Angry(b));
				if (s.equalsIgnoreCase("can_breed")) conditions.add(new Can_breed(b));
				if (s.equalsIgnoreCase("killed_by_player")) conditions.add(new Killed_by_player(b));
				if (s.equalsIgnoreCase("powered")) conditions.add(new Powered(b));
				if (s.equalsIgnoreCase("raining")) conditions.add(new Raining(b));
				if (s.equalsIgnoreCase("saddled")) conditions.add(new Saddled(b));
				if (s.equalsIgnoreCase("sheared")) conditions.add(new Sheared(b));
				if (s.equalsIgnoreCase("tamed")) conditions.add(new Tamed(b));
				if (s.equalsIgnoreCase("thundering")) conditions.add(new Thundering(b));
			}
		} 
		catch (XPathExpressionException e) {e.printStackTrace();}
	}
}
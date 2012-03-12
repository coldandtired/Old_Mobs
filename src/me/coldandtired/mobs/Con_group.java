package me.coldandtired.mobs;

import java.util.ArrayList;
import java.util.Map;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;

import me.coldandtired.mobs.conditions.*;

public class Con_group implements Condition
{
	ArrayList<Condition> conditions = new ArrayList<Condition>();
	
	@SuppressWarnings("unchecked")
	Con_group(Object ob)
	{
		if (ob instanceof ArrayList)
		{
			for (Map<String, Object> c : (ArrayList<Map<String, Object>>)ob)
			{			
				if (c.containsKey("angry")) conditions.add(new Angry(c.get("angry")));
				if (c.containsKey("area_mob_count")) conditions.add(new Area_mob_count(c.get("area_mob_count")));
				if (c.containsKey("area_mob_class_count")) conditions.add(new Area_mob_class_count(c.get("area_mob_class_count")));
				if (c.containsKey("biomes")) conditions.add(new Biomes(c.get("biomes")));
				if (c.containsKey("blaze_within")) conditions.add(new Blaze_within("blaze_within"));
				if (c.containsKey("dates")) conditions.add(new Dates(c.get("dates")));
				if (c.containsKey("days")) conditions.add(new Days(c.get("days")));
				if (c.containsKey("death_causes")) conditions.add(new Death_causes(c.get("death_causes")));
				if (c.containsKey("game_times")) conditions.add(new Game_times(c.get("game_times")));
				if (c.containsKey("heights")) conditions.add(new Heights(c.get("heights")));
				if (c.containsKey("hours")) conditions.add(new Hours(c.get("hours")));
				if (c.containsKey("killed_by_player")) conditions.add(new Killed_by_player(c.get("killed_by_player")));
				if (c.containsKey("killed_player_names")) conditions.add(new Killed_player_names(c.get("killed_player_names")));
				if (c.containsKey("light_levels")) conditions.add(new Light_levels(c.get("light_levels")));
				if (c.containsKey("minutes")) conditions.add(new Minutes(c.get("minutes")));
				if (c.containsKey("mob_ages")) conditions.add(new Mob_ages(c.get("mob_ages")));
				if (c.containsKey("mob_standing_on")) conditions.add(new Mob_standing_on(c.get("mob_standing_on")));
				if (c.containsKey("month_weeks")) conditions.add(new Month_weeks(c.get("month_weeks")));
				if (c.containsKey("months")) conditions.add(new Months(c.get("months")));
				if (c.containsKey("ocelot_type")) conditions.add(new Ocelot_type(c.get("ocelot_type")));
				if (c.containsKey("percent")) conditions.add(new Percent(c.get("percent")));
				if (c.containsKey("player_holding")) conditions.add(new Player_holding(c.get("player_holding")));
				if (c.containsKey("player_items")) conditions.add(new Player_items(c.get("player_items")));
				if (c.containsKey("player_items_all")) conditions.add(new Player_items_all(c.get("player_items_all")));
				if (c.containsKey("player_money")) conditions.add(new Player_money(c.get("player_money")));
				if (c.containsKey("player_names")) conditions.add(new Player_names(c.get("player_names")));
				if (c.containsKey("player_standing_on")) conditions.add(new Player_standing_on(c.get("player_standing_on")));
				if (c.containsKey("player_wearing")) conditions.add(new Player_wearing(c.get("player_wearing")));
				if (c.containsKey("player_wearing_all")) conditions.add(new Player_wearing_all(c.get("player_wearing_all")));
				if (c.containsKey("player_within")) conditions.add(new Player_within(c.get("player_within")));
				if (c.containsKey("powered")) conditions.add(new Powered(c.get("powered")));
				if (c.containsKey("raining")) conditions.add(new Raining(c.get("raining")));
				if (c.containsKey("regions")) conditions.add(new Regions(c.get("regions")));
				if (c.containsKey("saddled")) conditions.add(new Saddled(c.get("saddled")));
				if (c.containsKey("seconds")) conditions.add(new Seconds(c.get("seconds")));			
				if (c.containsKey("server_player_count")) conditions.add(new Server_player_count(c.get("server_player_count")));	
				if (c.containsKey("server_players")) conditions.add(new Server_players(c.get("server_players")));
				if (c.containsKey("sheared")) conditions.add(new Sheared(c.get("sheared")));
				if (c.containsKey("spawn_reasons")) conditions.add(new Spawn_reasons(c.get("spawn_reasons")));
				if (c.containsKey("tamed")) conditions.add(new Tamed(c.get("tamed")));
				if (c.containsKey("thundering")) conditions.add(new Thundering(c.get("thundering")));
				if (c.containsKey("villager_type")) conditions.add(new Villager_type(c.get("villager_type")));
				if (c.containsKey("wool_colours")) conditions.add(new Wool_colours(c.get("wool_colours")));
				if (c.containsKey("world_mob_count")) conditions.add(new World_mob_count(c.get("world_mob_count")));
				if (c.containsKey("world_mob_class_count")) conditions.add(new World_mob_class_count(c.get("world_mob_class_count")));
				if (c.containsKey("world_names")) conditions.add(new World_names(c.get("world_names")));
				if (c.containsKey("world_player_count")) conditions.add(new World_player_count(c.get("world_player_count")));
				if (c.containsKey("world_players")) conditions.add(new World_players(c.get("world_players")));			
				if (c.containsKey("world_types")) conditions.add(new World_types(c.get("world_types")));	
				if (c.containsKey("year_days")) conditions.add(new Year_days(c.get("year_days")));
				if (c.containsKey("year_weeks")) conditions.add(new Year_weeks(c.get("year_weeks")));			
				if (c.containsKey("years")) conditions.add(new Years(c.get("years")));						
			}
		}
		else
		{
			Map<String, Object> c = (Map<String, Object>)ob;
			if (c.containsKey("angry")) conditions.add(new Angry(c.get("angry")));
			if (c.containsKey("area_mob_count")) conditions.add(new Area_mob_count(c.get("area_mob_count")));
			if (c.containsKey("area_mob_class_count")) conditions.add(new Area_mob_class_count(c.get("area_mob_class_count")));
			if (c.containsKey("biomes")) conditions.add(new Biomes(c.get("biomes")));
			if (c.containsKey("blaze_within")) conditions.add(new Blaze_within("blaze_within"));
			if (c.containsKey("dates")) conditions.add(new Dates(c.get("dates")));
			if (c.containsKey("days")) conditions.add(new Days(c.get("days")));
			if (c.containsKey("death_causes")) conditions.add(new Death_causes(c.get("death_causes")));
			if (c.containsKey("game_times")) conditions.add(new Game_times(c.get("game_times")));
			if (c.containsKey("heights")) conditions.add(new Heights(c.get("heights")));
			if (c.containsKey("hours")) conditions.add(new Hours(c.get("hours")));
			if (c.containsKey("killed_by_player")) conditions.add(new Killed_by_player(c.get("killed_by_player")));
			if (c.containsKey("killed_player_names")) conditions.add(new Killed_player_names(c.get("killed_player_names")));
			if (c.containsKey("light_levels")) conditions.add(new Light_levels(c.get("light_levels")));
			if (c.containsKey("minutes")) conditions.add(new Minutes(c.get("minutes")));
			if (c.containsKey("mob_ages")) conditions.add(new Mob_ages(c.get("mob_ages")));
			if (c.containsKey("mob_standing_on")) conditions.add(new Mob_standing_on(c.get("mob_standing_on")));
			if (c.containsKey("month_weeks")) conditions.add(new Month_weeks(c.get("month_weeks")));
			if (c.containsKey("months")) conditions.add(new Months(c.get("months")));
			if (c.containsKey("ocelot_type")) conditions.add(new Ocelot_type(c.get("ocelot_type")));
			if (c.containsKey("percent")) conditions.add(new Percent(c.get("percent")));
			if (c.containsKey("player_holding")) conditions.add(new Player_holding(c.get("player_holding")));			
			if (c.containsKey("player_items")) conditions.add(new Player_items(c.get("player_items")));
			if (c.containsKey("player_items_all")) conditions.add(new Player_items_all(c.get("player_items_all")));
			if (c.containsKey("player_money")) conditions.add(new Player_money(c.get("player_money")));
			if (c.containsKey("player_names")) conditions.add(new Player_names(c.get("player_names")));
			if (c.containsKey("player_standing_on")) conditions.add(new Player_standing_on(c.get("player_standing_on")));
			if (c.containsKey("player_wearing")) conditions.add(new Player_wearing(c.get("player_wearing")));
			if (c.containsKey("player_wearing_all")) conditions.add(new Player_wearing_all(c.get("player_wearing_all")));
			if (c.containsKey("player_within")) conditions.add(new Player_within(c.get("player_within")));
			if (c.containsKey("powered")) conditions.add(new Powered(c.get("powered")));
			if (c.containsKey("raining")) conditions.add(new Raining(c.get("raining")));
			if (c.containsKey("regions")) conditions.add(new Regions(c.get("regions")));
			if (c.containsKey("saddled")) conditions.add(new Saddled(c.get("saddled")));
			if (c.containsKey("seconds")) conditions.add(new Seconds(c.get("seconds")));			
			if (c.containsKey("server_player_count")) conditions.add(new Server_player_count(c.get("server_player_count")));	
			if (c.containsKey("server_players")) conditions.add(new Server_players(c.get("server_players")));
			if (c.containsKey("sheared")) conditions.add(new Sheared(c.get("sheared")));
			if (c.containsKey("spawn_reasons")) conditions.add(new Spawn_reasons(c.get("spawn_reasons")));
			if (c.containsKey("tamed")) conditions.add(new Tamed(c.get("tamed")));
			if (c.containsKey("thundering")) conditions.add(new Thundering(c.get("thundering")));
			if (c.containsKey("villager_type")) conditions.add(new Villager_type(c.get("villager_type")));
			if (c.containsKey("wool_colours")) conditions.add(new Wool_colours(c.get("wool_colours")));
			if (c.containsKey("world_mob_count")) conditions.add(new World_mob_count(c.get("world_mob_count")));
			if (c.containsKey("world_mob_class_count")) conditions.add(new World_mob_class_count(c.get("world_mob_class_count")));
			if (c.containsKey("world_names")) conditions.add(new World_names(c.get("world_names")));
			if (c.containsKey("world_player_count")) conditions.add(new World_player_count(c.get("world_player_count")));
			if (c.containsKey("world_players")) conditions.add(new World_players(c.get("world_players")));			
			if (c.containsKey("world_types")) conditions.add(new World_types(c.get("world_types")));	
			if (c.containsKey("year_days")) conditions.add(new Year_days(c.get("year_days")));
			if (c.containsKey("year_weeks")) conditions.add(new Year_weeks(c.get("year_weeks")));			
			if (c.containsKey("years")) conditions.add(new Years(c.get("years")));
		}
	}
	
	@Override
	public boolean check(LivingEntity entity, World world, Location loc, String spawn_reason, Player player, int random) 
	{
		for (Condition c : conditions) if (!c.check(entity, world, loc, spawn_reason, player, random)) return false;
		return true;
	}
}

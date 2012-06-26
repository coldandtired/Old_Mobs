package me.coldandtired.mobs;

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class Biome_data 
{
	public ConcurrentMap<String, List<int[]>> chunks = new ConcurrentHashMap<String, List<int[]>>();
}

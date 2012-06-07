package me.coldandtired.mobs.listeners;

import me.coldandtired.mobs.L;
import me.coldandtired.mobs.Main;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.world.ChunkLoadEvent;

public class Chunk_listener implements Listener
{
	@EventHandler
	public void onChunkLoad(ChunkLoadEvent event)
	{
		if (L.ignore_world(event.getWorld())) return;
		
		Main.add_chunk(event.getChunk());		
	}
}

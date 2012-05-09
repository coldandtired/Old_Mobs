package me.coldandtired.mobs.listeners;

import me.coldandtired.mobs.Autospawner;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.world.ChunkLoadEvent;

public class Chunk_listener implements Listener
{
	@EventHandler
	public void onChunkLoad(ChunkLoadEvent event)
	{
		Autospawner.add_chunk(event.getChunk());		
	}
}

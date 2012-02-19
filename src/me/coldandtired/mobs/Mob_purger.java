package me.coldandtired.mobs;

public class Mob_purger implements Runnable
{
	Main plugin;
	
	Mob_purger(Main plugin)
	{
		this.plugin = plugin;
	}
	
	@Override
	public void run() 
	{
		plugin.purge_unique();		
	}

}

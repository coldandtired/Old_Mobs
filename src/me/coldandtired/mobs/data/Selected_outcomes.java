package me.coldandtired.mobs.data;

import java.io.Serializable;

public class Selected_outcomes implements Serializable
{
	private static final long serialVersionUID = 1L;
	public int damages = -1;
	public int properties = -1;
	public int drops = -1;
	public int potions = -1;
	public String spawn_reason = "NATURAL";
	public int random = -1;
}

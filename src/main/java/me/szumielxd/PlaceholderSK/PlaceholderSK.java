package me.szumielxd.PlaceholderSK;

import java.io.IOException;

import org.bukkit.plugin.java.JavaPlugin;

import ch.njol.skript.Skript;
import ch.njol.skript.SkriptAddon;
import me.szumielxd.PlaceholderSK.utils.ReflectionUtils;

public class PlaceholderSK extends JavaPlugin {
	
	
	private static PlaceholderSK instance;
	private ReflectionUtils reflections;
	private SkriptAddon addon;
	
	
	public void onEnable() {
		
		instance = this;
		reflections = new ReflectionUtils();
		try {
			getSkriptInstance();
			(this.addon = getSkriptInstance()).loadClasses("me.szumielxd.PlaceholderSK.skript", "events", "expressions");
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	
	
	public static PlaceholderSK getInstance() {
		
		if (instance == null) throw new IllegalStateException();
		return instance;
		
	}
	
	
	public SkriptAddon getSkriptInstance() {
		
		if(addon == null) addon = Skript.registerAddon(this);
		//this.getLogger().log(Level.INFO, "########### "+addon);
		return addon;
		
	}
	
	
	public ReflectionUtils getReflectionUtils() {
		return this.reflections;
	}
	

}

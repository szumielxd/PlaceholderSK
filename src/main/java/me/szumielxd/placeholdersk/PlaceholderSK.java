package me.szumielxd.placeholdersk;

import java.io.IOException;

import org.bukkit.plugin.java.JavaPlugin;

import ch.njol.skript.Skript;
import ch.njol.skript.SkriptAddon;
import me.szumielxd.placeholdersk.utils.ReflectionUtils;

public class PlaceholderSK extends JavaPlugin {
	
	
	private static PlaceholderSK instance;
	private ReflectionUtils reflections;
	private SkriptAddon addon;
	
	
	@Override
	public void onEnable() {
		
		setInstance(this);
		reflections = new ReflectionUtils();
		try {
			this.addon = getSkriptInstance();
			this.addon.loadClasses("me.szumielxd.placeholdersk.skript", "events", "expressions");
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	
	
	public static PlaceholderSK getInstance() {
		if (instance == null) throw new IllegalStateException();
		return instance;
	}
	
	private static void setInstance(PlaceholderSK instance) {
		PlaceholderSK.instance = instance;
	}
	
	public SkriptAddon getSkriptInstance() {	
		if(addon == null) addon = Skript.registerAddon(this);
		return addon;
	}
	
	public ReflectionUtils getReflectionUtils() {
		return this.reflections;
	}
	

}

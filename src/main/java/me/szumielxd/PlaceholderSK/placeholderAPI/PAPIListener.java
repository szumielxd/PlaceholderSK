package me.szumielxd.PlaceholderSK.placeholderAPI;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import me.szumielxd.PlaceholderSK.PlaceholderSK;
import me.szumielxd.PlaceholderSK.utils.ReflectionUtils;

public class PAPIListener extends PlaceholderExpansion {


	private String identifier;
	
	
	public PAPIListener(String prefix) {
		this.identifier = prefix;
	}
	
	
	@Override
	public boolean persist() {
		return true;
	}
	
	
	@Override
	public String getAuthor() {
		return String.join(", ", PlaceholderSK.getInstance().getDescription().getAuthors());
	}

	@Override
	public String getIdentifier() {
		return identifier;
	}

	@Override
	public String getVersion() {
		return PlaceholderSK.getInstance().getDescription().getVersion();
	}
	
	@Override
	public String getPlugin() {
		return "Skript";
	}
	
	@Override
	public String onPlaceholderRequest(Player p, String param) {
		PAPIEvent event;
		if(Bukkit.isPrimaryThread()) {
			event = new PAPIEvent(p, identifier, param);
		}else {
			event = new PAPIEvent(p, identifier, param, true);
		}
		ReflectionUtils utils = PlaceholderSK.getInstance().getReflectionUtils();
		Object res = utils.getField(Bukkit.getServer(), "console");
		boolean running = (boolean) utils.invokeMethod(res, "isRunning");
		if(running) Bukkit.getPluginManager().callEvent(event);
		return event.getResult();
	}

}

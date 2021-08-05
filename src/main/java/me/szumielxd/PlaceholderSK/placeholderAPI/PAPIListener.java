package me.szumielxd.PlaceholderSK.placeholderAPI;

import org.bukkit.entity.Player;
import org.bukkit.event.EventException;
import org.bukkit.event.Listener;

import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import me.szumielxd.PlaceholderSK.PlaceholderSK;
import me.szumielxd.PlaceholderSK.skript.events.SKPlaceholderRequestEvent;

public class PAPIListener extends PlaceholderExpansion {


	private final String identifier;
	private final SKPlaceholderRequestEvent skEvent;
	private final Listener listener;
	
	
	public PAPIListener(SKPlaceholderRequestEvent skEvent, String prefix) {
		this.skEvent = skEvent;
		this.identifier = prefix;
		this.listener = new Listener() {};
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
		PAPIEvent event = new PAPIEvent(p, identifier, param);
		try {
			this.skEvent.getExecutor().execute(this.listener, event);
		} catch (EventException e) {
			e.printStackTrace();
		}
		return event.getResult();
	}

}
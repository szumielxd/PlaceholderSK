package me.szumielxd.placeholdersk.placeholderapi;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.bukkit.entity.Player;
import org.bukkit.event.EventException;
import org.bukkit.event.Listener;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import ch.njol.skript.lang.Trigger;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import me.clip.placeholderapi.expansion.Relational;
import me.szumielxd.placeholdersk.PlaceholderSK;
import me.szumielxd.placeholdersk.skript.events.SKPlaceholderRequestEvent;

public class PAPIListener extends PlaceholderExpansion implements Relational {


	private final SKPlaceholderRequestEvent skEvent;
	private final String identifier;
	private final List<String> placeholders;
	private final String trigger;
	private final Listener listener = new Listener() {};
	
	
	public PAPIListener(@NotNull SKPlaceholderRequestEvent skEvent, @NotNull String prefix, @Nullable String[] placeholders, Trigger trigger) {
		this.skEvent = skEvent;
		this.identifier = prefix;
		this.placeholders = placeholders == null ? Collections.emptyList() : Stream.of(placeholders).map(s -> "%" + prefix + "_" + s + "%").collect(Collectors.toList());
		this.trigger = trigger.getDebugLabel();
	}
	
	
	@Override
	public boolean persist() {
		return true;
	}
	
	
	@Override
	public List<String> getPlaceholders() {
		return this.placeholders;
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
	
	@Deprecated
	@Override
	public String getPlugin() {
		return "PlaceholderSK";
	}
	
	@Override
	public String getRequiredPlugin() {
		return "Skript";
	}
	
	@Override
	public String getName() {
		return "%s (%s)".formatted(getIdentifier(), this.trigger);
	}
	
	@Override
	public String onPlaceholderRequest(Player player, String param) {
		return onPlaceholderRequest(null, player, param);
	}


	@Override
	public String onPlaceholderRequest(Player target, Player player, String param) {
		PAPIEvent event = new PAPIEvent(player, target, this.identifier, param);
		try {
			this.skEvent.getExecutor().execute(this.listener, event);
		} catch (EventException e) {
			e.printStackTrace();
		}
		return event.getResult();
	}

}

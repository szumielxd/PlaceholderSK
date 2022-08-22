package me.szumielxd.placeholdersk.placeholderapi;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class PAPIEvent extends Event {

	
	private static final @NotNull HandlerList handlerList = new HandlerList();
	private @NotNull Player player;
	private @Nullable Player target;
	private @NotNull String identifier;
	private @Nullable String result;
	private @NotNull String prefix;
	
	
	@Override
	public @NotNull HandlerList getHandlers() {
		return handlerList;
	}
	
	
	public static @NotNull HandlerList getHandlerList() {
		return handlerList;
	}
	
	public PAPIEvent(@NotNull Player player, @Nullable Player target, @NotNull String prefix, @NotNull String identifier) {
		this.identifier = identifier;
		this.player = player;
		this.target = target;
		this.prefix = prefix;
	}
	
	public PAPIEvent(@NotNull Player player, @Nullable Player target, @NotNull String prefix, @NotNull String identifier, boolean async) {
		super(async);
		this.identifier = identifier;
		this.player = player;
		this.target = target;
		this.prefix = prefix;
	}
	
	
	public void setResult(String result) {
		this.result = result;
	}
	
	
	public String getResult() {
		return this.result;
	}
	
	
	public String getIdentifier() {
		return this.identifier;
	}
	
	
	public Player getPlayer() {
		return this.player;
	}
	
	
	public Player getTarget() {
		return this.target;
	}
	
	
	public String getPrefix() {
		return this.prefix;
	}
	
	
	

}

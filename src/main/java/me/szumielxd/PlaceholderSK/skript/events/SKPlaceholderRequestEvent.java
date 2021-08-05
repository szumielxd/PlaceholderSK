package me.szumielxd.PlaceholderSK.skript.events;

import ch.njol.skript.Skript;
import ch.njol.skript.SkriptEventHandler;
import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Examples;
import ch.njol.skript.doc.Name;
import ch.njol.skript.lang.Literal;
import ch.njol.skript.lang.SelfRegisteringSkriptEvent;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.skript.lang.Trigger;
import ch.njol.skript.log.ErrorQuality;
import ch.njol.skript.util.Getter;
import me.clip.placeholderapi.PlaceholderAPI;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import me.szumielxd.PlaceholderSK.placeholderAPI.PAPIEvent;
import me.szumielxd.PlaceholderSK.placeholderAPI.PAPIListener;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.HashSet;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import ch.njol.skript.registrations.EventValues;
import org.bukkit.event.Event;
import org.bukkit.event.EventException;
import org.bukkit.event.Listener;
import org.bukkit.plugin.EventExecutor;

@Name("On Placeholder Request")
@Description("Called whenever a placeholder is requested")
@Examples("on placeholder request with prefix \"example\":\n\tif the identifier is \"name\": # example_name\n\t\tset the result to player's name\n\telse if the identifier is \"uuid\": # example_uuid\n\t\tset the result to the player's uuid\n\telse if the identifier is \"money\": # example_money\n\t\tset the result to \"$%{money::%player's uuid%}%\"")
public class SKPlaceholderRequestEvent extends SelfRegisteringSkriptEvent {

	static {
		Skript.registerEvent("Placeholder Request", SKPlaceholderRequestEvent.class, PAPIEvent.class, "(placeholder[api]|papi) request with [the] prefix %string%");
		EventValues.registerEventValue(PAPIEvent.class, Player.class, new Getter<Player, PAPIEvent>() {
			@Override
			public Player get(PAPIEvent e) {
				return e.getPlayer();
			}
		}, 0);
		EventValues.registerEventValue(PAPIEvent.class, String.class, new Getter<String, PAPIEvent>() {
			@Override
			public String get(PAPIEvent e) {
				return e.getIdentifier();
			}
		}, 0);
	}

	@SuppressWarnings("unchecked")
	@Override
	public boolean init(final Literal<?>[] args, final int matchedPattern, final SkriptParser.ParseResult parser) {
		prefix = ((Literal<String>) args[0]).getSingle();
		if ("".equals(prefix)) {
			Skript.error(prefix + " is not a valid placeholder", ErrorQuality.SEMANTIC_ERROR);
			return false;
		}
		if(PlaceholderAPI.isRegistered(prefix)) {
			Skript.error("Placeholder with prefix '" + prefix + "' is already registered", ErrorQuality.SEMANTIC_ERROR);
			return false;
		}
		return true;
	}

	private String prefix;
	final static HashMap<String, Trigger> triggers = new HashMap<>();
	final static HashMap<String, PAPIListener> listeners = new HashMap<>();
	
	private static boolean registeredExecutor = false;
	private final EventExecutor executor = new EventExecutor() {
		
		@Override
		public void execute(final Listener l, final Event e) throws EventException {
			if (e == null)
				return;
			PAPIEvent ev = (PAPIEvent)e;
			if(ev.getPrefix() != null) {
				Trigger tr = triggers.get(ev.getPrefix());
				if(tr != null) {
					SkriptEventHandler.logTriggerStart(tr);
					tr.execute(e);
					SkriptEventHandler.logTriggerEnd(tr);
				}
			}
			return;
		}
	};
	
	public EventExecutor getExecutor() {
		return this.executor;
	}

	@Override
	public String toString(Event e, boolean debug) {
		return "placeholder request" + (prefix != null ? (" with prefix \"" + prefix + "\"") : "");
	}

	@Override
	public void register(Trigger tr) {
		triggers.put(prefix, tr);
		PAPIListener l = new PAPIListener(this, prefix);
		listeners.put(prefix, l);
		l.register();
		if (!registeredExecutor) {
			registeredExecutor = true;
		}
	}

	@Override
	public void unregister(Trigger tr) {
		for(String key : new HashSet<>(triggers.keySet())) {
			if(triggers.get(key) != tr) continue;
			triggers.remove(key);
			PAPIListener l = listeners.remove(key);
			this.legacyUnregister(l);
		}
	}

	@Override
	public void unregisterAll() {
		for(String key : new HashSet<>(triggers.keySet())) {
			triggers.remove(key);
			PAPIListener l = listeners.remove(key);
			this.legacyUnregister(l);
		}
	}
	
	
	private void legacyUnregister(PlaceholderExpansion expansion) {
		if(!Bukkit.getPluginManager().isPluginEnabled("PlaceholderAPI")) return;
		try {
			expansion.getClass().getMethod("unregister").invoke(expansion);
		} catch (NoSuchMethodException | SecurityException | IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
			try {
				PlaceholderAPI.class.getMethod("unregisterExpansion", PlaceholderExpansion.class).invoke(null, expansion);
			} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException | NoSuchMethodException | SecurityException e1) {
				e1.printStackTrace();
			}
		}
	}
}
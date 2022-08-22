package me.szumielxd.placeholdersk.skript.expressions;

import ch.njol.skript.ScriptLoader;
import ch.njol.skript.Skript;
import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Examples;
import ch.njol.skript.doc.Name;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.ExpressionType;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.skript.lang.parser.ParserInstance;
import ch.njol.skript.lang.util.SimpleExpression;
import ch.njol.skript.log.ErrorQuality;
import ch.njol.util.Kleenean;
import me.szumielxd.placeholdersk.placeholderapi.PAPIEvent;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;

@Name("Placeholder Target")
@Description("Grabs the target of a relational placeholder request event")
@Examples("on placeholder request with prefix \"example\":\n\tif the target is \"hamster\": # example_name\n\t\tset the result to player's name\n\telse if the identifier is \"uuid\": # example_uuid\n\t\tset the result to the player's uuid\n\telse if the identifier is \"money\": # example_money\n\t\tset the result to \"$%{money::%player's uuid%}%\"")
public class ExprTarget extends SimpleExpression<Player> {

    static {
        Skript.registerExpression(ExprTarget.class, Player.class, ExpressionType.SIMPLE,"[the] [(placeholder[api]|papi)] target");
    }

    @Override
    public boolean init(Expression<?>[] exprs, int matchedPattern, Kleenean isDelayed, SkriptParser.ParseResult parseResult) {
        if (!this.isValidEvent()) {
            Skript.error("The PlaceholderAPI target can only be used in a placeholder request event", ErrorQuality.SEMANTIC_ERROR);
            return false;
        }
        return true;
    }
    
    @SuppressWarnings("deprecation")
	private boolean isValidEvent() {
    	try {
    		return ParserInstance.get().isCurrentEvent(PAPIEvent.class);
    	} catch (NoSuchMethodError e) {
    		return ScriptLoader.isCurrentEvent(PAPIEvent.class);
    	}
    }

    @Override
    protected Player[] get(final Event e) {
        return new Player[] {((PAPIEvent) e).getTarget()};
    }

    @Override
    public String toString(Event e, boolean debug) {
        return "the relational placeholder target";
    }

    @Override
    public boolean isSingle() {
        return true;
    }

    @Override
    public Class<? extends Player> getReturnType() {
        return Player.class;
    }
}
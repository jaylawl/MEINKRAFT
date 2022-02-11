package de.jaylawl.meinkraft.util;

import de.jaylawl.meinkraft.Meinkraft;
import org.bukkit.command.CommandException;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Method;
import java.util.function.Function;
import java.util.logging.Logger;

public class PingUtil {
	private static Function<@NotNull Player, Integer> pingQueryHandler;
	
	private PingUtil() {}
	
	/**
	 * Query a player's ping.<br>
	 * Will call {@link PingUtil#init()} if the handler has not been set up yet.
	 * @param player Target player
	 * @return The player's ping (in ms)
	 * @throws CommandException if ping query fails ({@link Throwable} cause included)
	 * @throws CommandException if no ping method found
	 */
	public static int getPing(@NotNull Player player) {
		if (pingQueryHandler == null) init();
		return pingQueryHandler.apply(player);
	}
	
	/**
	 * Finds the first handler in
	 * {{@link Player#getPing()}, {@link Player.Spigot#getPing()}}
	 * and uses it as the ping provider.<br>
	 * If none found, a handler which will throw a
	 * {@link CommandException} whenever called is used.
	 */
	public static void init() {
		if (pingQueryHandler != null) return;
		Logger logger = Meinkraft.getInstance().getLogger();
		
		//check for Player#getPing()
		try {
			Method getPing = Player.class.getMethod("getPing");
			pingQueryHandler = (player) -> {
				try {
					return (int) getPing.invoke(player);
				} catch (Exception e) {
					throw new CommandException("Failed to query ping", e); //will capture and log
				}
			};
			return;
		} catch (NoSuchMethodException e) {
			logger.warning("Player#getPing() does not exist, falling back to Player$Spigot#getPing().");
		}
		
		//check for Player$Spigot#getPing()
		try {
			Method getPing = Player.Spigot.class.getMethod("getPing");
			pingQueryHandler = (player) -> {
				try {
					return (int) getPing.invoke(player.spigot());
				} catch (Exception e) {
					throw new CommandException("Failed to query ping", e); //will capture and log
				}
			};
			return;
		} catch (NoSuchMethodException e) {
			logger.severe("Player$Spigot#getPing() does not exist! No more fallbacks remaining.");
		}
		
		//no more fallbacks; throw error every time
		pingQueryHandler = (player) -> {
			throw new CommandException("No method found to check for player ping.");
		};
	}
}

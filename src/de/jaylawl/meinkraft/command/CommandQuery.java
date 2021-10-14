package de.jaylawl.meinkraft.command;

import de.jaylawl.meinkraft.Meinkraft;
import de.jaylawl.meinkraft.util.MessagingUtil;
import de.jaylawl.meinkraft.util.TabHelper;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class CommandQuery implements MeinkraftCommand {

    public static final String PERMISSION_NODE = "mk.query";

    public CommandQuery() {
    }

    //

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String label, @NotNull String[] arguments) {

        List<String> completions = new ArrayList<>();
        int argumentNumber = TabHelper.getArgumentNumber(arguments);

        switch (argumentNumber) {

            case 1 -> {
                completions.addAll(TabCompleteUtil.getOnlinePlayerNames());
            }

            case 2 -> {
                completions.addAll(Arrays.asList(
                        "canfly",
                        "flightspeed",
                        "gamemode",
                        "godmode",
                        "health",
                        "ip",
                        "location",
                        "potioneffects",
                        "saturation",
                        "scoreboardtags",
                        "walkspeed",
                        "world"
                ));
            }

            default -> {
                return Collections.emptyList();
            }

        }

        return TabHelper.sortedCompletions(arguments[argumentNumber - 1], completions);

    }

    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String label, @NotNull String[] arguments) {

        if (arguments.length == 0) {
            commandSender.sendMessage(ChatColor.GREEN + "/query" + ChatColor.RED + " <player>" + ChatColor.GRAY + " <query>");
            return true;
        }

        Player targetPlayer = Bukkit.getPlayer(arguments[0]);
        if (targetPlayer == null) {
            MessagingUtil.invalidArguments(commandSender, arguments[0], "is not an online player");
            return true;
        }

        if (arguments.length == 1) {
            commandSender.sendMessage(ChatColor.GREEN + "/query <player>" + ChatColor.RED + " <query>");
            return true;
        }

        String queryArgument = arguments[1].toLowerCase();
        Object result;

        switch (queryArgument) {

            case "canfly" -> {
                result = targetPlayer.getAllowFlight();
            }

            case "gamemode" -> {
                result = targetPlayer.getGameMode();
            }

            case "godmode" -> {
                result = Meinkraft.getDataCenter().isInGodMode(targetPlayer.getUniqueId());
            }

            case "location" -> {
                Location location = targetPlayer.getLocation();
                location.setX(location.getBlockX());
                location.setY(location.getBlockY());
                location.setZ(location.getBlockZ());
                location.setYaw(Math.round(location.getYaw()));
                location.setPitch(Math.round(location.getPitch()));
                result = location;
            }

            case "world" -> {
                result = targetPlayer.getWorld().getName();
            }

            case "flightspeed" -> {
                result = targetPlayer.getFlySpeed();
            }

            case "walkspeed" -> {
                result = targetPlayer.getWalkSpeed();
            }

            case "health" -> {
                result = targetPlayer.getHealth();
            }

            case "saturation" -> {
                result = targetPlayer.getSaturation();
            }

            case "potioneffects" -> {
                result = targetPlayer.getActivePotionEffects();
            }

            case "scoreboardtags" -> {
                result = targetPlayer.getScoreboardTags();
            }

            // TODO: 13.10.2021 make the query for player IP into a command for security purposes
            case "ip" -> {
                result = targetPlayer.getAddress();
            }

            default -> {
                MessagingUtil.genericError(commandSender, "Unknown query argument");
                return true;
            }

        }

        commandSender.sendMessage("Player: " + targetPlayer.getName() + ", Query: \"" + queryArgument + "\", Result: " + result);

        return true;
    }
}

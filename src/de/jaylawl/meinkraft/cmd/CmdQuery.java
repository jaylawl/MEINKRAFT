package de.jaylawl.meinkraft.cmd;

import de.jaylawl.meinkraft.util.CmdPermission;
import de.jaylawl.meinkraft.util.MessagingUtil;
import de.jaylawl.meinkraft.util.TabHelper;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class CmdQuery implements CommandExecutor, TabCompleter {

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String label, @NotNull String[] arguments) {

        if (!CmdPermission.hasAny(commandSender, label)) {
            return Collections.emptyList();
        }

        int argN = TabHelper.getArgNumber(arguments);
        List<String> completions = new ArrayList<>();

        switch (argN) {
            case 1:
                for (Player p : Bukkit.getOnlinePlayers()) {
                    completions.add(p.getName());
                }
                break;
            case 2:
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
                break;
            default:
                return Collections.emptyList();
        }

        return TabHelper.sortedCompletions(arguments[argN - 1], completions);

    }

    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String label, @NotNull String[] arguments) {

        if (!CmdPermission.hasAny(commandSender, label)) {
            MessagingUtil.noPermission(commandSender);
            return true;
        }

        Player affectedPlayer;

        if (arguments.length < 1) {
            MessagingUtil.genericError(commandSender, "Missing player argument");
            return true;
        } else {
            affectedPlayer = Bukkit.getPlayer(arguments[0]);
            if (affectedPlayer == null) {
                MessagingUtil.invalidArguments(commandSender, arguments[0], "is not an online player");
                return true;
            }
        }
        if (arguments.length < 2) {
            MessagingUtil.genericError(commandSender, "Missing query argument");
            return true;
        }

        String query = arguments[1].toLowerCase();
        Object result = "?";

        switch (arguments[1].toLowerCase()) {

            case "canfly": {
                result = affectedPlayer.getAllowFlight();
                break;
            }
            case "gamemode": {
                result = affectedPlayer.getGameMode();
                break;
            }
            case "godmode": {
                result = affectedPlayer.hasMetadata("GodMode") && affectedPlayer.getMetadata("GodMode").get(0).asBoolean();
                break;
            }
            case "location": {
                Location l = affectedPlayer.getLocation();
                l.setX(l.getBlockX());
                l.setY(l.getBlockY());
                l.setZ(l.getBlockZ());
                l.setYaw(Math.round(l.getYaw()));
                l.setPitch(Math.round(l.getPitch()));
                result = l;
                break;
            }
            case "world": {
                result = affectedPlayer.getWorld().getName();
                break;
            }
            case "flightspeed": {
                result = affectedPlayer.getFlySpeed();
                break;
            }
            case "walkspeed": {
                result = affectedPlayer.getWalkSpeed();
                break;
            }
            case "health": {
                result = affectedPlayer.getHealth();
                break;
            }
            case "saturation": {
                result = affectedPlayer.getSaturation();
                break;
            }
            case "potioneffects": {
                result = affectedPlayer.getActivePotionEffects();
                break;
            }
            case "scoreboardtags": {
                result = affectedPlayer.getScoreboardTags();
                break;
            }
            case "ip": {
                result = affectedPlayer.getAddress();
                break;
            }
        }

        commandSender.sendMessage("Player: " + affectedPlayer.getName() + ", Query: \"" + query + "\", Result: " + result);

        return true;
    }
}

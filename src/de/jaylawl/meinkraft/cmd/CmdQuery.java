package de.jaylawl.meinkraft.cmd;

import de.jaylawl.meinkraft.util.CmdPermission;
import de.jaylawl.meinkraft.util.Messaging;
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
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String label, @NotNull String[] args) {

        if (!CmdPermission.hasAny(sender, label)) {
            return Collections.emptyList();
        }

        int argN = TabHelper.getArgNumber(args);
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

        return TabHelper.sortedCompletions(args[argN - 1], completions);

    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String label, @NotNull String[] args) {

        if (!CmdPermission.hasAny(sender, label)) {
            Messaging.noPermission(sender);
            return true;
        }

        Player affectedPlayer;

        if (args.length < 1) {
            Messaging.genericError(sender, "Missing player argument");
            return true;
        } else {
            affectedPlayer = Bukkit.getPlayer(args[0]);
            if (affectedPlayer == null) {
                Messaging.invalidArguments(sender, args[0], "is not an online player");
                return true;
            }
        }
        if (args.length < 2) {
            Messaging.genericError(sender, "Missing query argument");
            return true;
        }

        String query = args[1].toLowerCase();
        Object result = "?";

        switch (args[1].toLowerCase()) {

            case "canfly":
            case "mayfly":
            case "fly":
                result = affectedPlayer.getAllowFlight();
                break;
            case "gamemode":
            case "gm":
                result = affectedPlayer.getGameMode();
                break;
            case "godmode":
            case "god":
                result = affectedPlayer.hasMetadata("GodMode") && affectedPlayer.getMetadata("GodMode").get(0).asBoolean();
                break;
            case "location":
            case "loc":
                Location l = affectedPlayer.getLocation();
                l.setX(l.getBlockX());
                l.setY(l.getBlockY());
                l.setZ(l.getBlockZ());
                l.setYaw(Math.round(l.getYaw()));
                l.setPitch(Math.round(l.getPitch()));
                result = l;
                break;
            case "world":
                result = affectedPlayer.getWorld().getName();
                break;
            case "flyspeed":
            case "flightspeed":
                result = affectedPlayer.getFlySpeed();
                break;
            case "walkspeed":
                result = affectedPlayer.getWalkSpeed();
                break;
            case "hp":
            case "health":
                result = affectedPlayer.getHealth();
                break;
            case "saturation":
                result = affectedPlayer.getSaturation();
                break;
            case "activepotioneffects":
            case "potioneffects":
                result = affectedPlayer.getActivePotionEffects();
                break;
            case "scoreboardtags":
            case "tags":
                result = affectedPlayer.getScoreboardTags();
                break;
        }

        sender.sendMessage("Player: " + affectedPlayer.getName() + ", Query: \"" + query + "\", Result: " + result);

        return true;
    }
}

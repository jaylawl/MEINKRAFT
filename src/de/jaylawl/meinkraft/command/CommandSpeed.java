package de.jaylawl.meinkraft.command;

import de.jaylawl.meinkraft.command.util.TabCompleteUtil;
import de.jaylawl.meinkraft.command.util.TabHelper;
import de.jaylawl.meinkraft.util.MessagingUtil;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class CommandSpeed implements MeinkraftCommand {

    public CommandSpeed() {
    }

    //

    @Override
    public @NotNull String getBasePermissionNode() {
        return "mk.speed";
    }

    //

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String label, @NotNull String[] arguments) {

        final boolean permissionSelf = hasSelfPermission(commandSender);
        final boolean permissionOthers = hasOthersPermission(commandSender);

        if (!permissionSelf && !permissionOthers) {
            return Collections.emptyList();
        }

        List<String> completions = new ArrayList<>();
        int argumentNumber = TabHelper.getArgumentNumber(arguments);

        switch (argumentNumber) {
            case 1 -> {
                completions.add("flight");
                completions.add("walk");
            }
            case 2 -> {
                completions.add("[value or \"reset\"]");
            }
            case 3 -> {
                if (permissionOthers) {
                    completions.addAll(TabCompleteUtil.getOnlinePlayerNames());
                } else {
                    if (commandSender instanceof Player player) {
                        completions.add(player.getName());
                    }
                }
            }
            default -> {
                return Collections.emptyList();
            }
        }

        return TabHelper.sortedCompletions(arguments[argumentNumber - 1], completions);

    }

    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String label, @NotNull String[] arguments) {

        final boolean permissionSelf = hasSelfPermission(commandSender);
        final boolean permissionOthers = hasOthersPermission(commandSender);

        if (!permissionSelf && !permissionOthers) {
            MessagingUtil.noPermission(commandSender);
            return true;
        }

        Player targetPlayer;

        if (arguments.length < 1) {
            commandSender.sendMessage(ChatColor.GREEN + "/" + label + ChatColor.RED + " <flight/walk>" + ChatColor.GRAY + " <value> [player]");
            return true;
        } else if (arguments.length < 2) {
            commandSender.sendMessage(ChatColor.GREEN + "/" + label + " " + arguments[0] + ChatColor.RED + " <value>" + ChatColor.GRAY + " [player]");
            return true;
        } else if (arguments.length < 3) {
            if (commandSender instanceof Player) {
                targetPlayer = (Player) commandSender;
            } else {
                MessagingUtil.genericError(commandSender, "Missing player argument");
                return true;
            }
        } else {
            targetPlayer = Bukkit.getPlayer(arguments[2]);
            if (targetPlayer == null) {
                MessagingUtil.invalidArguments(commandSender, arguments[2], "is not an online player");
                return true;
            }
        }

        boolean targetEqualsSender = targetPlayer == commandSender;
        if (targetEqualsSender) {
            if (!permissionSelf) {
                MessagingUtil.noPermission(commandSender);
                return true;
            }
        } else {
            if (!permissionOthers) {
                MessagingUtil.noPermissionOthers(commandSender);
                return true;
            }
        }

        String speedType = arguments[0].toLowerCase();
        if (!speedType.equals("flight") && !speedType.equals("walk")) {
            MessagingUtil.invalidArguments(commandSender, arguments[0], "is not a valid speed type; must be \"flight\" or \"walk\"");
            return true;
        }

        float speedValue;
        if (arguments[1].equalsIgnoreCase("reset") || arguments[1].equalsIgnoreCase("r")) {
            speedValue = speedType.equals("flight") ? 0.1f : 0.2f;
        } else if (arguments[1].matches("\\d*.*\\d*")) {
            speedValue = Float.parseFloat(arguments[1]);
        } else {
            MessagingUtil.invalidArguments(commandSender, arguments[1], "unknown value; must be a number or \"reset\"");
            return true;
        }
        speedValue = Math.min(1f, speedValue);
        speedValue = Math.max(-1f, speedValue);

        if (speedType.equals("flight")) {
            targetPlayer.setFlySpeed(speedValue);
        } else {
            targetPlayer.setWalkSpeed(speedValue);
        }

        MessagingUtil.notifyExecutor(commandSender, "Set " + speedType + " speed of " + targetPlayer.getName() + " to " + speedValue);
        if (!targetEqualsSender) {
            MessagingUtil.notifyTargetPlayer(targetPlayer, "A wizard has set your " + speedType + " speed to " + speedValue);
        }

        return true;
    }

}

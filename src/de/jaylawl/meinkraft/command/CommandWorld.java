package de.jaylawl.meinkraft.command;

import de.jaylawl.meinkraft.command.util.TabCompleteUtil;
import de.jaylawl.meinkraft.command.util.TabHelper;
import de.jaylawl.meinkraft.util.MessagingUtil;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class CommandWorld implements MeinkraftCommand {

    public CommandWorld() {
    }

    //

    @Override
    public @NotNull String getBasePermissionNode() {
        return "mk.world";
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
                completions.addAll(TabCompleteUtil.getLoadedWorldNames());
            }

            case 2 -> {
                if (permissionOthers) {
                    completions.addAll(TabCompleteUtil.getOnlinePlayerNames());
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

        World targetWorld;

        if (arguments.length == 0) {
            commandSender.sendMessage(ChatColor.GREEN + "/world" + ChatColor.RED + " <world>" + ChatColor.GRAY + " [player]");
            return true;
        } else {
            targetWorld = Bukkit.getWorld(arguments[0]);
            if (targetWorld == null) {
                MessagingUtil.invalidArguments(commandSender, arguments[0], "does not match any loaded world");
                return true;
            }
        }

        Player targetPlayer;

        if (arguments.length == 1) {
            if (commandSender instanceof Player) {
                targetPlayer = (Player) commandSender;
            } else {
                MessagingUtil.genericError(commandSender, "Missing player argument");
                return true;
            }
        } else {
            targetPlayer = Bukkit.getPlayer(arguments[1]);
            if (targetPlayer == null) {
                MessagingUtil.invalidArguments(commandSender, arguments[1], "is not an online player");
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

        targetPlayer.teleport(targetWorld.getSpawnLocation());
        MessagingUtil.notifyExecutor(commandSender, "Sent player " + targetPlayer.getName() + " to world \"" + targetWorld.getName() + "\"");
        if (!targetEqualsSender) {
            MessagingUtil.notifyTargetPlayer(targetPlayer, "A wizard has poofed you into another world");
        }

        return true;
    }

}

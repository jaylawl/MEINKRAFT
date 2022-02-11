package de.jaylawl.meinkraft.command;

import de.jaylawl.meinkraft.command.util.TabCompleteUtil;
import de.jaylawl.meinkraft.command.util.TabHelper;
import de.jaylawl.meinkraft.util.MessagingUtil;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class CommandFly implements MeinkraftCommand {

    public static final String PERMISSION_NODE = "mk.fly";
    public static final String PERMISSION_NODE_SELF = "mk.fly.self";
    public static final String PERMISSION_NODE_OTHERS = "mk.fly.others";

    public CommandFly() {
    }

    //

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String label, @NotNull String[] arguments) {

        boolean permissionSelf = commandSender.hasPermission(PERMISSION_NODE_SELF);
        boolean permissionOthers = commandSender.hasPermission(PERMISSION_NODE_OTHERS);

        if (!permissionSelf && !permissionOthers) {
            return Collections.emptyList();
        }

        List<String> completions = new ArrayList<>();
        int argumentNumber = TabHelper.getArgumentNumber(arguments);

        if (argumentNumber == 1) {
            completions.addAll(TabCompleteUtil.getOnlinePlayerNames());
            return TabHelper.sortedCompletions(arguments[argumentNumber - 1], completions);

        } else {
            return Collections.emptyList();

        }

    }

    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String label, @NotNull String[] arguments) {

        boolean permissionSelf = commandSender.hasPermission(PERMISSION_NODE_SELF);
        boolean permissionOthers = commandSender.hasPermission(PERMISSION_NODE_OTHERS);

        if (!permissionSelf && !permissionOthers) {
            MessagingUtil.noPermission(commandSender);
            return true;
        }

        Player targetPlayer;

        if (arguments.length == 0) {
            if (commandSender instanceof Player) {
                targetPlayer = (Player) commandSender;
            } else {
                MessagingUtil.genericError(commandSender, "Missing player argument");
                return true;
            }
        } else {
            targetPlayer = Bukkit.getPlayer(arguments[0]);
            if (targetPlayer == null) {
                MessagingUtil.invalidArguments(commandSender, arguments[0], "is not an online player");
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

        boolean allowFlight = !targetPlayer.getAllowFlight();
        targetPlayer.setAllowFlight(allowFlight);

        MessagingUtil.notifyExecutor(commandSender, "Set flight mode of " + targetPlayer.getName() + " to " + allowFlight);
        if (!targetEqualsSender) {
            MessagingUtil.notifyTargetPlayer(targetPlayer, allowFlight ? "A wizard has granted you flight powers" : "A wizard has clipped your wings");
        }

        return true;
    }

    @Override
    public @NotNull String getBasePermissionNode() {
        return PERMISSION_NODE;
    }

}

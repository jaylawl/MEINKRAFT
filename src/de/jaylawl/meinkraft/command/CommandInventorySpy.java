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
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class CommandInventorySpy implements MeinkraftCommand {

    public CommandInventorySpy() {
    }

    //

    @Override
    public @NotNull String getBasePermissionNode() {
        return "mk.inventoryspy";
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
                completions.addAll(TabCompleteUtil.getOnlinePlayerNames());
            }

            case 2 -> {
                completions = Arrays.asList("inventory", "enderchest");
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

        if (!(commandSender instanceof Player executingPlayer)) {
            MessagingUtil.ingameExclusive(commandSender);
            return true;
        }

        Player targetPlayer;

        if (arguments.length == 0) {
            targetPlayer = (Player) commandSender;
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

        String targetInventory = arguments.length > 1 ? arguments[1].toLowerCase() : "inventory";
        switch (targetInventory) {
            case "inventory" -> {
                executingPlayer.openInventory(targetPlayer.getInventory());
            }
            case "enderchest" -> {
                executingPlayer.openInventory(targetPlayer.getEnderChest());
            }
            default -> {
                MessagingUtil.invalidArguments(commandSender, targetInventory, "is not a valid target inventory type");
                return true;
            }
        }

        return true;
    }

}

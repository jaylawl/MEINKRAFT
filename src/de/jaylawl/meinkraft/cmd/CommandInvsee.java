package de.jaylawl.meinkraft.cmd;

import de.jaylawl.meinkraft.util.CmdPermission;
import de.jaylawl.meinkraft.util.MessagingUtil;
import de.jaylawl.meinkraft.util.TabHelper;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class CommandInvsee implements CommandMeinkraft {

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String label, @NotNull String[] arguments) {

        if (!CmdPermission.hasAny(commandSender, label)) {
            return Collections.emptyList();
        }

        int argN = TabHelper.getArgumentNumber(arguments);
        List<String> completions = new ArrayList<>();

        switch (argN) {
            case 1:
                for (Player p : Bukkit.getOnlinePlayers()) {
                    completions.add(p.getName());
                }
                break;
            case 2:
                completions = Arrays.asList("inventory", "enderchest");
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
        } else if (commandSender instanceof ConsoleCommandSender) {
            MessagingUtil.ingameExclusive(commandSender);
            return true;
        }

        Player player = (Player) commandSender;
        Player affectedPlayer;

        if (arguments.length == 0) {
            affectedPlayer = player;
        } else {
            affectedPlayer = Bukkit.getPlayer(arguments[0]);
            if (affectedPlayer == null) {
                MessagingUtil.invalidArguments(commandSender, arguments[0], "is not an online player");
                return true;
            }
        }

        switch (arguments.length > 1 ? arguments[1].toLowerCase() : "") {

            case "", "inventory", "inv", "i" -> {
                player.openInventory(affectedPlayer.getInventory());
            }

            case "enderchest", "endchest", "echest", "e" -> {
                player.openInventory(affectedPlayer.getEnderChest());
            }

            default -> {
                MessagingUtil.invalidArguments(commandSender, arguments[1], "is not a valid inventory type");
                return true;
            }

        }

        return true;
    }

}

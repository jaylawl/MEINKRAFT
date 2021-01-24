package de.jaylawl.meinkraft.cmd;

import de.jaylawl.meinkraft.util.CmdPermission;
import de.jaylawl.meinkraft.util.MessagingUtil;
import de.jaylawl.meinkraft.util.TabHelper;
import org.bukkit.Bukkit;
import org.bukkit.command.*;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class CmdInvsee implements CommandExecutor, TabCompleter {

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
            case "":
            case "inventory":
            case "inv":
            case "i": {
                player.openInventory(affectedPlayer.getInventory());
                break;
            }
            case "enderchest":
            case "endchest":
            case "echest":
            case "e": {
                player.openInventory(affectedPlayer.getEnderChest());
                break;
            }
            default: {
                MessagingUtil.invalidArguments(commandSender, arguments[1], "is not a valid inventory type");
                return true;
            }
        }

        return true;
    }

}

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
                completions = Arrays.asList("inventory", "enderchest");
                break;
            default:
                return Collections.emptyList();
        }

        return TabHelper.sortedCompletions(args[argN - 1], completions);
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String label, @NotNull String[] args) {

        if (!CmdPermission.hasAny(sender, label)) {
            MessagingUtil.noPermission(sender);
            return true;
        } else if (sender instanceof ConsoleCommandSender) {
            MessagingUtil.ingameExclusive(sender);
            return true;
        }

        Player player = (Player) sender;
        Player affectedPlayer;

        if (args.length == 0) {
            affectedPlayer = player;
        } else {
            affectedPlayer = Bukkit.getPlayer(args[0]);
            if (affectedPlayer == null) {
                MessagingUtil.invalidArguments(sender, args[0], "is not an online player");
                return true;
            }
        }

        switch (args.length > 1 ? args[1].toLowerCase() : "") {
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
                MessagingUtil.invalidArguments(sender, args[1], "is not a valid inventory type");
                return true;
            }
        }

        return true;
    }

}

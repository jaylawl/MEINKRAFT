package de.jaylawl.meinkraft.cmd;

import de.jaylawl.meinkraft.util.CmdPermission;
import de.jaylawl.meinkraft.util.Messaging;
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
            Messaging.noPermission(sender);
            return true;
        } else if (sender instanceof ConsoleCommandSender) {
            Messaging.ingameExclusive(sender);
            return true;
        }

        Player player = (Player) sender;
        Player affectedPlayer;
        int inventoryType;

        if (args.length == 0) {
            affectedPlayer = player;
        } else {
            affectedPlayer = Bukkit.getPlayer(args[0]);
            if (affectedPlayer == null) {
                Messaging.invalidArguments(sender, args[0], "is not an online player");
                return true;
            }
        }

        if (args.length < 1) {
            inventoryType = 0;
        } else {
            switch (args[1]) {
                case "inventory":
                case "inv":
                case "i":
                    inventoryType = 0;
                    break;
                case "enderchest":
                case "endchest":
                case "echest":
                case "e":
                    inventoryType = 1;
                    break;
                default:
                    Messaging.invalidArguments(sender, args[1], "unknown inventory type");
                    return true;
            }
        }

        if (inventoryType == 0) {
            player.openInventory(affectedPlayer.getInventory());
        } else {
            player.openInventory(affectedPlayer.getEnderChest());
        }

        return true;
    }

}

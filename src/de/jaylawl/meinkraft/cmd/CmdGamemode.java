package de.jaylawl.meinkraft.cmd;

import de.jaylawl.meinkraft.util.CmdPermission;
import de.jaylawl.meinkraft.util.MessagingUtil;
import de.jaylawl.meinkraft.util.TabHelper;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
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

public class CmdGamemode implements CommandExecutor, TabCompleter {

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String label, @NotNull String[] arguments) {

        if (!CmdPermission.hasAny(commandSender, label)) {
            return Collections.emptyList();
        }

        int argumentNumber = TabHelper.getArgumentNumber(arguments);
        List<String> completions = new ArrayList<>();

        switch (argumentNumber) {
            case 1:
                for (GameMode gm : GameMode.values()) {
                    completions.add(gm.toString().toLowerCase());
                }
                completions.addAll(Arrays.asList("0", "1", "2", "3"));
                break;
            case 2:
                for (Player p : Bukkit.getOnlinePlayers()) {
                    completions.add(p.getName());
                }
                break;
            default:
                return Collections.emptyList();
        }

        return TabHelper.sortedCompletions(arguments[argumentNumber - 1], completions);
    }

    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String label, @NotNull String[] arguments) {

        if (!CmdPermission.hasAny(commandSender, label)) {
            MessagingUtil.noPermission(commandSender);
            return true;
        }

        GameMode gameMode = null;
        Player affectedPlayer;

        if (arguments.length == 0) {
            commandSender.sendMessage(ChatColor.GREEN + "/gm " + ChatColor.RED + "<gamemode> " + ChatColor.GRAY + "[player]");
            return true;
        } else {
            if (arguments[0].matches("(\\d+)")) {
                switch (Integer.parseInt(arguments[0])) {
                    case 0:
                        gameMode = GameMode.SURVIVAL;
                        break;
                    case 1:
                        gameMode = GameMode.CREATIVE;
                        break;
                    case 2:
                        gameMode = GameMode.ADVENTURE;
                        break;
                    case 3:
                        gameMode = GameMode.SPECTATOR;
                        break;
                    default:
                        MessagingUtil.genericError(commandSender, "Numeric gamemode value must be between 0 and 3 inclusive");
                        return true;
                }
            } else {
                for (GameMode gm : GameMode.values()) {
                    if (gm.toString().equals(arguments[0].toUpperCase())) {
                        gameMode = gm;
                        break;
                    }
                }
                if (gameMode == null) {
                    MessagingUtil.invalidArguments(commandSender, arguments[0], "is not a valid gamemode");
                    return true;
                }
            }
        }

        if (arguments.length > 1) {
            affectedPlayer = Bukkit.getPlayer(arguments[1]);
            if (affectedPlayer == null) {
                MessagingUtil.invalidArguments(commandSender, arguments[1], "is not an online player");
                return true;
            }
        } else {
            if (commandSender instanceof Player) {
                affectedPlayer = (Player) commandSender;
            } else {
                MessagingUtil.genericError(commandSender, "Missing player argument");
                return true;
            }
        }

        boolean senderEqualsAffected = commandSender == affectedPlayer;
        if (!senderEqualsAffected) {
            if (!CmdPermission.hasOthers(commandSender, label)) {
                MessagingUtil.noPermissionOthers(commandSender);
                return true;
            }
        }

        affectedPlayer.setGameMode(gameMode);
        String formattedGameMode = affectedPlayer.getGameMode().toString().toLowerCase();
        MessagingUtil.feedback(commandSender, "Set game mode of " + affectedPlayer.getName() + " to " + formattedGameMode);
        if (!senderEqualsAffected) {
            MessagingUtil.notifyPlayer(affectedPlayer, "A wizard set your game mode to " + formattedGameMode);
        }

        return true;
    }
}

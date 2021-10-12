package de.jaylawl.meinkraft.cmd;

import de.jaylawl.meinkraft.util.MessagingUtil;
import de.jaylawl.meinkraft.util.TabHelper;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class CommandGamemode implements CommandMeinkraft {

    public static final String PERMISSION_NODE = "mk.gamemode";
    public static final String PERMISSION_NODE_SELF = "mk.gamemode.self";

    public CommandGamemode() {
    }

    //

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String label, @NotNull String[] arguments) {

        boolean permissionAll = commandSender.hasPermission(CommandMaster.PERMISSION_NODE) || commandSender.hasPermission(PERMISSION_NODE);
        boolean permissionSelf = commandSender.hasPermission(PERMISSION_NODE_SELF);

        if (!permissionAll && !permissionSelf) {
            return Collections.emptyList();
        }

        List<String> completions = new ArrayList<>();
        int argumentNumber = TabHelper.getArgumentNumber(arguments);

        switch (argumentNumber) {

            case 1 -> {
                for (GameMode gameMode : GameMode.values()) {
                    completions.add(gameMode.toString().toLowerCase());
                }
                completions.addAll(Arrays.asList("0", "1", "2", "3"));
            }

            case 2 -> {
                if (permissionAll) {
                    for (Player player : Bukkit.getOnlinePlayers()) {
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

        boolean permissionAll = commandSender.hasPermission(CommandMaster.PERMISSION_NODE) || commandSender.hasPermission(PERMISSION_NODE);
        boolean permissionSelf = commandSender.hasPermission(PERMISSION_NODE_SELF);

        if (!permissionAll && !permissionSelf) {
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
                    case 0 -> gameMode = GameMode.SURVIVAL;
                    case 1 -> gameMode = GameMode.CREATIVE;
                    case 2 -> gameMode = GameMode.ADVENTURE;
                    case 3 -> gameMode = GameMode.SPECTATOR;
                    default -> {
                        MessagingUtil.genericError(commandSender, "Numeric gamemode value must be between 0 and 3 inclusive");
                        return true;
                    }
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
            if (!permissionAll) {
                MessagingUtil.noPermissionOthers(commandSender);
                return true;
            }
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

        affectedPlayer.setGameMode(gameMode);
        String formattedGameMode = affectedPlayer.getGameMode().toString().toLowerCase();
        MessagingUtil.notifyExecutor(commandSender, "Set game mode of " + affectedPlayer.getName() + " to " + formattedGameMode);
        if (!senderEqualsAffected) {
            MessagingUtil.notifyPlayer(affectedPlayer, "A wizard set your game mode to " + formattedGameMode);
        }

        return true;
    }
}

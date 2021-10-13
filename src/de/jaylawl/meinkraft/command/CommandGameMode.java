package de.jaylawl.meinkraft.command;

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

public class CommandGameMode implements MeinkraftCommand {

    public static final String PERMISSION_NODE = "mk.gamemode";
    public static final String PERMISSION_NODE_SELF = "mk.gamemode.self";
    public static final String PERMISSION_NODE_OTHERS = "mk.gamemode.others";

    public CommandGameMode() {
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

        switch (argumentNumber) {

            case 1 -> {
                for (GameMode gameMode : GameMode.values()) {
                    completions.add(gameMode.toString().toLowerCase());
                }
                completions.addAll(Arrays.asList("0", "1", "2", "3"));
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

        boolean permissionSelf = commandSender.hasPermission(PERMISSION_NODE_SELF);
        boolean permissionOthers = commandSender.hasPermission(PERMISSION_NODE_OTHERS);

        if (!permissionSelf && !permissionOthers) {
            MessagingUtil.noPermission(commandSender);
            return true;
        }

        GameMode targetGameMode = null;

        if (arguments.length == 0) {
            commandSender.sendMessage(ChatColor.GREEN + "/gm" + ChatColor.RED + " <gamemode>" + ChatColor.GRAY + " [player]");
            return true;
        } else {
            if (arguments[0].matches("(\\d+)")) {
                switch (Integer.parseInt(arguments[0])) {
                    case 0 -> targetGameMode = GameMode.SURVIVAL;
                    case 1 -> targetGameMode = GameMode.CREATIVE;
                    case 2 -> targetGameMode = GameMode.ADVENTURE;
                    case 3 -> targetGameMode = GameMode.SPECTATOR;
                    default -> {
                        MessagingUtil.genericError(commandSender, "Numeric gamemode value must be between 0 and 3 inclusive");
                        return true;
                    }
                }
            } else {
                for (GameMode gameMode : GameMode.values()) {
                    if (gameMode.toString().equals(arguments[0].toUpperCase())) {
                        targetGameMode = gameMode;
                        break;
                    }
                }
                if (targetGameMode == null) {
                    MessagingUtil.invalidArguments(commandSender, arguments[0], "is not a valid gamemode");
                    return true;
                }
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

        targetPlayer.setGameMode(targetGameMode);
        String formattedGameMode = targetPlayer.getGameMode().toString().toLowerCase();
        MessagingUtil.notifyExecutor(commandSender, "Set game mode of " + targetPlayer.getName() + " to " + formattedGameMode);
        if (!targetEqualsSender) {
            MessagingUtil.notifyTargetPlayer(targetPlayer, "A wizard set your game mode to " + formattedGameMode);
        }

        return true;
    }
}

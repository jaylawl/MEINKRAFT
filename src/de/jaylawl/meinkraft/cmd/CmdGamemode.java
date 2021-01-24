package de.jaylawl.meinkraft.cmd;

import de.jaylawl.meinkraft.util.CmdPermission;
import de.jaylawl.meinkraft.util.MessagingUtil;
import de.jaylawl.meinkraft.util.TabHelper;
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
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String label, @NotNull String[] args) {

        if (!CmdPermission.hasAny(sender, label)) {
            return Collections.emptyList();
        }

        int argN = TabHelper.getArgNumber(args);
        List<String> completions = new ArrayList<>();

        switch (argN) {
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

        return TabHelper.sortedCompletions(args[argN - 1], completions);

    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String label, @NotNull String[] args) {

        if (!CmdPermission.hasAny(sender, label)) {
            MessagingUtil.noPermission(sender);
            return true;
        }

        GameMode gameMode = null;
        Player affectedPlayer;
        boolean senderEqualsAffected = false;

        if (args.length == 0) {
            sender.sendMessage("§a/gm §c<gamemode> §7[player]");
            return true;
        } else {
            if (args[0].matches("(\\d+)")) {
                switch (Integer.parseInt(args[0])) {
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
                        MessagingUtil.genericError(sender, "Numeric gamemode value must be between 0 and 3");
                        return true;
                }
            } else {
                for (GameMode gm : GameMode.values()) {
                    if (gm.toString().equals(args[0].toUpperCase())) {
                        gameMode = gm;
                        break;
                    }
                }
                if (gameMode == null) {
                    MessagingUtil.invalidArguments(sender, args[0], "is not a valid gamemode");
                    return true;
                }
            }
        }

        if (args.length > 1) {
            affectedPlayer = Bukkit.getPlayer(args[1]);
            if (affectedPlayer == null) {
                MessagingUtil.invalidArguments(sender, args[1], "is not an online player");
                return true;
            }
        } else {
            if (sender instanceof Player) {
                affectedPlayer = (Player) sender;
            } else {
                MessagingUtil.genericError(sender, "§cMissing player argument");
                return true;
            }
        }

        if (sender instanceof Player && sender == affectedPlayer) {
            senderEqualsAffected = true;
            if (!CmdPermission.hasOthers(sender, label)) {
                MessagingUtil.noPermissionOthers(sender);
                return true;
            }
        }

        affectedPlayer.setGameMode(gameMode);
        String formattedGameMode = affectedPlayer.getGameMode().toString().toLowerCase();
        MessagingUtil.feedback(sender, "Set game mode of " + affectedPlayer.getName() + " to " + formattedGameMode);
        if (!senderEqualsAffected) {
            MessagingUtil.notifyPlayer(affectedPlayer, "A wizard set your game mode to " + formattedGameMode);
        }

        return true;
    }
}

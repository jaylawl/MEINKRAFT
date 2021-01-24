package de.jaylawl.meinkraft.cmd;

import de.jaylawl.meinkraft.util.CmdPermission;
import de.jaylawl.meinkraft.util.MessagingUtil;
import de.jaylawl.meinkraft.util.TabHelper;
import org.bukkit.Bukkit;
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

public class CmdSpeed implements CommandExecutor, TabCompleter {

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String label, @NotNull String[] args) {

        if (!CmdPermission.hasAny(sender, label)) {
            return Collections.emptyList();
        }

        int argN = TabHelper.getArgNumber(args);
        List<String> completions = new ArrayList<>();

        switch (argN) {
            case 1:
                completions = Arrays.asList("flight", "walk");
                break;
            case 2:
                completions = Collections.singletonList("[double or \"reset\"]");
                break;
            case 3:
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

        Player affectedPlayer;
        boolean senderEqualsAffected = false;

        if (args.length < 1) {
            MessagingUtil.genericError(sender, "Missing type argument");
            return true;
        } else if (args.length < 2) {
            MessagingUtil.genericError(sender, "Missing value argument");
            return true;
        } else if (args.length < 3) {
            if (sender instanceof Player) {
                affectedPlayer = (Player) sender;
            } else {
                MessagingUtil.genericError(sender, "Missing player argument");
                return true;
            }
        } else {
            affectedPlayer = Bukkit.getPlayer(args[2]);
            if (affectedPlayer == null) {
                MessagingUtil.invalidArguments(sender, args[2], "is not an online player");
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

        String type;
        switch (args[0].toLowerCase()) {
            case "flight":
            case "flying":
            case "fly":
            case "f":
                type = "flight";
                break;
            case "run":
            case "walking":
            case "walk":
            case "w":
                type = "walk";
                break;
            default:
                MessagingUtil.invalidArguments(sender, args[0], "unknown type; must be \"flight\" or \"walk\"");
                return true;
        }

        float value;
        if (args[1].toLowerCase().equals("reset") || args[1].toLowerCase().equals("r")) {
            value = type.equals("flight") ? 0.1f : 0.2f;
        } else if (args[1].matches("\\d*.*\\d*")) {
            value = Float.parseFloat(args[1]);
        } else {
            MessagingUtil.invalidArguments(sender, args[1], "unknown value; must be a number or \"reset\"");
            return true;
        }
        value = Math.min(1f, value);
        value = Math.max(-1f, value);

        if (type.equals("flight")) {
            affectedPlayer.setFlySpeed(value);
        } else {
            affectedPlayer.setWalkSpeed(value);
        }
        MessagingUtil.feedback(sender, "Set " + type + " speed of " + affectedPlayer.getName() + " to " + value);
        if (!senderEqualsAffected) {
            MessagingUtil.notifyPlayer(affectedPlayer, "A wizard has set your " + type + " speed to " + value);
        }

        return true;
    }




}

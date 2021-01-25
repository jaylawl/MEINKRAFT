package de.jaylawl.meinkraft.cmd;

import de.jaylawl.meinkraft.util.CmdPermission;
import de.jaylawl.meinkraft.util.MessagingUtil;
import de.jaylawl.meinkraft.util.TabHelper;
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

public class CmdSpeed implements CmdMeinkraft {

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String label, @NotNull String[] arguments) {

        if (!CmdPermission.hasAny(commandSender, label)) {
            return Collections.emptyList();
        }

        int argN = TabHelper.getArgumentNumber(arguments);
        List<String> completions = new ArrayList<>();

        switch (argN) {
            case 1:
                completions = Arrays.asList("flight", "walk");
                break;
            case 2:
                completions = Collections.singletonList("[value or \"reset\"]");
                break;
            case 3:
                for (Player p : Bukkit.getOnlinePlayers()) {
                    completions.add(p.getName());
                }
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
        }

        Player affectedPlayer;

        if (arguments.length < 1) {
            MessagingUtil.genericError(commandSender, "Missing type argument");
            return true;
        } else if (arguments.length < 2) {
            MessagingUtil.genericError(commandSender, "Missing value argument");
            return true;
        } else if (arguments.length < 3) {
            if (commandSender instanceof Player) {
                affectedPlayer = (Player) commandSender;
            } else {
                MessagingUtil.genericError(commandSender, "Missing player argument");
                return true;
            }
        } else {
            affectedPlayer = Bukkit.getPlayer(arguments[2]);
            if (affectedPlayer == null) {
                MessagingUtil.invalidArguments(commandSender, arguments[2], "is not an online player");
                return true;
            }
        }

        boolean senderEqualsAffected = commandSender == affectedPlayer;
        if (commandSender != affectedPlayer) {
            if (!CmdPermission.hasOthers(commandSender, label)) {
                MessagingUtil.noPermissionOthers(commandSender);
                return true;
            }
        }

        String type;
        switch (arguments[0].toLowerCase()) {
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
                MessagingUtil.invalidArguments(commandSender, arguments[0], "unknown type; must be \"flight\" or \"walk\"");
                return true;
        }

        float value;
        if (arguments[1].toLowerCase().equals("reset") || arguments[1].toLowerCase().equals("r")) {
            value = type.equals("flight") ? 0.1f : 0.2f;
        } else if (arguments[1].matches("\\d*.*\\d*")) {
            value = Float.parseFloat(arguments[1]);
        } else {
            MessagingUtil.invalidArguments(commandSender, arguments[1], "unknown value; must be a number or \"reset\"");
            return true;
        }
        value = Math.min(1f, value);
        value = Math.max(-1f, value);

        if (type.equals("flight")) {
            affectedPlayer.setFlySpeed(value);
        } else {
            affectedPlayer.setWalkSpeed(value);
        }
        MessagingUtil.feedback(commandSender, "Set " + type + " speed of " + affectedPlayer.getName() + " to " + value);
        if (!senderEqualsAffected) {
            MessagingUtil.notifyPlayer(affectedPlayer, "A wizard has set your " + type + " speed to " + value);
        }

        return true;
    }


}

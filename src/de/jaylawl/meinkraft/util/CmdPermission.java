package de.jaylawl.meinkraft.util;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CmdPermission {

    public static boolean hasAny(CommandSender commandSender, String cmd) {
        if (
                commandSender instanceof Player &&
                !commandSender.hasPermission("mk.admin") &&
                !commandSender.isOp()
        ) {
            return hasSelf(commandSender, cmd) || hasOthers(commandSender, cmd);
        }
        return true;
    }

    public static boolean hasSelf(CommandSender sender, String cmd) {
        return sender.hasPermission("mk." + cmd + ".self");
    }

    public static boolean hasOthers(CommandSender sender, String cmd) {
        return sender.hasPermission("mk." + cmd);
    }

}

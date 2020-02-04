package de.jaylawl.meinkraft.util;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CmdPermission {

    public static boolean hasAny(CommandSender sender, String cmd) {
        if (
                sender instanceof Player &&
                !sender.hasPermission("mk.admin") &&
                !sender.isOp()
        ) {
            return hasSelf(sender, cmd) || hasOthers(sender, cmd);
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

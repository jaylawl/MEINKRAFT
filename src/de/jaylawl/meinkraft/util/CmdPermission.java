package de.jaylawl.meinkraft.util;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CmdPermission {

    public static boolean hasAny(CommandSender sender, String node) {
        if (
                sender instanceof Player &&
                !sender.hasPermission("mk.admin") &&
                !sender.isOp()
        ) {
            return hasSelf(sender, node) || hasOthers(sender, node);
        }
        return true;
    }

    public static boolean hasSelf(CommandSender sender, String node) {
        return sender.hasPermission("mk." + node);
    }

    public static boolean hasOthers(CommandSender sender, String node) {
        return sender.hasPermission("mk." + node + ".others");
    }

}

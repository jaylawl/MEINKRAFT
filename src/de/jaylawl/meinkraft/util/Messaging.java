package de.jaylawl.meinkraft.util;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class Messaging {

    private static String pluginColor = "§4";
    private static String pluginTag = "§7[" + pluginColor + "MK§7]§r";

    public static void noPermission(CommandSender sender) {
        sender.sendMessage("§cInsufficient permission");
    }

    public static void noPermissionOthers(CommandSender sender) {
        sender.sendMessage("§cInsufficient permission for use on other players");
    }

    public static void ingameExclusive(CommandSender sender) {
        sender.sendMessage("§cIngame exclusive command");
    }

    public static void genericError(CommandSender sender, String msg) {
        sender.sendMessage("§c" + msg);
    }

    public static void invalidArguments(CommandSender sender, String arg, String msg) {
        sender.sendMessage("§c\"" + arg + "\" " + msg);
    }

    public static void feedback(CommandSender sender, String msg) {
        sender.sendMessage("§r" + msg);
    }

    public static void notifyPlayer(Player p, String msg) {
        p.sendMessage("§7§o" + msg);
    }

    public static String getPluginColor() {
        return pluginColor;
    }

    public static String getPluginTag() {
        return pluginTag;
    }
}


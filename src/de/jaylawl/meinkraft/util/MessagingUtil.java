package de.jaylawl.meinkraft.util;

import net.md_5.bungee.api.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class MessagingUtil {

    private final static String PLUGIN_COLOR = "§4";
    private final static String PLUGIN_TAG = "§7[" + PLUGIN_COLOR + "MK§7]§r";
    
    //

    public static void noPermission(@NotNull CommandSender sender) {
        sender.sendMessage(ChatColor.RED + "Insufficient permission");
    }

    public static void noPermissionOthers(@NotNull CommandSender sender) {
        sender.sendMessage("§cInsufficient permission for use on other players");
    }

    public static void ingameExclusive(@NotNull CommandSender sender) {
        sender.sendMessage(ChatColor.RED + "Ingame exclusive command");
    }

    public static void genericError(@NotNull CommandSender sender, @NotNull String message) {
        sender.sendMessage(ChatColor.RED + message);
    }

    public static void invalidArguments(@NotNull CommandSender sender, @NotNull String argument, @NotNull String message) {
        sender.sendMessage(ChatColor.RED + "\"" + argument + "\" " + message);
    }

    public static void notifyExecutor(@NotNull CommandSender sender, @NotNull String message) {
        sender.sendMessage(ChatColor.RESET + message);
    }

    public static void notifyPlayer(@NotNull Player player, @NotNull String message) {
        player.sendMessage(ChatColor.GRAY + "" + ChatColor.ITALIC + message);
    }

    public static String getPluginColor() {
        return PLUGIN_COLOR;
    }

    public static String getPluginTag() {
        return PLUGIN_TAG;
    }

}


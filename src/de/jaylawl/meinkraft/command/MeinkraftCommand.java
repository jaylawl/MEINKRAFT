package de.jaylawl.meinkraft.command;

import de.jaylawl.meinkraft.util.MessagingUtil;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.event.Listener;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

public interface MeinkraftCommand extends CommandExecutor, TabCompleter {

    //

    @NotNull String getBasePermissionNode();

    default @NotNull String getSelfPermissionNode() {
        return getBasePermissionNode() + ".self";
    }

    default @NotNull String getOthersPermissionNode() {
        return getBasePermissionNode() + ".others";
    }

    default boolean hasBasePermission(@NotNull CommandSender commandSender) {
        return commandSender.hasPermission(getBasePermissionNode());
    }

    default boolean hasSelfPermission(@NotNull CommandSender commandSender) {
        return commandSender.hasPermission(getSelfPermissionNode());
    }

    default boolean hasOthersPermission(@NotNull CommandSender commandSender) {
        return commandSender.hasPermission(getOthersPermissionNode());
    }

    default @NotNull String getNoPermissionMessage() {
        return MessagingUtil.NO_PERMISSION_MESSAGE;
    }

    default boolean requiresListeners() {
        return false;
    }

    default @NotNull Collection<Listener> getRequiredListenerClasses() {
        return Collections.emptyList();
    }

    //

    default @Nullable List<String> onTabComplete(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String label, @NotNull String[] arguments) {
        return Collections.emptyList();
    }

    default boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String label, @NotNull String[] arguments) {
        return false;
    }

}

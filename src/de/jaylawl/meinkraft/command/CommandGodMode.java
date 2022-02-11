package de.jaylawl.meinkraft.command;

import de.jaylawl.meinkraft.Meinkraft;
import de.jaylawl.meinkraft.command.util.TabCompleteUtil;
import de.jaylawl.meinkraft.command.util.TabHelper;
import de.jaylawl.meinkraft.listener.bukkit.GodModeListener;
import de.jaylawl.meinkraft.util.MessagingUtil;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class CommandGodMode implements MeinkraftCommand {

    public CommandGodMode() {
    }

    //

    @Override
    public @NotNull String getBasePermissionNode() {
        return "mk.godmode";
    }

    @Override
    public boolean requiresListeners() {
        return true;
    }

    @Override
    public @NotNull Collection<Listener> getRequiredListenerClasses() {
        return Collections.singletonList(new GodModeListener(Meinkraft.getDataCenter()));
    }

    //

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String label, @NotNull String[] arguments) {

        final boolean permissionSelf = hasSelfPermission(commandSender);
        final boolean permissionOthers = hasOthersPermission(commandSender);

        if (!permissionOthers) {
            return Collections.emptyList();
        }

        List<String> completions = new ArrayList<>();
        int argumentNumber = TabHelper.getArgumentNumber(arguments);

        if (argumentNumber == 1) {
            completions.addAll(TabCompleteUtil.getOnlinePlayerNames());
            return TabHelper.sortedCompletions(arguments[argumentNumber - 1], completions);

        } else {
            return Collections.emptyList();

        }

    }

    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String label, @NotNull String[] arguments) {

        final boolean permissionSelf = hasSelfPermission(commandSender);
        final boolean permissionOthers = hasOthersPermission(commandSender);

        if (!permissionSelf && !permissionOthers) {
            MessagingUtil.noPermission(commandSender);
            return true;
        }

        Player targetPlayer;

        if (arguments.length == 0) {
            if (commandSender instanceof Player) {
                targetPlayer = (Player) commandSender;
            } else {
                MessagingUtil.genericError(commandSender, "Missing player argument");
                return true;
            }
        } else {
            targetPlayer = Bukkit.getPlayer(arguments[0]);
            if (targetPlayer == null) {
                MessagingUtil.invalidArguments(commandSender, arguments[0], "is not an online player");
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

        boolean toggledGodModeState = Meinkraft.getDataCenter().toggleGodMode(targetPlayer.getUniqueId());

        MessagingUtil.notifyExecutor(commandSender, "Set god mode of " + targetPlayer.getName() + " to " + toggledGodModeState);
        if (!targetEqualsSender) {
            MessagingUtil.notifyTargetPlayer(targetPlayer, toggledGodModeState ? "A wizard has turned you into a god" : "A wizard has turned you back into a mortal");
        }

        return true;
    }

}

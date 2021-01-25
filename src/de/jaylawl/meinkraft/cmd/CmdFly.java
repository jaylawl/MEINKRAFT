package de.jaylawl.meinkraft.cmd;

import de.jaylawl.meinkraft.util.CmdPermission;
import de.jaylawl.meinkraft.util.MessagingUtil;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class CmdFly implements CmdMeinkraft {

    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String label, @NotNull String[] arguments) {

        if (!CmdPermission.hasAny(commandSender, label)) {
            MessagingUtil.noPermission(commandSender);
            return true;
        }

        Player affectedPlayer;

        if (arguments.length == 0) {
            if (commandSender instanceof Player) {
                affectedPlayer = (Player) commandSender;
            } else {
                MessagingUtil.genericError(commandSender, "Missing player argument");
                return true;
            }
        } else {
            affectedPlayer = Bukkit.getPlayer(arguments[0]);
            if (affectedPlayer == null) {
                MessagingUtil.invalidArguments(commandSender, arguments[0], "is not an online player");
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

        affectedPlayer.setAllowFlight(!affectedPlayer.getAllowFlight());
        commandSender.sendMessage("Set flight mode of " + affectedPlayer.getName() + " to " + affectedPlayer.getAllowFlight());
        if (!senderEqualsAffected) {
            if (affectedPlayer.getAllowFlight()) {
                MessagingUtil.notifyPlayer(affectedPlayer, "A wizard has granted you flight powers");
            } else {
                MessagingUtil.notifyPlayer(affectedPlayer, "A wizard has clipped your wings");
            }
        }

        return true;
    }
}

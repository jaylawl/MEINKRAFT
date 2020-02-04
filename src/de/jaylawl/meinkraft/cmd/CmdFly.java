package de.jaylawl.meinkraft.cmd;

import de.jaylawl.meinkraft.util.CmdPermission;
import de.jaylawl.meinkraft.util.Messaging;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class CmdFly implements CommandExecutor {

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String label, @NotNull String[] args) {

        if (!CmdPermission.hasAny(sender, label)) {
            Messaging.noPermission(sender);
            return true;
        }

        Player affectedPlayer;
        boolean senderEqualsAffected = false;

        if (args.length == 0) {
            if (sender instanceof Player) {
                affectedPlayer = (Player) sender;
            } else {
                Messaging.genericError(sender, "Missing player argument");
                return true;
            }
        } else {
            affectedPlayer = Bukkit.getPlayer(args[0]);
            if (affectedPlayer == null) {
                Messaging.invalidArguments(sender, args[0], "is not an online player");
                return true;
            }
        }

        if (sender instanceof Player && sender == affectedPlayer) {
            senderEqualsAffected = true;
            if (!CmdPermission.hasOthers(sender, label)) {
                Messaging.noPermissionOthers(sender);
                return true;
            }
        }

        affectedPlayer.setAllowFlight(!affectedPlayer.getAllowFlight());
        sender.sendMessage("Set flight mode of " + affectedPlayer.getName() + " to " + affectedPlayer.getAllowFlight());
        if (!senderEqualsAffected) {
            if (affectedPlayer.getAllowFlight()) {
                Messaging.notifyPlayer(affectedPlayer, "A wizard has granted you flight powers");
            } else {
                Messaging.notifyPlayer(affectedPlayer, "A wizard has clipped your wings");
            }
        }

        return true;
    }
}

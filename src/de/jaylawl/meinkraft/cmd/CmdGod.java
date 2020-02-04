package de.jaylawl.meinkraft.cmd;

import de.jaylawl.meinkraft.Main;
import de.jaylawl.meinkraft.util.CmdPermission;
import de.jaylawl.meinkraft.util.Messaging;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.metadata.FixedMetadataValue;
import org.jetbrains.annotations.NotNull;

public class CmdGod implements CommandExecutor {

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String label, @NotNull String[] args) {

        if (!CmdPermission.hasAny(sender, label)) {
            sender.sendMessage("§cInsufficient permission");
            return true;
        }

        Player affectedPlayer;
        boolean senderEqualsAffected = false;

        if (args.length < 1) {
            if (sender instanceof Player) {
                affectedPlayer = (Player) sender;
            } else {
                Messaging.genericError(sender, "§cMissing player argument");
                return true;
            }
        } else {
            affectedPlayer = Bukkit.getPlayer(args[0]);
            if (affectedPlayer == null) {
                Messaging.invalidArguments(sender, args[0], "is not an online player");
                return true;
            } else if (!sender.hasPermission("mk.god.others")) {
                Messaging.noPermissionOthers(sender);
                return true;
            }
        }

        if (sender instanceof Player && sender == affectedPlayer) {
            if (!CmdPermission.hasOthers(sender, label)) {
                Messaging.noPermissionOthers(sender);
                return true;
            }
            senderEqualsAffected = true;
        }

        boolean god = !affectedPlayer.hasMetadata("GodMode") || !affectedPlayer.getMetadata("GodMode").get(0).asBoolean();
        affectedPlayer.setMetadata("GodMode", new FixedMetadataValue(Main.inst(), god));

        Messaging.feedback(sender, "Set god mode of " + affectedPlayer.getName() + " to " + god);
        if (!senderEqualsAffected) {
            if (god) {
                Messaging.notifyPlayer(affectedPlayer, "A wizard has turned you into a god");
            } else {
                Messaging.notifyPlayer(affectedPlayer, "A wizard has turned you back into a mortal");
            }
        }

        return true;
    }

}

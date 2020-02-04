package de.jaylawl.meinkraft.cmd;

import de.jaylawl.meinkraft.util.CmdPermission;
import de.jaylawl.meinkraft.util.Messaging;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class CmdPing implements CommandExecutor {

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String label, @NotNull String[] args) {

        Player affectedPlayer;

        if (args.length == 0) {
            if (sender instanceof Player) {
                affectedPlayer = (Player) sender;
                Messaging.notifyPlayer(affectedPlayer, "A wizard responded within " + affectedPlayer.spigot().getPing() + " ms");
            } else {
                Messaging.genericError(sender, "§cMissing player argument");
            }
        } else {
            affectedPlayer = Bukkit.getPlayer(args[0]);
            if (affectedPlayer == null) {
                Messaging.invalidArguments(sender, args[0], "is not an online player");
            } else {
                if (!CmdPermission.hasOthers(sender, label)) {
                    Messaging.noPermissionOthers(sender);
                } else {
                    Messaging.feedback(sender, "Ping of " + affectedPlayer.getName() + ": " + affectedPlayer.spigot().getPing() + " ms");
                }
            }
        }

        return true;
    }
}

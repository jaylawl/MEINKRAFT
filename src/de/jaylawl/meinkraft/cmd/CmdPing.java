package de.jaylawl.meinkraft.cmd;

import de.jaylawl.meinkraft.util.CmdPermission;
import de.jaylawl.meinkraft.util.MessagingUtil;
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
                MessagingUtil.notifyPlayer(affectedPlayer, "A wizard responded within " + affectedPlayer.spigot().getPing() + " ms");
            } else {
                MessagingUtil.genericError(sender, "§cMissing player argument");
            }
        } else {
            affectedPlayer = Bukkit.getPlayer(args[0]);
            if (affectedPlayer == null) {
                MessagingUtil.invalidArguments(sender, args[0], "is not an online player");
            } else {
                if (!CmdPermission.hasOthers(sender, label)) {
                    MessagingUtil.noPermissionOthers(sender);
                } else {
                    MessagingUtil.feedback(sender, "Ping of " + affectedPlayer.getName() + ": " + affectedPlayer.spigot().getPing() + " ms");
                }
            }
        }

        return true;
    }
}

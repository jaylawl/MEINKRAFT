package de.jaylawl.meinkraft.cmd;

import de.jaylawl.meinkraft.util.CmdPermission;
import de.jaylawl.meinkraft.util.MessagingUtil;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class CommandPing implements CommandMeinkraft {

    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String label, @NotNull String[] arguments) {

        Player affectedPlayer;

        if (arguments.length == 0) {
            if (commandSender instanceof Player) {
                affectedPlayer = (Player) commandSender;
                MessagingUtil.notifyPlayer(affectedPlayer, "A wizard responded within " + affectedPlayer.spigot().getPing() + " ms");
            } else {
                MessagingUtil.genericError(commandSender, "Missing player argument");
            }
        } else {
            affectedPlayer = Bukkit.getPlayer(arguments[0]);
            if (affectedPlayer == null) {
                MessagingUtil.invalidArguments(commandSender, arguments[0], "is not an online player");
            } else {
                if (!CmdPermission.hasOthers(commandSender, label)) {
                    MessagingUtil.noPermissionOthers(commandSender);
                } else {
                    MessagingUtil.notifyExecutor(commandSender, "Ping of " + affectedPlayer.getName() + ": " + affectedPlayer.spigot().getPing() + " ms");
                }
            }
        }

        return true;
    }
}

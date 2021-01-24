package de.jaylawl.meinkraft.cmd;

import de.jaylawl.meinkraft.util.CmdPermission;
import de.jaylawl.meinkraft.util.DataCenter;
import de.jaylawl.meinkraft.util.MessagingUtil;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class CmdGod implements CommandExecutor {

    private final DataCenter dataCenter;

    public CmdGod(@NotNull DataCenter dataCenter) {
        this.dataCenter = dataCenter;
    }

    //

    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String label, @NotNull String[] arguments) {

        if (!CmdPermission.hasAny(commandSender, label)) {
            MessagingUtil.noPermission(commandSender);
            return true;
        }

        Player affectedPlayer;

        if (arguments.length < 1) {
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
        if (!senderEqualsAffected) {
            if (!CmdPermission.hasOthers(commandSender, label)) {
                MessagingUtil.noPermissionOthers(commandSender);
                return true;
            }
        }

        boolean toggledGodModeState = this.dataCenter.toggleGodMode(affectedPlayer.getUniqueId());

        MessagingUtil.feedback(commandSender, "Set god mode of " + affectedPlayer.getName() + " to " + toggledGodModeState);
        if (!senderEqualsAffected) {
            MessagingUtil.notifyPlayer(affectedPlayer, toggledGodModeState ? "A wizard has turned you into a god" : "A wizard has turned you back into a mortal");
        }

        return true;
    }

}

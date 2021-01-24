package de.jaylawl.meinkraft.cmd;

import de.jaylawl.meinkraft.util.CmdPermission;
import de.jaylawl.meinkraft.util.DataCenter;
import de.jaylawl.meinkraft.util.MessagingUtil;
import net.md_5.bungee.api.ChatColor;
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
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String label, @NotNull String[] args) {

        if (!CmdPermission.hasAny(sender, label)) {
            MessagingUtil.noPermission(sender);
            return true;
        }

        Player affectedPlayer;

        if (args.length < 1) {
            if (sender instanceof Player) {
                affectedPlayer = (Player) sender;
            } else {
                MessagingUtil.genericError(sender, "Missing player argument");
                return true;
            }
        } else {
            affectedPlayer = Bukkit.getPlayer(args[0]);
            if (affectedPlayer == null) {
                MessagingUtil.invalidArguments(sender, args[0], "is not an online player");
                return true;
            }
        }

        boolean senderEqualsAffected = sender == affectedPlayer;
        if (!senderEqualsAffected) {
            if (!CmdPermission.hasOthers(sender, label)) {
                MessagingUtil.noPermissionOthers(sender);
                return true;
            }
        }

        boolean toggledGodModeState = this.dataCenter.toggleGodMode(affectedPlayer.getUniqueId());

        MessagingUtil.feedback(sender, "Set god mode of " + affectedPlayer.getName() + " to " + toggledGodModeState);
        if (!senderEqualsAffected) {
            MessagingUtil.notifyPlayer(affectedPlayer, toggledGodModeState ? "A wizard has turned you into a god" : "A wizard has turned you back into a mortal");
        }

        return true;
    }

}

package de.jaylawl.meinkraft.cmd;

import de.jaylawl.meinkraft.util.CmdPermission;
import de.jaylawl.meinkraft.util.Messaging;
import org.bukkit.Bukkit;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeInstance;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class CmdHeal implements CommandExecutor {

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
            }
        }

        if (sender instanceof Player && sender == affectedPlayer) {
            if (!CmdPermission.hasOthers(sender, label)) {
                Messaging.noPermissionOthers(sender);
                return true;
            }
            senderEqualsAffected = true;
        }

        AttributeInstance maxHealth = affectedPlayer.getAttribute(Attribute.GENERIC_MAX_HEALTH);
        affectedPlayer.setHealth(maxHealth != null ? maxHealth.getValue() : 20);
        affectedPlayer.setExhaustion(0f);
        affectedPlayer.setSaturation(1f);
        affectedPlayer.setFoodLevel(20);

        Messaging.feedback(sender, "Fully healed player " + affectedPlayer.getName());
        if (!senderEqualsAffected) {
            Messaging.notifyPlayer(affectedPlayer, "You've been rejuvenated by a wizard");
        }

        return true;
    }


}

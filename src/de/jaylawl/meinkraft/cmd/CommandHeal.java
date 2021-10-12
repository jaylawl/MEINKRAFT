package de.jaylawl.meinkraft.cmd;

import de.jaylawl.meinkraft.util.CmdPermission;
import de.jaylawl.meinkraft.util.MessagingUtil;
import org.bukkit.Bukkit;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeInstance;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class CommandHeal implements CommandMeinkraft {

    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String label, @NotNull String[] arguments) {

        if (!CmdPermission.hasAny(commandSender, label)) {
            commandSender.sendMessage("§cInsufficient permission");
            return true;
        }

        Player affectedPlayer;

        if (arguments.length < 1) {
            if (commandSender instanceof Player) {
                affectedPlayer = (Player) commandSender;
            } else {
                MessagingUtil.genericError(commandSender, "§cMissing player argument");
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

        AttributeInstance maxHealth = affectedPlayer.getAttribute(Attribute.GENERIC_MAX_HEALTH);
        affectedPlayer.setHealth(maxHealth != null ? maxHealth.getValue() : 20);
        affectedPlayer.setExhaustion(0f);
        affectedPlayer.setSaturation(1f);
        affectedPlayer.setFoodLevel(20);

        MessagingUtil.notifyExecutor(commandSender, "Fully healed player " + affectedPlayer.getName());
        if (!senderEqualsAffected) {
            MessagingUtil.notifyPlayer(affectedPlayer, "You've been rejuvenated by a wizard");
        }

        return true;
    }


}

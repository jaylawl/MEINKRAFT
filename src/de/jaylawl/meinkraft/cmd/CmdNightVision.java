package de.jaylawl.meinkraft.cmd;

import de.jaylawl.meinkraft.util.CmdPermission;
import de.jaylawl.meinkraft.util.Messaging;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.jetbrains.annotations.NotNull;

public class CmdNightVision implements CommandExecutor {

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

        if (sender == affectedPlayer) {
            senderEqualsAffected = true;
            if (!CmdPermission.hasOthers(sender, label)) {
                Messaging.noPermissionOthers(sender);
                return true;
            }
        }

        boolean grant = true;
        if (affectedPlayer.hasPotionEffect(PotionEffectType.NIGHT_VISION)) {
            grant = affectedPlayer.getPotionEffect(PotionEffectType.NIGHT_VISION).getAmplifier() != 214;
        }

        if (grant) {
            affectedPlayer.addPotionEffect(new PotionEffect(PotionEffectType.NIGHT_VISION, 2147483647, 214, true, false, true), true);
            sender.sendMessage("Gave \"permanent\" night vision to " + affectedPlayer.getName());
            if (!senderEqualsAffected) {
                Messaging.notifyPlayer(affectedPlayer, "A wizard made you drink a cauldron of night vision potion");
            }

        } else {
            affectedPlayer.removePotionEffect(PotionEffectType.NIGHT_VISION);
            sender.sendMessage("Removed \"permanent\" night vision from " + affectedPlayer.getName());
            if (!senderEqualsAffected) {
                Messaging.notifyPlayer(affectedPlayer, "A wizard has cast away your night vision ability");
            }

        }

        return true;
    }
}

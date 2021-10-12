package de.jaylawl.meinkraft.cmd;

import de.jaylawl.meinkraft.util.CmdPermission;
import de.jaylawl.meinkraft.util.MessagingUtil;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class CommandNightVision implements CommandMeinkraft {

    private final static short CUSTOM_EFFECT_IDENTIFIER = 214;
    private final static PotionEffect PERMANENT_NIGHT_VISION = new PotionEffect(
            PotionEffectType.NIGHT_VISION,
            2147483647,
            CUSTOM_EFFECT_IDENTIFIER,
            true,
            false,
            true
    );

    private final ConcurrentHashMap<UUID, PotionEffect> priorNightVisionEffects = new ConcurrentHashMap<>();

    public CommandNightVision() {
    }

    //

    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String label, @NotNull String[] arguments) {

        if (!CmdPermission.hasAny(commandSender, label)) {
            MessagingUtil.noPermission(commandSender);
            return true;
        }

        Player affectedPlayer;

        if (arguments.length == 0) {
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

        boolean hasPermanentNightVision = false;
        if (affectedPlayer.hasPotionEffect(PotionEffectType.NIGHT_VISION)) {
            PotionEffect priorNightVisionEffect = affectedPlayer.getPotionEffect(PotionEffectType.NIGHT_VISION);
            if (priorNightVisionEffect != null) {
                if (priorNightVisionEffect.getAmplifier() == CUSTOM_EFFECT_IDENTIFIER) {
                    hasPermanentNightVision = true;
                } else {
                    this.priorNightVisionEffects.put(affectedPlayer.getUniqueId(), priorNightVisionEffect);
                }
            }
        }

        if (hasPermanentNightVision) {
            affectedPlayer.removePotionEffect(PotionEffectType.NIGHT_VISION);
            UUID affectedPlayerUniqueId = affectedPlayer.getUniqueId();
            PotionEffect priorNightVisionEffect = this.priorNightVisionEffects.get(affectedPlayerUniqueId);
            if (priorNightVisionEffect != null) {
                affectedPlayer.addPotionEffect(priorNightVisionEffect);
                this.priorNightVisionEffects.remove(affectedPlayerUniqueId);
            }
            commandSender.sendMessage("Removed \"permanent\" night vision from " + affectedPlayer.getName());
            if (!senderEqualsAffected) {
                MessagingUtil.notifyPlayer(affectedPlayer, "A wizard has cast away your permanent night vision ability");
            }
        } else {
            affectedPlayer.addPotionEffect(PERMANENT_NIGHT_VISION);
            commandSender.sendMessage("Gave \"permanent\" night vision to " + affectedPlayer.getName());
            if (!senderEqualsAffected) {
                MessagingUtil.notifyPlayer(affectedPlayer, "A wizard made you drink a cauldron of night vision potion");
            }
        }

        return true;
    }
}

package de.jaylawl.meinkraft.cmd;

import de.jaylawl.meinkraft.util.CmdPermission;
import de.jaylawl.meinkraft.util.MessagingUtil;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class CmdNightVision implements CommandExecutor {

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

    public CmdNightVision() {
    }

    //

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String label, @NotNull String[] args) {

        if (!CmdPermission.hasAny(sender, label)) {
            MessagingUtil.noPermission(sender);
            return true;
        }

        Player affectedPlayer;

        if (args.length == 0) {
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
            sender.sendMessage("Removed \"permanent\" night vision from " + affectedPlayer.getName());
            if (!senderEqualsAffected) {
                MessagingUtil.notifyPlayer(affectedPlayer, "A wizard has cast away your permanent night vision ability");
            }
        } else {
            affectedPlayer.addPotionEffect(PERMANENT_NIGHT_VISION);
            sender.sendMessage("Gave \"permanent\" night vision to " + affectedPlayer.getName());
            if (!senderEqualsAffected) {
                MessagingUtil.notifyPlayer(affectedPlayer, "A wizard made you drink a cauldron of night vision potion");
            }
        }

        return true;
    }
}

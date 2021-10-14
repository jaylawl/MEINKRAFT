package de.jaylawl.meinkraft.command;

import de.jaylawl.meinkraft.util.MessagingUtil;
import de.jaylawl.meinkraft.util.TabHelper;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class CommandNightVision implements MeinkraftCommand {

    public static final String PERMISSION_NODE = "mk.nightvision";
    public static final String PERMISSION_NODE_SELF = "mk.nightvision.self";
    public static final String PERMISSION_NODE_OTHERS = "mk.nightvision.others";

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
    public @Nullable List<String> onTabComplete(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String label, @NotNull String[] arguments) {

        boolean permissionSelf = commandSender.hasPermission(PERMISSION_NODE_SELF);
        boolean permissionOthers = commandSender.hasPermission(PERMISSION_NODE_OTHERS);

        if (!permissionSelf && !permissionOthers) {
            return Collections.emptyList();
        }

        List<String> completions = new ArrayList<>();
        int argumentNumber = TabHelper.getArgumentNumber(arguments);

        if (argumentNumber == 1) {
            if (permissionOthers) {
                completions.addAll(TabCompleteUtil.getOnlinePlayerNames());
            } else {
                if (commandSender instanceof Player player) {
                    completions.add(player.getName());
                }
            }
            return TabHelper.sortedCompletions(arguments[argumentNumber - 1], completions);

        } else {
            return Collections.emptyList();

        }

    }

    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String label, @NotNull String[] arguments) {

        // TODO: 14.10.2021 probably broken in 1.17?

        boolean permissionSelf = commandSender.hasPermission(PERMISSION_NODE_SELF);
        boolean permissionOthers = commandSender.hasPermission(PERMISSION_NODE_OTHERS);

        if (!permissionSelf && !permissionOthers) {
            MessagingUtil.noPermission(commandSender);
            return true;
        }

        Player targetPlayer;

        if (arguments.length == 0) {
            if (commandSender instanceof Player) {
                targetPlayer = (Player) commandSender;
            } else {
                MessagingUtil.genericError(commandSender, "Missing player argument");
                return true;
            }
        } else {
            targetPlayer = Bukkit.getPlayer(arguments[0]);
            if (targetPlayer == null) {
                MessagingUtil.invalidArguments(commandSender, arguments[0], "is not an online player");
                return true;
            }
        }

        boolean targetEqualsSender = targetPlayer == commandSender;
        if (targetEqualsSender) {
            if (!permissionSelf) {
                MessagingUtil.noPermission(commandSender);
                return true;
            }
        } else {
            if (!permissionOthers) {
                MessagingUtil.noPermissionOthers(commandSender);
                return true;
            }
        }

        //

        boolean hasPermanentNightVision = false;
        if (targetPlayer.hasPotionEffect(PotionEffectType.NIGHT_VISION)) {
            PotionEffect priorNightVisionEffect = targetPlayer.getPotionEffect(PotionEffectType.NIGHT_VISION);
            if (priorNightVisionEffect != null) {
                if (priorNightVisionEffect.getAmplifier() == CUSTOM_EFFECT_IDENTIFIER) {
                    hasPermanentNightVision = true;
                } else {
                    this.priorNightVisionEffects.put(targetPlayer.getUniqueId(), priorNightVisionEffect);
                }
            }
        }

        if (hasPermanentNightVision) {
            targetPlayer.removePotionEffect(PotionEffectType.NIGHT_VISION);
            UUID affectedPlayerUniqueId = targetPlayer.getUniqueId();
            PotionEffect priorNightVisionEffect = this.priorNightVisionEffects.get(affectedPlayerUniqueId);
            if (priorNightVisionEffect != null) {
                targetPlayer.addPotionEffect(priorNightVisionEffect);
                this.priorNightVisionEffects.remove(affectedPlayerUniqueId);
            }
            commandSender.sendMessage("Removed \"permanent\" night vision from " + targetPlayer.getName());
            if (!targetEqualsSender) {
                MessagingUtil.notifyTargetPlayer(targetPlayer, "A wizard has cast away your permanent night vision ability");
            }
        } else {
            targetPlayer.addPotionEffect(PERMANENT_NIGHT_VISION);
            commandSender.sendMessage("Gave \"permanent\" night vision to " + targetPlayer.getName());
            if (!targetEqualsSender) {
                MessagingUtil.notifyTargetPlayer(targetPlayer, "A wizard made you drink a cauldron of night vision potion");
            }
        }

        return true;
    }
}

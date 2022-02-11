package de.jaylawl.meinkraft.command;

import de.jaylawl.meinkraft.command.util.TabCompleteUtil;
import de.jaylawl.meinkraft.command.util.TabHelper;
import de.jaylawl.meinkraft.listener.bukkit.NightVisionListener;
import de.jaylawl.meinkraft.util.MessagingUtil;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class CommandNightVision implements MeinkraftCommand {

    public static final String PERMISSION_NODE = "mk.nightvision";
    public static final String PERMISSION_NODE_SELF = "mk.nightvision.self";
    public static final String PERMISSION_NODE_OTHERS = "mk.nightvision.others";

    public final static short MAX_VANILLA_POTION_DURATION = 9600; // 8 minutes * 60 seconds * 20 game ticks = 9600 | 8 minutes is the maximum duration a vanilla potion may have
    private final static PotionEffect PERMANENT_NIGHT_VISION = new PotionEffect(
            PotionEffectType.NIGHT_VISION,
            Integer.MAX_VALUE,
            0,
            true,
            false,
            true
    );

    public CommandNightVision() {
    }

    //

    @Override
    public @NotNull String getBasePermissionNode() {
        return PERMISSION_NODE;
    }

    @Override
    public boolean requiresListeners() {
        return true;
    }

    @Override
    public @NotNull Collection<Listener> getRequiredListenerClasses() {
        return Collections.singletonList(new NightVisionListener());
    }

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

        boolean applyEffect = false;
        if (!targetPlayer.hasPotionEffect(PotionEffectType.NIGHT_VISION)) {
            applyEffect = true;
        } else {
            PotionEffect currentNightVisionEffect = targetPlayer.getPotionEffect(PotionEffectType.NIGHT_VISION);
            if (currentNightVisionEffect != null) {
                if (currentNightVisionEffect.getDuration() <= MAX_VANILLA_POTION_DURATION) {
                    applyEffect = true;
                }
            }
        }

        if (!applyEffect) {
            targetPlayer.removePotionEffect(PotionEffectType.NIGHT_VISION);
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

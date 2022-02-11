package de.jaylawl.meinkraft.listener.bukkit;

import de.jaylawl.meinkraft.command.CommandNightVision;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.jetbrains.annotations.NotNull;

public class NightVisionListener implements Listener {

    public NightVisionListener() {
    }

    //

    @EventHandler(priority = EventPriority.MONITOR)
    public void onPlayerJoin(@NotNull PlayerJoinEvent event) {
        final Player player = event.getPlayer();
        PotionEffect nightVisionEffect = player.getPotionEffect(PotionEffectType.NIGHT_VISION);
        if (nightVisionEffect != null) {
            if (nightVisionEffect.getDuration() > CommandNightVision.MAX_VANILLA_POTION_DURATION) {
                player.removePotionEffect(PotionEffectType.NIGHT_VISION);
            }
        }
    }

}

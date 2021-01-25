package de.jaylawl.meinkraft.listener.bukkit;

import de.jaylawl.meinkraft.MEINKRAFT;
import de.jaylawl.meinkraft.settings.ResourcePackHandler;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerResourcePackStatusEvent;
import org.bukkit.scheduler.BukkitRunnable;
import org.jetbrains.annotations.NotNull;

public class ResourcePackListener implements Listener {

    public ResourcePackListener() {
    }

    @EventHandler
    public void onResourcePackStatus(@NotNull PlayerResourcePackStatusEvent event) {

        ResourcePackHandler resourcePackHandler = MEINKRAFT.getSettings().getResourcePackHandler();
        if (!resourcePackHandler.isEnabled()) {
            return;
        }

        Player player = event.getPlayer();

        if (resourcePackHandler.ignoresOperators() && player.isOp()) {
            return;
        } else if (resourcePackHandler.getIgnoredUniqueIds().contains(player.getUniqueId())) {
            return;
        }

        switch (event.getStatus()) {
            case DECLINED: {
                if (!resourcePackHandler.shouldKickOnDecline()) {
                    return;
                }
                break;
            }
            case FAILED_DOWNLOAD: {
                if (!resourcePackHandler.shouldKickOnFailure()) {
                    return;
                }
                break;
            }
        }

        long ticksBeforeKick = resourcePackHandler.getTicksBeforeKick();
        String kickMessage = resourcePackHandler.getKickMessage();
        if (ticksBeforeKick < 1L) {
            player.kickPlayer(kickMessage);
        } else {
            new BukkitRunnable() {
                @Override
                public void run() {
                    player.kickPlayer(kickMessage);
                }
            }.runTaskLater(MEINKRAFT.inst(), ticksBeforeKick);
        }

    }

}

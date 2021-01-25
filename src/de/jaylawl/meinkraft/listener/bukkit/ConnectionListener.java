package de.jaylawl.meinkraft.listener.bukkit;

import de.jaylawl.meinkraft.MEINKRAFT;
import de.jaylawl.meinkraft.settings.UnsafePlayerBlocker;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerLoginEvent;
import org.jetbrains.annotations.NotNull;

import java.util.logging.Logger;

public class ConnectionListener implements Listener {

    public ConnectionListener() {
    }

    //

    @EventHandler
    public void onLogin(@NotNull PlayerLoginEvent event) {
        Logger logger = MEINKRAFT.inst().getLogger();
        UnsafePlayerBlocker unsafePlayerBlocker = MEINKRAFT.getSettings().getUnsafePlayerBlocker();
        if (!unsafePlayerBlocker.isEnabled()) {
            return;
        }

        Player player = event.getPlayer();
        String playerName = player.getName();

        for (UnsafePlayerBlocker.Category category : UnsafePlayerBlocker.Category.values()) {
            if (unsafePlayerBlocker.isEnabled(category)) {
                if (unsafePlayerBlocker.shouldReject(category, playerName)) {
                    event.setResult(PlayerLoginEvent.Result.KICK_OTHER);
                    event.setKickMessage(unsafePlayerBlocker.getKickMessage(category));
                    logger.info("UnsafePlayerBlocker prevented player \"" + playerName + "\" (uuid: " + player.getUniqueId() + ") from connecting");
                    break;
                }
            }
        }

    }

}

package de.jaylawl.meinkraft.listeners;

import de.jaylawl.meinkraft.MEINKRAFT;
import de.jaylawl.meinkraft.util.Messaging;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;

import java.util.List;

public class CommandListener implements Listener {

    public CommandListener() {
    }

    @EventHandler
    public void event(PlayerCommandPreprocessEvent event) {

        if (!event.getPlayer().hasPermission("mk.plugins")) {

            List<String> blockedCmds = MEINKRAFT.inst().getConfig().getStringList("Modules.CommandBlocker.BlockedCommands");
            if (!blockedCmds.isEmpty() && blockedCmds.contains(event.getMessage())) {
                event.setCancelled(true);
                if (MEINKRAFT.inst().getConfig().getBoolean("Modules.CommandBlocker.SendFeedback", true)) {
                    Messaging.noPermission(event.getPlayer());
                }
            }
        }
    }

}

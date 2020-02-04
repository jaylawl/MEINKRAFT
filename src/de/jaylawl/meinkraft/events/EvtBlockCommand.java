package de.jaylawl.meinkraft.events;

import de.jaylawl.meinkraft.Main;
import de.jaylawl.meinkraft.util.Messaging;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;

import java.util.List;

public class EvtBlockCommand implements Listener {

    public EvtBlockCommand() {
    }

    @EventHandler
    public void event(PlayerCommandPreprocessEvent event) {

        if (!event.getPlayer().hasPermission("mk.plugins")) {

            List<String> blockedCmds = Main.inst().getConfig().getStringList("Modules.CommandBlocker.BlockedCommands");
            if (!blockedCmds.isEmpty() && blockedCmds.contains(event.getMessage())) {
                event.setCancelled(true);
                if (Main.inst().getConfig().getBoolean("Modules.CommandBlocker.SendFeedback", true)) {
                    Messaging.noPermission(event.getPlayer());
                }
            }
        }
    }

}

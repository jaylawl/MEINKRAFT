package de.jaylawl.meinkraft.listener.bukkit;

import de.jaylawl.meinkraft.MEINKRAFT;
import de.jaylawl.meinkraft.settings.CommandBlocker;
import de.jaylawl.meinkraft.util.MessagingUtil;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;

import java.util.List;

public class CommandListener implements Listener {

    public CommandListener() {
    }

    //

    @EventHandler
    public void onCommand(PlayerCommandPreprocessEvent event) {
        CommandBlocker commandBlocker = MEINKRAFT.getSettings().getCommandBlocker();
        if (commandBlocker.isEnabled()) {
            if (!event.getPlayer().hasPermission("mk.plugins")) {
                List<String> blockedCommands = commandBlocker.getBlockedCommands();
                if (!blockedCommands.isEmpty() && blockedCommands.contains(event.getMessage())) {
                    event.setCancelled(true);
                    if (commandBlocker.sendFeedback()) {
                        MessagingUtil.noPermission(event.getPlayer());
                    }
                }
            }
        }
    }

}

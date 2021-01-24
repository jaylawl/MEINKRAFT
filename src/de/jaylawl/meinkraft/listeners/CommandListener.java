package de.jaylawl.meinkraft.listeners;

import de.jaylawl.meinkraft.Meinkraft;
import de.jaylawl.meinkraft.util.MessagingUtil;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;

import java.util.List;

public class CommandListener implements Listener {

    public CommandListener() {
    }

    @EventHandler
    public void onCommand(PlayerCommandPreprocessEvent event) {

        if (!event.getPlayer().hasPermission("mk.plugins")) {
            List<String> blockedCommands = Meinkraft.inst().getConfig().getStringList("Modules.CommandBlocker.BlockedCommands");
            if (!blockedCommands.isEmpty() && blockedCommands.contains(event.getMessage())) {
                event.setCancelled(true);
                if (Meinkraft.inst().getConfig().getBoolean("Modules.CommandBlocker.SendFeedback", true)) {
                    MessagingUtil.noPermission(event.getPlayer());
                }
            }
        }
    }

}

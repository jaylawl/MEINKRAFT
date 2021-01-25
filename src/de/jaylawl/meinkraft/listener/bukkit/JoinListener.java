package de.jaylawl.meinkraft.listener.bukkit;

import de.jaylawl.meinkraft.MEINKRAFT;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class JoinListener implements Listener {

    public JoinListener() {
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        FileConfiguration config = MEINKRAFT.inst().getConfig();
        String downloadURL = config.getString("Modules.ResourcePackHandler.Link", "");
        String packHash = config.getString("Modules.ResourcePackHandler.Hash", "");
        if (downloadURL != null && packHash != null) {
            event.getPlayer().setResourcePack(downloadURL, packHash);
        }
    }

}

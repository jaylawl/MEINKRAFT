package de.jaylawl.meinkraft.listeners;

import de.jaylawl.meinkraft.Main;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class EvtJoin implements Listener {

    public EvtJoin() {
    }

    @EventHandler
    public void event(PlayerJoinEvent event) {
        FileConfiguration config = Main.inst().getConfig();
        String downloadURL = config.getString("Modules.ResourcePackHandler.Link", "");
        String packHash = config.getString("Modules.ResourcePackHandler.Hash", "");
        if (downloadURL != null && packHash != null) {
            event.getPlayer().setResourcePack(downloadURL, packHash);
        }
    }

}

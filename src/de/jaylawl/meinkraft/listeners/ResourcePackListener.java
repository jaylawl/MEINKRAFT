package de.jaylawl.meinkraft.listeners;

import de.jaylawl.meinkraft.Main;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerResourcePackStatusEvent;
import org.bukkit.scheduler.BukkitRunnable;

public class ResourcePackListener implements Listener {

    public ResourcePackListener() {
    }

    @EventHandler
    public void event(PlayerResourcePackStatusEvent event) {

        System.out.println("EVENT");

        FileConfiguration config = Main.inst().getConfig();
        Player p = event.getPlayer();
        boolean kick = false;
        String mn = "Modules.ResourcePackHandler.";

        if (
                event.getStatus() == PlayerResourcePackStatusEvent.Status.DECLINED &&
                config.getBoolean(mn + "KickOnDecline", true)
        ) {
            kick = true;
        } else if (
                event.getStatus() == PlayerResourcePackStatusEvent.Status.FAILED_DOWNLOAD &&
                config.getBoolean(mn + "KickOnFailure", true)
        ) {
            kick = true;
        }

        if (kick) {
            if (p.isOp() && config.getBoolean(mn + "WhitelistOperators", false)) {
                return;
            }
            for (String whitelistedUUID : config.getStringList(mn + "WhitelistedUUIDs")) {
                if (whitelistedUUID.equals(p.getUniqueId().toString())) {
                    return;
                }
            }

            long kickDelay = config.getLong(mn + "TicksBeforeKick", 20L);
            kickDelay = Math.max(1L, kickDelay);

            StringBuilder sb = new StringBuilder("§r");
            for (String s : config.getStringList(mn + "KickMessage")) {
                sb.append(s);
                sb.append(System.lineSeparator());
            }
            new BukkitRunnable() {
                @Override
                public void run() {
                    p.kickPlayer(ChatColor.translateAlternateColorCodes('&', sb.toString()));
                }
            }.runTaskLater(Main.inst(), kickDelay);
        }

    }

}

package de.jaylawl.meinkraft.listeners;

import de.jaylawl.meinkraft.Main;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerLoginEvent;

public class ConnectionListener implements Listener {

    public ConnectionListener() {
    }

    @EventHandler
    public void event(PlayerLoginEvent event) {

        String pn = event.getPlayer().getName();
        FileConfiguration config = Main.inst().getConfig();
        String mn = "Modules.UnsafePlayerBlocker.";
        StringBuilder sb = new StringBuilder("§r");

        if (config.getBoolean(mn + "ContainingIllegalCharacters.Block", true)) {

            boolean kick = false;
            if (config.getBoolean(mn + "ContainingIllegalCharacters.BlockSpacesOnly", true)) {
                if (pn.contains(" ")) {
                    kick = true;
                }
            } else {
                String[] split = pn.split("");
                for (String c : split) {
                    if (c.matches("\\W")) {
                        kick = true;
                        break;
                    }
                }
            }

            if (kick) {
                for (String s : config.getStringList(mn + "ContainingIllegalCharacters.KickMessage")) {
                    sb.append(s);
                    sb.append(System.lineSeparator());
                }
                event.setResult(PlayerLoginEvent.Result.KICK_OTHER);
                event.setKickMessage(ChatColor.translateAlternateColorCodes('&', sb.toString()));
                return;
            }

        }

        if (config.getBoolean(mn + "NonUniqueUsernames.Block", true)) {
            for (Player p : Bukkit.getOnlinePlayers()) {
                if (p.getName().equals(pn)) {
                    for (String s : config.getStringList(mn + "NonUniqueUsernames.KickMessage")) {
                        sb.append(s);
                        sb.append(System.lineSeparator());
                    }
                    event.setResult(PlayerLoginEvent.Result.KICK_OTHER);
                    event.setKickMessage(ChatColor.translateAlternateColorCodes('&', sb.toString()));
                    return;
                }
            }
        }

        if (config.getBoolean(mn + "ContainingKeywords.Block", false)) {
            for (String keyword : config.getStringList(mn + "ContainingKeywords.Keywords")) {
                if (pn.toLowerCase().contains(keyword.toLowerCase())) {
                    for (String s : config.getStringList(mn + "ContainingKeywords.KickMessage")) {
                        sb.append(s);
                        sb.append(System.lineSeparator());
                    }
                    event.setResult(PlayerLoginEvent.Result.KICK_OTHER);
                    event.setKickMessage(ChatColor.translateAlternateColorCodes('&', sb.toString()));
                    return;
                }
            }
        }

    }

}

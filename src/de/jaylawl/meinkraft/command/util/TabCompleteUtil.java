package de.jaylawl.meinkraft.command.util;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collection;

public class TabCompleteUtil {

    public static @NotNull Collection<String> getOnlinePlayerNames() {
        Collection<String> onlinePlayerNames = new ArrayList<>();
        for (Player player : Bukkit.getOnlinePlayers()) {
            onlinePlayerNames.add(player.getName());
        }
        return onlinePlayerNames;
    }

    public static @NotNull Collection<String> getLoadedWorldNames() {
        Collection<String> loadedWorldNames = new ArrayList<>();
        for (World world : Bukkit.getWorlds()) {
            loadedWorldNames.add(world.getName());
        }
        return loadedWorldNames;
    }

}

package de.jaylawl.meinkraft.util;

import org.jetbrains.annotations.NotNull;

import java.util.HashSet;
import java.util.UUID;

public class DataCenter {

    private final HashSet<UUID> playersInGodMode = new HashSet<>();

    public DataCenter() {
    }

    //

    public boolean toggleGodMode(@NotNull UUID uniqueId) {
        if (this.playersInGodMode.contains(uniqueId)) {
            this.playersInGodMode.remove(uniqueId);
            return false;
        } else {
            this.playersInGodMode.add(uniqueId);
            return true;
        }
    }

    public boolean isInGodMode(@NotNull UUID uniqueId) {
        return this.playersInGodMode.contains(uniqueId);
    }

}

package de.jaylawl.meinkraft.settings;

import de.jaylawl.meinkraft.command.CommandIndex;
import de.jaylawl.meinkraft.command.MeinkraftCommand;
import org.bukkit.configuration.file.FileConfiguration;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;

public class Settings {

    private final HashMap<Class<? extends MeinkraftCommand>, Boolean> enabledCommands = new HashMap<>();

    public Settings(@NotNull FileConfiguration configuration) {
        for (CommandIndex optionalType : CommandIndex.values()) {
            this.enabledCommands.put(optionalType.getClazz(), configuration.getBoolean("Commands." + optionalType.getCommandLabel(), true));
        }
    }

    //

    public boolean isCommandEnabled(@NotNull Class<? extends MeinkraftCommand> clazz) {
        return this.enabledCommands.get(clazz);
    }

}

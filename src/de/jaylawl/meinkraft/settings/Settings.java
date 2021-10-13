package de.jaylawl.meinkraft.settings;

import de.jaylawl.meinkraft.command.*;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;

public class Settings {

    private final HashMap<Class<? extends MeinkraftCommand>, Boolean> enabledCommands = new HashMap<>();
    private final CommandBlocker commandBlocker;
    private final UnsafePlayerBlocker unsafePlayerBlocker;
    private final ResourcePackHandler resourcePackHandler;

    public Settings(@NotNull FileConfiguration configuration) {
        for (CmdOptionalType optionalType : CmdOptionalType.values()) {
            this.enabledCommands.put(optionalType.getClazz(), configuration.getBoolean("Commands." + optionalType.getCommandLabel(), true));
        }

        ConfigurationSection commandBlockerSection = configuration.getConfigurationSection("Modules.CommandBlocker");
        this.commandBlocker = commandBlockerSection == null ? new CommandBlocker() : new CommandBlocker(commandBlockerSection);

        ConfigurationSection unsafePlayerBlockerSection = configuration.getConfigurationSection("Modules.UnsafePlayerBlocker");
        this.unsafePlayerBlocker = unsafePlayerBlockerSection == null ? new UnsafePlayerBlocker() : new UnsafePlayerBlocker(unsafePlayerBlockerSection);

        ConfigurationSection resourcePackHandlerSection = configuration.getConfigurationSection("Modules.ResourcePackHandler");
        this.resourcePackHandler = resourcePackHandlerSection == null ? new ResourcePackHandler() : new ResourcePackHandler(resourcePackHandlerSection);
    }

    //

    public boolean getEnableCommand(@NotNull Class<? extends MeinkraftCommand> clazz) {
        return this.enabledCommands.get(clazz);
    }

    public @NotNull CommandBlocker getCommandBlocker() {
        return this.commandBlocker;
    }

    public @NotNull UnsafePlayerBlocker getUnsafePlayerBlocker() {
        return this.unsafePlayerBlocker;
    }

    public @NotNull ResourcePackHandler getResourcePackHandler() {
        return this.resourcePackHandler;
    }

}

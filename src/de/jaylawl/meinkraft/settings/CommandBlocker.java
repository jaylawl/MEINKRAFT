package de.jaylawl.meinkraft.settings;

import org.bukkit.configuration.ConfigurationSection;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CommandBlocker {

    public static final boolean DEFAULT_ENABLED = false;
    public static final boolean DEFAULT_SEND_FEEDBACK = true;
    public static final List<String> DEFAULT_BLOCKED_COMMANDS = Arrays.asList(
            "/example1",
            "/example2",
            "/asmanyasyoulike"
    );

    private final boolean enabled;
    private final boolean sendFeedback;
    private final List<String> blockedCommands = new ArrayList<>();

    public CommandBlocker() {
        this.enabled = DEFAULT_ENABLED;
        this.sendFeedback = DEFAULT_SEND_FEEDBACK;
    }

    public CommandBlocker(@NotNull ConfigurationSection configSection) {
        this.enabled = configSection.getBoolean("Enabled", DEFAULT_ENABLED);
        this.sendFeedback = configSection.getBoolean("SendFeedback", DEFAULT_SEND_FEEDBACK);
        this.blockedCommands.addAll(configSection.getStringList("BlockedCommands"));
    }

    //

    public boolean isEnabled() {
        return this.enabled;
    }

    public boolean sendFeedback() {
        return this.sendFeedback;
    }

    public @NotNull List<String> getBlockedCommands() {
        return this.blockedCommands;
    }

}

package de.jaylawl.meinkraft.settings;

import de.jaylawl.meinkraft.MEINKRAFT;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.configuration.ConfigurationSection;
import org.jetbrains.annotations.NotNull;

import java.util.*;
import java.util.logging.Logger;

public class ResourcePackHandler {

    public static final boolean DEFAULT_ENABLED = false;
    public static final String DEFAULT_LINK = "";
    public static final String DEFAULT_HASH = "";
    public static final boolean DEFAULT_KICK_ON_DECLINE = true;
    public static final boolean DEFAULT_KICK_ON_FAILURE = true;
    public static final long DEFAULT_TICKS_BEFORE_KICK = 20;
    public static final boolean DEFAULT_IGNORE_OPERATORS = true;
    public static final List<String> DEFAULT_IGNORED_UUIDS = Collections.emptyList();
    public static final List<String> DEFAULT_KICK_MESSAGE_STRINGS = Arrays.asList(
            "&c&lResource pack download declined or failed",
            "&r",
            "&fThe resource pack is mandatory for play on this server",
            "&fSet resource packs to &9Enabled&r or &9Prompt&r and re-connect"
    );

    private final boolean enabled;
    private final String link;
    private final String hash;
    private final boolean kickOnDecline;
    private final boolean kickOnFailure;
    private final long ticksBeforeKick;
    private final boolean ignoreOperators;
    private final List<UUID> ignoredUniqueIds = new ArrayList<>();
    private final String kickMessage;

    public ResourcePackHandler() {
        this.enabled = DEFAULT_ENABLED;
        this.link = DEFAULT_LINK;
        this.hash = DEFAULT_HASH;
        this.kickOnDecline = DEFAULT_KICK_ON_DECLINE;
        this.kickOnFailure = DEFAULT_KICK_ON_FAILURE;
        this.ticksBeforeKick = DEFAULT_TICKS_BEFORE_KICK;
        this.ignoreOperators = DEFAULT_IGNORE_OPERATORS;
        String lineSeparator = System.lineSeparator();
        StringBuilder kickMessageBuilder = new StringBuilder();
        for (String kickMessageString : DEFAULT_KICK_MESSAGE_STRINGS) {
            kickMessageString = ChatColor.translateAlternateColorCodes('&', kickMessageString);
            kickMessageString = ChatColor.translateAlternateColorCodes('§', kickMessageString);
            kickMessageBuilder.append(kickMessageString);
            kickMessageBuilder.append(lineSeparator);
        }
        this.kickMessage = kickMessageBuilder.toString();
    }

    public ResourcePackHandler(@NotNull ConfigurationSection configSection) {
        Logger logger = MEINKRAFT.inst().getLogger();
        this.enabled = configSection.getBoolean("Enabled", DEFAULT_ENABLED);
        this.link = configSection.getString("Link", DEFAULT_LINK);
        this.hash = configSection.getString("Hash", DEFAULT_HASH);
        this.kickOnDecline = configSection.getBoolean("KickOnDecline", DEFAULT_KICK_ON_DECLINE);
        this.kickOnFailure = configSection.getBoolean("KickOnFailure", DEFAULT_KICK_ON_FAILURE);
        this.ticksBeforeKick = configSection.getLong("TicksBeforeKick", DEFAULT_TICKS_BEFORE_KICK);
        this.ignoreOperators = configSection.getBoolean("IgnoreOperators", DEFAULT_IGNORE_OPERATORS);
        for (String ignoreUniqueIdString : configSection.getStringList("IgnoredUUIDs")) {
            UUID ignoredUniqueId;
            try {
                ignoredUniqueId = UUID.fromString(ignoreUniqueIdString);
            } catch (IllegalArgumentException exception) {
                logger.warning("\"" + ignoreUniqueIdString + "\" is not valid UUID format");
                continue;
            }
            this.ignoredUniqueIds.add(ignoredUniqueId);
        }
        List<String> kickMessageStrings = configSection.getStringList("KickMessage");
        if (kickMessageStrings.isEmpty()) {
            kickMessageStrings = DEFAULT_KICK_MESSAGE_STRINGS;
        }

        String lineSeparator = System.lineSeparator();
        StringBuilder kickMessageBuilder = new StringBuilder();
        for (String kickMessageString : kickMessageStrings) {
            kickMessageString = ChatColor.translateAlternateColorCodes('&', kickMessageString);
            kickMessageString = ChatColor.translateAlternateColorCodes('§', kickMessageString);
            kickMessageBuilder.append(kickMessageString);
            kickMessageBuilder.append(lineSeparator);
        }
        this.kickMessage = kickMessageBuilder.toString();
    }

    //

    public boolean isEnabled() {
        return this.enabled;
    }

    public @NotNull String getLink() {
        return this.link;
    }

    public @NotNull String getHash() {
        return this.hash;
    }

    public boolean shouldKickOnDecline() {
        return this.kickOnDecline;
    }

    public boolean shouldKickOnFailure() {
        return this.kickOnFailure;
    }

    public long getTicksBeforeKick() {
        return this.ticksBeforeKick;
    }

    public boolean ignoresOperators() {
        return this.ignoreOperators;
    }

    public @NotNull List<UUID> getIgnoredUniqueIds() {
        return this.ignoredUniqueIds;
    }

    public @NotNull String getKickMessage() {
        return this.kickMessage;
    }

}

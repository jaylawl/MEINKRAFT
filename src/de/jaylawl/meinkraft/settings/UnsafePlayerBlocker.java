package de.jaylawl.meinkraft.settings;

import net.md_5.bungee.api.ChatColor;
import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.*;

public class UnsafePlayerBlocker {

    public enum Category {
        UNSAFE_CHARACTERS("UnsafeCharacters"),
        DUPLICATE_USERNAMES("DuplicateUsernames"),
        CONTAINING_KEYWORDS("ContainingKeywords");

        private final String node;

        Category(@NotNull String node) {
            this.node = node;
        }

        //

        public @NotNull String getNode() {
            return this.node;
        }

        public @NotNull List<String> getDefaultKickMessage() {
            switch (this) {
                case UNSAFE_CHARACTERS: {
                    return Arrays.asList(
                            "&c&lConnection rejected by the server",
                            "&4Username contains disallowed character(s)",
                            "&f",
                            "&fUsernames must only contain letters, numbers and underscores",
                            "&fConsider changing your username and rejoin afterwards"
                    );
                }
                case DUPLICATE_USERNAMES: {
                    return Arrays.asList(
                            "&c&lConnection rejected by the server",
                            "&4Duplicate username",
                            "&f",
                            "&fThere already is a player with the same name on the server",
                            "&fConsider changing your username and rejoin afterwards"
                    );
                }
                case CONTAINING_KEYWORDS: {
                    return Arrays.asList(
                            "&c&lConnection rejected by the server",
                            "&4Username contains blacklisted keyword(s)",
                            "&r",
                            "&fThis server deems your username unhealthy to its environment",
                            "&fConsider changing your username and rejoin afterwards"
                    );
                }
                default: {
                    return Collections.emptyList();
                }
            }
        }

    }

    public static final boolean DEFAULT_ENABLED = false;
    public static final boolean DEFAULT_UNSAFE_CHARACTERS_BLOCK = true;
    public static final List<String> DEFAULT_UNSAFE_CHARACTERS_KICK_MESSAGE = Category.UNSAFE_CHARACTERS.getDefaultKickMessage();
    public static final boolean DEFAULT_DUPLICATE_USERNAMES_BLOCK = true;
    public static final List<String> DEFAULT_DUPLICATE_USERNAMES_KICK_MESSAGE = Category.DUPLICATE_USERNAMES.getDefaultKickMessage();
    public static final boolean DEFAULT_CONTAINING_KEYWORDS_BLOCK = false;
    public static final List<String> DEFAULT_CONTAINING_KEYWORDS_KICK_MESSAGE = Category.CONTAINING_KEYWORDS.getDefaultKickMessage();
    public static final List<String> DEFAULT_BANNED_KEYWORDS = Arrays.asList("Hitler?", "Whatever", "offends", "you", "goeshere");

    private final boolean enabled;
    private final List<String> bannedKeywords = new ArrayList<>();
    private final HashMap<Category, Boolean> enabledCategories = new HashMap<>();
    private final HashMap<Category, String> categoryKickMessages = new HashMap<>();

    public UnsafePlayerBlocker() {
        this.enabled = false;
    }

    public UnsafePlayerBlocker(@NotNull ConfigurationSection configSection) {
        String lineSeparator = System.lineSeparator();

        this.enabled = configSection.getBoolean("Enabled", false);
        this.bannedKeywords.addAll(configSection.getStringList(Category.CONTAINING_KEYWORDS.getNode() + ".Keywords"));
        for (Category category : Category.values()) {
            String node = category.getNode();
            this.enabledCategories.put(category, configSection.getBoolean(node + ".Block", true));
            List<String> kickMessageList = configSection.getStringList(node + ".KickMessage");
            if (kickMessageList.isEmpty()) {
                kickMessageList = category.getDefaultKickMessage();
            }

            StringBuilder kickMessageBuilder = new StringBuilder();
            for (String kickMessageString : kickMessageList) {
                kickMessageString = ChatColor.translateAlternateColorCodes('&', kickMessageString);
                kickMessageString = ChatColor.translateAlternateColorCodes('§', kickMessageString);
                kickMessageBuilder.append(kickMessageString);
                kickMessageBuilder.append(lineSeparator);
            }
            this.categoryKickMessages.put(category, kickMessageBuilder.toString());
        }
    }

    //

    public boolean isEnabled() {
        return this.enabled;
    }

    public @NotNull List<String> getBannedKeywords() {
        return this.bannedKeywords;
    }

    public boolean isEnabled(@NotNull Category category) {
        return this.enabledCategories.getOrDefault(category, false);
    }

    public @NotNull String getKickMessage(@NotNull Category category) {
        return this.categoryKickMessages.get(category);
    }

    public boolean shouldReject(@NotNull Category category, @NotNull String playerName) {
        switch (category) {
            case UNSAFE_CHARACTERS: {
                for (String character : playerName.split("")) {
                    if (character.matches("\\W")) {
                        return true;
                    }
                }
                break;
            }
            case DUPLICATE_USERNAMES: {
                for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
                    if (playerName.equalsIgnoreCase(onlinePlayer.getName())) {
                        return true;
                    }
                }
                break;
            }
            case CONTAINING_KEYWORDS: {
                for (String bannedKeyword : this.bannedKeywords) {
                    if (playerName.toLowerCase().contains(bannedKeyword.toLowerCase())) {
                        return true;
                    }
                }
                break;
            }
        }
        return false;
    }

}

package de.jaylawl.meinkraft.settings;

import de.jaylawl.meinkraft.MEINKRAFT;
import de.jaylawl.meinkraft.cmd.CmdOptionalType;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.logging.Logger;

public class FileUtil {

    private static final String CONFIG_SUB_PATH = "/meinkraft_settings.yml";

    private FileUtil() {
    }

    //

    public static boolean createPluginDirectory() {
        File folder = MEINKRAFT.inst().getDataFolder();
        if (!folder.exists() || !folder.isDirectory()) {
            return folder.mkdirs();
        }
        return true;
    }

    public static @NotNull File getConfigFile() {
        MEINKRAFT instance = MEINKRAFT.inst();
        Logger logger = instance.getLogger();
        if (!createPluginDirectory()) {
            logger.warning("Failed to create the plugin's data folder");
        }
        File configFile = new File(instance.getDataFolder() + CONFIG_SUB_PATH);
        if (!configFile.exists() || !configFile.isFile()) {
            try {
                if (!configFile.createNewFile()) {
                    logger.warning("Failed to create the plugin's config file");
                }
            } catch (IOException exception) {
                logger.warning("An exception occurred while trying to create the plugin's config file");
                exception.printStackTrace();
            }
        }
        return configFile;
    }

    public static @NotNull YamlConfiguration getOrCreateConfig() {
        YamlConfiguration config = new YamlConfiguration();
        File configFile = getConfigFile();
        try {
            config.load(configFile);
        } catch (InvalidConfigurationException | IOException exception) {
            exception.printStackTrace();
        }
        fillDefaultValues(config);
        try {
            config.save(configFile);
        } catch (IOException exception) {
            exception.printStackTrace();
        }
        return config;
    }

    public static void fillDefaultValues(@NotNull YamlConfiguration config) {
        HashMap<String, Object> requiredValues = new HashMap<>();

        for (CmdOptionalType optionalType : CmdOptionalType.values()) {
            requiredValues.put("Commands." + optionalType.getCommandLabel(), true);
        }

        requiredValues.put("Modules.CommandBlocker.Enabled", CommandBlocker.DEFAULT_ENABLED);
        requiredValues.put("Modules.CommandBlocker.SendFeedback", CommandBlocker.DEFAULT_SEND_FEEDBACK);
        requiredValues.put("Modules.CommandBlocker.BlockedCommands", CommandBlocker.DEFAULT_BLOCKED_COMMANDS);

        requiredValues.put("Modules.UnsafePlayerBlocker.Enabled", UnsafePlayerBlocker.DEFAULT_ENABLED);
        requiredValues.put("Modules.UnsafePlayerBlocker.UnsafeCharacters.Block", UnsafePlayerBlocker.DEFAULT_UNSAFE_CHARACTERS_BLOCK);
        requiredValues.put("Modules.UnsafePlayerBlocker.UnsafeCharacters.KickMessage", UnsafePlayerBlocker.DEFAULT_UNSAFE_CHARACTERS_KICK_MESSAGE);
        requiredValues.put("Modules.UnsafePlayerBlocker.DuplicateUsernames.Block", UnsafePlayerBlocker.DEFAULT_DUPLICATE_USERNAMES_BLOCK);
        requiredValues.put("Modules.UnsafePlayerBlocker.DuplicateUsernames.KickMessage", UnsafePlayerBlocker.DEFAULT_DUPLICATE_USERNAMES_KICK_MESSAGE);
        requiredValues.put("Modules.UnsafePlayerBlocker.ContainingKeywords.Block", UnsafePlayerBlocker.DEFAULT_CONTAINING_KEYWORDS_BLOCK);
        requiredValues.put("Modules.UnsafePlayerBlocker.ContainingKeywords.Keywords", UnsafePlayerBlocker.DEFAULT_BANNED_KEYWORDS);
        requiredValues.put("Modules.UnsafePlayerBlocker.ContainingKeywords.KickMessage", UnsafePlayerBlocker.DEFAULT_CONTAINING_KEYWORDS_KICK_MESSAGE);

        requiredValues.put("Modules.ResourcePackHandler.Enabled", ResourcePackHandler.DEFAULT_ENABLED);
        requiredValues.put("Modules.ResourcePackHandler.Link", ResourcePackHandler.DEFAULT_LINK);
        requiredValues.put("Modules.ResourcePackHandler.Hash", ResourcePackHandler.DEFAULT_HASH);
        requiredValues.put("Modules.ResourcePackHandler.KickOnDecline", ResourcePackHandler.DEFAULT_KICK_ON_DECLINE);
        requiredValues.put("Modules.ResourcePackHandler.KickOnFailure", ResourcePackHandler.DEFAULT_KICK_ON_FAILURE);
        requiredValues.put("Modules.ResourcePackHandler.TicksBeforeKick", ResourcePackHandler.DEFAULT_TICKS_BEFORE_KICK);
        requiredValues.put("Modules.ResourcePackHandler.IgnoreOperators", ResourcePackHandler.DEFAULT_IGNORE_OPERATORS);
        requiredValues.put("Modules.ResourcePackHandler.IgnoredUUIDs", ResourcePackHandler.DEFAULT_IGNORED_UUIDS);
        requiredValues.put("Modules.ResourcePackHandler.KickMessage", ResourcePackHandler.DEFAULT_KICK_MESSAGE_STRINGS);

        for (String node : requiredValues.keySet()) {
            if (config.get(node) == null) {
                config.set(node, requiredValues.get(node));
            }
        }
    }

}

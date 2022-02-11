package de.jaylawl.meinkraft.settings;

import de.jaylawl.meinkraft.Meinkraft;
import de.jaylawl.meinkraft.command.CommandIndex;
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
        File folder = Meinkraft.getInstance().getDataFolder();
        if (!folder.exists() || !folder.isDirectory()) {
            return folder.mkdirs();
        }
        return true;
    }

    public static @NotNull File getConfigFile() {
        Meinkraft instance = Meinkraft.getInstance();
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

        for (CommandIndex ci : CommandIndex.values()) {
            requiredValues.put("Commands." + ci.getCommandLabel(), true);
        }

        for (String node : requiredValues.keySet()) {
            if (config.get(node) == null) {
                config.set(node, requiredValues.get(node));
            }
        }
    }

}

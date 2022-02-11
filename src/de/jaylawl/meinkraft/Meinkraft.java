package de.jaylawl.meinkraft;

import de.jaylawl.meinkraft.command.CommandIndex;
import de.jaylawl.meinkraft.command.CommandMaster;
import de.jaylawl.meinkraft.command.MeinkraftCommand;
import de.jaylawl.meinkraft.settings.FileUtil;
import de.jaylawl.meinkraft.settings.Settings;
import de.jaylawl.meinkraft.util.DataCenter;
import de.jaylawl.meinkraft.util.MessagingUtil;
import de.jaylawl.meinkraft.util.PingUtil;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.command.PluginCommand;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.logging.Logger;

public class Meinkraft extends JavaPlugin {

    private static Meinkraft INSTANCE;

    private DataCenter dataCenter;
    private Settings settings;

    private int enabledCommands = 0;
    private int enabledListeners = 0;

    public Meinkraft() {
    }

    //

    @Override
    public void onEnable() {

        INSTANCE = this;

        this.dataCenter = new DataCenter();

        Logger logger = getLogger();
        PluginManager pluginManager = getServer().getPluginManager();

        reload(Bukkit.getConsoleSender());
        PingUtil.init();
        //

        for (CommandIndex ci : CommandIndex.values()) {

            String commandLabel = ci.getCommandLabel();
            PluginCommand pluginCommand = getCommand(commandLabel);
            if (pluginCommand == null) {
                logger.severe("Skipped enabling command \"" + commandLabel + "\"; (PluginCommand was null)");
            } else {
                if (!this.settings.isCommandEnabled(ci.getClazz())) {
                    logger.info("Skipped enabling command \"" + commandLabel + "\"; (deactivated in settings file)");
                } else {
                    MeinkraftCommand commandInstance;
                    try {
                        commandInstance = ci.getClazz().getDeclaredConstructor().newInstance();
                    } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException exception) {
                        logger.warning("An exception occurred while trying to instantiate command of type \"" + ci + "\":");
                        exception.printStackTrace();
                        continue;
                    }
                    pluginCommand.setPermission(commandInstance.getBasePermissionNode());
                    pluginCommand.setPermissionMessage(commandInstance.getNoPermissionMessage());
                    pluginCommand.setTabCompleter(commandInstance);
                    pluginCommand.setExecutor(commandInstance);

                    if (commandInstance.requiresListeners()) {
                        for (Listener listener : commandInstance.getRequiredListenerClasses()) {
                            pluginManager.registerEvents(listener, this);
                            for (Method method : listener.getClass().getMethods()) {
                                if (method.isAnnotationPresent(EventHandler.class)) {
                                    this.enabledListeners++;
                                }
                            }
                        }
                    }

                    this.enabledCommands++;
                    logger.info("Enabled \"" + commandLabel + "\" command");
                }
            }

        }

        PluginCommand masterCommand = getCommand("meinkraft");
        if (masterCommand != null) {
            CommandMaster commandMaster = new CommandMaster();
            masterCommand.setPermission(commandMaster.getBasePermissionNode());
            masterCommand.setPermissionMessage(MessagingUtil.NO_PERMISSION_MESSAGE);
            masterCommand.setTabCompleter(commandMaster);
            masterCommand.setExecutor(commandMaster);
            this.enabledCommands++;
        } else {
            logger.severe("Failed to enable the master command; disabling plugin...");
            pluginManager.disablePlugin(this);
        }

        logger.info("Enabled " + this.enabledCommands + " command(s)");
        logger.info("Enabled " + this.enabledListeners + " listener(s)");

    }

    //

    public static Meinkraft getInstance() {
        return INSTANCE;
    }

    public static DataCenter getDataCenter() {
        return INSTANCE.dataCenter;
    }

    public static Settings getSettings() {
        return INSTANCE.settings;
    }

    //

    public static int getEnabledCommands() {
        return INSTANCE.enabledCommands;
    }

    public static int getEnabledListeners() {
        return INSTANCE.enabledListeners;
    }

    //

    public void reload(@NotNull CommandSender issuer) {
        this.settings = new Settings(FileUtil.getOrCreateConfig());
        MessagingUtil.notifyExecutor(issuer, "Reloaded MEINKRAFT/meinkraft_settings.yml");
    }

}

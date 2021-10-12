package de.jaylawl.meinkraft;

import de.jaylawl.meinkraft.cmd.*;
import de.jaylawl.meinkraft.listener.bukkit.*;
import de.jaylawl.meinkraft.settings.FileUtil;
import de.jaylawl.meinkraft.settings.Settings;
import de.jaylawl.meinkraft.util.DataCenter;
import de.jaylawl.meinkraft.util.MessagingUtil;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.command.PluginCommand;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

import java.util.logging.Logger;

public class MEINKRAFT extends JavaPlugin {

    private static MEINKRAFT INSTANCE;
    private Settings settings;
    private DataCenter dataCenter;
    private int enabledCommands = 0;
    private int enabledModules = 0;
    private int enabledListeners = 0;

    @Override
    public void onEnable() {

        Logger logger = getLogger();
        PluginManager pluginManager = getServer().getPluginManager();

        INSTANCE = this;
        reload(Bukkit.getConsoleSender());
        this.dataCenter = new DataCenter();

        if (this.settings.getCommandBlocker().isEnabled()) {
            pluginManager.registerEvents(new CommandListener(), this);
            this.enabledModules++;
            this.enabledListeners++;
        }
        if (this.settings.getUnsafePlayerBlocker().isEnabled()) {
            pluginManager.registerEvents(new ConnectionListener(), this);
            this.enabledModules++;
            this.enabledListeners++;
        }
        if (this.settings.getResourcePackHandler().isEnabled()) {
            pluginManager.registerEvents(new JoinListener(), this);
            pluginManager.registerEvents(new ResourcePackListener(), this);
            this.enabledModules++;
            this.enabledListeners = (this.enabledListeners + 2);
        }

        if (this.settings.getEnableCommand(CommandFly.class)) {
            PluginCommand flyCmd = getCommand("fly");
            if (flyCmd != null) {
                flyCmd.setExecutor(new CommandFly());
                this.enabledCommands++;
            }
        }
        if (this.settings.getEnableCommand(CommandGamemode.class)) {
            PluginCommand gmCmd = getCommand("gm");
            if (gmCmd != null) {
                CommandGamemode commandGamemode = new CommandGamemode();
                gmCmd.setExecutor(commandGamemode);
                gmCmd.setTabCompleter(commandGamemode);
                this.enabledCommands++;
            }
        }
        if (this.settings.getEnableCommand(CommandGod.class)) {
            PluginCommand godCmd = getCommand("god");
            if (godCmd != null) {
                godCmd.setExecutor(new CommandGod(this.dataCenter));
                pluginManager.registerEvents(new GodListener(this.dataCenter), this);
                this.enabledCommands++;
                this.enabledListeners++;
            }
        }
        if (this.settings.getEnableCommand(CommandHeal.class)) {
            PluginCommand healCmd = getCommand("heal");
            if (healCmd != null) {
                healCmd.setExecutor(new CommandHeal());
                this.enabledCommands++;
            }
        }
        if (this.settings.getEnableCommand(CommandInvsee.class)) {
            PluginCommand invseeCmd = getCommand("invsee");
            if (invseeCmd != null) {
                CommandInvsee cmdInvsee = new CommandInvsee();
                invseeCmd.setExecutor(cmdInvsee);
                invseeCmd.setTabCompleter(cmdInvsee);
                this.enabledCommands++;
            }
        }
        if (this.settings.getEnableCommand(CommandPing.class)) {
            PluginCommand pingCmd = getCommand("ping");
            if (pingCmd != null) {
                pingCmd.setExecutor(new CommandPing());
                this.enabledCommands++;
            }
        }
        if (this.settings.getEnableCommand(CommandNightVision.class)) {
            PluginCommand nightvisionCmd = getCommand("nightvision");
            if (nightvisionCmd != null) {
                nightvisionCmd.setExecutor(new CommandNightVision());
                this.enabledCommands++;
            }
        }
        if (this.settings.getEnableCommand(CommandQuery.class)) {
            PluginCommand queryCmd = getCommand("query");
            if (queryCmd != null) {
                CommandQuery cmdQuery = new CommandQuery();
                queryCmd.setExecutor(cmdQuery);
                queryCmd.setTabCompleter(cmdQuery);
                this.enabledCommands++;
            }
        }
        if (this.settings.getEnableCommand(CommandSpeed.class)) {
            PluginCommand speedCmd = getCommand("speed");
            if (speedCmd != null) {
                CommandSpeed cmdSpeed = new CommandSpeed();
                speedCmd.setExecutor(cmdSpeed);
                speedCmd.setTabCompleter(cmdSpeed);
                this.enabledCommands++;
            }
        }
        if (this.settings.getEnableCommand(CommandStatistic.class)) {
            PluginCommand statCmd = getCommand("stat");
            if (statCmd != null) {
                CommandStatistic cmdStatistic = new CommandStatistic();
                statCmd.setExecutor(cmdStatistic);
                statCmd.setTabCompleter(cmdStatistic);
                this.enabledCommands++;
            }
        }
        if (this.settings.getEnableCommand(CommandWorld.class)) {
            PluginCommand worldCmd = getCommand("world");
            if (worldCmd != null) {
                CommandWorld cmdWorld = new CommandWorld();
                worldCmd.setExecutor(cmdWorld);
                worldCmd.setTabCompleter(cmdWorld);
                this.enabledCommands++;
            }
        }

        if (this.enabledCommands + this.enabledModules + this.enabledListeners == 0) {
            logger.severe("All commands & modules were disabled via the config file; disabling plugin...");
            pluginManager.disablePlugin(this);
            return;
        }

        PluginCommand masterCmd = getCommand("meinkraft");
        if (masterCmd != null) {
            masterCmd.setExecutor(new CommandMaster());
        } else {
            logger.severe("Failed to enable the master command; disabling plugin...");
            pluginManager.disablePlugin(this);
        }

    }

    //

    public static MEINKRAFT inst() {
        return INSTANCE;
    }

    public static Settings getSettings() {
        return INSTANCE.settings;
    }

    public static DataCenter getDataCenter() {
        return INSTANCE.dataCenter;
    }

    public static int getEnabledCommands() {
        return INSTANCE.enabledCommands;
    }

    public static int getEnabledModules() {
        return INSTANCE.enabledModules;
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

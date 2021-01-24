package de.jaylawl.meinkraft;

import de.jaylawl.meinkraft.cmd.*;
import de.jaylawl.meinkraft.listeners.*;
import de.jaylawl.meinkraft.util.DataCenter;
import org.bukkit.command.PluginCommand;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.logging.Logger;

public class Meinkraft extends JavaPlugin {

    private static Meinkraft INSTANCE;
    private DataCenter dataCenter;
    private int enabledCommands = 0;
    private int enabledModules = 0;
    private int enabledListeners = 0;

    @Override
    public void onEnable() {

        INSTANCE = this;

        this.dataCenter = new DataCenter();

        this.saveDefaultConfig();

        Logger logger = getLogger();
        PluginManager pluginManager = getServer().getPluginManager();
        FileConfiguration config = getConfig();

        if (config.getBoolean("Modules.CommandBlocker.Enabled", true)) {
            pluginManager.registerEvents(new CommandListener(), this);
            this.enabledModules++;
            this.enabledListeners++;
        }
        if (config.getBoolean("Modules.UnsafePlayerBlocker.Enabled", false)) {
            pluginManager.registerEvents(new ConnectionListener(), this);
            this.enabledModules++;
            this.enabledListeners++;
        }
        if (config.getBoolean("Modules.ResourcePackHandler.Enabled", false)) {
            pluginManager.registerEvents(new JoinListener(), this);
            pluginManager.registerEvents(new ResourcePackListener(), this);
            this.enabledModules++;
            this.enabledListeners = (this.enabledListeners + 2);
        }

        if (config.getBoolean("Commands.fly", true)) {
            PluginCommand flyCmd = getCommand("fly");
            if (flyCmd != null) {
                flyCmd.setExecutor(new CmdFly());
                this.enabledCommands++;
            }
        }
        if (config.getBoolean("Commands.gm", true)) {
            PluginCommand gmCmd = getCommand("gm");
            if (gmCmd != null) {
                CmdGamemode cmdGamemode = new CmdGamemode();
                gmCmd.setExecutor(cmdGamemode);
                gmCmd.setTabCompleter(cmdGamemode);
                this.enabledCommands++;
            }
        }
        if (config.getBoolean("Commands.god", true)) {
            PluginCommand godCmd = getCommand("god");
            if (godCmd != null) {
                godCmd.setExecutor(new CmdGod(this.dataCenter));
                pluginManager.registerEvents(new GodListener(this.dataCenter), this);
                this.enabledCommands++;
                this.enabledListeners++;
            }
        }
        if (config.getBoolean("Commands.heal", true)) {
            PluginCommand healCmd = getCommand("heal");
            if (healCmd != null) {
                healCmd.setExecutor(new CmdHeal());
                this.enabledCommands++;
            }
        }
        if (config.getBoolean("Commands.invsee", true)) {
            PluginCommand invseeCmd = getCommand("invsee");
            if (invseeCmd != null) {
                CmdInvsee cmdInvsee = new CmdInvsee();
                invseeCmd.setExecutor(cmdInvsee);
                invseeCmd.setTabCompleter(cmdInvsee);
                this.enabledCommands++;
            }
        }
        if (config.getBoolean("Commands.ping", true)) {
            PluginCommand pingCmd = getCommand("ping");
            if (pingCmd != null) {
                pingCmd.setExecutor(new CmdPing());
                this.enabledCommands++;
            }
        }
        if (config.getBoolean("Commands.nightvision", true)) {
            PluginCommand nightvisionCmd = getCommand("nightvision");
            if (nightvisionCmd != null) {
                nightvisionCmd.setExecutor(new CmdNightVision());
                this.enabledCommands++;
            }
        }
        if (config.getBoolean("Commands.query", true)) {
            PluginCommand queryCmd = getCommand("query");
            if (queryCmd != null) {
                CmdQuery cmdQuery = new CmdQuery();
                queryCmd.setExecutor(cmdQuery);
                queryCmd.setTabCompleter(cmdQuery);
                this.enabledCommands++;
            }
        }
        if (config.getBoolean("Commands.speed", true)) {
            PluginCommand speedCmd = getCommand("speed");
            if (speedCmd != null) {
                CmdSpeed cmdSpeed = new CmdSpeed();
                speedCmd.setExecutor(cmdSpeed);
                speedCmd.setTabCompleter(cmdSpeed);
                this.enabledCommands++;
            }
        }
        if (config.getBoolean("Commands.stat", true)) {
            PluginCommand statCmd = getCommand("stat");
            if (statCmd != null) {
                CmdStatistic cmdStatistic = new CmdStatistic();
                statCmd.setExecutor(cmdStatistic);
                statCmd.setTabCompleter(cmdStatistic);
                this.enabledCommands++;
            }
        }
        if (config.getBoolean("Commands.world", true)) {
            PluginCommand worldCmd = getCommand("world");
            if (worldCmd != null) {
                CmdWorld cmdWorld = new CmdWorld();
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
            masterCmd.setExecutor(new CmdMaster());
            logger.info("Enabled " + this.enabledCommands + " command(s)");
            logger.info("Enabled " + this.enabledModules + " module(s)");
            logger.info("Enabled " + this.enabledListeners + " listener(s)");
        } else {
            logger.severe("Failed to enable the master command; disabling plugin...");
            pluginManager.disablePlugin(this);
        }

    }

    @Override
    public void onDisable() {
    }

    //

    public static Meinkraft inst() {
        return INSTANCE;
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

}

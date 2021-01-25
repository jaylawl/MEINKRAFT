package de.jaylawl.meinkraft;

import de.jaylawl.meinkraft.cmd.*;
import de.jaylawl.meinkraft.listener.bukkit.*;
import de.jaylawl.meinkraft.settings.FileUtil;
import de.jaylawl.meinkraft.settings.Settings;
import de.jaylawl.meinkraft.util.DataCenter;
import org.bukkit.command.PluginCommand;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

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
        reload();
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

        if (this.settings.getEnableCommand(CmdFly.class)) {
            PluginCommand flyCmd = getCommand("fly");
            if (flyCmd != null) {
                flyCmd.setExecutor(new CmdFly());
                this.enabledCommands++;
            }
        }
        if (this.settings.getEnableCommand(CmdGamemode.class)) {
            PluginCommand gmCmd = getCommand("gm");
            if (gmCmd != null) {
                CmdGamemode cmdGamemode = new CmdGamemode();
                gmCmd.setExecutor(cmdGamemode);
                gmCmd.setTabCompleter(cmdGamemode);
                this.enabledCommands++;
            }
        }
        if (this.settings.getEnableCommand(CmdGod.class)) {
            PluginCommand godCmd = getCommand("god");
            if (godCmd != null) {
                godCmd.setExecutor(new CmdGod(this.dataCenter));
                pluginManager.registerEvents(new GodListener(this.dataCenter), this);
                this.enabledCommands++;
                this.enabledListeners++;
            }
        }
        if (this.settings.getEnableCommand(CmdHeal.class)) {
            PluginCommand healCmd = getCommand("heal");
            if (healCmd != null) {
                healCmd.setExecutor(new CmdHeal());
                this.enabledCommands++;
            }
        }
        if (this.settings.getEnableCommand(CmdInvsee.class)) {
            PluginCommand invseeCmd = getCommand("invsee");
            if (invseeCmd != null) {
                CmdInvsee cmdInvsee = new CmdInvsee();
                invseeCmd.setExecutor(cmdInvsee);
                invseeCmd.setTabCompleter(cmdInvsee);
                this.enabledCommands++;
            }
        }
        if (this.settings.getEnableCommand(CmdPing.class)) {
            PluginCommand pingCmd = getCommand("ping");
            if (pingCmd != null) {
                pingCmd.setExecutor(new CmdPing());
                this.enabledCommands++;
            }
        }
        if (this.settings.getEnableCommand(CmdNightVision.class)) {
            PluginCommand nightvisionCmd = getCommand("nightvision");
            if (nightvisionCmd != null) {
                nightvisionCmd.setExecutor(new CmdNightVision());
                this.enabledCommands++;
            }
        }
        if (this.settings.getEnableCommand(CmdQuery.class)) {
            PluginCommand queryCmd = getCommand("query");
            if (queryCmd != null) {
                CmdQuery cmdQuery = new CmdQuery();
                queryCmd.setExecutor(cmdQuery);
                queryCmd.setTabCompleter(cmdQuery);
                this.enabledCommands++;
            }
        }
        if (this.settings.getEnableCommand(CmdSpeed.class)) {
            PluginCommand speedCmd = getCommand("speed");
            if (speedCmd != null) {
                CmdSpeed cmdSpeed = new CmdSpeed();
                speedCmd.setExecutor(cmdSpeed);
                speedCmd.setTabCompleter(cmdSpeed);
                this.enabledCommands++;
            }
        }
        if (this.settings.getEnableCommand(CmdStatistic.class)) {
            PluginCommand statCmd = getCommand("stat");
            if (statCmd != null) {
                CmdStatistic cmdStatistic = new CmdStatistic();
                statCmd.setExecutor(cmdStatistic);
                statCmd.setTabCompleter(cmdStatistic);
                this.enabledCommands++;
            }
        }
        if (this.settings.getEnableCommand(CmdWorld.class)) {
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

    public void reload() {
        this.settings = new Settings(FileUtil.getOrCreateConfig());
    }

}

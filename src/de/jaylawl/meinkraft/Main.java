package de.jaylawl.meinkraft;

import de.jaylawl.meinkraft.cmd.*;
import de.jaylawl.meinkraft.listeners.*;
import org.bukkit.command.PluginCommand;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.logging.Logger;

public class Main extends JavaPlugin  {

    private static Main instance;
    private static int enabledCommands = 0;
    private static int enabledModules = 0;
    private static int enabledListeners = 0;

    @Override
    public void onEnable() {

        instance = this;
        this.saveDefaultConfig();

        Logger logger = getLogger();
        PluginManager pluginManager = getServer().getPluginManager();
        FileConfiguration config = getConfig();

        if (config.getBoolean("Modules.CommandBlocker.Enabled", true)) {
            pluginManager.registerEvents(new CommandListener(), this);
            enabledModules++;
            enabledListeners++;
        }
        if (config.getBoolean("Modules.UnsafePlayerBlocker.Enabled", false)) {
            pluginManager.registerEvents(new ConnectionListener(), this);
            enabledModules++;
            enabledListeners++;
        }
        if (config.getBoolean("Modules.ResourcePackHandler.Enabled", false)) {
            pluginManager.registerEvents(new JoinListener(), this);
            pluginManager.registerEvents(new ResourcePackListener(), this);
            enabledModules++;
            enabledListeners = (enabledListeners + 2);
        }

        if (config.getBoolean("Commands.fly", true)) {
            PluginCommand flyCmd = getCommand("fly");
            if (flyCmd != null) {
                flyCmd.setExecutor(new CmdFly());
                enabledCommands++;
            }
        }
        if (config.getBoolean("Commands.gm", true)) {
            PluginCommand gmCmd = getCommand("gm");
            if (gmCmd != null) {
                CmdGamemode cmdGamemode = new CmdGamemode();
                gmCmd.setExecutor(cmdGamemode);
                gmCmd.setTabCompleter(cmdGamemode);
                enabledCommands++;
            }
        }
        if (config.getBoolean("Commands.god", true)) {
            PluginCommand godCmd = getCommand("god");
            if (godCmd != null) {
                godCmd.setExecutor(new CmdGod());
                pluginManager.registerEvents(new GodListener(), this);
                enabledCommands++;
                enabledListeners++;
            }
        }
        if (config.getBoolean("Commands.heal", true)) {
            PluginCommand healCmd = getCommand("heal");
            if (healCmd != null) {
                healCmd.setExecutor(new CmdHeal());
                enabledCommands++;
            }
        }
        if (config.getBoolean("Commands.invsee", true)) {
            PluginCommand invseeCmd = getCommand("invsee");
            if (invseeCmd != null) {
                CmdInvsee cmdInvsee = new CmdInvsee();
                invseeCmd.setExecutor(cmdInvsee);
                invseeCmd.setTabCompleter(cmdInvsee);
                enabledCommands++;
            }
        }
        if (config.getBoolean("Commands.ping", true)) {
            PluginCommand pingCmd = getCommand("ping");
            if (pingCmd != null) {
                pingCmd.setExecutor(new CmdPing());
                enabledCommands++;
            }
        }
        if (config.getBoolean("Commands.nightvision", true)) {
            PluginCommand nightvisionCmd = getCommand("nightvision");
            if (nightvisionCmd != null) {
                nightvisionCmd.setExecutor(new CmdNightVision());
                enabledCommands++;
            }
        }
        if (config.getBoolean("Commands.query", true)) {
            PluginCommand queryCmd = getCommand("query");
            if (queryCmd != null) {
                CmdQuery cmdQuery = new CmdQuery();
                queryCmd.setExecutor(cmdQuery);
                queryCmd.setTabCompleter(cmdQuery);
                enabledCommands++;
            }
        }
        if (config.getBoolean("Commands.speed", true)) {
            PluginCommand speedCmd = getCommand("speed");
            if (speedCmd != null) {
                CmdSpeed cmdSpeed = new CmdSpeed();
                speedCmd.setExecutor(cmdSpeed);
                speedCmd.setTabCompleter(cmdSpeed);
                enabledCommands++;
            }
        }
        if (config.getBoolean("Commands.world", true)) {
            PluginCommand worldCmd = getCommand("world");
            if (worldCmd != null) {
                CmdWorld cmdWorld = new CmdWorld();
                worldCmd.setExecutor(cmdWorld);
                worldCmd.setTabCompleter(cmdWorld);
                enabledCommands++;
            }
        }

        if (enabledCommands + enabledModules + enabledListeners > 0) {
            PluginCommand masterCmd = getCommand("mk");
            if (masterCmd != null) {
                masterCmd.setExecutor(new CmdMaster());
                logger.info("Enabled " + enabledCommands + " command(s)");
                logger.info("Enabled " + enabledModules + " module(s)");
                logger.info("Enabled " + enabledListeners + " listener(s)");
            } else {
                logger.info("§cFailed to enable the master command; disabling plugin");
                pluginManager.disablePlugin(this);
            }
        } else {
            logger.info("§cAll commands & modules were disabled via config.yml; disabling plugin");
            pluginManager.disablePlugin(this);
        }

    }

    public static Main inst() {
        return instance;
    }

    public static int getEnabledCommands() {
        return enabledCommands;
    }

    public static int getEnabledModules() {
        return enabledModules;
    }

    public static int getEnabledListeners() {
        return enabledListeners;
    }

}

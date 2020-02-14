package de.jaylawl.meinkraft;

import de.jaylawl.meinkraft.cmd.*;
import de.jaylawl.meinkraft.listeners.*;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin  {

    private static Main instance;
    private static int enabledCommands = 0;
    private static int enabledModules = 0;
    private static int enabledListeners = 0;

    @Override
    public void onEnable() {

        instance = this;
        this.saveDefaultConfig();
        PluginManager pm = getServer().getPluginManager();
        FileConfiguration config = getConfig();

        if (config.getBoolean("Modules.CommandBlocker.Enabled", true)) {
            pm.registerEvents(new EvtBlockCommand(), this);
            enabledModules++;
            enabledListeners++;
        }
        if (config.getBoolean("Modules.UnsafePlayerBlocker.Enabled", false)) {
            pm.registerEvents(new EvtBlockPlayer(), this);
            enabledModules++;
            enabledListeners++;
        }

        if (config.getBoolean("Modules.ResourcePackHandler.Enabled", false)) {
            pm.registerEvents(new EvtJoin(), this);
            pm.registerEvents(new EvtResourcePackStatus(), this);
            enabledModules++;
            enabledListeners = (enabledListeners + 2);
        }

        if (config.getBoolean("Commands.fly", true)) {
            getCommand("fly").setExecutor(new CmdFly());
            enabledCommands++;
        }
        if (config.getBoolean("Commands.gm", true)) {
            CmdGamemode cmdGamemode = new CmdGamemode();
            getCommand("gm").setExecutor(cmdGamemode);
            getCommand("gm").setTabCompleter(cmdGamemode);
            enabledCommands++;
        }
        if (config.getBoolean("Commands.god", true)) {
            getCommand("god").setExecutor(new CmdGod());
            pm.registerEvents(new GodListener(), this);
            enabledCommands++;
            enabledListeners++;
        }
        if (config.getBoolean("Commands.heal", true)) {
            getCommand("heal").setExecutor(new CmdHeal());
            enabledCommands++;
        }
        if (config.getBoolean("Commands.invsee", true)) {
            CmdInvsee cmdInvsee = new CmdInvsee();
            getCommand("invsee").setExecutor(cmdInvsee);
            getCommand("invsee").setTabCompleter(cmdInvsee);
            enabledCommands++;
        }
        if (config.getBoolean("Commands.ping", true)) {
            getCommand("ping").setExecutor(new CmdPing());
            enabledCommands++;
        }
        if (config.getBoolean("Commands.nightvision", true)) {
            getCommand("nightvision").setExecutor(new CmdNightVision());
            enabledCommands++;
        }
        if (config.getBoolean("Commands.query", true)) {
            CmdQuery cmdQuery = new CmdQuery();
            getCommand("query").setExecutor(cmdQuery);
            getCommand("query").setTabCompleter(cmdQuery);
            enabledCommands++;
        }
        if (config.getBoolean("Commands.speed", true)) {
            CmdSpeed cmdSpeed = new CmdSpeed();
            getCommand("speed").setExecutor(cmdSpeed);
            getCommand("speed").setTabCompleter(cmdSpeed);
            enabledCommands++;
        }
        if (config.getBoolean("Commands.world", true)) {
            CmdWorld cmdWorld = new CmdWorld();
            getCommand("world").setExecutor(cmdWorld);
            getCommand("world").setTabCompleter(cmdWorld);
            enabledCommands++;
        }

        if (enabledCommands + enabledModules + enabledListeners > 0) {
            getCommand("mk").setExecutor(new CmdMaster());
            getLogger().info("Enabled " + enabledCommands + " command(s)");
            getLogger().info("Enabled " + enabledModules + " module(s)");
            getLogger().info("Enabled " + enabledListeners + " listener(s)");
        } else {
            getLogger().info("§cAll commands & modules were disabled via config.yml; disabling plugin");
            pm.disablePlugin(this);
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

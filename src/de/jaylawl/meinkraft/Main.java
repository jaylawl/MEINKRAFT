package de.jaylawl.meinkraft;

import de.jaylawl.meinkraft.cmd.*;
import de.jaylawl.meinkraft.events.*;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.List;

public class Main extends JavaPlugin  {

    private static Main instance;
    private static List<String> enabledCommands = new ArrayList<>();
    private static int enabledModules = 0;
    private static int enabledListeners = 0;

    @Override
    public void onEnable() {

        instance = this;
        this.saveDefaultConfig();
        PluginManager pm = getServer().getPluginManager();
        FileConfiguration config = getConfig();

        if (config.getBoolean("Modules.CommandBlocker.Active", true)) {
            pm.registerEvents(new EvtBlockCommand(), this);
            enabledModules++;
            enabledListeners++;
        }
        if (config.getBoolean("Modules.UnsafePlayerBlocker.Active", false)) {
            pm.registerEvents(new EvtBlockPlayer(), this);
            enabledModules++;
            enabledListeners++;
        }

        if (config.getBoolean("Modules.ResourcePackHandler.Active", false)) {
            pm.registerEvents(new EvtJoin(), this);
            pm.registerEvents(new EvtResourcePackStatus(), this);
            enabledModules++;
            enabledListeners = (enabledListeners + 2);
        }

        if (config.getBoolean("Commands.fly", true)) {
            getCommand("fly").setExecutor(new CmdFly());
            enabledCommands.add("/fly");
        }
        if (config.getBoolean("Commands.gm", true)) {
            CmdGamemode cmdGamemode = new CmdGamemode();
            getCommand("gm").setExecutor(cmdGamemode);
            getCommand("gm").setTabCompleter(cmdGamemode);
            enabledCommands.add("/gm");
        }
        if (config.getBoolean("Commands.invsee", true)) {
            CmdInvsee cmdInvsee = new CmdInvsee();
            getCommand("invsee").setExecutor(cmdInvsee);
            getCommand("invsee").setTabCompleter(cmdInvsee);
            enabledCommands.add("/invsee");
        }
        if (config.getBoolean("Commands.ping", true)) {
            getCommand("ping").setExecutor(new CmdPing());
            enabledCommands.add("/ping");
        }
        if (config.getBoolean("Commands.speed", true)) {
            CmdSpeed cmdSpeed = new CmdSpeed();
            getCommand("speed").setExecutor(cmdSpeed);
            getCommand("speed").setTabCompleter(cmdSpeed);
            enabledCommands.add("/speed");
        }
        if (config.getBoolean("Commands.god", true)) {
            getCommand("god").setExecutor(new CmdGod());
            pm.registerEvents(new EvtGod(), this);
            enabledCommands.add("/god");
            enabledListeners++;
        }
        if (config.getBoolean("Commands.world", true)) {
            CmdWorld cmdWorld = new CmdWorld();
            getCommand("world").setExecutor(cmdWorld);
            getCommand("world").setTabCompleter(cmdWorld);
            enabledCommands.add("/world");
        }

        if (enabledCommands.size() + enabledModules + enabledListeners > 0) {
            getCommand("mk").setExecutor(new CmdMaster());
            getLogger().info("Enabled " + enabledCommands.size() + " command(s)");
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

    public static List<String> getEnabledCommands() {
        return enabledCommands;
    }

    public static int getEnabledModules() {
        return enabledModules;
    }

    public static int getEnabledListeners() {
        return enabledListeners;
    }

}

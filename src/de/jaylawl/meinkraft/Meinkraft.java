package de.jaylawl.meinkraft;

import de.jaylawl.meinkraft.command.*;
import de.jaylawl.meinkraft.listener.bukkit.GodModeListener;
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

        //

        if (this.settings.getEnableCommand(CommandFly.class)) {
            PluginCommand flyCommand = getCommand("fly");
            if (flyCommand != null) {
                flyCommand.setPermission(CommandFly.PERMISSION_NODE);
                flyCommand.setPermissionMessage(MessagingUtil.NO_PERMISSION_MESSAGE);
                CommandFly commandFly = new CommandFly();
                flyCommand.setTabCompleter(commandFly);
                flyCommand.setExecutor(commandFly);
                this.enabledCommands++;
                logger.info("Enabled \"fly\" command");
            }
        } else {
            logger.info("Skipped enabling \"fly\" command");
        }

        if (this.settings.getEnableCommand(CommandGameMode.class)) {
            PluginCommand gamemodeCommand = getCommand("gm");
            if (gamemodeCommand != null) {
                gamemodeCommand.setPermission(CommandGameMode.PERMISSION_NODE);
                gamemodeCommand.setPermissionMessage(MessagingUtil.NO_PERMISSION_MESSAGE);
                CommandGameMode commandGameMode = new CommandGameMode();
                gamemodeCommand.setExecutor(commandGameMode);
                gamemodeCommand.setTabCompleter(commandGameMode);
                this.enabledCommands++;
                logger.info("Enabled \"gm\" command");
            }
        } else {
            logger.info("Skipped enabling \"gm\" command");
        }

        if (this.settings.getEnableCommand(CommandGodMode.class)) {
            PluginCommand godModeCommand = getCommand("godmode");
            if (godModeCommand != null) {
                godModeCommand.setPermission(CommandGodMode.PERMISSION_NODE);
                godModeCommand.setPermissionMessage(MessagingUtil.NO_PERMISSION_MESSAGE);
                CommandGodMode commandGodMode = new CommandGodMode();
                godModeCommand.setTabCompleter(commandGodMode);
                godModeCommand.setExecutor(commandGodMode);
                pluginManager.registerEvents(new GodModeListener(), this);
                this.enabledListeners += 2; // TODO: 13.10.2021 programmatically get the amount of added listeners in the class
                this.enabledCommands++;
                logger.info("Enabled \"godmode\" command");
            }
        } else {
            logger.info("Skipped enabling \"godmode\" command");
        }

        if (this.settings.getEnableCommand(CommandHeal.class)) {
            PluginCommand healCommand = getCommand("heal");
            if (healCommand != null) {
                healCommand.setPermission(CommandHeal.PERMISSION_NODE);
                healCommand.setPermissionMessage(MessagingUtil.NO_PERMISSION_MESSAGE);
                CommandHeal commandHeal = new CommandHeal();
                healCommand.setTabCompleter(commandHeal);
                healCommand.setExecutor(commandHeal);
                this.enabledCommands++;
                logger.info("Enabled \"heal\" command");
            }
        } else {
            logger.info("Skipped enabling \"heal\" command");
        }

        if (this.settings.getEnableCommand(CommandInventorySpy.class)) {
            PluginCommand inventorySpyCommand = getCommand("inventoryspy");
            if (inventorySpyCommand != null) {
                inventorySpyCommand.setPermission(CommandInventorySpy.PERMISSION_NODE);
                inventorySpyCommand.setPermissionMessage(MessagingUtil.NO_PERMISSION_MESSAGE);
                CommandInventorySpy commandInventorySpy = new CommandInventorySpy();
                inventorySpyCommand.setExecutor(commandInventorySpy);
                inventorySpyCommand.setTabCompleter(commandInventorySpy);
                this.enabledCommands++;
                logger.info("Enabled \"inventoryspy\" command");
            }
        } else {
            logger.info("Skipped enabling \"inventoryspy\" command");
        }

        if (this.settings.getEnableCommand(CommandPing.class)) {
            PluginCommand pingCommand = getCommand("ping");
            if (pingCommand != null) {
                pingCommand.setPermission(CommandPing.PERMISSION_NODE);
                pingCommand.setPermissionMessage(MessagingUtil.NO_PERMISSION_MESSAGE);
                CommandPing commandPing = new CommandPing();
                pingCommand.setTabCompleter(commandPing);
                pingCommand.setExecutor(commandPing);
                this.enabledCommands++;
                logger.info("Enabled \"ping\" command");
            }
        } else {
            logger.info("Skipped enabling \"ping\" command");
        }

        if (this.settings.getEnableCommand(CommandNightVision.class)) {
            PluginCommand nightvisionCommand = getCommand("nightvision");
            if (nightvisionCommand != null) {
                CommandNightVision commandNightVision = new CommandNightVision();
                nightvisionCommand.setTabCompleter(commandNightVision);
                nightvisionCommand.setExecutor(commandNightVision);
                this.enabledCommands++;
            }
        }

        if (this.settings.getEnableCommand(CommandQuery.class)) {
            PluginCommand queryCommand = getCommand("query");
            if (queryCommand != null) {
                queryCommand.setPermission(CommandQuery.PERMISSION_NODE);
                queryCommand.setPermissionMessage(MessagingUtil.NO_PERMISSION_MESSAGE);
                CommandQuery commandQuery = new CommandQuery();
                queryCommand.setExecutor(commandQuery);
                queryCommand.setTabCompleter(commandQuery);
                this.enabledCommands++;
                logger.info("Enabled \"query\" command");
            }
        } else {
            logger.info("Skipped enabling \"query\" command");
        }

        if (this.settings.getEnableCommand(CommandSpeed.class)) {
            PluginCommand speedCommand = getCommand("speed");
            if (speedCommand != null) {
                CommandSpeed cmdSpeed = new CommandSpeed();
                speedCommand.setExecutor(cmdSpeed);
                speedCommand.setTabCompleter(cmdSpeed);
                this.enabledCommands++;
            }
        }

        if (this.settings.getEnableCommand(CommandStatistic.class)) {
            PluginCommand statisticCommand = getCommand("stat");
            if (statisticCommand != null) {
                CommandStatistic cmdStatistic = new CommandStatistic();
                statisticCommand.setExecutor(cmdStatistic);
                statisticCommand.setTabCompleter(cmdStatistic);
                this.enabledCommands++;
            }
        }

        if (this.settings.getEnableCommand(CommandWorld.class)) {
            PluginCommand worldCommand = getCommand("world");
            if (worldCommand != null) {
                worldCommand.setPermission(CommandWorld.PERMISSION_NODE);
                worldCommand.setPermissionMessage(MessagingUtil.NO_PERMISSION_MESSAGE);
                CommandWorld commandWorld = new CommandWorld();
                worldCommand.setExecutor(commandWorld);
                worldCommand.setTabCompleter(commandWorld);
                this.enabledCommands++;
                logger.info("Enabled \"world\" command");
            }
        } else {
            logger.info("Skipped enabling \"world\" command");
        }

        logger.info("Enabled " + this.enabledCommands + " command(s)");
        logger.info("Enabled " + this.enabledListeners + " listener(s)");

        PluginCommand masterCommand = getCommand("meinkraft");
        if (masterCommand != null) {
            masterCommand.setPermission(CommandMaster.PERMISSION_NODE);
            masterCommand.setPermissionMessage(MessagingUtil.NO_PERMISSION_MESSAGE);
            CommandMaster commandMaster = new CommandMaster();
            masterCommand.setTabCompleter(commandMaster);
            masterCommand.setExecutor(commandMaster);
        } else {
            logger.severe("Failed to enable the master command; disabling plugin...");
            pluginManager.disablePlugin(this);
        }

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

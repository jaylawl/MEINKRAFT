package de.jaylawl.meinkraft.cmd;

import de.jaylawl.meinkraft.util.CmdPermission;
import de.jaylawl.meinkraft.util.MessagingUtil;
import de.jaylawl.meinkraft.util.TabHelper;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.Statistic;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

public class CommandStatistic implements CommandMeinkraft {

    private final List<String> statisticStrings = new ArrayList<>();
    private final HashMap<Statistic.Type, List<String>> subStatisticStrings = new HashMap<>();

    public CommandStatistic() {
        for (Statistic statistic : Statistic.values()) {
            String statisticString = statistic.toString().toLowerCase();
            this.statisticStrings.add(statisticString);
        }
        List<String> itemMaterialStrings = new ArrayList<>();
        List<String> blockMaterialStrings = new ArrayList<>();
        List<String> entityTypeStrings = new ArrayList<>();
        for (Material material : Material.values()) {
            String materialString = material.toString().toLowerCase();
            if (material.isItem()) {
                itemMaterialStrings.add(materialString);
            } else if (material.isBlock()) {
                blockMaterialStrings.add(materialString);
            }
        }
        for (EntityType entityType : EntityType.values()) {
            entityTypeStrings.add(entityType.toString().toLowerCase());
        }
        this.subStatisticStrings.put(Statistic.Type.ITEM, itemMaterialStrings);
        this.subStatisticStrings.put(Statistic.Type.BLOCK, blockMaterialStrings);
        this.subStatisticStrings.put(Statistic.Type.ENTITY, entityTypeStrings);
    }

    //

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String label, @NotNull String[] arguments) {

        if (!CmdPermission.hasAny(commandSender, label)) {
            return Collections.emptyList();
        }

        int argumentNumber = TabHelper.getArgumentNumber(arguments);
        List<String> completions = new ArrayList<>();

        switch (argumentNumber) {
            case 1: {
                for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
                    completions.add(onlinePlayer.getName());
                }
                break;
            }
            case 2: {
                completions.addAll(this.statisticStrings);
                break;
            }
            case 3: {
                Statistic statistic;
                try {
                    statistic = Statistic.valueOf(arguments[1].toUpperCase());
                } catch (IllegalArgumentException exception) {
                    return Collections.emptyList();
                }
                if (statistic.isSubstatistic()) {
                    completions.addAll(this.subStatisticStrings.get(statistic.getType()));
                }
                break;
            }
            default: {
                return Collections.emptyList();
            }
        }

        return TabHelper.sortedCompletions(arguments[argumentNumber - 1], completions);
    }

    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String label, @NotNull String[] arguments) {

        if (!CmdPermission.hasAny(commandSender, label)) {
            MessagingUtil.noPermission(commandSender);
            return true;
        }

        OfflinePlayer offlineAffectedPlayer;
        Statistic statistic;

        if (arguments.length == 0) {
            commandSender.sendMessage(ChatColor.GREEN + "/stat " + ChatColor.RED + "<player> <statistic> " + ChatColor.GRAY + "[]");
            return true;
        } else {
            offlineAffectedPlayer = Bukkit.getOfflinePlayerIfCached(arguments[0]);
            if (offlineAffectedPlayer == null || !offlineAffectedPlayer.hasPlayedBefore()) {
                Player player = Bukkit.getPlayer(arguments[0]);
                if (player == null) {
                    commandSender.sendMessage(ChatColor.WHITE + "No player of name \"" + arguments[0] + "\" has played on the server before");
                    return true;
                }
                offlineAffectedPlayer = player;
            }
        }

        if (arguments.length < 2) {
            commandSender.sendMessage(ChatColor.GREEN + "/stat " + offlineAffectedPlayer.getName() + " " + ChatColor.RED + "<statistic> " + ChatColor.GRAY + "[]");
            return true;
        }

        try {
            statistic = Statistic.valueOf(arguments[1].toUpperCase());
        } catch (IllegalArgumentException exception) {
            commandSender.sendMessage(ChatColor.RED + "\"" + arguments[1] + "\" is not a valid statistic");
            return true;
        }

        String statisticString = statistic.toString().toLowerCase();
        Integer statisticValue = null;

        if (!statistic.isSubstatistic()) {
            statisticValue = offlineAffectedPlayer.getStatistic(statistic);

        } else {

            Statistic.Type statisticType = statistic.getType();

            if (arguments.length < 3) {
                String requiredArgument = "";
                switch (statisticType) {
                    case ITEM: {
                        requiredArgument = "item";
                        break;
                    }
                    case BLOCK: {
                        requiredArgument = "block";
                        break;
                    }
                    case ENTITY: {
                        requiredArgument = "entity-type";
                        break;
                    }
                }
                commandSender.sendMessage(ChatColor.GREEN + "/stat " + offlineAffectedPlayer.getName() + " " + statistic.toString().toLowerCase() + " " + ChatColor.RED + "<" + requiredArgument + ">");
                return true;

            } else {

                List<String> lookupList = this.subStatisticStrings.get(statisticType);
                String lookupString = arguments[2];
                if (lookupString.startsWith("minecraft:")) {
                    lookupString = lookupString.replace("minecraft:", "");
                }
                if (!lookupList.contains(lookupString)) {
                    commandSender.sendMessage(ChatColor.RED + "\"" + arguments[2] + "\" is not a valid parameter for this statistic");
                    return true;
                }

                switch (statisticType) {
                    case ITEM:
                    case BLOCK: {
                        String materialString = lookupString.replace("minecraft:", "");
                        Material material;
                        try {
                            material = Material.valueOf(materialString.toUpperCase());
                        } catch (IllegalArgumentException exception) {
                            commandSender.sendMessage(ChatColor.RED + "\"" + arguments[2] + "\" is not a valid material");
                            return true;
                        }
                        statisticValue = offlineAffectedPlayer.getStatistic(statistic, material);
                        statisticString = statisticString + ":" + materialString.toLowerCase();
                        break;
                    }
                    case ENTITY: {
                        String entityTypeString = lookupString.replace("minecraft:", "");
                        EntityType entityType;
                        try {
                            entityType = EntityType.valueOf(entityTypeString.toUpperCase());
                        } catch (IllegalArgumentException exception) {
                            commandSender.sendMessage(ChatColor.RED + "\"" + arguments[2] + "\" is not a valid entity type");
                            return true;
                        }
                        statisticValue = offlineAffectedPlayer.getStatistic(statistic, entityType);
                        statisticString = statisticString + ":" + entityTypeString.toLowerCase();
                        break;
                    }
                }

            }
        }

        if (statisticValue == null) {
            commandSender.sendMessage(ChatColor.RED + "Unexpected error; unable to retrieve statistic value...");
        } else {
            commandSender.sendMessage(ChatColor.WHITE + "Player: " + offlineAffectedPlayer.getName() + ", Stat: \"" + statisticString + "\", Value: " + statisticValue);
        }

        return true;
    }

}

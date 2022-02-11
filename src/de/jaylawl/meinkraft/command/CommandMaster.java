package de.jaylawl.meinkraft.command;

import de.jaylawl.meinkraft.Meinkraft;
import de.jaylawl.meinkraft.command.util.TabHelper;
import de.jaylawl.meinkraft.util.HelpBook;
import de.jaylawl.meinkraft.util.MessagingUtil;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class CommandMaster implements MeinkraftCommand {

    public static final String PERMISSION_NODE = "mk.admin";

    public CommandMaster() {
    }

    //

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String label, @NotNull String[] arguments) {

        int argumentNumber = TabHelper.getArgumentNumber(arguments);

        if (argumentNumber == 1) {
            List<String> completions = new ArrayList<>();
            completions.add("help");
            completions.add("reload");
            return TabHelper.sortedCompletions(arguments[argumentNumber - 1], completions);

        } else {
            return Collections.emptyList();

        }

    }

    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String label, @NotNull String[] arguments) {

        switch (arguments.length > 0 ? arguments[0].toLowerCase() : "") {

            case "help" -> {
                if (commandSender instanceof Player player) {
                    player.openBook(HelpBook.make());
                } else {
                    MessagingUtil.ingameExclusive(commandSender);
                }
            }

            case "reload" -> {
                Meinkraft.getInstance().reload(commandSender);
            }

            default -> {
                MessagingUtil.genericError(commandSender, "Unknown/missing argument(s)");
            }

        }

        return true;
    }

    @Override
    public @NotNull String getBasePermissionNode() {
        return PERMISSION_NODE;
    }

}



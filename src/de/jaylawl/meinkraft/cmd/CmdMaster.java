package de.jaylawl.meinkraft.cmd;

import de.jaylawl.meinkraft.MEINKRAFT;
import de.jaylawl.meinkraft.util.CmdPermission;
import de.jaylawl.meinkraft.util.HelpBook;
import de.jaylawl.meinkraft.util.MessagingUtil;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class CmdMaster implements CmdMeinkraft {

    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String label, @NotNull String[] arguments) {

        if (!CmdPermission.hasAny(commandSender, "admin")) {
            MessagingUtil.noPermission(commandSender);
            return true;
        }

        String mainArgument;

        if (arguments.length == 0) {
            mainArgument = "help";
        } else {
            mainArgument = arguments[0];
        }

        switch (mainArgument) {

            case "reload":
            case "r": {
                MEINKRAFT.inst().reload(commandSender);
                break;
            }

            case "help":
            case "h":
            case "?":
            default: {
                if (!(commandSender instanceof Player)) {
                    MessagingUtil.ingameExclusive(commandSender);
                } else {
                    ((Player) commandSender).openBook(HelpBook.make());
                }
                break;
            }

        }

        return true;
    }

}



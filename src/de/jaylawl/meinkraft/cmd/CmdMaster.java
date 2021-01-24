package de.jaylawl.meinkraft.cmd;

import de.jaylawl.meinkraft.Meinkraft;
import de.jaylawl.meinkraft.util.CmdPermission;
import de.jaylawl.meinkraft.util.MessagingUtil;
import de.jaylawl.meinkraft.util.HelpBook;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class CmdMaster implements CommandExecutor {

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String label, @NotNull String[] args) {

        if (!CmdPermission.hasAny(sender, "admin")) {
            MessagingUtil.noPermission(sender);
            return true;
        }

        String mainArg;

        if (args.length == 0) {
            mainArg = "help";
        } else {
            mainArg = args[0];
        }

        switch (mainArg) {

            case "reload":
            case "r":
                Meinkraft.inst().reloadConfig();
                MessagingUtil.feedback(sender, "Successfully reloaded MEINKRAFT/config.yml");
                break;

            case "help":
            case "h":
            case "?":
            default:
                if (!(sender instanceof Player)) {
                    MessagingUtil.ingameExclusive(sender);
                    return true;
                }
                ((Player) sender).openBook(HelpBook.make());
                break;

        }

        return true;
    }

}



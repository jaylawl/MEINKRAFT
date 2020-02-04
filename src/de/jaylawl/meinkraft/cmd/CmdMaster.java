package de.jaylawl.meinkraft.cmd;

import de.jaylawl.meinkraft.Main;
import de.jaylawl.meinkraft.util.CmdPermission;
import de.jaylawl.meinkraft.util.Messaging;
import de.jaylawl.meinkraft.util.WikiBook;
import net.md_5.bungee.api.chat.*;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BookMeta;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;

public class CmdMaster implements CommandExecutor {

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String label, @NotNull String[] args) {

        if (!CmdPermission.hasAny(sender, "admin")) {
            Messaging.noPermission(sender);
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
                Main.inst().reloadConfig();
                Messaging.feedback(sender, "Successfully reloaded MEINKRAFT/config.yml");
                break;

            case "wiki":
            case "w":

                if (!(sender instanceof Player)) {
                    Messaging.ingameExclusive(sender);
                    return true;
                }

                ((Player) sender).openBook(WikiBook.make());

                break;

            case "help":
            case "h":
            case "?":
            default:

                sender.sendMessage("§r ");
                sender.sendMessage("§8 ||  " + Messaging.getPluginColor() + "§lMEINKRAFT");
                sender.sendMessage("§8 ||  §7by jaylawl, version: " + Main.inst().getDescription().getVersion());
                sender.sendMessage("§8 ||");
                sender.sendMessage("§8 ||  §f/mk h[elp]");
                sender.sendMessage("§8 ||  §f/mk s[tats]");
                sender.sendMessage("§8 ||  §f/mk r[eload]");
                sender.sendMessage("§8 ||");
                sender.sendMessage("§r ");
                break;

        }

        return true;
    }

}



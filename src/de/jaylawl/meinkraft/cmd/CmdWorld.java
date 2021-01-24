package de.jaylawl.meinkraft.cmd;

import de.jaylawl.meinkraft.util.CmdPermission;
import de.jaylawl.meinkraft.util.MessagingUtil;
import de.jaylawl.meinkraft.util.TabHelper;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class CmdWorld implements CommandExecutor, TabCompleter {

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String label, @NotNull String[] args) {

        if (!CmdPermission.hasAny(sender, label)) {
            return Collections.emptyList();
        }

        int argN = TabHelper.getArgNumber(args);
        List<String> completions = new ArrayList<>();

        switch (argN) {
            case 1:
                for (World w : Bukkit.getWorlds()) {
                    completions.add(w.getName());
                }
                break;
            case 2:
                for (Player p : Bukkit.getOnlinePlayers()) {
                    completions.add(p.getName());
                }
                break;
            default:
                return Collections.emptyList();
        }

        return TabHelper.sortedCompletions(args[argN - 1], completions);

    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String label, @NotNull String[] args) {

        if (!CmdPermission.hasAny(sender, label)) {
            MessagingUtil.noPermission(sender);
            return true;
        }

        Player affectedPlayer;
        boolean senderEqualsAffected = false;
        World world;

        if (args.length < 1) {
            MessagingUtil.genericError(sender, "Missing world argument");
            return true;
        } else if (args.length < 2) {
            if (sender instanceof Player) {
                affectedPlayer = (Player) sender;
            } else {
                MessagingUtil.genericError(sender, "Missing player argument");
                return true;
            }
        } else {
            affectedPlayer = Bukkit.getPlayer(args[1]);
        }

        world = Bukkit.getWorld(args[0]);

        if (world == null) {
            MessagingUtil.invalidArguments(sender, args[0], "is not a loaded world");
            return true;
        }
        if (affectedPlayer == null) {
            MessagingUtil.invalidArguments(sender, args[1], "is not an online player");
            return true;
        }

        if (sender instanceof Player && sender == affectedPlayer) {
            senderEqualsAffected = true;
            if (!CmdPermission.hasOthers(sender, label)) {
                MessagingUtil.noPermissionOthers(sender);
                return true;
            }
        }

        affectedPlayer.teleport(world.getSpawnLocation().add(0.5, 0, 0.5));
        MessagingUtil.feedback(sender, "Sent player " + affectedPlayer.getName() + " to world \"" + world.getName() + "\"");
        if (!senderEqualsAffected) {
            MessagingUtil.notifyPlayer(affectedPlayer, "A wizard has poofed you into another world");
        }

        return true;
    }
}

package de.jaylawl.meinkraft.util;

import de.jaylawl.meinkraft.Meinkraft;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BookMeta;

public class HelpBook {

    public static ItemStack make() {

        ItemStack book = new ItemStack(Material.WRITTEN_BOOK);
        BookMeta b = ((BookMeta) book.getItemMeta());

        b.setGeneration(BookMeta.Generation.ORIGINAL);
        b.setAuthor("jaylawl");
        b.setTitle("MEINKRAFT ingame manual");

        b.spigot().addPage(
                new ComponentBuilder("")
                        .append(MessagingUtil.getPluginColor() + "§lMEINKRAFT§r\n" +
                                "§8version " + Meinkraft.getInstance().getDescription().getVersion() + "\n" +
                                "§8created by §rjaylawl")
                        .append("\n\n").reset()
                        .append("§8§l>§r §rReload plugin§r")
                        .event(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/mk reload"))
                        .event(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder("Click here to run\nor use \"/mk reload\"").create()))
                        .append("\n\n").reset()
                        .append("Enabled:              ")
                        .append("§7[§6?§7]§r")
                        .event(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder("(De-)activating commands and/or\nmodules requires a server restart").create()))
                        .append("\n").reset()
                        .append("  Commands: " + Meinkraft.getEnabledCommands() + "\n" +
                                "  Modules: 0" + "\n" +
                                "  Listeners: " + Meinkraft.getEnabledListeners() + "\n\n")
                        .append("§8§l>§r §9Donate via PayPal§r\n")
                        .event(new ClickEvent(ClickEvent.Action.OPEN_URL, "http://paypal.me/langejulian"))
                        .event(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder("Click here to donate to the developers\nOpens your browser to paypal.me").create()))
                        .append("").reset()
                        .append("§8§l>§r §6Rate on Spigot§r\n")
                        .event(new ClickEvent(ClickEvent.Action.OPEN_URL, "https://www.spigotmc.org/resources/meinkraft.74914/"))
                        .event(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder("Click here to rate the plugin\nOpens your browser to spigotmc.org").create()))
                        .append("").reset()
                        .append("§8§l>§r §cReport an issue§r\n")
                        .event(new ClickEvent(ClickEvent.Action.OPEN_URL, "https://github.com/jaylawl/MEINKRAFT/issues"))
                        .event(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder("Click here to report bugs & grievances\nOpens your browser to github.com").create()))
                        .append("").reset()
                        .create()
        );

        b.addPage(
                "§lColor code chart§r\n\n" +
                        "&0 §0BLACK§r | &1 §1BLUE§r\n" +
                        "&2 §2GREEN§r | &3 §3CYAN§r\n" +
                        "&4 §4RED§r | &5 §5PURPLE§r\n" +
                        "&6 §6ORANGE§r | &7 §7L.GRAY§r\n" +
                        "&8 §8GRAY§r | &9 §9L.BLUE§r\n" +
                        "&a §aLIME§r | &b §bAQUA§r\n" +
                        "&c §cL.RED§r | &d §dPINK§r\n" +
                        "&e §eYELLOW§r | &f §fWHITE§r\n" +
                        "&l §lBOLD§r | &k §kFUCK§r\n" +
                        "&n §nUNDER§r | &m §mSTRIKE§r\n" +
                        "&o §oITALIC§r | &r RESET\n"
        );

        book.setItemMeta(b);

        return book;
    }

}

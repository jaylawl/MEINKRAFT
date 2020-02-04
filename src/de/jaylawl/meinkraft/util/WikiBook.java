package de.jaylawl.meinkraft.util;

import de.jaylawl.meinkraft.Main;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BookMeta;

public class WikiBook {

    public static ItemStack make() {

        ItemStack book = new ItemStack(Material.WRITTEN_BOOK);
        BookMeta b = ((BookMeta) book.getItemMeta());
        b.setGeneration(BookMeta.Generation.ORIGINAL);
        b.setAuthor("jaylawl");
        b.setTitle("MEINKRAFT ingame manual");

        b.spigot().addPage(
                new ComponentBuilder("")
                        .append(Messaging.getPluginColor() + "§lMEINKRAFT§r\n" +
                                "§8version " + Main.inst().getDescription().getVersion() + "\n" +
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
                        .append("  Commands: " + Main.getEnabledCommands().size() + "\n" +
                                "  Modules: " + Main.getEnabledModules() + "\n" +
                                "  Listeners: " + Main.getEnabledListeners() + "\n\n")
                        .append("§8§l>§r §9Donate via PayPal§r\n")
                        .event(new ClickEvent(ClickEvent.Action.OPEN_URL, "http://paypal.me/langejulian"))
                        .event(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder("Click here to make jaylul happy boi").create()))
                        .append("").reset()
                        .append("§8§l>§r §6Rate on Spigot§r\n")
                        .event(new ClickEvent(ClickEvent.Action.OPEN_URL, "http://spigotmc.org/members/jaylawl.178412/"))
                        .event(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder("Click here to give a 1-star rating").create()))
                        .append("").reset()
                        .append("§8§l>§r §cReport an issue§r\n")
                        .event(new ClickEvent(ClickEvent.Action.OPEN_URL, "http://github.com/jaylawl"))
                        .event(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder("Click here to report a bug or grievance").create()))
                        .append("").reset()
                        .create()
        );

        book.setItemMeta(b);

        return book;
    }

}

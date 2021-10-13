package de.jaylawl.meinkraft.command;

import org.jetbrains.annotations.NotNull;

public enum CmdOptionalType {

    FLY(CommandFly.class, "fly"),
    GAMEMODE(CommandGameMode.class, "gm"),
    GOD(CommandGodMode.class, "godmode"),
    HEAL(CommandHeal.class, "heal"),
    INVSEE(CommandInventorySpy.class, "inventoryspy"),
    NIGHT_VISION(CommandNightVision.class, "nightvision"),
    PING(CommandPing.class, "ping"),
    QUERY(CommandQuery.class, "query"),
    SPEED(CommandSpeed.class, "speed"),
    STATISTIC(CommandStatistic.class, "statistic"),
    WORLD(CommandWorld.class, "world");

    private final Class<? extends MeinkraftCommand> clazz;
    private final String commandLabel;

    CmdOptionalType(@NotNull Class<? extends MeinkraftCommand> clazz, @NotNull String commandLabel) {
        this.clazz = clazz;
        this.commandLabel = commandLabel;
    }

    //

    public @NotNull Class<? extends MeinkraftCommand> getClazz() {
        return this.clazz;
    }

    public @NotNull String getCommandLabel() {
        return this.commandLabel;
    }

}

package de.jaylawl.meinkraft.cmd;

import org.jetbrains.annotations.NotNull;

public enum CmdOptionalType {

    FLY(CommandFly.class, "fly"),
    GAMEMODE(CommandGamemode.class, "gm"),
    GOD(CommandGod.class, "god"),
    HEAL(CommandHeal.class, "heal"),
    INVSEE(CommandInvsee.class, "invsee"),
    NIGHT_VISION(CommandNightVision.class, "nightvision"),
    PING(CommandPing.class, "ping"),
    QUERY(CommandQuery.class, "query"),
    SPEED(CommandSpeed.class, "speed"),
    STATISTIC(CommandStatistic.class, "stat"),
    WORLD(CommandWorld.class, "world");

    private final Class<? extends CommandMeinkraft> clazz;
    private final String commandLabel;

    CmdOptionalType(Class<? extends CommandMeinkraft> clazz, @NotNull String commandLabel) {
        this.clazz = clazz;
        this.commandLabel = commandLabel;
    }

    //

    public @NotNull Class<? extends CommandMeinkraft> getClazz() {
        return this.clazz;
    }

    public @NotNull String getCommandLabel() {
        return this.commandLabel;
    }

}

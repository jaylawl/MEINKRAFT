package de.jaylawl.meinkraft.cmd;

import org.jetbrains.annotations.NotNull;

public enum CmdOptionalType {

    FLY(CmdFly.class, "fly"),
    GAMEMODE(CmdGamemode.class, "gm"),
    GOD(CmdGod.class, "god"),
    HEAL(CmdHeal.class, "heal"),
    INVSEE(CmdInvsee.class, "invsee"),
    NIGHT_VISION(CmdNightVision.class, "nightvision"),
    PING(CmdPing.class, "ping"),
    QUERY(CmdQuery.class, "query"),
    SPEED(CmdSpeed.class, "speed"),
    STATISTIC(CmdStatistic.class, "stat"),
    WORLD(CmdWorld.class, "world");

    private final Class<? extends CmdMeinkraft> clazz;
    private final String commandLabel;

    CmdOptionalType(Class<? extends CmdMeinkraft> clazz, @NotNull String commandLabel) {
        this.clazz = clazz;
        this.commandLabel = commandLabel;
    }

    //

    public @NotNull Class<? extends CmdMeinkraft> getClazz() {
        return this.clazz;
    }

    public @NotNull String getCommandLabel() {
        return this.commandLabel;
    }

}

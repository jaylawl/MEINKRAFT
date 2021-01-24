package de.jaylawl.meinkraft.util;

import org.bukkit.util.StringUtil;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class TabHelper {

    public static int getArgumentNumber(@NotNull String[] arguments) {
        int i = 0;
        for (String arg : arguments) {
            if (!arg.equals("")) {
                i++;
            }
        }
        if (arguments[arguments.length - 1].equals("")) {
            i++;
        }
        return i;
    }

    public static List<String> sortedCompletions(@NotNull String lastArgument, @NotNull List<String> completions) {
        List<String> sortedCompletions = new ArrayList<>();
        StringUtil.copyPartialMatches(lastArgument, completions, sortedCompletions);
        Collections.sort(sortedCompletions);
        return sortedCompletions;
    }

}

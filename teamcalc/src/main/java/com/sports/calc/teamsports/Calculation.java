package com.sports.calc.teamsports;

public class Calculation {
    public static String getStreakAsString(int streak) {
        if (streak == 0)
            return "-";

        String prefix = streak < 0 ? "L" : "W";

        return prefix + Math.abs(streak);
    }
}

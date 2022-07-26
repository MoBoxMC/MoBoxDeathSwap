package org.mossmc.mosscg.MoBoxDeathSwap;

import org.bukkit.Location;

public class BasicInfo {
    public static String pluginName = "MoBoxDeathSwap";
    public static String pluginVersion = "V1.2.1.3.2355";
    public static String gameName = "DeathSwap";
    public static String winner = "nope";

    public static int startTime = 10;
    public static int gameSecond = 0;
    public static int nextChange = 0;
    public static int safeTime = 300;
    public static int changeBasicTime = 180;
    public static int changeRandomTime = 120;

    public static long lastChangeLocationTime;

    public static boolean canInteract = false;
    public static boolean canDamage = false;
    public static boolean canPoint = false;

    public static Location endLocation;

    public enum playerStatus {
        Alive,Dead
    }
}

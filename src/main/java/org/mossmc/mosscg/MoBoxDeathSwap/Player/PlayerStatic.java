package org.mossmc.mosscg.MoBoxDeathSwap.Player;

import java.util.HashMap;
import java.util.Map;

public class PlayerStatic {
    public static Map<String,Integer> killCountMap = new HashMap<>();
    public static Map<String,Integer> deathCountMap = new HashMap<>();

    public static void addKillCount(String player) {
        if (killCountMap.containsKey(player)) {
            killCountMap.replace(player,killCountMap.get(player)+1);
        } else {
            killCountMap.put(player,0);
        }
    }

    public static void addDeathCount(String player) {
        if (deathCountMap.containsKey(player)) {
            deathCountMap.replace(player,deathCountMap.get(player)+1);
        } else {
            deathCountMap.put(player,1);
        }
    }
}

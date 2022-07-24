package org.mossmc.mosscg.MoBoxDeathSwap.Player;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.mossmc.mosscg.MoBoxDeathSwap.BasicInfo;

import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;

public class PlayerCache {
    public static List<UUID> playerList = new ArrayList<>();
    public static List<String> playerNameList = new ArrayList<>();

    public static Map<UUID,Long> lastDamageTime = new HashMap<>();

    public static Map<String, BasicInfo.playerStatus> playerStatusMap = new HashMap<>();

    public static String getPlayerNameList() {
        StringBuilder nameList = new StringBuilder();
        AtomicBoolean first = new AtomicBoolean(false);
        AtomicBoolean alive = new AtomicBoolean(false);
        playerNameList.forEach(name -> {
            if (first.get()) {
                nameList.append("、");
            }
            nameList.append(name);
            first.set(true);
            alive.set(true);
        });
        if (!alive.get()) {
            nameList.append("无人生还");
        }
        return nameList.toString();
    }

    public static String getAliveNameList() {
        StringBuilder nameList = new StringBuilder();
        AtomicBoolean first = new AtomicBoolean(false);
        AtomicBoolean alive = new AtomicBoolean(false);
        playerList.forEach(uuid -> {
            OfflinePlayer player = Bukkit.getOfflinePlayer(uuid);
            if (first.get()) {
                nameList.append("、");
            }
            nameList.append(player.getName());
            first.set(true);
            alive.set(true);
        });
        if (!alive.get()) {
            nameList.append("无人生还");
        }
        return nameList.toString();
    }
}

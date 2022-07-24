package org.mossmc.mosscg.MoBoxDeathSwap.Player;

import org.mossmc.mosscg.MoBoxDeathSwap.BasicInfo;
import org.mossmc.mosscg.MoBoxDeathSwap.Main;
import org.mossmc.mosscg.MoBoxPoint.User.UserUpdate;

import java.util.*;
import java.util.concurrent.atomic.AtomicReference;

public class PlayerPoint {
    public static Map<String,Integer> playerPointMap = new HashMap<>();
    public static Map<String,Integer> playerPointKillMap = new HashMap<>();
    public static Map<Integer,String> playerPointRankMap = new HashMap<>();

    public static List<String> playerCompleteList = new ArrayList<>();

    public static int basicPoint;
    public static int pointKill;
    public static int pointWinner;

    public static String pointNameMain;
    public static String pointNameTotal;
    public static String pointNameWin;
    public static String pointNameKill;
    public static String pointNameDeath;

    public static void countPlayerPoint() {
        basicPoint = Main.getConfig.getInt("pointBasic");
        pointKill = Main.getConfig.getInt("pointKill");
        pointWinner = Main.getConfig.getInt("pointWinner");

        pointNameMain = Main.getConfig.getString("pointNameMain");
        pointNameTotal = Main.getConfig.getString("pointNameTotal");
        pointNameWin = Main.getConfig.getString("pointNameWin");
        pointNameKill = Main.getConfig.getString("pointNameKill");
        pointNameDeath = Main.getConfig.getString("pointNameDeath");

        PlayerCache.playerNameList.forEach(name -> {
            addPlayerPoint(name,basicPoint);
            if (PlayerStatic.killCountMap.containsKey(name)) {
                int kill = PlayerStatic.killCountMap.get(name);
                playerPointKillMap.put(name,pointKill*kill);
                addPlayerPoint(name,pointKill*kill);
            }
        });

        addPlayerPoint(BasicInfo.winner, pointWinner);

        Map<String,Integer> cacheMap = new HashMap<>(playerPointMap);
        String rankPlayer;
        rankPlayer = getMapMaxKey(cacheMap);
        playerPointRankMap.put(1,rankPlayer);
        rankPlayer = getMapMaxKey(cacheMap);
        playerPointRankMap.put(2,rankPlayer);
        rankPlayer = getMapMaxKey(cacheMap);
        playerPointRankMap.put(3,rankPlayer);
    }

    public static void savePlayerPoint() {
        if (!BasicInfo.canPoint) {
            return;
        }
        //增加本场积分（包括基础积分，击杀积分，胜场积分）
        playerPointMap.forEach((name, integer) -> {
            if (!playerCompleteList.contains(name)) {
                UserUpdate.userAddScore(name,pointNameMain,integer);
                playerCompleteList.add(name);
            }
        });

        //记录玩家数据
        playerCompleteList.forEach(uuid -> UserUpdate.userAddScore(uuid,pointNameTotal,1));
        PlayerStatic.killCountMap.forEach((name, integer) -> UserUpdate.userAddScore(name,pointNameKill,integer));
        PlayerStatic.deathCountMap.forEach((name, integer) -> UserUpdate.userAddScore(name,pointNameDeath,integer));
        UserUpdate.userAddScore(BasicInfo.winner,pointNameWin,1);
    }

    public static void addPlayerPoint(String name,int point) {
        if (!playerPointMap.containsKey(name)) {
            playerPointMap.put(name,0);
        }
        int oldPoint = playerPointMap.get(name);
        playerPointMap.replace(name,oldPoint+point);
    }

    public static String getMapMaxKey(Map<String,Integer> targetMap) {
        AtomicReference<String> playerReturn = new AtomicReference<>();
        final int[] playerPointMax = {0};
        targetMap.forEach((player, integer) -> {
            if (integer > playerPointMax[0]) {
                playerReturn.set(player);
                playerPointMax[0] = integer;
            }
        });
        targetMap.remove(playerReturn.get());
        return playerReturn.get();
    }

    public static String getRankPlayerName(int rank) {
        if (playerPointRankMap.get(rank) == null) {
            return "无";
        } else {
            return playerPointRankMap.get(rank);
        }

    }

    public static String getRankPlayerPoint(int rank) {
        if (playerPointRankMap.get(rank) == null) {
            return "0";
        } else {
            return String.valueOf(PlayerPoint.playerPointMap.get(playerPointRankMap.get(rank)));
        }
    }
}

package org.mossmc.mosscg.MoBoxDeathSwap.Player;

import org.bukkit.*;
import org.bukkit.entity.Player;
import org.mossmc.mosscg.MoBoxDeathSwap.BasicInfo;
import org.mossmc.mosscg.MoBoxDeathSwap.Main;

import java.util.*;

public class PlayerLocation {
    public static Map<UUID,Location> locationCacheMap = new HashMap<>();
    public static Map<UUID,UUID> locationKillerMap = new HashMap<>();

    public static int startXBasic = 5;
    public static int startYBasic = 5;
    public static String startWorldName = "world";
    public static World startWorld;
    public static Location startLocation;

    public static void initStartLocationInfo() {
        startXBasic = Main.getConfig.getInt("startXBasic");
        startYBasic = Main.getConfig.getInt("startYBasic");
        startWorldName = Main.getConfig.getString("startWorldName");
        assert startWorldName != null;
        startWorld = Bukkit.getWorld(startWorldName);
        assert startWorld != null;
        Location spawn = startWorld.getSpawnLocation();
        spawn.setX(0);
        spawn.setY(0);
        spawn = startWorld.getHighestBlockAt(spawn).getLocation().add(0,1,0);
        startWorld.setSpawnLocation(spawn);
        startLocation = spawn;
    }

    public static void distributeSpawn() {
        PlayerCache.playerList.forEach(uuid -> {
            Player player = Bukkit.getPlayer(uuid);
            if (player != null) {
                player.teleport(getSpawnLocation());
            }
        });
    }

    public static Location getSpawnLocation() {
        Location location = startLocation;
        Random random = new Random();
        if (random.nextBoolean()) {
            location.add(random.nextInt(startXBasic),0,0);
        } else {
            location.add(-random.nextInt(startXBasic),0,0);
        }
        if (random.nextBoolean()) {
            location.add(0,0,random.nextInt(startYBasic));
        } else {
            location.add(0,0,-random.nextInt(startYBasic));
        }
        location = startWorld.getHighestBlockAt(location).getLocation().add(0.5,1,0.5);
        startWorld.getHighestBlockAt(location).setType(Material.GLASS);
        return location;
    }

    public static void distributeLocation() {
        //初始化常量
        Random random = new Random();
        Map<Integer,Player> playerNumberMap = new HashMap<>();
        int count = 0;

        //初始化变量
        List<UUID> playerList = new ArrayList<>(PlayerCache.playerList);
        locationKillerMap.clear();
        locationCacheMap.clear();

        //随机抽取玩家并编号
        while (playerList.size() > 0) {
            UUID playerUUID = playerList.get(random.nextInt(playerList.size()));
            Player player = Bukkit.getPlayer(playerUUID);
            if (player != null) {
                if (PlayerCache.playerStatusMap.get(player.getName()).equals(BasicInfo.playerStatus.Alive)) {
                    if (player.isOnline()) {
                        count++;
                        playerNumberMap.put(count,player);
                    }
                }
            }
            playerList.remove(playerUUID);
        }

        //分配坐标
        int nowNumber = 1;
        while (nowNumber <= count) {
            Player player = playerNumberMap.get(nowNumber);
            Player playerNext;
            if (nowNumber == count) {
                playerNext = playerNumberMap.get(1);
            } else {
                playerNext = playerNumberMap.get(nowNumber+1);
            }
            Main.logger.info(ChatColor.GREEN+"玩家"+player.getName()+"分配到了"+playerNext.getName()+"的坐标！");
            locationCacheMap.put(player.getUniqueId(),playerNext.getLocation());
            locationKillerMap.put(player.getUniqueId(),playerNext.getUniqueId());
            nowNumber++;
        }
    }

    public static void locationTeleport() {
        BasicInfo.lastChangeLocationTime = System.currentTimeMillis();
        locationCacheMap.forEach((uuid, location) -> {
            Player player = Bukkit.getPlayer(uuid);
            if (player != null) {
                player.teleport(location);
            }
        });
    }
}

package org.mossmc.mosscg.MoBoxDeathSwap.Player;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.mossmc.mosscg.MoBoxDeathSwap.BasicInfo;
import org.mossmc.mosscg.MoBoxDeathSwap.Main;

import java.util.*;

public class PlayerLocation {
    public static List<Location> locationCacheList = new ArrayList<>();
    public static List<UUID> playerCacheList = new ArrayList<>();
    public static Map<UUID,Location> locationCacheMap = new HashMap<>();
    public static Map<Location,UUID> locationGiverMap = new HashMap<>();
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
        locationCacheList.clear();
        playerCacheList.clear();
        locationCacheMap.clear();
        locationGiverMap.clear();
        locationKillerMap.clear();
        PlayerCache.playerList.forEach(uuid -> {
            Player player = Bukkit.getPlayer(uuid);
            if (player != null) {
                if (PlayerCache.playerStatusMap.get(player.getName()).equals(BasicInfo.playerStatus.Alive)) {
                    if (player.isOnline()) {
                        locationCacheList.add(player.getLocation());
                        playerCacheList.add(uuid);
                        locationGiverMap.put(player.getLocation(), uuid);
                    }
                }
            }
        });
        playerCacheList.forEach(uuid -> {
            Player player = Bukkit.getPlayer(uuid);
            Random random = new Random();
            boolean isSelf = true;
            Location location = null;
            int times = 0;
            if (player != null) {
                while (isSelf) {
                    location = locationCacheList.get(random.nextInt(locationCacheList.size()));
                    if (player.getUniqueId() != locationGiverMap.get(location)) {
                        isSelf = false;
                    }
                    if (locationCacheList.size() <= 1) {
                        PlayerCheck.check();
                        isSelf = false;
                    }
                    times++;
                    if (times > 10) {
                        isSelf = false;
                    }
                }
                locationCacheList.remove(location);
                locationCacheMap.put(uuid, location);
                locationKillerMap.put(uuid, locationGiverMap.get(location));
            }
        });
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

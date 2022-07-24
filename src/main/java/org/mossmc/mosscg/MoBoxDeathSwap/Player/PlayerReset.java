package org.mossmc.mosscg.MoBoxDeathSwap.Player;

import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.mossmc.mosscg.MoBoxDeathSwap.BasicInfo;

public class PlayerReset {
    @SuppressWarnings("deprecation")
    public static void resetPlayer(Player player) {
        player.setExp(0);
        player.setFoodLevel(40);
        player.setHealth(player.getMaxHealth());
        player.getInventory().clear();
    }

    public static void resetPlayerRole(Player player, boolean inGame) {
        if (inGame) {
            PlayerChat.setPlayerChatPlayer(player.getUniqueId());
            player.setGameMode(GameMode.SURVIVAL);
            if (!PlayerCache.playerList.contains(player.getUniqueId())) {
                PlayerCache.playerList.add(player.getUniqueId());
            }
            if (!PlayerCache.playerNameList.contains(player.getName())) {
                PlayerCache.playerNameList.add(player.getName());
            }
            if (!PlayerCache.playerStatusMap.containsKey(player.getName())) {
                PlayerCache.playerStatusMap.put(player.getName(), BasicInfo.playerStatus.Alive);
            } else {
                PlayerCache.playerStatusMap.replace(player.getName(), BasicInfo.playerStatus.Alive);
            }
        } else {
            PlayerChat.setPlayerChatObserver(player.getUniqueId());
            player.setGameMode(GameMode.SPECTATOR);
            PlayerCache.playerList.remove(player.getUniqueId());
            PlayerCache.playerNameList.add(player.getName());
            PlayerCache.playerStatusMap.remove(player.getName());
        }
    }
}

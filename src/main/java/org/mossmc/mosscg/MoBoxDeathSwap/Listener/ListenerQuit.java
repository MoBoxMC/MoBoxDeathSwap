package org.mossmc.mosscg.MoBoxDeathSwap.Listener;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;
import org.mossmc.mosscg.MoBoxCore.Game.GameBasicInfo;
import org.mossmc.mosscg.MoBoxDeathSwap.BasicInfo;
import org.mossmc.mosscg.MoBoxDeathSwap.Player.PlayerCache;
import org.mossmc.mosscg.MoBoxDeathSwap.Player.PlayerCheck;

public class ListenerQuit implements Listener {
    @EventHandler
    public static void onQuit(PlayerQuitEvent event) {
        event.setQuitMessage(null);
        Player player = event.getPlayer();
        switch (GameBasicInfo.gameStatus) {
            case Waiting:
                PlayerCache.playerList.remove(player.getUniqueId());
                PlayerCache.playerNameList.remove(player.getName());
                break;
            case Starting:
            case Running:
                if (PlayerCache.playerList.contains(player.getUniqueId())) {
                    if (PlayerCache.playerStatusMap.get(player.getName()).equals(BasicInfo.playerStatus.Alive)) {
                        PlayerCache.playerList.remove(player.getUniqueId());
                        PlayerCache.playerStatusMap.replace(player.getName(), BasicInfo.playerStatus.Dead);
                        Bukkit.broadcastMessage(ChatColor.YELLOW+"玩家"+player.getName()+"已离开游戏！");
                    }
                }
                BasicInfo.endLocation = player.getLocation();
                PlayerCheck.check();
                break;
            default:
                break;
        }
    }
}

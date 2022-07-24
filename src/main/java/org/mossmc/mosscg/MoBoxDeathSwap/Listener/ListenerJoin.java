package org.mossmc.mosscg.MoBoxDeathSwap.Listener;

import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.mossmc.mosscg.MoBoxCore.Game.GameBasicInfo;
import org.mossmc.mosscg.MoBoxCore.Player.PlayerPick;
import org.mossmc.mosscg.MoBoxDeathSwap.Player.PlayerCache;
import org.mossmc.mosscg.MoBoxDeathSwap.Player.PlayerChat;
import org.mossmc.mosscg.MoBoxDeathSwap.Player.PlayerDamage;

public class ListenerJoin implements Listener {
    @EventHandler
    public static void onJoin(PlayerJoinEvent event) {
        event.setJoinMessage(null);
        Player player = event.getPlayer();
        switch (GameBasicInfo.gameStatus) {
            case Waiting:
                PlayerCache.playerList.add(player.getUniqueId());
                PlayerCache.playerNameList.add(player.getName());
                player.setGameMode(GameMode.ADVENTURE);
                PlayerDamage.disableDamage(player);
                PlayerPick.disablePickUp(player);
                break;
            case Starting:
            case Running:
                PlayerDamage.enableDamage(player);
                PlayerPick.enablePickUp(player);
                player.setGameMode(GameMode.SPECTATOR);
                player.sendMessage(ChatColor.YELLOW+"游戏已经开始！您现在处于观战模式！");
                PlayerChat.setPlayerChatObserver(player.getUniqueId());
                break;
            case Ending:
                PlayerDamage.enableDamage(player);
                PlayerPick.enablePickUp(player);
                player.setGameMode(GameMode.SPECTATOR);
                event.getPlayer().kickPlayer(ChatColor.RED+"本场游戏已结束！");
                break;
            default:
                PlayerDamage.enableDamage(player);
                PlayerPick.enablePickUp(player);
                player.setGameMode(GameMode.SPECTATOR);
                event.getPlayer().kickPlayer(ChatColor.RED+"未知的游戏状态！");
                break;
        }
    }
}

package org.mossmc.mosscg.MoBoxDeathSwap.Listener;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.World;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.mossmc.mosscg.MoBoxDeathSwap.BasicInfo;
import org.mossmc.mosscg.MoBoxDeathSwap.Player.*;

public class ListenerKill implements Listener {
    @EventHandler
    public static void onKill(PlayerDeathEvent event) {
        Player target = event.getEntity();
        Player killer = event.getEntity().getKiller();
        if (killer == null) {
            try {
                if (BasicInfo.lastChangeLocationTime + 1000L * 20 > System.currentTimeMillis()) {
                    killer = Bukkit.getPlayer(PlayerLocation.locationKillerMap.get(target.getUniqueId()));
                } else {
                    if (PlayerCache.lastDamageTime.containsKey(target.getUniqueId())) {
                        if (BasicInfo.lastChangeLocationTime < PlayerCache.lastDamageTime.get(target.getUniqueId())) {
                            if ((System.currentTimeMillis() - PlayerCache.lastDamageTime.get(target.getUniqueId()) < 1000 * 10)) {
                                killer = Bukkit.getPlayer(PlayerLocation.locationKillerMap.get(target.getUniqueId()));
                            }
                        }
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        target.setGameMode(GameMode.SPECTATOR);
        if (PlayerCache.playerList.contains(target.getUniqueId())) {
            PlayerStatic.addDeathCount(target.getName());
            PlayerCache.playerList.remove(target.getUniqueId());
            PlayerCache.playerStatusMap.replace(target.getName(), BasicInfo.playerStatus.Dead);
            PlayerChat.setPlayerChatObserver(target.getUniqueId());
            BasicInfo.endLocation = target.getLocation();
            World world = target.getLocation().getWorld();
            assert world != null;
            world.strikeLightningEffect(target.getLocation());
            if (killer != null) {
                PlayerStatic.addKillCount(killer.getName());
                Bukkit.broadcastMessage(ChatColor.RED+"玩家"+target.getName()+"被"+killer.getName()+"击杀了！");
            }
        }
        PlayerCheck.check();
    }
}

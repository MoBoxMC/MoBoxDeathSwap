package org.mossmc.mosscg.MoBoxDeathSwap.Listener;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.mossmc.mosscg.MoBoxCore.Game.GameBasicInfo;
import org.mossmc.mosscg.MoBoxCore.Game.GameStatus;
import org.mossmc.mosscg.MoBoxDeathSwap.BasicInfo;


public class ListenerRespawn implements Listener {
    @EventHandler
    public static void onRespawn(PlayerRespawnEvent event) {
        Player player = event.getPlayer();
        if (!GameBasicInfo.gameStatus.equals(GameStatus.gameStatus.Waiting)) {
            event.setRespawnLocation(BasicInfo.endLocation);
        }
    }
}

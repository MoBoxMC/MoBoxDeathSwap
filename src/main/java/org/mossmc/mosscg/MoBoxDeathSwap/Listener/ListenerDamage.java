package org.mossmc.mosscg.MoBoxDeathSwap.Listener;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.mossmc.mosscg.MoBoxDeathSwap.BasicInfo;
import org.mossmc.mosscg.MoBoxDeathSwap.Player.PlayerCache;

public class ListenerDamage implements Listener {
    @EventHandler
    public static void onDamage(EntityDamageByEntityEvent event) {
        if (!(event.getEntity() instanceof Player)) {
            return;
        }
        if (!(event.getDamager() instanceof Player)) {
            return;
        }
        if (!BasicInfo.canDamage) {
            event.setCancelled(true);
        } else {
            PlayerCache.lastDamageTime.put(event.getEntity().getUniqueId(),System.currentTimeMillis());
        }
    }
}

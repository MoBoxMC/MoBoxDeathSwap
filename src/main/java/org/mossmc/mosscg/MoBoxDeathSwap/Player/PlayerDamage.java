package org.mossmc.mosscg.MoBoxDeathSwap.Player;

import org.bukkit.entity.Player;

public class PlayerDamage {
    public static void disableDamage(Player player) {
        player.setInvulnerable(true);
    }

    public static void enableDamage(Player player) {
        player.setInvulnerable(false);
    }}

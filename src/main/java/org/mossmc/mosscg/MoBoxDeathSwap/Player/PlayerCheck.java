package org.mossmc.mosscg.MoBoxDeathSwap.Player;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.mossmc.mosscg.MoBoxDeathSwap.BasicInfo;
import org.mossmc.mosscg.MoBoxDeathSwap.Step.StepEnding;

public class PlayerCheck {
    public static void check() {
        if (PlayerCache.playerList.size() <= 1) {
            if (PlayerCache.playerList.size() == 1) {
                OfflinePlayer player = Bukkit.getOfflinePlayer(PlayerCache.playerList.get(0));
                BasicInfo.winner = player.getName();
            }
            StepEnding.runStep();
        }
    }
}

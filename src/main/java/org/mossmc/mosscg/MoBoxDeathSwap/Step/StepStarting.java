package org.mossmc.mosscg.MoBoxDeathSwap.Step;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.mossmc.mosscg.MoBoxCore.Entity.EntityKill;
import org.mossmc.mosscg.MoBoxCore.Game.GameBasicInfo;
import org.mossmc.mosscg.MoBoxCore.Game.GameStart;
import org.mossmc.mosscg.MoBoxCore.Game.GameStatus;
import org.mossmc.mosscg.MoBoxCore.Player.PlayerMove;
import org.mossmc.mosscg.MoBoxDeathSwap.BasicInfo;
import org.mossmc.mosscg.MoBoxDeathSwap.Main;
import org.mossmc.mosscg.MoBoxDeathSwap.Player.PlayerCache;
import org.mossmc.mosscg.MoBoxDeathSwap.Player.PlayerChat;
import org.mossmc.mosscg.MoBoxDeathSwap.Player.PlayerLocation;
import org.mossmc.mosscg.MoBoxDeathSwap.Player.PlayerReset;

public class StepStarting {
    public static void runStep() {
        Main.logger.info(ChatColor.GREEN+"游戏正在进入启动阶段！");
        GameBasicInfo.gameStatus = GameStatus.gameStatus.Starting;
        PlayerMove.disableAllMove();
        PlayerChat.initPlayerChat();
        PlayerLocation.distributeSpawn();
        PlayerCache.playerList.forEach(uuid -> {
            try {
                Player player = Bukkit.getPlayer(uuid);
                assert player != null;
                PlayerCache.playerStatusMap.put(player.getName(), BasicInfo.playerStatus.Alive);
                PlayerReset.resetPlayer(player);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        EntityKill.addKillList(EntityType.DROPPED_ITEM);
        EntityKill.addKillList(EntityType.EXPERIENCE_ORB);
        EntityKill.killAllWorldTargetEntity();
        Main.logger.info(ChatColor.GREEN+"游戏进入到启动阶段！");
        GameStart.startStart();
    }
}

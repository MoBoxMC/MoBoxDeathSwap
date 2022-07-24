package org.mossmc.mosscg.MoBoxDeathSwap.Step;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.mossmc.mosscg.MoBoxCore.Chat.ChatChannel;
import org.mossmc.mosscg.MoBoxCore.Game.GameBasicInfo;
import org.mossmc.mosscg.MoBoxCore.Game.GameEnd;
import org.mossmc.mosscg.MoBoxCore.Game.GameStatus;
import org.mossmc.mosscg.MoBoxDeathSwap.BasicInfo;
import org.mossmc.mosscg.MoBoxDeathSwap.Main;
import org.mossmc.mosscg.MoBoxDeathSwap.Player.PlayerPoint;

public class StepEnding {
    public static void runStep() {
        Main.logger.info(ChatColor.GREEN+"游戏正在进入结束阶段！");
        GameBasicInfo.gameStatus = GameStatus.gameStatus.Ending;
        ChatChannel.useChannelChat = false;
        Bukkit.broadcastMessage(ChatColor.DARK_GRAY + "====================================");
        Bukkit.broadcastMessage(ChatColor.GREEN+"游戏结束！");
        Bukkit.broadcastMessage(ChatColor.AQUA+"正在结算玩家信息！");
        Bukkit.broadcastMessage(ChatColor.YELLOW+"胜利者："+ BasicInfo.winner);
        try {
            PlayerPoint.countPlayerPoint();
            PlayerPoint.savePlayerPoint();
            Bukkit.getOnlinePlayers().forEach(player -> {
                player.setGameMode(GameMode.SPECTATOR);
                if (BasicInfo.endLocation != null) {
                    player.teleport(BasicInfo.endLocation);
                }
                player.setGameMode(GameMode.SPECTATOR);
                player.sendTitle("恭喜"+BasicInfo.winner+"存活到最后",null);
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
        Bukkit.broadcastMessage(ChatColor.DARK_GRAY + "====================================");
        Bukkit.broadcastMessage(ChatColor.GREEN+"本局积分排行：");
        Bukkit.broadcastMessage(ChatColor.GREEN+"第一名："+PlayerPoint.getRankPlayerName(1)+" - "+PlayerPoint.getRankPlayerPoint(1));
        Bukkit.broadcastMessage(ChatColor.GREEN+"第二名："+PlayerPoint.getRankPlayerName(2)+" - "+PlayerPoint.getRankPlayerPoint(2));
        Bukkit.broadcastMessage(ChatColor.GREEN+"第三名："+PlayerPoint.getRankPlayerName(3)+" - "+PlayerPoint.getRankPlayerPoint(3));
        Bukkit.broadcastMessage(ChatColor.DARK_GRAY + "====================================");
        PlayerPoint.playerCompleteList.forEach(uuid -> {
            Player player = Bukkit.getPlayer(uuid);
            if (player != null) {
                player.sendMessage(ChatColor.GREEN + "基础积分：" + PlayerPoint.basicPoint);
                if (player.getName().equals(BasicInfo.winner)) {
                    player.sendMessage(ChatColor.GREEN + "胜利积分：" + PlayerPoint.pointWinner);
                }
                player.sendMessage(ChatColor.GREEN + "击杀积分：" + PlayerPoint.playerPointKillMap.getOrDefault(uuid, 0));
                player.sendMessage(ChatColor.GREEN + "本局总积分：" + PlayerPoint.playerPointMap.get(uuid));
            }
        });
        Bukkit.broadcastMessage(ChatColor.DARK_GRAY + "====================================");
        Main.logger.info(ChatColor.GREEN+"游戏进入到结束阶段！");
        GameEnd.startEnd();
    }
}

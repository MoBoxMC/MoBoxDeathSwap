package org.mossmc.mosscg.MoBoxDeathSwap.Step;

import org.bukkit.ChatColor;
import org.mossmc.mosscg.MoBoxCore.Game.Game;
import org.mossmc.mosscg.MoBoxCore.Game.GameBasicInfo;
import org.mossmc.mosscg.MoBoxCore.Game.GameStatus;
import org.mossmc.mosscg.MoBoxCore.Game.GameWait;
import org.mossmc.mosscg.MoBoxDeathSwap.BasicInfo;
import org.mossmc.mosscg.MoBoxDeathSwap.Main;
import org.mossmc.mosscg.MoBoxDeathSwap.Player.PlayerLocation;
import org.mossmc.mosscg.MoBoxDeathSwap.World.WorldRule;

public class StepWaiting {
    public static void runStep() {
        Main.logger.info(ChatColor.GREEN+"游戏正在进入等待阶段！");
        try {
            Main.logger.info(ChatColor.GREEN+"正在初始化游戏......");
            GameBasicInfo.getGame = new Game() {
                @Override
                public int waitTime() {
                    return Main.getConfig.getInt("waitTime");
                }

                @Override
                public int reduceTime() {
                    return Main.getConfig.getInt("reduceTime");
                }

                @Override
                public int minPlayer() {
                    return Main.getConfig.getInt("minPlayer");
                }

                @Override
                public int maxPlayer() {
                    return Main.getConfig.getInt("maxPlayer");
                }

                @Override
                public String gameName() {
                    return BasicInfo.gameName;
                }

                @Override
                public String gameShortName() {
                    return BasicInfo.gameName;
                }

                @Override
                public String gameServerNext() {
                    return Main.getConfig.getString("gameServerNext");
                }

                @Override
                public String gameServerLobby() {
                    return Main.getConfig.getString("gameServerLobby");
                }

                @Override
                public boolean gameEndServerTeleport() {
                    return Main.getConfig.getBoolean("gameEndServerTeleport");
                }
            };
            GameBasicInfo.gameStatus = GameStatus.gameStatus.Waiting;
            GameBasicInfo.startMethod = StepStarting.class.getMethod("runStep");
            GameBasicInfo.runMethod = StepRunning.class.getMethod("runStep");
            BasicInfo.canInteract = false;
            PlayerLocation.initStartLocationInfo();
            WorldRule.setBeforeStartStatus();
            Main.logger.info(ChatColor.GREEN+"游戏初始化完成！");
        } catch (Exception e) {
            e.printStackTrace();
            Main.logger.warning(ChatColor.RED+"游戏初始化失败！");
        }
        GameWait.startWait();
        Main.logger.info(ChatColor.GREEN+"游戏进入到等待阶段！");
    }
}

package org.mossmc.mosscg.MoBoxDeathSwap.Step;

import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.mossmc.mosscg.MoBoxCore.Game.GameBasicInfo;
import org.mossmc.mosscg.MoBoxCore.Game.GameStatus;
import org.mossmc.mosscg.MoBoxCore.Info.InfoCountDown;
import org.mossmc.mosscg.MoBoxCore.Player.PlayerMove;
import org.mossmc.mosscg.MoBoxCore.Player.PlayerPick;
import org.mossmc.mosscg.MoBoxDeathSwap.BasicInfo;
import org.mossmc.mosscg.MoBoxDeathSwap.Main;
import org.mossmc.mosscg.MoBoxDeathSwap.Player.PlayerCache;
import org.mossmc.mosscg.MoBoxDeathSwap.Player.PlayerDamage;
import org.mossmc.mosscg.MoBoxDeathSwap.Player.PlayerLocation;
import org.mossmc.mosscg.MoBoxDeathSwap.World.WorldRule;

import java.util.Random;

public class StepRunning {
    public static void runStep() {
        Main.logger.info(ChatColor.GREEN+"游戏正在进入游玩阶段！");
        GameBasicInfo.gameStatus = GameStatus.gameStatus.Running;
        BasicInfo.startTime = Main.getConfig.getInt("startTime");
        BasicInfo.safeTime = Main.getConfig.getInt("safeTime");
        BasicInfo.changeBasicTime = Main.getConfig.getInt("changeBasicTime");
        BasicInfo.changeRandomTime = Main.getConfig.getInt("changeRandomTime");
        Bukkit.broadcastMessage(ChatColor.DARK_GRAY + "====================================");
        Bukkit.broadcastMessage(ChatColor.GOLD + "欢迎来到MoBoxMC-死亡交换");
        Bukkit.broadcastMessage(ChatColor.AQUA + "游戏规则：开局玩家将有"+BasicInfo.safeTime+"秒的和平发展时间，期间无法PVP。");
        Bukkit.broadcastMessage(ChatColor.AQUA + "每间隔一段随机时间，玩家将会进行随机互相传送");
        Bukkit.broadcastMessage(ChatColor.AQUA + "你可以在传送的时候利用机关坑死对方，也可以在和平期后，线下单杀对方！");
        Bukkit.broadcastMessage(ChatColor.DARK_GRAY + "====================================");
        Main.logger.info(ChatColor.GREEN+"游戏进入到游玩阶段！");
        new BukkitRunnable() {
            @Override
            public void run() {
                if (BasicInfo.startTime > 0) {
                    String titleMain = InfoCountDown.getRemainSecondString(BasicInfo.startTime);
                    String titleSub = ChatColor.GREEN+"观察四周...";
                    Bukkit.getOnlinePlayers().forEach(player -> {
                        player.sendTitle(titleMain,titleSub);
                        player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_PLING,1.0f,1.0f);
                    });
                    BasicInfo.startTime--;
                } else {
                    String titleMain = ChatColor.AQUA+"Run!";
                    PlayerCache.playerList.forEach(uuid -> {
                        Player player = Bukkit.getPlayer(uuid);
                        if (player != null) {
                            player.resetTitle();
                            player.sendTitle(titleMain, null);
                            player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_BELL, 1.0f, 1.0f);
                            player.setGameMode(GameMode.SURVIVAL);
                            PlayerDamage.enableDamage(player);
                            PlayerPick.enablePickUp(player);
                        }
                    });
                    PlayerMove.enableAllMove();
                    BasicInfo.canInteract = true;
                    WorldRule.setAfterStartStatus();
                    cancel();
                }
            }
        }.runTaskTimer(Main.instance, 0, 20);
        new BukkitRunnable() {
            @Override
            public void run() {
                if (!GameBasicInfo.gameStatus.equals(GameStatus.gameStatus.Running)) {
                    cancel();
                    return;
                }
                if (BasicInfo.gameSecond >= BasicInfo.safeTime) {
                    BasicInfo.canDamage = true;
                    String message = ChatColor.RED+"安全时间结束！猎杀开始！";
                    Bukkit.getOnlinePlayers().forEach(player -> {
                        player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_BELL, 1.0f, 1.0f);
                        TextComponent textComponent = new TextComponent(message);
                        player.spigot().sendMessage(ChatMessageType.ACTION_BAR, textComponent);
                    });
                    Bukkit.broadcastMessage(message);
                    cancel();
                } else {
                    Bukkit.getOnlinePlayers().forEach(player -> {
                        int remain = BasicInfo.safeTime-BasicInfo.gameSecond;
                        String message = ChatColor.GREEN+"距离安全时间结束还有"+remain+"秒";
                        TextComponent textComponent = new TextComponent(message);
                        player.spigot().sendMessage(ChatMessageType.ACTION_BAR, textComponent);
                    });
                }
                BasicInfo.gameSecond++;
            }
        }.runTaskTimer(Main.instance, 20L*BasicInfo.startTime,20);
        BasicInfo.nextChange = BasicInfo.changeBasicTime+new Random().nextInt(BasicInfo.changeRandomTime);
        new BukkitRunnable() {
            @Override
            public void run() {
                if (!GameBasicInfo.gameStatus.equals(GameStatus.gameStatus.Running)) {
                    cancel();
                    return;
                }
                if (BasicInfo.nextChange <= 0) {
                    BasicInfo.nextChange = BasicInfo.changeBasicTime+new Random().nextInt(BasicInfo.changeRandomTime)+1;
                    Bukkit.getOnlinePlayers().forEach(player -> {
                        player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_BELL, 1.0f, 1.0f);
                    });
                    PlayerLocation.distributeLocation();
                    PlayerLocation.locationTeleport();
                    String message = ChatColor.RED+"位置已交换！小心脚下哦！";
                    Bukkit.broadcastMessage(message);
                }
                if (BasicInfo.nextChange <= 10 && BasicInfo.nextChange > 0) {
                    Bukkit.getOnlinePlayers().forEach(player -> {
                        player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_PLING, 1.0f, 1.0f);
                    });
                    Bukkit.broadcastMessage(ChatColor.GREEN + "即将在" + BasicInfo.nextChange + "秒后交换位置！");
                }
                BasicInfo.nextChange--;
            }
        }.runTaskTimer(Main.instance,20L*(BasicInfo.startTime),20);
    }
}

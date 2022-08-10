package org.mossmc.mosscg.MoBoxDeathSwap;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.Configuration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import org.mossmc.mosscg.MoBoxCore.Game.GameBasicInfo;
import org.mossmc.mosscg.MoBoxCore.Game.GameStatus;
import org.mossmc.mosscg.MoBoxCore.Game.GameWait;
import org.mossmc.mosscg.MoBoxCore.Info.InfoGroupBackend;
import org.mossmc.mosscg.MoBoxCore.Listener.ListenerChat;
import org.mossmc.mosscg.MoBoxCore.Listener.ListenerMove;
import org.mossmc.mosscg.MoBoxCore.Listener.ListenerPing;
import org.mossmc.mosscg.MoBoxDeathSwap.Listener.*;
import org.mossmc.mosscg.MoBoxDeathSwap.PlaceHolder.PlaceHolderMain;
import org.mossmc.mosscg.MoBoxDeathSwap.Player.PlayerCache;
import org.mossmc.mosscg.MoBoxDeathSwap.Player.PlayerReset;
import org.mossmc.mosscg.MoBoxDeathSwap.Step.StepWaiting;

import java.util.logging.Logger;

public class Main extends JavaPlugin {
    public static Configuration getConfig;
    public static Logger logger;
    public static Main instance;

    @Override
    public void onLoad() {
        saveDefaultConfig();
        getConfig().options().copyDefaults(true);
        logger = getLogger();
        getConfig = getConfig();
        instance = this;
    }

    @Override
    public void onEnable() {
        InfoGroupBackend.sendPluginStartGroup(BasicInfo.pluginName, BasicInfo.pluginVersion);
        Bukkit.getPluginManager().registerEvents(new ListenerDamage(),this);
        Bukkit.getPluginManager().registerEvents(new ListenerInteract(),this);
        Bukkit.getPluginManager().registerEvents(new ListenerJoin(),this);
        Bukkit.getPluginManager().registerEvents(new ListenerKill(),this);
        Bukkit.getPluginManager().registerEvents(new ListenerQuit(),this);
        Bukkit.getPluginManager().registerEvents(new ListenerRespawn(),this);

        Bukkit.getPluginManager().registerEvents(new ListenerChat(),this);
        Bukkit.getPluginManager().registerEvents(new ListenerPing(),this);
        Bukkit.getPluginManager().registerEvents(new ListenerMove(),this);
        Plugin pluginPlaceholderAPI = Bukkit.getPluginManager().getPlugin("PlaceholderAPI");
        if (pluginPlaceholderAPI != null){
            logger.info(ChatColor.GREEN+"检测到PlaceHolderAPI插件，变量功能已启用！");
            new PlaceHolderMain().register();
        }
        Plugin MoBoxPoint = Bukkit.getPluginManager().getPlugin("MoBoxPoint");
        if (MoBoxPoint != null){
            logger.info(ChatColor.GREEN+"检测到MoBoxPoint插件，积分功能已启用！");
            BasicInfo.canPoint = true;
        }
        StepWaiting.runStep();
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length < 1) {
            return false;
        }
        switch (args[0]) {
            case "help":
                if (!sender.hasPermission("moboxdeathswap.help") && !sender.isOp()) {
                    sender.sendMessage(ChatColor.RED+"你没有权限执行这个指令！");
                    return false;
                }
                sender.sendMessage(ChatColor.DARK_GREEN+"MoBoxDeathSwap帮助信息");
                if (sender.hasPermission("moboxdeathswap.list")) {
                    sender.sendMessage(ChatColor.GREEN+"/mbds list - 游戏内玩家列表");
                }
                if (sender.hasPermission("moboxdeathswap.resetcountdown")) {
                    sender.sendMessage(ChatColor.GREEN+"/mbds resetcountdown - 重置等待时间");
                }
                if (sender.hasPermission("moboxdeathswap.reducecountdown")) {
                    sender.sendMessage(ChatColor.GREEN+"/mbds reducecountdown - 减少等待时间");
                }
                if (sender.hasPermission("moboxdeathswap.changerole")) {
                    sender.sendMessage(ChatColor.GREEN+"/mbds join <player/observer> [玩家ID] - 更换自己或别人的游戏角色");
                }
                sender.sendMessage(ChatColor.YELLOW+"By 墨守MossCG");
                break;
            case "list":
                if (!sender.hasPermission("moboxdeathswap.list") && !sender.isOp()) {
                    sender.sendMessage(ChatColor.RED+"你没有权限执行这个指令！");
                    return false;
                }
                if (!GameBasicInfo.gameStatus.equals(GameStatus.gameStatus.Running)) {
                    sender.sendMessage(ChatColor.RED+"游戏还未开始或已结束！您无法使用此指令！");
                    return false;
                }
                sender.sendMessage(ChatColor.DARK_GREEN + "MoBoxMC猎人游戏玩家列表");
                sender.sendMessage(ChatColor.RED + "全部玩家: " + PlayerCache.getPlayerNameList());
                sender.sendMessage(ChatColor.GREEN + "存活玩家: " + PlayerCache.getAliveNameList());
                break;
            case "resetcountdown":
                if (!sender.hasPermission("moboxdeathswap.resetcountdown") && !sender.isOp()) {
                    sender.sendMessage(ChatColor.RED+"你没有权限执行这个指令！");
                    return false;
                }
                if (!GameBasicInfo.gameStatus.equals(GameStatus.gameStatus.Waiting)) {
                    sender.sendMessage(ChatColor.RED+"游戏不处于等待阶段！无法执行该指令！");
                    return false;
                }
                GameWait.remainSecond = GameBasicInfo.getGame.waitTime();
                sender.sendMessage(ChatColor.GREEN+"已重置等待时间！");
                break;
            case "reducecountdown":
                if (!sender.hasPermission("moboxdeathswap.reducecountdown") && !sender.isOp()) {
                    sender.sendMessage(ChatColor.RED+"你没有权限执行这个指令！");
                    return false;
                }
                if (!GameBasicInfo.gameStatus.equals(GameStatus.gameStatus.Waiting)) {
                    sender.sendMessage(ChatColor.RED+"游戏不处于等待阶段！无法执行该指令！");
                    return false;
                }
                GameWait.remainSecond = GameBasicInfo.getGame.reduceTime();
                sender.sendMessage(ChatColor.GREEN+"已减少等待时间！");
                break;
            case "join":
                if (!sender.hasPermission("moboxdeathswap.changerole") && !sender.isOp()) {
                    sender.sendMessage(ChatColor.RED+"你没有权限执行这个指令！");
                    return false;
                }
                if (!(sender instanceof Player) && args.length == 2) {
                    sender.sendMessage(ChatColor.RED+"该指令只能由玩家执行！");
                    return false;
                }
                if (!GameBasicInfo.gameStatus.equals(GameStatus.gameStatus.Running)) {
                    sender.sendMessage(ChatColor.RED+"游戏不处于运行阶段！无法执行该指令！");
                    return false;
                }
                if (args.length < 2) {
                    sender.sendMessage(ChatColor.RED+"指令示例：/moboxdeathswap join <身份>");
                    return false;
                }
                if (!args[1].equals("player") && !args[1].equals("observer")) {
                    sender.sendMessage(ChatColor.RED+"未知的身份名称！可用身份：player observer");
                    return false;
                }
                Player player;
                if (args.length >= 3) {
                    if (!sender.hasPermission("moboxdeathswap.changeotherrole") && !sender.isOp()) {
                        sender.sendMessage(ChatColor.RED+"你没有权限执行这个指令！");
                        return false;
                    }
                    player = Bukkit.getPlayer(args[2]);
                    if (player == null) {
                        sender.sendMessage(ChatColor.RED+"未知的玩家！");
                        return false;
                    }
                } else {
                    player = ((Player) sender).getPlayer();
                }
                assert player != null;
                switch (args[1]) {
                    case "player":
                        PlayerReset.resetPlayerRole(player, true);
                        break;
                    case "observer":
                        PlayerReset.resetPlayerRole(player, false);
                        break;
                    default:
                        break;
                }
                break;
            default:
                sender.sendMessage(ChatColor.RED+"未知指令！");
                return false;
        }
        return true;
    }
}

package org.mossmc.mosscg.MoBoxDeathSwap.PlaceHolder;

import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.mossmc.mosscg.MoBoxCore.Game.GameBasicInfo;
import org.mossmc.mosscg.MoBoxCore.Game.GameStatus;
import org.mossmc.mosscg.MoBoxDeathSwap.BasicInfo;
import org.mossmc.mosscg.MoBoxDeathSwap.Player.PlayerCache;

public class PlaceHolderMain extends PlaceholderExpansion {
    @Override
    public String getAuthor() {
        return "MossCG";
    }

    @Override
    public String getIdentifier() {
        return "moboxdeathswap";
    }

    @Override
    public String getVersion() {
        return "1.0.0";
    }

    @Override
    public String onPlaceholderRequest(Player player, String identifier) {
        GameStatus.gameStatus status = GameBasicInfo.gameStatus;
        switch (identifier) {
            case "role":
                if (status == GameStatus.gameStatus.Waiting) {
                    return ChatColor.GRAY+"[等待中]";
                }
                if (PlayerCache.playerNameList.contains(player.getName())) {
                    if (PlayerCache.playerStatusMap.get(player.getName()).equals(BasicInfo.playerStatus.Alive)) {
                        return ChatColor.GREEN+"[玩家]";
                    } else {
                        return ChatColor.RED+"[玩家]";
                    }
                } else {
                    return ChatColor.GRAY+"[观察者]";
                }
            default:
                return "未知变量";
        }
    }
}

package org.mossmc.mosscg.MoBoxDeathSwap.Player;

import org.bukkit.ChatColor;
import org.mossmc.mosscg.MoBoxCore.Chat.ChatChannel;

import java.util.UUID;

public class PlayerChat {
    public static void initPlayerChat() {
        ChatChannel.addChannel("player");
        ChatChannel.addChannel("observer");
        ChatChannel.setChatCopyChannel("player","observer");
        PlayerCache.playerList.forEach(PlayerChat::setPlayerChatPlayer);
        ChatChannel.useChannelChat = true;
    }

    public static void setPlayerChatPlayer(UUID player) {
        ChatChannel.resetPlayerChat(player);
        ChatChannel.addPlayerChatChannel(player,"player");
        ChatChannel.setPlayerChatColor(player, ChatColor.GRAY);
        ChatChannel.setPlayerChatPrefix(player,ChatColor.GREEN+"[玩家]");
    }

    public static void setPlayerChatObserver(UUID player) {
        ChatChannel.resetPlayerChat(player);
        ChatChannel.addPlayerChatChannel(player,"observer");
        ChatChannel.setPlayerChatColor(player, ChatColor.GRAY);
        ChatChannel.setPlayerChatPrefix(player,ChatColor.GRAY+"[观察者]");
    }
}

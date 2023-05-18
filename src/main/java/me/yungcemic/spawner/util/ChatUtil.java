package me.yungcemic.spawner.util;

import org.bukkit.ChatColor;

import java.util.Collection;
import java.util.List;

public final class ChatUtil {

    private ChatUtil() {}

    public static String colorize(String text) {
        return ChatColor.translateAlternateColorCodes('&', text);
    }

    public static List<String> colorize(Collection<String> text) {
        return text.stream().map(ChatUtil::colorize).toList();
    }
}
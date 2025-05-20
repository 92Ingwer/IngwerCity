package org.kim.ingwerCity.messages;

import lombok.Getter;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;


public enum UsageEnum {
    PUNISH_USAGE(mm("Usage: /punish <type> <player> <reason>")),
    UNBAN_USAGE(mm("Usage: /unban <player>")),
    NAVI_USAGE(mm("Usage: /navi <x> <y> <z>")),
    BANK_USAGE(mm("Usage: /bank (<withdraw|deposit> <amount>)")),
    FACTION_CREATE_USAGE(mm("Usage: /fcreate <name> <type> <spawnX> <spawnY> <spawnZ>")),
    PLAYER_SET_FACTION_USAGE(mm("Usage: /fset <player> <faction>")),
    INVITE_FACTION_USAGE(mm("Usage: /finvite <player>\n Usage: /finvite accept")),
    FACTION_CHAT_USAGE(mm("Usage: /f <message>")),
    FACTION_BANK_USAGE(mm("Usage: /fbank <withdraw|deposit> <amount>")),
    FACTION_UNINVITE_USAGE(mm("Usage: /funinvite <player>")),
    SET_RANK_USAGE(mm("Usage: /setrank <player> <rank>")),
    STATS_USAGE(mm("Usage: /stats <player>"));

    @Getter
    private final Component message;

    UsageEnum(Component message) {
        this.message = message;
    }

    public static Component mm(String message) {
        return MiniMessage.miniMessage().deserialize(message);
    }
}
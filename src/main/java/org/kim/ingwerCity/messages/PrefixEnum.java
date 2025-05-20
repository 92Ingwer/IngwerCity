package org.kim.ingwerCity.messages;

import lombok.Getter;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;

public enum PrefixEnum {
    PUNISH_PREFIX(mm("<gradient:#D44F4F:#FF7171>[Punish]</gradient>")),
    NAVI_PREFIX(mm("<gradient:#D44F4F:#FF7171>[Navigator]</gradient>")),
    BANK_PREFIX(mm("<gradient:#D44F4F:#FF7171>[Bank]</gradient>")),
    FACTION_PREIFX(mm("<gradient:#D44F4F:#FF7171>[Faction]</gradient>")),
    STATS_PREFIX(mm("<gradient:#D44F4F:#FF7171>[Stats]</gradient>"));

    @Getter
    private final Component message;

    PrefixEnum(Component message) {
        this.message = message;
    }

    public static Component mm(String message) {
        return MiniMessage.miniMessage().deserialize(message);
    }
}

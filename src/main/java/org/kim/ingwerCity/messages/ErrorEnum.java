package org.kim.ingwerCity.messages;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;

import java.util.HashMap;

public enum ErrorEnum {

    PLAYER_NOT_FOUND("<red>Player not found.</red>"),
    BAN_REASON_NOT_FOUND("<red>Reason not found.</red>"),
    PLAYER_ALREADY_BANNED("<red>%s is already banned.</red>"),
    PLAYER_NOT_BANNED("<red>%s is not banned.</red>"),
    INVALID_PUNISHMENT_TYPE("<red>Invalid punishment type.</red>"),
    INVALID_NUMBERS("<red>Invalid numbers.</red>"),
    INVALID_NUMBERS_NEGATIVE("<red>Invalid numbers. Negative numbers are not allowed.</red>"),
    NOT_ENOUGH_MONEY("<red>You don't have enough money.</red>"),
    ATM_NOT_NEAR("<red>You are not near an ATM.</red>"),
    FACTION_NOT_FOUND("<red>Faction not found.</red>"),
    NOT_IN_FACTION("<red>You are not in a faction.</red>"),
    NOT_LEADER("<red>You are not the leader of the faction.</red>"),
    PLAYER_IS_ALREADY_IN_FACTION("<red>%s is already in a faction.</red>"),
    PLAYER_NOT_NEAR("<red>%s is not near you.</red>"),
    NO_INVITE("<red>You have no invite to accept.</red>"),
    FACTION_NOT_ENOUGH_MONEY("<red>Faction does not have enough money.</red>"),
    PLAYER_NOT_YOUR_FACTION("<red>%s is not in your faction.</red>"),
    RANK_NOT_FOUND("<red>Rank not found. Only 0-6</red>");


    private final String message;


    ErrorEnum(String message) {
        this.message = message;
    }

    public Component getMessage(Object... args) {
        String formattedMessage = message.contains("%") ? String.format(message, args) : message;
        return MiniMessage.miniMessage().deserialize(formattedMessage);
    }
}

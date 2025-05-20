package org.kim.ingwerCity.messages;

import lombok.Getter;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;

public enum SuccessfullEnum {
    PLAYER_SUCCESSFULL_UNBAN("<green>You have unbanned %s</green>"),
    PLAYER_SUCCESSFULL_BAN("You have punished %s until %s"),
    PLAYER_SUCCESSFULL_BAN_PLAYER("You have been punished for %s until %s"),
    PLAYER_KICK_MESSAGE("<yellow>IngwerCity</yellow>\n" +
            "<red>You have been kicked!</red>\n" +
            "<red>Reason: %s </red>\n" +
            "<red>From: %s</red>\n"),
    PLAYER_BAN_MESSAGE("<yellow>IngwerCity</yellow>\n" +
            "<red>You are banned!</red>\n" +
            "<red>Reason: %s</red>\n" +
            "<red>Until: %s</red>\n" +
            "<red>Unban request: www.ingwercity.de/unban</red>"),
    NAVI_SUCCESSFULL("<green>You get a navigator to %.0f/%.0f/%.0f</green>"),
    NAVI_SUCCESSFULL_REMOVE("<green>You have removed the navigator</green>"),
    NAVI_REACHED("<green>You have reached the target" + "</green>"),
    BANK_DEPOSIT_QUESTION("<green>How much money do you want to deposit?</green>"),
    BANK_WITHDRAW_QUESTION("<green>How much money do you want to withdraw?</green>"),
    BANK_DEPOSIT_SUCCESSFULL("<green>You have deposited %.0f$</green>"),
    BANK_WITHDRAW_SUCCESSFULL("<green>You have withdrawn %.0f$</green>"),
    FACTON_SUCCESSFULL_CREATE("<green>You have created the faction %s</green>"),
    FACTION_SUCCESSFULL_SET("<green>You have set %s to the faction %s</green>"),
    FACTION_SUCCESSFULL_INVITED("<green>You have invited %s to the faction %s</green>"),
    PLAYER_SUCCESSFULL_INVITED("<green>You have been invited to the faction %s</green>"),
    PLAYER_SUCCESSFULL_ACCEPTED("<green>You have accepted the faction invite</green>"),
    PLAYER_JOINED_FACTION("<green>%s joined the faction</green>"),
    FACTION_BANK_SUCCESSFULL_WITHDRAWN("<green>%s has withdrawn %.0f$. New-Bank: %.0f$</green>"),
    FACTION_BANK_SUCCESSFULL_DEPOSIT("<green>%s has deposited %.0f$. New-Bank: %.0f$</green>"),
    FACTION_UNINVITE_SUCCESSFULL("<green>You have uninvited %s from the faction</green>"),
    FACTION_GET_UNINVITED("<green>You have been uninvited from the faction</green>"),
    FACTION_SUCCESSFULL_SET_RANK("<green>%s has now the rank: %s</green>");

    private final String  message;

    SuccessfullEnum(String  message) {
        this.message = message;
    }

    public Component getMessage(Object... args) {
        String formattedMessage = message.contains("%") ? String.format(message, args) : message;
        return MiniMessage.miniMessage().deserialize(formattedMessage);
    }
}

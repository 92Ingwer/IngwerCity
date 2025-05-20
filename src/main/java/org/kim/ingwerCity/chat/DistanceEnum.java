package org.kim.ingwerCity.chat;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;


public enum DistanceEnum {
    LOW_DISTANCE("<color:#ffffff>", 10),
    MEDIUM_DISTANCE("<color:#a3a3a3>", 20),
    HIGH_DISTANCE("<color:#545353>", 30);


    private final String chatColor;
    private final double distance;

    DistanceEnum(String chatColor, double distance) {
        this.chatColor = chatColor;
        this.distance = distance;
    }

    public static Component mm(String message) {
        return MiniMessage.miniMessage().deserialize(message);
    }
    public static Component getChatColor(double distance) {
        for(DistanceEnum distanceEnum : DistanceEnum.values()) {
            if(distance <= distanceEnum.distance) {
                return mm(distanceEnum.chatColor);
            }
        }
        return null;
    }
}

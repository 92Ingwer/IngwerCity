package org.kim.ingwerCity.faction;

import lombok.Getter;

@Getter
public enum FChatenum {

    CHAT_1("Hautton", "<#fb7777>", "<#ffcccc>", 1),
    CHAT_2("Orange/Gelb", "<#f2910a>", "<#efd510>", 2),
    CHAT_3("Rot/Hellbraun", "<#da5151>", "<#ed9f66>", 3),
    CHAT_4("Blautöne", "<#2f3c4f>", "<#506f86>", 4);

    private final String name;
    private final String color1;
    private final String color2;
    private final int id;

    FChatenum(String name, String color1, String color2, int id) {
        this.name = name;
        this.color1 = color1;
        this.color2 = color2;
        this.id = id;
    }

    public static String getNameByID(int id) {
        for (FChatenum chat : FChatenum.values()) {
            if (chat.getId() == id) {
                return chat.name;
            }
        }
        return null;
    }

    public static FChatenum getFChatEnum(int id) {
        for (FChatenum chat : FChatenum.values()) {
            if (chat.getId() == id) {
                return chat;
            }
        }
        return null;
    }
}

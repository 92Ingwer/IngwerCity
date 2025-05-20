package org.kim.ingwerCity.faction;

public enum FactionTypeEnum {


    BAD_FRAK("Badfrak", 30),
    STAATS_FRAK("Staatsfrak", 30),
    NEUTRAL_FRAK("Neutralfrak", 30),
    GOOD_FRAK("Goodfrak", 20);


    final String type;
    final int size;
    FactionTypeEnum(String type, int size) {
        this.type = type;
        this.size = size;
    }

    public static int getSize(String type) {
        for (FactionTypeEnum factionType : FactionTypeEnum.values()) {
            if (factionType.type.equalsIgnoreCase(type)) {
                return factionType.size;
            }
        }
        return 0;
    }
}

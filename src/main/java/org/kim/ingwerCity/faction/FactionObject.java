package org.kim.ingwerCity.faction;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;


@Getter
@Setter

public class FactionObject {

    private String name;
    private String type;
    private double spawnX;
    private double spawnY;
    private double spawnZ;
    private int id;
    private double bank;
    private int chatDyeID;

    public static final List< FactionObject> factionList = new ArrayList<>();
    public FactionObject(int id, String name, String type, double spawnX, double spawnY, double spawnZ, double bank, int chatDyeID) {
        this.name = name;
        this.type = type;
        this.spawnX = spawnX;
        this.spawnY = spawnY;
        this.spawnZ = spawnZ;
        this.id = id;
        this.bank = bank;
        this.chatDyeID = chatDyeID;
    }
}

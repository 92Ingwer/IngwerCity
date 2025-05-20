package org.kim.ingwerCity.money;

import lombok.Getter;
import lombok.Setter;
import org.bukkit.Location;

import java.util.HashMap;


@Getter
@Setter
public class ATMObject {
    private int atmid;
    private int capcity;
    private Location location;
    public static final HashMap<Integer, ATMObject> ATM_OBJECT_HASH_MAP = new HashMap<>();
    ATMObject(int atmid, int capcity, Location location) {
        this.atmid = atmid;
        this.capcity = capcity;
        this.location = location;
    }
}

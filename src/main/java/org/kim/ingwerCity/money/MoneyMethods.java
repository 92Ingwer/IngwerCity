package org.kim.ingwerCity.money;

import org.bukkit.Location;

public class MoneyMethods {

    public void initATMS() {
        for (ATMLocations atm : ATMLocations.values()) {
            int atmid = atm.getAtmid();
            int capacity = atm.getCapacity();
            Location location = atm.getLocation();
            ATMObject atmObject = new ATMObject(atmid, capacity, location);
            ATMObject.ATM_OBJECT_HASH_MAP.put(atmid, atmObject);
        }
    }
    public boolean isATMInNear(Location location) {
        for (ATMLocations atm : ATMLocations.values()) {
            if (location.distance(atm.getLocation()) < 3) {
                return true;
            }
        }
        return false;
    }
}

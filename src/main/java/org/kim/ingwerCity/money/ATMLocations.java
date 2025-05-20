package org.kim.ingwerCity.money;

import org.bukkit.Bukkit;
import org.bukkit.Location;

public enum ATMLocations {
    ATM_LOCATION_01(1,100000, new Location(Bukkit.getWorlds().getFirst(), 0, 0, 0)),
    ATM_LOCATION_02(2,100000, new Location(Bukkit.getWorlds().getFirst(), 10, 10, 10));


    private int atmid;
    private int capacity;
    private Location location;

    ATMLocations(int atmid, int capacity,Location location) {
        this.atmid = atmid;
        this.capacity = capacity;
        this.location = location;
    }
    public int getAtmid() {
        return atmid;
    }
    public Location getLocation() {
        return location;
    }
    public static Location getLocationByID(int id) {
        for (ATMLocations atm : values()) {
            if (atm.getAtmid() == id) {
                return atm.getLocation();
            }
        }
        return null;
    }
    public int getCapacity() {
        return capacity;
    }
}

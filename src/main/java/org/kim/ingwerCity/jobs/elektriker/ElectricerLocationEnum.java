package org.kim.ingwerCity.jobs.elektriker;


import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.Location;

@Getter
public enum ElectricerLocationEnum {
    ELECTRICER_LOCATION_1(new Location(Bukkit.getWorld("world"), -1034, 68, 487)),
    ELECTRICER_LOCATION_2(new Location(Bukkit.getWorld("world"), -1038, 68, 487)),
    ELECTRICER_LOCATION_3(new Location(Bukkit.getWorld("world"), -1042, 68, 487)),
    ELECTRICER_LOCATION_4(new Location(Bukkit.getWorld("world"), -1046, 68, 487)),
    ELECTRICER_LOCATION_5(new Location(Bukkit.getWorld("world"), -1050, 68, 487)),
    ELECTRICER_LOCATION_6(new Location(Bukkit.getWorld("world"), -1054, 68, 487)),
    ELECTRICER_LOCATION_7(new Location(Bukkit.getWorld("world"), -1058, 68, 487)),
    ELECTRICER_LOCATION_8(new Location(Bukkit.getWorld("world"), -1062, 68, 487)),
    ELECTRICER_LOCATION_9(new Location(Bukkit.getWorld("world"), -1066, 68, 487)),
    ELECTRICER_LOCATION_10(new Location(Bukkit.getWorld("world"), -1070, 68, 487));


    private final Location location;
    ElectricerLocationEnum(Location location) {
        this.location = location;
    }
    public static Location getRandomElectricerLocation() {
        ElectricerLocationEnum[] values = ElectricerLocationEnum.values();
        int randomIndex = (int) (Math.random() * values.length);
        return values[randomIndex].getLocation();
    }
}

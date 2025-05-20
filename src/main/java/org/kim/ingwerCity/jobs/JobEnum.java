package org.kim.ingwerCity.jobs;

import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.kim.ingwerCity.jobs.elektriker.ElectricerJob;
import org.kim.ingwerCity.jobs.timberjack.TimberjackJob;

@Getter
public enum JobEnum {
    ELECTRICIAN("Electricer", 1, ElectricerJob::new, new Location(Bukkit.getWorld("world"),-1034, 68, 487),1,150),
    TIMBERJACK("Timberjack", 2, TimberjackJob::new, new Location(Bukkit.getWorld("world"),1,1,1),1,150);


    private final String name;
    private final int jobID;
    private final JobFactory jobFactory;
    private final Location location;
    private final double minMoney;
    private final double maxMoney;
    JobEnum(String name, int jobID, JobFactory jobFactory, Location location, double minMoney, double maxMoney) {
        this.name = name;
        this.jobID = jobID;
        this.jobFactory = jobFactory;
        this.location = location;
        this.minMoney = minMoney;
        this.maxMoney = maxMoney;
    }

    public JobInterface create(int level, int xp, Player player) {
        return jobFactory.create(level, xp, player);
    }

    @FunctionalInterface
    public interface JobFactory {
        JobInterface create(int level, int xp, Player player);
    }
    public static Location nearestLocation(Location playerLocation) {
        double maxDistance = 10;
        for (JobEnum jobEnum : values()) {
            double distance = jobEnum.getLocation().distance(playerLocation);
            if (distance < maxDistance) {
                return jobEnum.getLocation();
            }
        }
        return null;
    }
    public static String getJobName(Location location) {
        for (JobEnum jobEnum : values()) {
            if (jobEnum.getLocation().equals(location)) {
                return jobEnum.getName();
            }
        }
        return null;
    }
}

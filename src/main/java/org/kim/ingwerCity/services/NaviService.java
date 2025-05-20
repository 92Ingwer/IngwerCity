package org.kim.ingwerCity.services;

import org.bukkit.Location;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import java.util.HashMap;
import java.util.UUID;

public class NaviService {
    public static final HashMap<UUID, Location> naviLocation = new HashMap<>();
    public static final HashMap<UUID, BukkitTask> naviRunnable = new HashMap<>();
}

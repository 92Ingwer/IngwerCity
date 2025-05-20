package org.kim.ingwerCity.schedulers;

import com.destroystokyo.paper.ParticleBuilder;
import net.kyori.adventure.text.Component;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;
import org.bukkit.util.Vector;
import org.kim.ingwerCity.IngwerCity;
import org.kim.ingwerCity.messages.PrefixEnum;
import org.kim.ingwerCity.navigator.NavigatorCommand;
import org.kim.ingwerCity.services.NaviService;

import java.util.UUID;

public class Navigator {
    public void startNavi(Player player, Location location) {

        IngwerCity plugin = IngwerCity.getInstance();
        UUID playerUUID = player.getUniqueId();
        NavigatorCommand navigatorCommand = new NavigatorCommand();
        if(NaviService.naviLocation.containsKey(playerUUID)) {
            navigatorCommand.deleteNavi(player);
        }
        NaviService.naviLocation.put(playerUUID, location);
        BukkitTask bukkitTask = new BukkitRunnable() {
            @Override
            public void run() {
                if (NaviService.naviLocation.containsKey(playerUUID) && player.isOnline()) {
                    refreshActionbar(player);
                    spawnRoute(player);
                    spawnTarget(player);
                } else {
                    this.cancel();
                }
            }
        }.runTaskTimer(plugin, 0, 20);
        NaviService.naviRunnable.put(playerUUID, bukkitTask);
    }
    public void refreshActionbar(Player player) {
        Location targetLocation = NaviService.naviLocation.get(player.getUniqueId());
        int distance = (int) targetLocation.distance(player.getLocation());
        player.sendActionBar(PrefixEnum.NAVI_PREFIX.getMessage().append(Component.space()).append(Component.text("Distance: " +  distance +  "m")));
    }
    public void spawnRoute(Player player) {
        Location targetLocation = NaviService.naviLocation.get(player.getUniqueId());
        Vector direction = targetLocation.toVector().subtract(player.getLocation().toVector()).normalize();
        Location current = player.getLocation().add(0,1,0).clone();
        for (int i = 0; i < 15; i++) {
            current.add(direction.clone().multiply(0.2));
            new ParticleBuilder(Particle.DUST)
                    .location(current)
                    .color(Color.BLUE, 1)
                    .receivers(player)
                    .spawn();
        }
    }
    public void spawnTarget(Player player) {
        Location center = NaviService.naviLocation.get(player.getUniqueId());
        double radius = 1.5;
        int points = 72;
        for(int i = 0; i < points; i++) {
            double angle = 2* Math.PI * i / points;
            double x = center.getX() + radius*Math.cos(angle);
            double z = center.getZ() + radius*Math.sin(angle);
            Location particleLocation = new Location(center.getWorld(), x, center.getY(), z);
            new ParticleBuilder(Particle.DUST)
                    .location(particleLocation)
                    .color(Color.WHITE,1)
                    .receivers(player)
                    .spawn();
        }
    }
}

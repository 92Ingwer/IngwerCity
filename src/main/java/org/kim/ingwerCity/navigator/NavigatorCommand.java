package org.kim.ingwerCity.navigator;

import net.kyori.adventure.text.Component;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.kim.ingwerCity.messages.ErrorEnum;
import org.kim.ingwerCity.messages.PrefixEnum;
import org.kim.ingwerCity.messages.SuccessfullEnum;
import org.kim.ingwerCity.messages.UsageEnum;
import org.kim.ingwerCity.schedulers.Navigator;
import org.kim.ingwerCity.services.NaviService;

import java.util.UUID;

public class NavigatorCommand implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String @NotNull [] strings) {
        if (!(commandSender instanceof Player player)) {
            return false;
        }
        UUID playerUUID = player.getUniqueId();
        if (strings.length == 0) {
            if (NaviService.naviLocation.containsKey(playerUUID)) {
                deleteNavi(player);
                player.sendMessage(PrefixEnum.NAVI_PREFIX.getMessage().append(Component.space()).append(SuccessfullEnum.NAVI_SUCCESSFULL_REMOVE.getMessage()));
                return false;
            }
            player.sendMessage(PrefixEnum.NAVI_PREFIX.getMessage().append(Component.space()).append(UsageEnum.NAVI_USAGE.getMessage()));
            return false;
        }

        if (strings.length != 3) {
            player.sendMessage(PrefixEnum.NAVI_PREFIX.getMessage().append(Component.space()).append(UsageEnum.NAVI_USAGE.getMessage()));
            return false;
        }
        double x;
        double y;
        double z;
        try {
            x = Double.parseDouble(strings[0]);
            y = Double.parseDouble(strings[1]);
            z = Double.parseDouble(strings[2]);

            player.sendMessage(PrefixEnum.NAVI_PREFIX.getMessage().append(Component.space()).append(SuccessfullEnum.NAVI_SUCCESSFULL.getMessage(x, y, z)));
        } catch (NumberFormatException e) {
            player.sendMessage(PrefixEnum.NAVI_PREFIX.getMessage().append(Component.space()).append(ErrorEnum.INVALID_NUMBERS.getMessage()));
            return false;
        }
        Location location = new Location(player.getWorld(), x, y, z);
        Navigator naviRefresh = new Navigator();
        naviRefresh.startNavi(player, location);
        return false;
    }
    public void deleteNavi(Player player) {
        NaviService.naviLocation.remove(player.getUniqueId());
        NaviService.naviRunnable.get(player.getUniqueId()).cancel();
        NaviService.naviRunnable.remove(player.getUniqueId());
    }
}

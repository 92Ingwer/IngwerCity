package org.kim.ingwerCity.listeners;

import net.kyori.adventure.text.Component;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.kim.ingwerCity.messages.PrefixEnum;
import org.kim.ingwerCity.messages.SuccessfullEnum;
import org.kim.ingwerCity.navigator.NavigatorCommand;
import org.kim.ingwerCity.services.NaviService;

import java.util.UUID;

public class PlayerMoveListener implements Listener {
    @EventHandler
    public void onPlayerMove(org.bukkit.event.player.PlayerMoveEvent event) {
        Player player = event.getPlayer();
        UUID uuidPlayer = player.getUniqueId();
        NavigatorCommand naviCommand = new NavigatorCommand();
        if(NaviService.naviLocation.containsKey(uuidPlayer)) {
            if(player.getLocation().distance(NaviService.naviLocation.get(uuidPlayer)) < 3) {
                player.sendMessage(PrefixEnum.NAVI_PREFIX.getMessage().append(Component.space().append(SuccessfullEnum.NAVI_REACHED.getMessage())));
                naviCommand.deleteNavi(player);
            }
        }
    }
}

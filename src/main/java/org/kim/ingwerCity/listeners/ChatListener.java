package org.kim.ingwerCity.listeners;

import io.papermc.paper.event.player.AsyncChatEvent;
import net.kyori.adventure.text.Component;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.kim.ingwerCity.chat.DistanceEnum;
import org.kim.ingwerCity.services.BankService;

public class ChatListener implements Listener {
    @EventHandler
    public void onChatEvent(AsyncChatEvent event) {
        event.setCancelled(true);
        Player player = event.getPlayer();
        //If Player is in the bank action map (he must decide how much to withdraw etc.), return
        if(BankService.BANK_ACTION_HASH_MAP.containsKey(player)) {
            return;
        }
        for(Player all : player.getWorld().getPlayers()) {
            double distance = player.getLocation().distance(all.getLocation());
            Component message = DistanceEnum.getChatColor(distance);
            if (message != null) {
                message = message.append(Component.text(player.getName() + ": ").append(event.message()));
                all.sendMessage(message);
            }
        }
    }
}

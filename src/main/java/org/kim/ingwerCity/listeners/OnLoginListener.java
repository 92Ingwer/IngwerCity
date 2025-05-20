package org.kim.ingwerCity.listeners;

import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.entity.Axolotl;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractAtEntityEvent;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.kim.ingwerCity.IngwerCity;
import org.kim.ingwerCity.database.SQLMethods;
import org.kim.ingwerCity.messages.SuccessfullEnum;

import java.util.Arrays;
import java.util.Date;
import java.util.Objects;
import java.util.UUID;

public class OnLoginListener implements Listener {

    @EventHandler
    public void onLogin(PlayerLoginEvent e) {
        Player player = e.getPlayer();
        UUID uuid = player.getUniqueId();
        SQLMethods sqlMethods = new SQLMethods();
        if (sqlMethods.isPlayerBanned(uuid)) {
            long until = sqlMethods.getDate(uuid);
            Date date = new Date(until);
            String reason = sqlMethods.getReason(uuid);
            e.disallow(PlayerLoginEvent.Result.KICK_OTHER, SuccessfullEnum.PLAYER_KICK_MESSAGE.getMessage(reason, date));
        }
    }
}

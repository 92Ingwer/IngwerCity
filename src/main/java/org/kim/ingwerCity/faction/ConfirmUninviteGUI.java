package org.kim.ingwerCity.faction;

import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.kim.ingwerCity.IngwerCity;
import org.kim.ingwerCity.database.SQLMethods;
import org.kim.ingwerCity.messages.PrefixEnum;
import org.kim.ingwerCity.messages.SuccessfullEnum;
import org.kim.ingwerCity.objects.ICPlayer;
import org.kim.ingwerCity.utils.ItemBuilder;
import org.kim.ingwerCity.utils.SimpleInventory;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

public class ConfirmUninviteGUI implements Listener {


    public void openInventory(Player player) {
        IngwerCity plugin = IngwerCity.getInstance();
        SimpleInventory inventory = new SimpleInventory(plugin, Component.text("Confirm Uninvite"), 9)
                .item(2, new ItemBuilder(Material.GREEN_WOOL, 1, Component.text("Confirm"), List.of(Component.text("Click to confirm"))).build(), (p, clickEvent) -> {
                    UUID targetUUID = FactionUninviteCommand.uninviteMap.get(p.getUniqueId());
                    OfflinePlayer target = Bukkit.getPlayer(targetUUID);
                    if(target.isOnline()) {
                        Player targetPlayer = (Player) target;
                        ICPlayer icTargetPlayer = ICPlayer.IC_PLAYER_HASH_MAP.get(targetPlayer.getUniqueId());
                        icTargetPlayer.setFactionID(0);
                        icTargetPlayer.setFactionRank(0);
                    }
                    p.sendMessage(PrefixEnum.FACTION_PREIFX.getMessage().appendSpace().append(SuccessfullEnum.FACTION_UNINVITE_SUCCESSFULL.getMessage(target.getName())));
                    p.closeInventory();
                    CompletableFuture.runAsync(() -> {
                        SQLMethods sqlMethods = new SQLMethods();
                        sqlMethods.uninvitePlayer(targetUUID);
                    });
                })
                .item(6, new ItemBuilder(Material.RED_WOOL, 1, Component.text("Cancel"), List.of(Component.text("Click to cancel"))).build(), (p, clickEvent) -> {
                    FactionUninviteCommand.uninviteMap.remove(p.getUniqueId());
                    p.closeInventory();
                });
        inventory.open(player);
    }
}

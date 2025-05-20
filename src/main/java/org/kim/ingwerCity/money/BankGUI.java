package org.kim.ingwerCity.money;

import net.kyori.adventure.text.Component;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.kim.ingwerCity.IngwerCity;
import org.kim.ingwerCity.messages.PrefixEnum;
import org.kim.ingwerCity.messages.SuccessfullEnum;
import org.kim.ingwerCity.objects.ICPlayer;
import org.kim.ingwerCity.services.BankService;
import org.kim.ingwerCity.utils.InventoryBuilder;

public class BankGUI implements Listener {
    private final Player player;
    private final Inventory inventory;

    public BankGUI(Player player) {
        this.player = player;
        this.inventory = new InventoryBuilder(27, Component.text("Bank - GUI"))
                .setItem(11, Material.EMERALD, Component.text("Deposit"), Component.text("Click to deposit money"))
                .setItem(13, Material.GOLD_INGOT, Component.text("Balance"),Component.text( "Current balance: " + ICPlayer.IC_PLAYER_HASH_MAP.get(player.getUniqueId()).getBankmoney() + "$"))
                .setItem(15, Material.REDSTONE, Component.text("Withdraw"), Component.text("Click to withdraw money"))
                .build();
        for (int i = 0; i < 27; i++) {
            if(inventory.getItem(i) == null) {
                inventory.setItem(i,new ItemStack(Material.GRAY_STAINED_GLASS_PANE));
            }
        }
    }
    public void openInventory() {
        IngwerCity plugin = IngwerCity.getInstance();
        player.openInventory(this.inventory);
        player.getServer().getPluginManager().registerEvents(this,plugin);
    }
    @EventHandler
    public void onClick(InventoryClickEvent event) {
        if(event.getView().title().equals(Component.text("Bank - GUI"))) {
            event.setCancelled(true);
            if(event.getCurrentItem() == null || event.getCurrentItem().getType().equals(Material.GRAY_STAINED_GLASS_PANE)) return;
            if(event.getSlot() == 11) {
                player.sendMessage(PrefixEnum.BANK_PREFIX.getMessage().append(Component.space().append(SuccessfullEnum.BANK_DEPOSIT_QUESTION.getMessage())));
                BankService.BANK_ACTION_HASH_MAP.put(player, "deposit");
            }
            if(event.getSlot() == 15) {
                player.sendMessage(PrefixEnum.BANK_PREFIX.getMessage().append(Component.space().append(SuccessfullEnum.BANK_WITHDRAW_QUESTION.getMessage())));
                BankService.BANK_ACTION_HASH_MAP.put(player, "withdraw");
            }
            player.closeInventory();
            HandlerList.unregisterAll(this);
        }
    }
}

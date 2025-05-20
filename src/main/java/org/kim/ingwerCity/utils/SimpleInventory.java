package org.kim.ingwerCity.utils;

import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;

import java.util.HashMap;
import java.util.Map;

public class SimpleInventory implements Listener {

    private Inventory inventory;
    private final Map<Integer, ClickAction> actions = new HashMap<>();

    public SimpleInventory(Plugin plugin, Component title, int size) {
        inventory = Bukkit.createInventory(null, size, title);
        Bukkit.getPluginManager().registerEvents(this, plugin);
    }

    public SimpleInventory item(int slot, ItemStack item) {
        inventory.setItem(slot, item);
        return this;
    }

    public SimpleInventory item(int slot, ItemStack item, ClickAction action) {
        inventory.setItem(slot, item);
        actions.put(slot, action);
        return this;
    }

    public void open(Player player) {
        player.openInventory(inventory);
    }


    @FunctionalInterface
    public interface ClickAction {
        void action(Player player, InventoryClickEvent event);
    }

    @EventHandler
    public void onClick(InventoryClickEvent event) {
        if (!event.getView().getTopInventory().equals(inventory)) {
            return;
        }
        event.setCancelled(true);
        ClickAction action = actions.get(event.getRawSlot());
        if (action != null && event.getWhoClicked() instanceof Player player) {
            action.action(player, event);
        }
    }
}

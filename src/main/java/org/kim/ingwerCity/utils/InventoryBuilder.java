package org.kim.ingwerCity.utils;

import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.HashMap;
import java.util.Map;

public class InventoryBuilder {

    private final Inventory inventory;
    private final Map<Integer, ItemStack> itemMap = new HashMap<>();

    public InventoryBuilder(int size, Component title) {
        this.inventory = Bukkit.createInventory(null, size, title);
    }

    public InventoryBuilder(InventoryType type, Component title) {
        this.inventory = Bukkit.createInventory(null, type, title);
    }

    public InventoryBuilder setItem(int slot, Material material, Component displayName, Component... lore) {
        ItemStack item = new ItemStack(material);
        ItemMeta meta = item.getItemMeta();

        if (meta != null) {
            meta.displayName(displayName);
            if (lore.length > 0) meta.lore(java.util.Arrays.asList(lore));
            item.setItemMeta(meta);
        }

        itemMap.put(slot, item);
        return this;
    }

    public InventoryBuilder setItem(int slot, ItemStack item) {
        itemMap.put(slot, item);
        return this;
    }

    public Inventory build() {
        itemMap.forEach(inventory::setItem);
        return inventory;
    }

    public void open(Player player) {
        player.openInventory(build());
    }
}


package org.kim.ingwerCity.utils;

import net.kyori.adventure.text.Component;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.List;


public class ItemBuilder {

    private Material material;
    private Component title;
    private int amount;
    private List<Component> lore;

    public ItemBuilder(Material material, int amount, Component title, List<Component> lore) {
        this.material = material;
        this.amount = amount;
        this.title = title;
        this.lore = lore;
    }
    public ItemStack build() {
        ItemStack item = new ItemStack(material, amount);
        ItemMeta meta = item.getItemMeta();
        if (meta != null) {
            meta.displayName(title);
            if (lore != null && !lore.isEmpty()) {
                meta.lore(lore);
            }
            item.setItemMeta(meta);
        }
        return item;
    }
}

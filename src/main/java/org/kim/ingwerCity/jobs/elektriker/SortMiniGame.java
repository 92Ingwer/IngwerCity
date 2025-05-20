package org.kim.ingwerCity.jobs.elektriker;

import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryAction;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.kim.ingwerCity.IngwerCity;
import org.kim.ingwerCity.jobs.JobInterface;
import org.kim.ingwerCity.jobs.JobService;
import org.kim.ingwerCity.utils.InventoryBuilder;
import org.kim.ingwerCity.utils.ItemBuilder;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class SortMiniGame implements Listener {
    private Inventory sortierInv;

    public SortMiniGame() {
        Bukkit.getPluginManager().registerEvents(this, IngwerCity.getInstance());
    }

    public void openSortMinigame(Player player) {
        Inventory inventory = new InventoryBuilder(6 * 9, Component.text("Sortier die Kabeln!")).build();
        //Set the stained-glass
        for (int i = 0; i < 6 * 9; i++) {
            if (i % 9 == 0 || i % 9 == 8 || i / 9 == 1 || i == 2 || i == 4 || i == 6) {
                inventory.setItem(i, new ItemBuilder(Material.GRAY_STAINED_GLASS_PANE, 1, Component.text(""), null).build());
            }
        }
        inventory.setItem(53, new ItemBuilder(Material.RED_STAINED_GLASS_PANE, 1, Component.text("Fertig!"), null).build());
        List<Integer> firstSlots = new ArrayList<>(List.of(1, 3, 5, 7));
        List<Material> woolMaterials = new ArrayList<>(List.of(Material.RED_WOOL, Material.YELLOW_WOOL, Material.LIME_WOOL, Material.BLUE_WOOL));
        //Set the first 4 wool blocks (at the top)
        woolMaterials.forEach(wool -> {
            int randomIndex = new Random().nextInt(firstSlots.size());
            int slot = firstSlots.get(randomIndex);
            inventory.setItem(slot, new ItemBuilder(wool, 1, Component.text(""), null).build());
            firstSlots.remove(randomIndex);
        });
        //List of the avaible slots
        List<Integer> avaibleSlots = new ArrayList<>();
        for (int i = 0; i < 6 * 9; i++) {
            if (inventory.getItem(i) == null) {
                avaibleSlots.add(i);
            }
        }
        //Set random wool blocks in the rest of the slots
        woolMaterials.forEach(wool -> {
            for (int i = 0; i < 4; i++) {
                int randomIndex = new Random().nextInt(avaibleSlots.size());
                int slot = avaibleSlots.get(randomIndex);
                inventory.setItem(slot, new ItemBuilder(wool, 1, Component.text(""), null).build());
                avaibleSlots.remove(randomIndex);
            }
        });
        this.sortierInv = inventory;
        player.openInventory(sortierInv);
    }

    public boolean hasPlayerWonSortGame() {
        for (int i = 0; i < 9; i++) {
            Material wool;
            if (sortierInv.getItem(i).getType() != Material.GRAY_STAINED_GLASS_PANE) {
                wool = sortierInv.getItem(i).getType();
                for (int j = i + 9; j < 6 * 9; j += 9) {
                    if (sortierInv.getItem(j) == null) {
                        return false;
                    }
                    if (sortierInv.getItem(j).getType() != wool && sortierInv.getItem(j).getType() != Material.GRAY_STAINED_GLASS_PANE) {
                        return false;
                    }
                }
            }
        }
        return true;
    }

    @EventHandler
    public void onSortGame(InventoryClickEvent event) {
        if (!event.getInventory().equals(sortierInv)) return;

        Player player = (Player) event.getWhoClicked();
        if (event.getSlot() == 53 && hasPlayerWonSortGame()) {
            player.closeInventory();
            ElectricerJob electricerJob = (ElectricerJob) JobService.PLAYER_IN_A_JOB.get(player.getUniqueId());
            electricerJob.onGetMoney();
            electricerJob.onGetExperience();
            electricerJob.getLocationRoute();
        }
        if (isIllegalAction(event.getAction()) || isPlayerInventory(event.getClickedInventory()) || isProtectedSlot(event)) {
            event.setCancelled(true);
        }

    }

    private boolean isIllegalAction(InventoryAction action) {
        return switch (action) {
            case MOVE_TO_OTHER_INVENTORY, DROP_ONE_SLOT, DROP_ALL_SLOT, DROP_ONE_CURSOR, DROP_ALL_CURSOR, HOTBAR_SWAP,
                 COLLECT_TO_CURSOR -> true;
            default -> false;
        };
    }

    private boolean isPlayerInventory(Inventory clickedInventory) {
        return clickedInventory != null && clickedInventory.getType() == InventoryType.PLAYER;
    }

    private boolean isProtectedSlot(InventoryClickEvent event) {
        ItemStack item = event.getCurrentItem();
        if (item == null) return false;
        return item.getType() == Material.GRAY_STAINED_GLASS_PANE || item.getType() == Material.RED_STAINED_GLASS_PANE || event.getSlot() < 18;
    }

}

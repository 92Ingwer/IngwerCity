package org.kim.ingwerCity.faction;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;
import org.kim.ingwerCity.IngwerCity;
import org.kim.ingwerCity.database.SQLMethods;
import org.kim.ingwerCity.objects.ICPlayer;
import org.kim.ingwerCity.utils.InventoryBuilder;
import org.kim.ingwerCity.utils.ItemBuilder;
import org.kim.ingwerCity.utils.SimpleInventory;

import java.util.HashMap;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

public class FactionSettingsGUI implements Listener {
    private Inventory inventory;
    private HashMap<Integer, FChatenum> dyeMap = new HashMap<>();
    private HashMap<Inventory, String> inventoryMap = new HashMap<>();
    private HashMap<Integer, UUID> memberMap = new HashMap<>();

    public void openSettingsInventory(Player player) {
        IngwerCity plugin = IngwerCity.getInstance();
        FactionMethods factionMethods = new FactionMethods();
        UUID playerUUID = player.getUniqueId();
        ICPlayer icPlayer = ICPlayer.IC_PLAYER_HASH_MAP.get(playerUUID);
        FactionObject factionObject = factionMethods.getPlayerFaction(icPlayer);
        String name = factionObject.getName();
        List<Component> loreChest = List.of(
                Component.text("Faction ID: " + factionObject.getId()),
                Component.text("Faction Name: " + name),
                Component.text("Faction Bank: " + factionObject.getBank()),
                Component.text("Faction Spawn: " + factionObject.getSpawnX() + "/" + factionObject.getSpawnY() + "/" + factionObject.getSpawnZ())
        );
        List<Component> loreDye = List.of(
                Component.text("Change the dye color of the faction chat"),
                Component.text("Current Dye: " + FChatenum.getNameByID(factionObject.getChatDyeID()))
        );
        SQLMethods sqlMethods = new SQLMethods();
        sqlMethods.getFactionMembers(factionObject.getId()).thenAccept(factionMembersList -> {
            List<Component> loreMember = List.of(
                    Component.text("Faction Members: " + factionMembersList.size() + "/" + FactionTypeEnum.getSize(factionObject.getType())),
                    Component.text("Click to view members")
            );
            Bukkit.getScheduler().runTask(plugin, () -> {
                SimpleInventory inventory = new SimpleInventory(plugin, Component.text("Faction - Settings"), 36)
                        .item(13, new ItemBuilder(Material.CHEST, 1, Component.text("Faction: " + name).color(TextColor.color(0x00FF00)), loreChest).build())
                        .item(20, new ItemBuilder(Material.PURPLE_DYE, 1, Component.text("Change Chat-Dye").color(TextColor.color(0x00FF00)), loreDye).build(), (p, clickEvent) -> {
                            openDyeSettings(p);
                        }).item(24, new ItemBuilder(Material.PLAYER_HEAD, 1, Component.text("Faction Members").color(TextColor.color(0x00FF00)), loreMember).build(), (p, clickEvent) -> {
                            openMemberSettings(p, factionMembersList);
                        });
                inventory.open(player);
            });
        });
    }

    public void openDyeSettings(Player player) {
        Inventory inventory = new InventoryBuilder(54, Component.text("Faction - Dye Settings")).build();
        int slot = 0;
        for (FChatenum fChatenum : FChatenum.values()) {
            inventory.addItem(new ItemBuilder(Material.PURPLE_DYE, 1, Component.text(fChatenum.getName()), null).build());
            dyeMap.put(slot, fChatenum);
            slot++;
        }
        player.openInventory(inventory);
        this.inventory = inventory;
        inventoryMap.put(inventory, "dye");
        Bukkit.getPluginManager().registerEvents(this, IngwerCity.getInstance());
    }

    public void openMemberSettings(Player player, List<UUID> factionMembersList) {
        Inventory inventory = new InventoryBuilder(54, Component.text("Faction - Member Settings")).build();
        int slot = 0;
        for (UUID uuid : factionMembersList) {
            OfflinePlayer targetPlayer = Bukkit.getPlayer(uuid);
            if (targetPlayer != null) {
                inventory.addItem(new ItemBuilder(Material.PLAYER_HEAD, 1, Component.text(targetPlayer.getName()), List.of(Component.text("Clicke, um ihn zu uninviten!"))).build());
                memberMap.put(slot, uuid);
                slot++;
            }
        }
        player.openInventory(inventory);
        this.inventory = inventory;
        inventoryMap.put(inventory, "member");
        Bukkit.getPluginManager().registerEvents(this, IngwerCity.getInstance());
    }

    @EventHandler
    public void dyeInventoryClick(InventoryClickEvent event) {
        if (event.getInventory().equals(inventory)) {
            event.setCancelled(true);
            Player player = (Player) event.getWhoClicked();
            int slot = event.getSlot();
            if(inventoryMap.get(inventory).equals("dye")) {
                if (dyeMap.containsKey(slot)) {
                    FChatenum fChatenum = dyeMap.get(slot);
                    ICPlayer icPlayer = ICPlayer.IC_PLAYER_HASH_MAP.get(player.getUniqueId());
                    FactionMethods factionMethods = new FactionMethods();
                    FactionObject factionObject = factionMethods.getPlayerFaction(icPlayer);
                    FactionObject.factionList.remove(factionObject);
                    factionObject.setChatDyeID(fChatenum.getId());
                    FactionObject.factionList.add(factionObject);
                    SQLMethods sqlMethods = new SQLMethods();
                    player.sendMessage(Component.text("§aYou have changed the chat dye to " + fChatenum.getName()));
                    player.closeInventory();
                    CompletableFuture.runAsync(() -> sqlMethods.updateChatDye(factionObject.getId(), fChatenum.getId()));
                }
            } else if (inventoryMap.get(inventory).equals("member")) {
                if(memberMap.containsKey(slot)) {
                    UUID targetUUID = memberMap.get(slot);
                    UUID playerUUID = player.getUniqueId();
                    FactionUninviteCommand.uninviteMap.put(playerUUID, targetUUID);
                    player.closeInventory();
                    ConfirmUninviteGUI confirmUninviteGUI = new ConfirmUninviteGUI();
                    confirmUninviteGUI.openInventory(player);
                }
            }
        }
    }

    @EventHandler
    public void closeInventory(InventoryCloseEvent event) {
        if (event.getInventory().equals(inventory)) {
            HandlerList.unregisterAll(this);
            inventory = null;
        }
    }
}

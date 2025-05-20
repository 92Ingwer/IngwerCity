package org.kim.ingwerCity.jobs.elektriker;

import lombok.Getter;
import lombok.Setter;
import net.kyori.adventure.bossbar.BossBar;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.kim.ingwerCity.jobs.JobEnum;
import org.kim.ingwerCity.jobs.JobInterface;
import org.kim.ingwerCity.jobs.JobService;
import org.kim.ingwerCity.objects.ICPlayer;
import org.kim.ingwerCity.schedulers.Navigator;
import org.kim.ingwerCity.utils.ItemBuilder;

import java.util.HashMap;
import java.util.List;

@Getter
@Setter
public class ElectricerJob implements JobInterface, Listener {
    private String jobName = "Electricer";
    private int jobID;
    private int level;
    private int jobXP;
    private Player player;
    private BossBar activeBossBar;
    private Inventory sortierInv;

    private HashMap<Player, Location> locationHashMap = new HashMap<>();

    public ElectricerJob(int level, int jobXP, Player player) {
        this.level = level;
        this.jobXP = jobXP;
        this.player = player;
        for (JobEnum jobEnum : JobEnum.values()) {
            if (jobEnum.getName().equalsIgnoreCase(jobName)) {
                this.jobID = jobEnum.getJobID();
            }
        }
    }
    @Override
    public int getJobID() {
        return jobID;
    }


    @Override
    public void onJobStart() {
        JobService.PLAYER_IN_A_JOB.put(player.getUniqueId(), this);
        BossBar bossBar = BossBar.bossBar(Component.text("Electricer: Level: " + level + " XP: " + jobXP + "/" + getMaxXP()).color(TextColor.color(0xFF0000)), (float) jobXP / getMaxXP(), BossBar.Color.BLUE, BossBar.Overlay.PROGRESS);
        player.showBossBar(bossBar);
        this.activeBossBar = bossBar;
        getLocationRoute();
        player.getInventory().addItem(getZange());
    }


    public void getLocationRoute() {
        Location location = ElectricerLocationEnum.getRandomElectricerLocation();
        Navigator navigator = new Navigator();
        navigator.startNavi(player, location);
        locationHashMap.put(player, location);
    }

    public static ItemStack getZange() {
        return new ItemBuilder(Material.TRIPWIRE_HOOK, 1, Component.text("Reparaturzange"), List.of(Component.text("Klicke den Stromkasten an (was leuchtet)"))).build();
    }

    public void openSortGame() {
        SortMiniGame sortMiniGame = new SortMiniGame();
        sortMiniGame.openSortMinigame(player);
    }
    @Override
    public void onJobEnd() {
        activeBossBar.removeViewer(player);
        activeBossBar = null;
        JobService.PLAYER_IN_A_JOB.remove(player.getUniqueId());
        player.getInventory().removeItem(getZange());
        locationHashMap.remove(player);
    }

    @Override
    public void onGetMoney() {
        ICPlayer icPlayer = ICPlayer.IC_PLAYER_HASH_MAP.get(player.getUniqueId());
        double money = JobService.jobMoneyMap.get(jobID);
        icPlayer.setPayday(icPlayer.getPayday() + money);
        player.sendMessage("Du hast " + money + "$ verdient! Das kriegst du am PayDay");
    }

    @Override
    public void onGetExperience() {
        ICPlayer icPlayer = ICPlayer.IC_PLAYER_HASH_MAP.get(player.getUniqueId());
        icPlayer.setXp(icPlayer.getXp() + 50);
        player.sendMessage("Du hast 50 XP verdient!");
        jobXP += 50;
        if (jobXP >= getMaxXP()) {
            level++;
            jobXP = 0;
            player.sendMessage("Du hast Level " + level + " erreicht!");
        }
        refreshBossbar();
    }
    public void refreshBossbar() {
        activeBossBar.name(Component.text("Electricer: Level: " + level + " XP: " + jobXP + "/" + getMaxXP()));
        activeBossBar.progress((float) jobXP / getMaxXP());
    }
    @Override
    public int getMaxXP() {
        return (int) ((level + 1) * 100 + Math.pow(level, 4));
    }

    @Override
    public String getJobName() {
        return jobName;
    }

    @Override
    public int getXP() {
        return jobXP;
    }

    @Override
    public int getLevel() {
        return level;
    }
}

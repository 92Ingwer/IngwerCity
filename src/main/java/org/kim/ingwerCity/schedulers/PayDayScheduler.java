package org.kim.ingwerCity.schedulers;

import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitRunnable;
import org.kim.ingwerCity.IngwerCity;
import org.kim.ingwerCity.jobs.JobEnum;
import org.kim.ingwerCity.jobs.JobService;
import org.kim.ingwerCity.objects.ICPlayer;

import java.time.LocalDateTime;

public class PayDayScheduler {
    private int lastHour = -1;

    public void startScheduler(IngwerCity plugin) {
        setJobSalary();
        new BukkitRunnable() {
            @Override
            public void run() {
                LocalDateTime now = LocalDateTime.now();
                int currentHour = now.getHour();
                int currentMinute = now.getMinute();
                int currentSecond = now.getSecond();
                if (currentMinute == 0 && currentSecond == 0 && lastHour != currentHour) {
                    if (currentHour % 3 == 0) {
                        setJobSalary();
                    }
                    lastHour = currentHour;
                    Bukkit.getOnlinePlayers().forEach(player -> {
                        player.sendMessage("PayDay! You have received your salary.");
                        ICPlayer icPlayer = ICPlayer.IC_PLAYER_HASH_MAP.get(player.getUniqueId());
                        double payday = icPlayer.getPayday();
                        icPlayer.setBankmoney(icPlayer.getBankmoney() + payday);
                        icPlayer.setPayday(0);
                    });
                }
            }
        }.runTaskTimer(plugin, 0L, 20L);
    }

    public void setJobSalary() {
        for (JobEnum jobEnum : JobEnum.values()) {
            double minMoney = jobEnum.getMinMoney();
            double maxMoney = jobEnum.getMaxMoney();
            double randomMoney = Math.random() * (maxMoney - minMoney) + minMoney;
            randomMoney = Math.round(randomMoney * 1); // Round to 2 decimal places
            JobService.jobMoneyMap.put(jobEnum.getJobID(), randomMoney);
        }
    }
}

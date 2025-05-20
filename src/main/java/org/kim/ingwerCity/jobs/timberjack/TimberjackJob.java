package org.kim.ingwerCity.jobs.timberjack;

import net.kyori.adventure.bossbar.BossBar;
import org.bukkit.entity.Player;
import org.kim.ingwerCity.jobs.JobEnum;
import org.kim.ingwerCity.jobs.JobInterface;

public class TimberjackJob implements JobInterface {
    private String jobName = "Timberjack";
    private int jobID;
    private int level;
    private int jobXP;
    private Player player;
    private BossBar activeBossBar;

    public TimberjackJob(int level, int jobXP, Player player) {
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
    public void onJobStart() {

    }

    @Override
    public void onJobEnd() {

    }

    @Override
    public void onGetMoney() {
        return;
    }

    @Override
    public int getJobID() {
        return jobID;
    }

    @Override
    public void onGetExperience() {
        return;
    }

    @Override
    public int getMaxXP() {
        return 0;
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

package org.kim.ingwerCity.jobs;

import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public class JobService {
    public static HashMap<UUID,JobInterface> PLAYER_IN_A_JOB = new HashMap<>();
    public static HashMap<UUID, List<JobInterface>> playerJobsListMap = new HashMap<>();
    public static HashMap<Integer, Double> jobMoneyMap = new HashMap<>();
    public int getJobID(String name) {
        for (JobEnum jobEnum : JobEnum.values()) {
            if (jobEnum.getName().equalsIgnoreCase(name)) {
                return jobEnum.getJobID();
            }
        }
        return -1;
    }
}

package org.kim.ingwerCity.jobs;

public interface JobInterface {

    void onJobStart();
    void onJobEnd();
    void onGetMoney();
    void onGetExperience();
    int getMaxXP();
    String getJobName();
    int getXP();
    int getLevel();
    int getJobID();
}

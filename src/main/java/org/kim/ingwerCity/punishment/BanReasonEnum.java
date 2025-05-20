package org.kim.ingwerCity.punishment;

public enum BanReasonEnum {
    VERBAL_HARASSMENT("Verbal_Harassment",0,0,0),
    SPAM("Spam",0,0,1);


    private String reason;
    private int days;
    private int hours;
    private int minutes;
    BanReasonEnum(String reason, int days, int hours, int minutes) {
        this.reason = reason;
        this.days = days;
        this.hours = hours;
        this.minutes = minutes;
    }
    public static long getDate(String reason) {
        for(BanReasonEnum banReasonEnum : BanReasonEnum.values()) {
            if(banReasonEnum.reason.equalsIgnoreCase(reason)) {
                return System.currentTimeMillis() + ((long) banReasonEnum.days * 24 * 60 * 60 * 1000) + ((long) banReasonEnum.hours * 60 * 60 * 1000) + ((long) banReasonEnum.minutes * 60 * 1000);
            }
        }
        return -1;
    }
    public String getReason() {
        return reason;
    }
}

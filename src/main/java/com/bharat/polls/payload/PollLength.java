package com.bharat.polls.payload;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public class PollLength {

    @NotNull
    @Size(max=7)
    private Integer days;

    @NotNull
    @Size(max=23)
    private Integer hours;

    public Integer getDays() {
        return days;
    }

    public void setDays(Integer days) {
        this.days = days;
    }

    public Integer getHours() {
        return hours;
    }

    public void setHours(Integer hours) {
        this.hours = hours;
    }
}

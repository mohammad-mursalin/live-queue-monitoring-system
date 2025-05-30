package com.mursalin.queue_monitoring_system.model;

import lombok.Data;

import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.List;

@Data
public class Schedule {
    private List<DayOfWeek> dayOfWeeks;
    private LocalTime startTime;
}

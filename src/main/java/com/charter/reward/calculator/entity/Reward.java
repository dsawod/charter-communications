package com.charter.reward.calculator.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.Month;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@NoArgsConstructor(force = true)
public class Reward implements Serializable {

    private static final long DEFAULT_TOTAL_VALUE = 0l;

    private final String customerName;
    private final Map<Month, Long> pointsByMonth;

    public Reward(String customerName, Map<Month, Long> pointsByMonth) {
        this.customerName = customerName;
        this.pointsByMonth = pointsByMonth;
    }


    public String getCustomerName() {
        return customerName;
    }

    public Map<Month, Long> getPointsByMonth() {
        return new HashMap<>(pointsByMonth);
    }

    public void addPointsByMonth(Map<Month, Long> pointsByMonth) {
        this.pointsByMonth.entrySet().addAll(pointsByMonth.entrySet());
    }

    @JsonProperty("totalPoints")
    private long totalPoints() {
        return Optional.ofNullable(this.pointsByMonth)
            .map(this::sum)
            .orElse(DEFAULT_TOTAL_VALUE);
    }

    private Long sum(Map<Month, Long> pointMonthMap) {
        return pointMonthMap.entrySet()
            .stream()
            .map(Map.Entry::getValue)
            .mapToLong(Long::longValue)
            .sum();
    }
}

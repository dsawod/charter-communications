package com.charter.reward.calculator.service;

import com.charter.reward.calculator.entity.Customer;
import com.charter.reward.calculator.entity.Purchase;
import com.charter.reward.calculator.entity.Reward;
import com.charter.reward.calculator.repository.CustomerRepository;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.Month;
import java.time.ZoneId;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import javax.validation.constraints.NotNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Slf4j
@Service
public class RewardService {
    protected final Function<Instant, Month> instantMonthFunction =
        instant -> LocalDateTime.ofInstant(instant, ZoneId.systemDefault()).getMonth();

    private final CustomerRepository customerRepository;

    @Autowired
    public RewardService(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    public List<Reward> allRewards() {
        log.info("all rewards hitted...");
        return this.customerRepository.findAll()
                .stream()
                .map(Customer::getId)
                .map(this::rewardByCustomerId)
                .collect(Collectors.toList());
    }

    public Reward rewardByCustomerId(@NotNull Long customerId) {
       log.info("Calculating Reward for customer id: "+ customerId);
        return this.customerRepository.findById(customerId)
                .map(Customer::getPurchases)
                .map(this::from)
                .orElseThrow(() ->
                        new ResponseStatusException(HttpStatus.NOT_FOUND,
                                String.format("Customer id %s not found", customerId)));
    }

    protected Reward from(List<Purchase> purchases) {
        checkArgument(!purchases.isEmpty(),
            new ResponseStatusException(
                HttpStatus.OK, "Customer Has No Purchase History"));
        log.info("purchase list {}", purchases);
        final Map<Month, Long> pointsByMonth = new HashMap<>();
        purchases.stream()
            .forEach(purchase -> {
                final Month month = instantMonthFunction.apply(purchase.getTimestamp());
                pointsByMonth.put(month,
                    pointsByMonth.getOrDefault(month, 0l) + rewardCalculator(purchase.getAmount()));
            });
        return new Reward(purchases.get(0).getCustomer().getName(), pointsByMonth);
    }

    public Long rewardCalculator(BigDecimal amount) {
        long amt = amount.longValue();
        return amt < 50 ? 0l : amt <= 100 ? amt - 50 : (2l * (amt - 100)) + 50;
    }

    void checkArgument(boolean expression, RuntimeException exceptionToThrow) {
        if (!expression) {
            throw exceptionToThrow;
        }
    }

}

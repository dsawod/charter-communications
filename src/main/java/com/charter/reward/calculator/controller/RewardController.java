package com.charter.reward.calculator.controller;

import com.charter.reward.calculator.entity.Reward;
import com.charter.reward.calculator.service.RewardService;

import java.util.List;
import java.util.Optional;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/rewards")
public class RewardController {

    private final RewardService rewardService;

    @Autowired
    public RewardController(RewardService rewardService) {
        this.rewardService = rewardService;
    }

    @GetMapping
    public ResponseEntity<List<Reward>> getRewards(@RequestParam Optional<Long> customerId) {
        List<Reward> rewards = customerId.isPresent()?
                List.of(rewardService.rewardByCustomerId(customerId.get())) :
                rewardService.allRewards();
        if (customerId.isPresent()) log.info("CustomerIdIs Present: {}", customerId.get());
        return new ResponseEntity(rewards, HttpStatus.OK);
    }
}
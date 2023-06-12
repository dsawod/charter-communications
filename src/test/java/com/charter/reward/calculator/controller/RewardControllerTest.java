package com.charter.reward.calculator.controller;

import com.charter.reward.calculator.entity.Reward;
import com.charter.reward.calculator.service.RewardService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class RewardControllerTest {
    @Mock
    private RewardService rewardService;

    @InjectMocks
    private RewardController rewardReSource;

    @Test
    public void testGetRewardsForCustomerId() {
        Reward reward = new Reward();
        when(rewardService.rewardByCustomerId(anyLong())).thenReturn(reward);

        ResponseEntity<List<Reward>> response = rewardReSource.getRewards(java.util.Optional.of((1L)));
        List<Reward> rewards = response.getBody();
        assertEquals(Collections.singletonList(reward), rewards);
    }
}
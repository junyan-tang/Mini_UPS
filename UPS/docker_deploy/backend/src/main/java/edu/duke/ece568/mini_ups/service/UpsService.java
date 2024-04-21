package edu.duke.ece568.mini_ups.service;

import org.springframework.beans.factory.annotation.Autowired;

import edu.duke.ece568.mini_ups.service.network.AmazonNetService;
import edu.duke.ece568.mini_ups.service.network.WorldNetService;

public class UpsService {
    WorldNetService worldNetService;
    AmazonNetService amazonNetService;

    @Autowired
    public UpsService(WorldNetService worldNetService, AmazonNetService amazonNetService) {
        this.worldNetService = worldNetService;
        this.amazonNetService = amazonNetService;
    }
}

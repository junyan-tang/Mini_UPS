package edu.duke.ece568.mini_ups.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import edu.duke.ece568.mini_ups.service.network.AmazonNetService;
import edu.duke.ece568.mini_ups.service.network.WorldNetService;
import edu.duke.ece568.mini_ups.service.sender.AmazonCmdSender;
import edu.duke.ece568.mini_ups.service.sender.WorldCmdSender;

@Service
public class UpsService {
    WorldNetService worldNetService;
    AmazonNetService amazonNetService;

    @Autowired
    public UpsService(WorldNetService worldNetService, AmazonNetService amazonNetService) {
        this.worldNetService = worldNetService;
        this.amazonNetService = amazonNetService;
    }
    // @Autowired
    // public UpsService(WorldNetService worldNetService) {
    // this.worldNetService = worldNetService;
    // }

    public void start() {
        AmazonCmdSender amazonCmdSender = new AmazonCmdSender();
        amazonCmdSender.setAmazonCmdSender(amazonNetService.out);
        WorldCmdSender worldCmdSender = new WorldCmdSender();
        worldCmdSender.setWorldCmdSender(worldNetService.out);
        amazonNetService.setamazonResHandlerACmdSender(amazonCmdSender);
        amazonNetService.setamazonResHandlerWCmdSender(worldCmdSender);
        worldNetService.setworldRespHandlerACmdSender(amazonCmdSender);
        worldNetService.setworldRespHandlerWCmdSender(worldCmdSender);

        System.out.println("UPS Service Started");
        worldNetService.sendUConnect(2L, false);
        if (worldNetService.receiveUconnected()) {
            System.out.println("Connected to World");
        } else {
            return;
        }
        while (true) {
            try {
                amazonNetService.receiveCommand();
                worldNetService.receiveMessage();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}

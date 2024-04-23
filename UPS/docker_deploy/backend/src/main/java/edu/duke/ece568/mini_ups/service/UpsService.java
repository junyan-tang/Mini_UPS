// package edu.duke.ece568.mini_ups.service;

// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.stereotype.Service;

// import edu.duke.ece568.mini_ups.service.network.AmazonNetService;
// import edu.duke.ece568.mini_ups.service.network.WorldNetService;

// @Service
// public class UpsService {
//     WorldNetService worldNetService;
//     //AmazonNetService amazonNetService;

//     // @Autowired
//     // public UpsService(WorldNetService worldNetService, AmazonNetService amazonNetService) {
//     //     this.worldNetService = worldNetService;
//     //     this.amazonNetService = amazonNetService;
//     // }
//     @Autowired
//     public UpsService(WorldNetService worldNetService) {
//         this.worldNetService = worldNetService;
//     }

//     public void start() {
//         System.out.println("UPS Service Started");
//         worldNetService.sendUConnect(1L, false);
//         try {
//             worldNetService.receiveMessage();
//         } catch (Exception e) {
//             e.printStackTrace();
//         }
//     }
// }

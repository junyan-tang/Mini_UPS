// package edu.duke.ece568.mini_ups.service.handler;

// import org.springframework.stereotype.Service;

// import edu.duke.ece568.mini_ups.protocol.upsToAmazon.AmazonUps.ACheckUsername;
// import edu.duke.ece568.mini_ups.protocol.upsToAmazon.AmazonUps.ACommand;
// import edu.duke.ece568.mini_ups.protocol.upsToAmazon.AmazonUps.AOrderATruck;
// import edu.duke.ece568.mini_ups.protocol.upsToAmazon.AmazonUps.AStartDelivery;

// @Service
// public class AmazonRespHandler {
//     public AmazonRespHandler() {
//     }
//     public void handle(ACommand command) {
//         System.out.println("AmazonResHandler: " + command.toString());
//         handleOrderTruck(command);
//         handleStartDelivery(command);
//         handleCheckUsername(command);
//         handleAck(command);
//     }
//     private void handleOrderTruck(ACommand command) {
//         for (AOrderATruck order : command.getToOrderList()) {
//             System.out.println("Order truck for package " + order.getPackageID());
//         }
//     }
//     private void handleStartDelivery(ACommand command) {
//         for (AStartDelivery delivery : command.getToStartList()) {
//             System.out.println("Start delivery for package " + delivery.getPackageID());
//         }
//     }
//     private void handleCheckUsername(ACommand command) {
//         for (ACheckUsername checkUser : command.getCheckUsersList()) {
//             System.out.println("Check username for " + checkUser.getUpsUsername());
//         }
//     }
//     private void handleAck(ACommand command) {
//         for (Long ack : command.getAcksList()) {
//             System.out.println("Ack for package ");
//         }
//     }
// }

package edu.duke.ece568.mini_ups.service.handler;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import edu.duke.ece568.mini_ups.entity.Item;
import edu.duke.ece568.mini_ups.entity.Package;
import edu.duke.ece568.mini_ups.entity.Truck;
import edu.duke.ece568.mini_ups.entity.Users;
import edu.duke.ece568.mini_ups.protocol.upsToAmazon.AmazonUps.ACheckUsername;
import edu.duke.ece568.mini_ups.protocol.upsToAmazon.AmazonUps.ACommand;
import edu.duke.ece568.mini_ups.protocol.upsToAmazon.AmazonUps.AOrderATruck;
import edu.duke.ece568.mini_ups.protocol.upsToAmazon.AmazonUps.AStartDelivery;
import edu.duke.ece568.mini_ups.service.CommandStore;
import edu.duke.ece568.mini_ups.service.ItemService;
import edu.duke.ece568.mini_ups.service.PackageService;
import edu.duke.ece568.mini_ups.service.TruckService;
import edu.duke.ece568.mini_ups.service.UserService;
import edu.duke.ece568.mini_ups.service.sender.AmazonCmdSender;
import edu.duke.ece568.mini_ups.service.sender.WorldCmdSender;

@Service
public class AmazonRespHandler {
    private UserService userService;
    private PackageService packageService;
    private ItemService itemService;
    private TruckService truckService;
    private AmazonCmdSender amazonCmdSender;
    private WorldCmdSender worldCmdSender;

    @Autowired
    public AmazonRespHandler(UserService userService, PackageService packageService,
            TruckService truckService, ItemService itemService) {
        this.userService = userService;
        this.packageService = packageService;
        this.truckService = truckService;
        this.itemService = itemService;
    }

    public void setAmazonCmdSender(AmazonCmdSender amazonCmdSender) {
        this.amazonCmdSender = amazonCmdSender;
    }

    public void setWorldCmdSender(WorldCmdSender worldCmdSender) {
        this.worldCmdSender = worldCmdSender;
    }

    public void handle(ACommand command) {
        System.out.println("AmazonResHandler: " + command.toString());
        handleOrderTruck(command);
        handleStartDelivery(command);
        handleCheckUsername(command);
        handleAck(command);
    }

    public void handleAnOrderTruck(AOrderATruck order) {
        try {
            //amazonCmdSender.sendAck(order.getSeqnum());
            System.out.println("Order truck for package " + order.getPackageID());
            Users user = userService.findByUsername(order.getUpsUsername());

            if (user == null) {
                amazonCmdSender.sendError("User does not exist", order.getSeqnum());
                return;
            }

            Truck truck = truckService.findBestAvailableTruck(order.getDestinationInfo().getX(),
                    order.getDestinationInfo().getY());
            if (truck == null) {
                System.out.println("No available trucks");
                CommandStore.pendingOrders.add(order);
                //amazonCmdSender.sendError("No available trucks", order.getSeqnum());
                return;
            }
            truckService.updateStatus(truck.getTruckId(), "TRAVELLING");

            Package newPackage = new Package();
            newPackage.setPackageId(order.getPackageID());
            newPackage.setTruck(truck);
            newPackage.setUser(user);
            newPackage.setStatus("Wait for Pick Up");
            newPackage.setCurrentX(order.getWarehouseInfo().getX());
            newPackage.setCurrentY(order.getWarehouseInfo().getY());
            newPackage.setDestinationX(order.getDestinationInfo().getX());
            newPackage.setDestinationY(order.getDestinationInfo().getY());
            packageService.save(newPackage);

            for (int i = 0; i < order.getProductInfoCount(); i++) {
                Item item = new Item();
                item.setPackages(newPackage);
                item.setProductId(order.getProductInfo(i).getProductID());
                item.setDescription(order.getProductInfo(i).getDescription());
                item.setQuantity(order.getProductInfo(i).getCount());
                itemService.save(item);
            }

            worldCmdSender.sendPickups(truck.getTruckId(), order.getWarehouseInfo().getWarehouseID());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void handleOrderTruck(ACommand command) {
        for (AOrderATruck order : command.getToOrderList()) {
            try {
                amazonCmdSender.sendAck(order.getSeqnum());
                System.out.println("Order truck for package " + order.getPackageID());
                Users user = userService.findByUsername(order.getUpsUsername());

                if (user == null) {
                    amazonCmdSender.sendError("User does not exist", order.getSeqnum());
                    continue;
                }

                Truck truck = truckService.findBestAvailableTruck(order.getDestinationInfo().getX(),
                        order.getDestinationInfo().getY());
                if (truck == null) {
                    System.out.println("No available trucks");
                    CommandStore.pendingOrders.add(order);
                    //amazonCmdSender.sendError("No available trucks", order.getSeqnum());
                    continue;
                }
                truckService.updateStatus(truck.getTruckId(), "TRAVELLING");

                Package newPackage = new Package();
                newPackage.setPackageId(order.getPackageID());
                newPackage.setTruck(truck);
                newPackage.setUser(user);
                newPackage.setStatus("Wait for Pick Up");
                newPackage.setCurrentX(order.getWarehouseInfo().getX());
                newPackage.setCurrentY(order.getWarehouseInfo().getY());
                newPackage.setDestinationX(order.getDestinationInfo().getX());
                newPackage.setDestinationY(order.getDestinationInfo().getY());
                packageService.save(newPackage);

                for (int i = 0; i < order.getProductInfoCount(); i++) {
                    Item item = new Item();
                    item.setPackages(newPackage);
                    item.setProductId(order.getProductInfo(i).getProductID());
                    item.setDescription(order.getProductInfo(i).getDescription());
                    item.setQuantity(order.getProductInfo(i).getCount());
                    itemService.save(item);
                }

                worldCmdSender.sendPickups(truck.getTruckId(), order.getWarehouseInfo().getWarehouseID());
                return;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void handleStartDelivery(ACommand command) {
        for (AStartDelivery delivery : command.getToStartList()) {
            try {
                amazonCmdSender.sendAck(delivery.getSeqnum());
                System.out.println("Start delivery for package " + delivery.getPackageID());
                Package p = packageService.findById(delivery.getPackageID());
                if (p == null) {
                    try {
                        amazonCmdSender.sendError("Package does not exist", delivery.getSeqnum());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    continue;
                }
                p.setStatus("Out for Delivery");
                packageService.save(p);

                truckService.updateStatus(p.getTruck().getTruckId(), "DELIVERING");

                worldCmdSender.sendDelivery(p.getTruck().getTruckId(), p);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void handleCheckUsername(ACommand command) {
        for (ACheckUsername checkUser : command.getCheckUsersList()) {
            try {
                amazonCmdSender.sendAck(checkUser.getSeqnum());
                System.out.println("Check username for " + checkUser.getUpsUsername());
                Users user = userService.findByUsername(checkUser.getUpsUsername());
                if (user == null) {
                    amazonCmdSender.sendUsernameCheckResponse(checkUser.getUpsUsername(), -1);
                    // amazonCmdSender.sendError("User does not exist", checkUser.getSeqnum());

                } else {
                    amazonCmdSender.sendUsernameCheckResponse(user.getUsername(), user.getUserid());
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void handleAck(ACommand command) {
        for (Long ack : command.getAcksList()) {
            System.out.println("Ack for package " + ack);
        }
    }
}

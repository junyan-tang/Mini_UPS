package edu.duke.ece568.mini_ups.service.handler;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import edu.duke.ece568.mini_ups.entity.Package;
import edu.duke.ece568.mini_ups.protocol.upsToWorld.WorldUps.UDeliveryMade;
import edu.duke.ece568.mini_ups.protocol.upsToWorld.WorldUps.UErr;
import edu.duke.ece568.mini_ups.protocol.upsToWorld.WorldUps.UFinished;
import edu.duke.ece568.mini_ups.protocol.upsToWorld.WorldUps.UResponses;
import edu.duke.ece568.mini_ups.protocol.upsToWorld.WorldUps.UTruck;
import edu.duke.ece568.mini_ups.service.EmailSender;
import edu.duke.ece568.mini_ups.service.PackageService;
import edu.duke.ece568.mini_ups.service.TruckService;
import edu.duke.ece568.mini_ups.service.network.ConnectionCloser;
import edu.duke.ece568.mini_ups.service.sender.AmazonCmdSender;
import edu.duke.ece568.mini_ups.service.sender.WorldCmdSender;

@Service
public class WorldRespHandler {

    private ConnectionCloser connectionCloser;
    private PackageService packageService;
    private TruckService truckService;
    private AmazonCmdSender amazonCmdSender;
    private WorldCmdSender worldCmdSender;
    private EmailSender emailService;

    @Autowired
    public WorldRespHandler(PackageService packageService,
            TruckService truckService, EmailSender emailService) {
        this.packageService = packageService;
        this.truckService = truckService;
        this.emailService = emailService;
    }

    public void setConnectionCloser(ConnectionCloser connectionCloser) {
        this.connectionCloser = connectionCloser;
    }

    public void setAmazonCmdSender(AmazonCmdSender amazonCmdSender) {
        this.amazonCmdSender = amazonCmdSender;
    }

    public void setWorldCmdSender(WorldCmdSender worldCmdSender) {
        this.worldCmdSender = worldCmdSender;
    }

    public void handle(UResponses response) {
        //System.out.println("Received response from world: " + response);
        handleQuery(response);
        handleFinished(response);
        handleCompletions(response);
        handleDelivered(response);
        //handleTruckStatusUpdates(response);
        handleErrors(response);
    }

    public void sendTruckQueries(int truck_num) {
        try {
            worldCmdSender.sendTruckQueries(truck_num);
        } catch (Exception e) {
            System.out.println("Error: " + e);
        }
    }

    public void sendChangeDest(Long packageId, int x, int y) {
        try {
            amazonCmdSender.sendChangeDestination(packageId, x, y);
        } catch (Exception e) {
            System.out.println("Error: " + e);
        }
    }

    private void handleQuery(UResponses response) {
        for (UTruck truck : response.getTruckstatusList()) {
            try {
                worldCmdSender.sendAck(truck.getSeqnum());
                truckService.updateStatus(truck.getTruckid(), truck.getStatus());
                truckService.updateLocation(truck.getTruckid(), truck.getX(), truck.getY());
            } catch (Exception e) {
                System.out.println("Error: " + e);
            }
        }
    }

    private void handleFinished(UResponses response) {
        if (response.hasFinished()) {
            boolean isFinished = response.getFinished();
            System.out.println("Finished field: " + isFinished);
            if (isFinished) {
                System.out.println("Server has finished processing commands and will close the connection.");
                connectionCloser.closeConnection();
            }
        }
    }

    private void handleCompletions(UResponses response) {
        for (UFinished finished : response.getCompletionsList()) {
            try {
                worldCmdSender.sendAck(finished.getSeqnum());

                System.out.println("Truck " + finished.getTruckid() + " completed at (" + finished.getX() + ", "
                        + finished.getY() + ") with status: " + finished.getStatus());
                truckService.updateStatus(finished.getTruckid(), finished.getStatus());
                truckService.updateLocation(finished.getTruckid(), finished.getX(), finished.getY());

                if (finished.getStatus().equals("ARRIVE WAREHOUSE")) {
                    List<Package> packages = packageService.findByStatusAndTruckIdAndLocation("Wait for Pick Up",
                            finished.getTruckid(), finished.getX(), finished.getY());
                    for (Package p : packages) {
                        // p.setStatus("LOADING");
                        // packageService.save(p);
                        truckService.updateStatus(finished.getTruckid(), "LOADING");
                        amazonCmdSender.sendTruckArrival(p.getPackageId(), finished.getTruckid());

                    }
                }
            } catch (Exception e) {
                System.out.println("Error: " + e);
            }
        }
    }

    private void handleDelivered(UResponses response) {
        for (UDeliveryMade delivery : response.getDeliveredList()) {
            try {
                worldCmdSender.sendAck(delivery.getSeqnum());
                System.out.println(
                        "Delivery made by truck " + delivery.getTruckid() + " for package " + delivery.getPackageid());
                Optional<Package> p = packageService.findByStatusAndTruckIdAndPackageId("Out for Delivery",
                        delivery.getTruckid(), delivery.getPackageid());
                if (p.isPresent()) {
                    Package pack = p.get();
                    pack.setStatus("Delivered");
                    packageService.save(pack);

                    emailService.sendEmail(pack.getUser().getEmail(), "Ups notification");

                    try {
                        amazonCmdSender.sendDelivered(pack.getPackageId());
                    } catch (Exception e) {
                        System.out.println("Error: " + e);
                    }
                } else {
                    System.out.println("Package not found");

                    worldCmdSender.sendError("Package not found", delivery.getSeqnum());

                }
            } catch (Exception e) {
                System.out.println("Error: " + e);
            }
        }
    }

    // private void handleTruckStatusUpdates(UResponses response) {
    //     if (!response.getTruckstatusList().isEmpty()) {
    //         System.out.println("Truck status updates:");
    //         for (UTruck truck : response.getTruckstatusList()) {
    //             System.out.println("Truck " + truck.getTruckid() + " is " + truck.getStatus() + " at (" + truck.getX()
    //                     + ", " + truck.getY() + ")");
    //         }
    //     }
    // }

    private void handleErrors(UResponses response) {
        for (UErr error : response.getErrorList()) {
            System.out.println("Error: " + error.getErr() + " from sequence number: " + error.getOriginseqnum());
        }
    }
}

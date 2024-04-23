package edu.duke.ece568.mini_ups.service.handler;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import edu.duke.ece568.mini_ups.protocol.upsToWorld.WorldUps.UDeliveryMade;
import edu.duke.ece568.mini_ups.protocol.upsToWorld.WorldUps.UErr;
import edu.duke.ece568.mini_ups.protocol.upsToWorld.WorldUps.UFinished;
import edu.duke.ece568.mini_ups.protocol.upsToWorld.WorldUps.UResponses;
import edu.duke.ece568.mini_ups.protocol.upsToWorld.WorldUps.UTruck;
import edu.duke.ece568.mini_ups.repository.PackageRepository;
import edu.duke.ece568.mini_ups.repository.TruckRepository;
import edu.duke.ece568.mini_ups.repository.UserRepository;
import edu.duke.ece568.mini_ups.service.network.ConnectionCloser;
import edu.duke.ece568.mini_ups.service.sender.AmazonCmdSender;
import edu.duke.ece568.mini_ups.service.sender.WorldCmdSender;

@Service
public class WorldRespHandler {

    private ConnectionCloser connectionCloser;
    private UserRepository userRepository;
    private PackageRepository packageRepository;
    private TruckRepository truckRepository;
    private AmazonCmdSender amazonCmdSender;
    private WorldCmdSender worldCmdSender;

    @Autowired
    private WorldRespHandler(UserRepository userRepository, PackageRepository packageRepository,
            TruckRepository truckRepository) {
        this.userRepository = userRepository;
        this.packageRepository = packageRepository;
        this.truckRepository = truckRepository;
    }

    public WorldRespHandler(@Qualifier("worldNetService") ConnectionCloser connectionCloser) {
        this.connectionCloser = connectionCloser;
    }

    public void setAmazonCmdSender(AmazonCmdSender amazonCmdSender) {
        this.amazonCmdSender = amazonCmdSender;
    }
    public void setWorldCmdSender(WorldCmdSender worldCmdSender) {
        this.worldCmdSender = worldCmdSender;
    }
    public void handle(UResponses response) {
        System.out.println("Received response from world: " + response);
        handleFinished(response);
        handleCompletions(response);
        handleDeliveries(response);
        handleTruckStatusUpdates(response);
        handleErrors(response);
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
            System.out.println("Truck " + finished.getTruckid() + " completed at (" + finished.getX() + ", "
                    + finished.getY() + ") with status: " + finished.getStatus());
            
        }
    }

    private void handleDeliveries(UResponses response) {
        for (UDeliveryMade delivery : response.getDeliveredList()) {
            System.out.println(
                    "Delivery made by truck " + delivery.getTruckid() + " for package " + delivery.getPackageid());
        }
    }

    private void handleTruckStatusUpdates(UResponses response) {
        if (!response.getTruckstatusList().isEmpty()) {
            System.out.println("Truck status updates:");
            for (UTruck truck : response.getTruckstatusList()) {
                System.out.println("Truck " + truck.getTruckid() + " is " + truck.getStatus() + " at (" + truck.getX()
                        + ", " + truck.getY() + ")");
            }
        }
    }

    private void handleErrors(UResponses response) {
        for (UErr error : response.getErrorList()) {
            System.out.println("Error: " + error.getErr() + " from sequence number: " + error.getOriginseqnum());
        }
    }
}

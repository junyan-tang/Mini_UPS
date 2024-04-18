package edu.duke.ece568.mini_ups.service.handler;

import edu.duke.ece568.mini_ups.protocol.upsToWorld.WorldUps.UDeliveryMade;
import edu.duke.ece568.mini_ups.protocol.upsToWorld.WorldUps.UErr;
import edu.duke.ece568.mini_ups.protocol.upsToWorld.WorldUps.UFinished;
import edu.duke.ece568.mini_ups.protocol.upsToWorld.WorldUps.UResponses;
import edu.duke.ece568.mini_ups.protocol.upsToWorld.WorldUps.UTruck;
import edu.duke.ece568.mini_ups.service.network.ConnectionCloser;

public class WorldResHandler {

    private ConnectionCloser connectionCloser;

    public WorldResHandler(ConnectionCloser connectionCloser) {
        this.connectionCloser = connectionCloser;
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

package edu.duke.ece568.mini_ups.service.sender;

import java.io.IOException;
import java.io.OutputStream;

import org.springframework.stereotype.Service;

import edu.duke.ece568.mini_ups.protocol.upsToAmazon.AmazonUps.Err;
import edu.duke.ece568.mini_ups.protocol.upsToAmazon.AmazonUps.UChangeDestination;
import edu.duke.ece568.mini_ups.protocol.upsToAmazon.AmazonUps.UCommand;
import edu.duke.ece568.mini_ups.protocol.upsToAmazon.AmazonUps.UTruckArrival;
import edu.duke.ece568.mini_ups.protocol.upsToAmazon.AmazonUps.UcheckUsernameResponse;
import edu.duke.ece568.mini_ups.protocol.upsToAmazon.AmazonUps.Udelivered;
import edu.duke.ece568.mini_ups.protocol.upsToAmazon.AmazonUps.destination;

@Service
public class AmazonCmdSender {
    private OutputStream outputStream;
    private Long seqnum = 0L;

    public void setAmazonCmdSender(OutputStream output) {
        outputStream = output;
    }

    public void sendUCommand(UCommand command) throws IOException {
        command.writeDelimitedTo(outputStream);
        outputStream.flush();
    }


    public void sendTruckArrival(long packageID, int truckID) throws IOException {
        
        UTruckArrival arrival = UTruckArrival.newBuilder()
                .setPackageID(packageID)
                .setTruckID(truckID)
                .setSeqnum(++seqnum)
                .build();

        UCommand command = UCommand.newBuilder()
                .addArrived(arrival)
                .build();

        System.out.println("Sending truck arrival: " + packageID + " " + truckID);
        sendUCommand(command);
    }

    public void sendDeliveryConfirmation(long packageID) throws IOException {
        
        Udelivered delivered = Udelivered.newBuilder()
                .setPackageID(packageID)
                .setSeqnum(++seqnum)
                .build();

        UCommand command = UCommand.newBuilder()
                .addDelivered(delivered)
                .build();

        System.out.println("Sending delivery confirmation: " + packageID);
        sendUCommand(command);
    }

    public void sendUsernameCheckResponse(String username, long userID) throws IOException {
        UcheckUsernameResponse response = UcheckUsernameResponse.newBuilder()
                .setUpsUsername(username)
                .setUpsUserID(userID)
                .setSeqnum(++seqnum)
                .build();

        UCommand command = UCommand.newBuilder()
                .addCheckUser(response)
                .build();

        System.out.println("Sending username check response: " + response.toString());

        sendUCommand(command);
    }

    public void sendDelivered(long packageID) throws IOException {
        Udelivered delivered = Udelivered.newBuilder()
                .setPackageID(packageID)
                .setSeqnum(++seqnum)
                .build();

        UCommand command = UCommand.newBuilder()
                .addDelivered(delivered)
                .build();

        sendUCommand(command);
    }

    public void sendError(String errMsg, long originSeqNum) throws IOException {
        Err error = Err.newBuilder()
                .setErr(errMsg)
                .setOriginseqnum(originSeqNum)
                .setSeqnum(++seqnum)
                .build();

        UCommand command = UCommand.newBuilder()
                .addError(error)
                .build();

        sendUCommand(command);
    }

    public void sendAck(long originSeqNum) throws IOException {

        System.out.println("Sending ack for seqnum: " + originSeqNum);
        UCommand command = UCommand.newBuilder()
                .addAcks(originSeqNum)
                .build();

        sendUCommand(command);
        System.out.println("Sent ack for seqnum: " + originSeqNum);
    }

    public void sendChangeDestination(long packageID, int x, int y) throws IOException {

        destination des = destination.newBuilder()
                .setX(x)
                .setY(y)
                .build();
        UChangeDestination changeDestination = UChangeDestination.newBuilder()
                .setPackageID(packageID)
                .setNewDestination(des)
                .setSeqnum(++seqnum)
                .build();
        UCommand command = UCommand.newBuilder()
                .addChanged(changeDestination)
                .build();

        sendUCommand(command);
    }

}

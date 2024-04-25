package edu.duke.ece568.mini_ups.service.sender;

import java.io.OutputStream;
import java.util.List;

import org.springframework.stereotype.Service;

import edu.duke.ece568.mini_ups.entity.Package;
import edu.duke.ece568.mini_ups.protocol.upsToWorld.WorldUps.UCommands;
import edu.duke.ece568.mini_ups.protocol.upsToWorld.WorldUps.UDeliveryLocation;
import edu.duke.ece568.mini_ups.protocol.upsToWorld.WorldUps.UErr;
import edu.duke.ece568.mini_ups.protocol.upsToWorld.WorldUps.UGoDeliver;
import edu.duke.ece568.mini_ups.protocol.upsToWorld.WorldUps.UGoPickup;
import edu.duke.ece568.mini_ups.protocol.upsToWorld.WorldUps.UQuery;

@Service
public class WorldCmdSender {
    private OutputStream outputStream;
    private Long seqnum = 0L;

    public void setWorldCmdSender(OutputStream output) {
        outputStream = output;
    }

    public void sendCommands(UCommands commands) throws Exception {
        commands.writeDelimitedTo(outputStream);
        outputStream.flush();
    }

    public void sendTruckQueries(int truck_num) throws Exception{
        System.out.println("sendQueryTrucks");
        UCommands.Builder command = UCommands.newBuilder();
        for (int i = 0; i < truck_num; i++) {
                UQuery query = UQuery.newBuilder().setTruckid(i).setSeqnum(++seqnum).build();
                command.addQueries(query);
        }
        sendCommands(command.build());
            
    }
    public void sendQueryTrucks(int truckID) throws Exception {
        System.out.println("sendQueryTrucks");
        UQuery query = UQuery.newBuilder()
                .setTruckid(truckID)
                .setSeqnum(++seqnum)
                .build();
        UCommands command = UCommands.newBuilder()
                .addQueries(query)
                .build();
        command.writeDelimitedTo(outputStream);
        outputStream.flush();
    }

    public void sendPickups(int truckID, int whid) throws Exception {
        System.out.println("sendPickups");
        UGoPickup goPickup = UGoPickup.newBuilder()
                .setTruckid(truckID)
                .setWhid(whid)
                .setSeqnum(++seqnum)
                .build();
        UCommands command = UCommands.newBuilder()
                .addPickups(goPickup)
                .build();
        command.writeDelimitedTo(outputStream);
        outputStream.flush();
    }

    public void sendDeliveries(Integer truckID, List<Package> packages) throws Exception {

        UGoDeliver.Builder goDeliverBuilder = UGoDeliver.newBuilder()
                .setTruckid(truckID)
                .setSeqnum(++seqnum);
        for (Package p : packages) {
            UDeliveryLocation location = UDeliveryLocation.newBuilder()
                    .setPackageid(p.getPackageId())
                    .setX(p.getDestinationX())
                    .setY(p.getDestinationY())
                    .build();
            goDeliverBuilder.addPackages(location);
        }
        UGoDeliver goDeliver = goDeliverBuilder.build();
        UCommands command = UCommands.newBuilder()
                .addDeliveries(goDeliver)
                .build();
        command.writeDelimitedTo(outputStream);
        outputStream.flush();
    }

    public void sendDelivery(int truckID, Package p) throws Exception {
        System.out.println("sendDelivery: packageid: " + p.getPackageId() + " truckid: " + truckID);
        UDeliveryLocation location = UDeliveryLocation.newBuilder()
                .setPackageid(p.getPackageId())
                .setX(p.getDestinationX())
                .setY(p.getDestinationY())
                .build();
        UGoDeliver goDeliver = UGoDeliver.newBuilder()
                .setTruckid(truckID)
                .setSeqnum(++seqnum)
                .addPackages(location)
                .build();
        UCommands command = UCommands.newBuilder()
                .addDeliveries(goDeliver)
                .build();
        command.writeDelimitedTo(outputStream);
        outputStream.flush();
    }

    public void sendError(String error, long originSeqNum) throws Exception {
        UErr err = UErr.newBuilder()
                .setErr(error)
                .setOriginseqnum(originSeqNum)
                .setSeqnum(++seqnum)
                .build();
        err.writeDelimitedTo(outputStream);
        outputStream.flush();
    }

    public void sendAck(long seq) throws Exception {
        UCommands command = UCommands.newBuilder()
                .addAcks(seq)
                .build();
        command.writeDelimitedTo(outputStream);
        outputStream.flush();
    }
}

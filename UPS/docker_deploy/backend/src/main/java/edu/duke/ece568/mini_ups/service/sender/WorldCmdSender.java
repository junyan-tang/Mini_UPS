package edu.duke.ece568.mini_ups.service.sender;

import java.io.OutputStream;

import org.springframework.stereotype.Service;

import edu.duke.ece568.mini_ups.protocol.upsToWorld.WorldUps.UCommands;
import edu.duke.ece568.mini_ups.protocol.upsToWorld.WorldUps.UGoPickup;

@Service
public class WorldCmdSender {
    private OutputStream outputStream;
    private Long seqnum = 0L;

    public WorldCmdSender(OutputStream output) {
        outputStream = output;
    }
    public void sendPickups(int truckID,int whid) throws Exception{
        UGoPickup goPickup = UGoPickup.newBuilder()
                .setTruckid(truckID)
                .setWhid(whid)
                .setSeqnum(seqnum++)
                .build();
        UCommands command = UCommands.newBuilder()
                .addPickups(goPickup)
                .build();
        command.writeTo(outputStream);
        outputStream.flush();
    }
    public void sendAck(long seqnum) throws Exception{
        UCommands command = UCommands.newBuilder()
                .addAcks(seqnum)
                .build();
        command.writeTo(outputStream);
        outputStream.flush();
    }
}

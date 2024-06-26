package edu.duke.ece568.mini_ups.service.network;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import edu.duke.ece568.mini_ups.protocol.upsToAmazon.AmazonUps.ACommand;
import edu.duke.ece568.mini_ups.protocol.upsToAmazon.AmazonUps.AOrderATruck;
import edu.duke.ece568.mini_ups.service.CommandStore;
import edu.duke.ece568.mini_ups.service.handler.AmazonRespHandler;
import edu.duke.ece568.mini_ups.service.sender.AmazonCmdSender;
import edu.duke.ece568.mini_ups.service.sender.WorldCmdSender;

@Service
public class AmazonNetService implements ConnectionCloser {
    private SocketService socketService;
    private AmazonRespHandler amazonResHandler;
    public OutputStream out;
    public InputStream in;

    @Autowired
    public AmazonNetService(SocketService socketService, AmazonRespHandler amazonResHandler) {
        this.socketService = socketService;
        this.amazonResHandler = amazonResHandler;
        initializeConnection();
    }

    public void setamazonResHandlerACmdSender(AmazonCmdSender amazonCmdSender) {
        this.amazonResHandler.setAmazonCmdSender(amazonCmdSender);
    }
    public void setamazonResHandlerWCmdSender(WorldCmdSender worldCmdSender) {
        this.amazonResHandler.setWorldCmdSender(worldCmdSender);
    }

    private void initializeConnection() {
        try{
        String host = "vcm-40425.vm.duke.edu";
        //String host = "vcm-41021.vm.duke.edu";
        int port = 34567;
        this.socketService.startClient(host, port);
        this.out = this.socketService.out;
        this.in = this.socketService.in;}catch(Exception e){
            throw new RuntimeException("Failed to start socket connection", e);
        }
    }

    public ACommand receiveCommand() throws IOException {
        try {
            ACommand command = ACommand.parseDelimitedFrom(in);
            if (command != null) {
                //sendAcksIfNecessary(command);
                amazonResHandler.handle(command);                
            }
            retryPendingOrders();
            return command;
        } catch (Exception e) {
            return null;
            //throw new IOException("Failed to receive message from Amazon");
        }
    }

    private void retryPendingOrders() {
        int size = CommandStore.pendingOrders.size(); // 获取当前队列大小
        for (int i = 0; i < size; i++) {
            AOrderATruck order = CommandStore.pendingOrders.poll();
            if (order != null) {
                amazonResHandler.handleAnOrderTruck(order);
            }
        }
    }

    // private void sendAcksIfNecessary(ACommand command) throws IOException {
    //     List<Long> acks = command.getAcksList();
    //     if (!acks.isEmpty()) {
    //         UCommands commands = UCommands.newBuilder()
    //                 .addAllAcks(acks)
    //                 .build();
    //         commands.writeDelimitedTo(out);
    //         out.flush();
    //     }
    // }

    @Override
    public void closeConnection() {
        socketService.stopClient();
    }
}

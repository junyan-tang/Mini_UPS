package edu.duke.ece568.mini_ups.service.network;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import edu.duke.ece568.mini_ups.entity.Truck;
import edu.duke.ece568.mini_ups.protocol.upsToWorld.WorldUps.UConnect;
import edu.duke.ece568.mini_ups.protocol.upsToWorld.WorldUps.UConnected;
import edu.duke.ece568.mini_ups.protocol.upsToWorld.WorldUps.UInitTruck;
import edu.duke.ece568.mini_ups.protocol.upsToWorld.WorldUps.UResponses;
import edu.duke.ece568.mini_ups.repository.PackageRepository;
import edu.duke.ece568.mini_ups.repository.TruckRepository;
import edu.duke.ece568.mini_ups.repository.UserRepository;
import edu.duke.ece568.mini_ups.service.CommandStore;
import edu.duke.ece568.mini_ups.service.DestStruct;
import edu.duke.ece568.mini_ups.service.handler.WorldRespHandler;
import edu.duke.ece568.mini_ups.service.sender.AmazonCmdSender;
import edu.duke.ece568.mini_ups.service.sender.WorldCmdSender;

@Service
public class WorldNetService implements ConnectionCloser {
    private SocketService socketService;
    private TruckRepository TruckRepository;
    private PackageRepository PackageRepository;
    private UserRepository UserRepository;
    private WorldRespHandler worldRespHandler;
    public OutputStream out;
    public InputStream in;
    final int TRUCK_NUM = 100;
    final int TRUCK_X = 10;
    final int TRUCK_Y = 10;

    @Autowired
    public WorldNetService(SocketService socketService, TruckRepository TruckRepository, PackageRepository PackageRepository, UserRepository UserRepository, WorldRespHandler worldRespHandler) {
        this.socketService = socketService;
        this.TruckRepository = TruckRepository;
        this.PackageRepository = PackageRepository;
        this.UserRepository = UserRepository;
        this.worldRespHandler = worldRespHandler;
        initializeConnection();
    }

    public void setworldRespHandlerACmdSender(AmazonCmdSender amazonCmdSender) {
        this.worldRespHandler.setAmazonCmdSender(amazonCmdSender);
    }
    public void setworldRespHandlerWCmdSender(WorldCmdSender worldCmdSender) {
        this.worldRespHandler.setWorldCmdSender(worldCmdSender);
    }
    private void initializeConnection() {
        try {
            String host = "vcm-38181.vm.duke.edu"; // 替换为实际的host
            //String host = "vcm-41021.vm.duke.edu";
            int port = 12345; // 替换为实际的port
            this.socketService.startClient(host, port);
            this.out = this.socketService.out;
            this.in = this.socketService.in;
            this.worldRespHandler.setConnectionCloser(this);
        } catch (Exception e) {
            throw new RuntimeException("Failed to start socket connection", e);
        }
    }

    // public WorldNetService() {
    //     String host = "localhost";
    //     int port = 12345;
    //     this.socketService = new SocketService();
    //     this.socketService.startClient(host, port);
    //     this.out = this.socketService.out;
    //     this.in = this.socketService.in;
    //     this.worldResHandler = new WorldRespHandler(this);
    // }

    // @Bean
    // public WorldNetService worldNetService() {
    //     return new WorldNetService();
    // }

    public void sendUConnect(Long worldId, boolean isAmazon) {
        UConnect.Builder uConnectBuilder = UConnect.newBuilder()
                .setWorldid(worldId)
                .setIsAmazon(isAmazon);
        for (int i = 0; i < TRUCK_NUM; i++) {
            UInitTruck truckmess = UInitTruck.newBuilder()
                    .setId(i)
                    .setX(TRUCK_X)
                    .setY(TRUCK_Y)
                    .build();
            uConnectBuilder.addTrucks(truckmess);
            Truck truck = new Truck();
            truck.setTruckId(i);
            truck.setCurrentX(TRUCK_X);
            truck.setCurrentY(TRUCK_Y);
            truck.setStatus("IDLE");
            TruckRepository.save(truck);
        }
        UConnect uConnect = uConnectBuilder.build();
        try {
            uConnect.writeDelimitedTo(out);
            out.flush();
            System.out.println("Sent UConnect to world");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Boolean receiveUconnected(){
        try {
            UConnected response = UConnected.parseDelimitedFrom(in);
            if ("connected!".equals(response.getResult())) {
                System.out.println("Successfully connected: " + response.getResult());
            } else if (response.getResult().startsWith("error:")) {
                System.out.println("Error from server: " + response.getResult());
                return false;
            }
            return true;
        } catch (IOException e) {
            return false;
        }
    }

    public UResponses receiveMessage() throws IOException {
        try {
            UResponses response = UResponses.parseDelimitedFrom(in);
            if (response != null) {
                //sendAcksIfNecessary(response);
                worldRespHandler.handle(response);               
            }
            checkpedingDestinations();
            return response;
        } catch (IOException e) {
            return null;
            //throw new IOException("Failed to receive message from world");
        }
    }

    public void checkpedingDestinations() {
        while (!CommandStore.pendingDest.isEmpty()) {
            DestStruct destStruct = CommandStore.pendingDest.poll();
            worldRespHandler.sendChangeDest(destStruct.packageId, destStruct.newX, destStruct.newY);
        }
    }

    public void sendTruckQueries(){
        worldRespHandler.sendTruckQueries(TRUCK_NUM);
    }

    // private void sendAcksIfNecessary(UResponses response) throws IOException {
    //     List<Long> acks = response.getAcksList();
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
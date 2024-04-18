package edu.duke.ece568.mini_ups.service.network;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;

import edu.duke.ece568.mini_ups.entity.Truck;
import edu.duke.ece568.mini_ups.protocol.upsToWorld.WorldUps.UCommands;
import edu.duke.ece568.mini_ups.protocol.upsToWorld.WorldUps.UConnect;
import edu.duke.ece568.mini_ups.protocol.upsToWorld.WorldUps.UInitTruck;
import edu.duke.ece568.mini_ups.protocol.upsToWorld.WorldUps.UResponses;
import edu.duke.ece568.mini_ups.repository.PackageRepository;
import edu.duke.ece568.mini_ups.repository.TruckRepository;
import edu.duke.ece568.mini_ups.repository.UserRepository;
import edu.duke.ece568.mini_ups.service.handler.WorldResHandler;

public class WorldNetService implements ConnectionCloser {
    private SocketService socketService;
    private TruckRepository TruckRepository;
    private PackageRepository PackageRepository;
    private UserRepository UserRepository;
    private WorldResHandler worldResHandler;
    public OutputStream out;
    public InputStream in;
    final int TRUCK_NUM = 1000;
    final int TRUCK_X = 10;
    final int TRUCK_Y = 10;

    public WorldNetService() {
        String host = "localhost";
        int port = 12345;
        this.socketService = new SocketService();
        this.socketService.startClient(host, port);
        this.out = this.socketService.out;
        this.in = this.socketService.in;
        this.worldResHandler = new WorldResHandler(this);
    }

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
            TruckRepository.save(truck);
        }
        UConnect uConnect = uConnectBuilder.build();
        try {
            uConnect.writeDelimitedTo(out);
            out.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public UResponses receiveMessage() throws IOException {
        try {
            UResponses response = UResponses.parseDelimitedFrom(in);
            if (response != null) {
                worldResHandler.handle(response);
                sendAcksIfNecessary(response);
            }
            return response;
        } catch (IOException e) {
            throw new IOException("Failed to receive message from world");
        }
    }

    private void sendAcksIfNecessary(UResponses response) throws IOException {
        List<Long> acks = response.getAcksList();
        if (!acks.isEmpty()) {
            UCommands commands = UCommands.newBuilder()
                    .addAllAcks(acks)
                    .build();
            commands.writeDelimitedTo(out);
            out.flush();
        }
    }

    @Override
    public void closeConnection() {
        socketService.stopClient();
    }
}
package edu.duke.ece568.mini_ups.service.network;

import java.io.IOException;
import edu.duke.ece568.mini_ups.protocol.upsToWorld.WorldUps.*;

public class WorldNetService {
    private SocketService socketService;
    final int TRUCK_NUM = 1000;
    final int TRUCK_X = 10;
    final int TRUCK_Y = 10;

    public WorldNetService() {
        String host = "localhost";
        int port = 12345;
        this.socketService = new SocketService();
        this.socketService.startClient(host, port);
    }

    public void sendUConnect(Long worldId, boolean isAmazon) throws IOException {
        
        for (int i = 0; i < TRUCK_NUM; i++) {
            UInitTruck connect = UInitTruck.newBuilder()
                    .setId(i)
                    .setX(TRUCK_X)
                    .setY(TRUCK_Y)
                    .build();
        }
    }
}

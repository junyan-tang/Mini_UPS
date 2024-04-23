// package edu.duke.ece568.mini_ups.service.network;

// import java.io.IOException;
// import java.io.InputStream;
// import java.io.OutputStream;
// import java.util.List;

// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.stereotype.Service;

// import edu.duke.ece568.mini_ups.protocol.upsToAmazon.AmazonUps.ACommand;
// import edu.duke.ece568.mini_ups.protocol.upsToWorld.WorldUps.UCommands;
// import edu.duke.ece568.mini_ups.repository.UserRepository;
// import edu.duke.ece568.mini_ups.service.handler.AmazonRespHandler;

// //@Service
// public class AmazonNetService implements ConnectionCloser {
//     private SocketService socketService;
//     private UserRepository userRepository;
//     private AmazonRespHandler amazonResHandler;
//     public OutputStream out;
//     public InputStream in;

//     @Autowired
//     public AmazonNetService(SocketService socketService, UserRepository userRepository, AmazonRespHandler amazonResHandler) {
//         this.socketService = socketService;
//         this.userRepository = userRepository;
//         this.amazonResHandler = amazonResHandler;
//         initializeConnection();
//     }

//     private void initializeConnection() {
//         String host = "amazon-server-host";
//         int port = 23456;
//         this.socketService.startClient(host, port);
//         this.out = this.socketService.out;
//         this.in = this.socketService.in;
//     }

//     // public AmazonNetService() {
//     //     String host = "amazon-server-host";
//     //     int port = 23456; 
//     //     this.socketService = new SocketService();
//     //     this.socketService.startClient(host, port);
//     //     this.out = this.socketService.out;
//     //     this.in = this.socketService.in;
//     //     this.amazonResHandler = new AmazonRespHandler();
//     // }

//     // @Bean
//     // public AmazonNetService amazonNetService() {
//     //     return new AmazonNetService();
//     // }

//     public ACommand receiveCommand() throws IOException {
//         try {
//             ACommand command = ACommand.parseDelimitedFrom(in);
//             if (command != null) {
//                 amazonResHandler.handle(command);
//                 sendAcksIfNecessary(command);
//             }
//             return command;
//         } catch (IOException e) {
//             throw new IOException("Failed to receive message from Amazon");
//         }
//     }

//     private void sendAcksIfNecessary(ACommand command) throws IOException {
//         List<Long> acks = command.getAcksList();
//         if (!acks.isEmpty()) {
//             UCommands commands = UCommands.newBuilder()
//                     .addAllAcks(acks)
//                     .build();
//             commands.writeDelimitedTo(out);
//             out.flush();
//         }
//     }

//     @Override
//     public void closeConnection() {
//         socketService.stopClient();
//     }
// }

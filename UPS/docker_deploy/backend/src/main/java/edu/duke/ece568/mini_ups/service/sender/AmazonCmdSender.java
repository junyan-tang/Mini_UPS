package edu.duke.ece568.mini_ups.service.sender;

import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;

import edu.duke.ece568.mini_ups.protocol.upsToAmazon.AmazonUps.*;
public class AmazonCmdSender {
    private Socket socket;
    private OutputStream outputStream;

    public AmazonCmdSender(String host, int port) {
        try {
            socket = new Socket(host, port);
            outputStream = socket.getOutputStream();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void sendUCommand(UCommand command) throws IOException {
        // 将UCommand对象序列化并发送
        command.writeTo(outputStream);
        outputStream.flush(); // 确保数据被发送
    }

    public void closeConnection() throws IOException {
        outputStream.close();
        socket.close();
    }

    // 示例方法来构建和发送不同类型的消息
    public void sendTruckArrival(long packageID, int truckID, long seqnum) throws IOException {
        UTruckArrival arrival = UTruckArrival.newBuilder()
            .setPackageID(packageID)
            .setTruckID(truckID)
            .setSeqnum(seqnum)
            .build();

        UCommand command = UCommand.newBuilder()
            .addArrived(arrival)
            .build();

        sendUCommand(command);
    }

    public void sendDeliveryConfirmation(long packageID, long seqnum) throws IOException {
        Udelivered delivered = Udelivered.newBuilder()
            .setPackageID(packageID)
            .setSeqnum(seqnum)
            .build();

        UCommand command = UCommand.newBuilder()
            .addDelivered(delivered)
            .build();

        sendUCommand(command);
    }

    public void sendUsernameCheckResponse(String username, long userID, long seqnum) throws IOException {
        UcheckUsernameResponse response = UcheckUsernameResponse.newBuilder()
            .setUpsUsername(username)
            .setUpsUserID(userID)
            .setSeqnum(seqnum)
            .build();

        UCommand command = UCommand.newBuilder()
            .addCheckUser(response)
            .build();

        sendUCommand(command);
    }

    public void sendError(String errMsg, long originSeqNum, long seqNum) throws IOException {
        Err error = Err.newBuilder()
            .setErr(errMsg)
            .setOriginseqnum(originSeqNum)
            .setSeqnum(seqNum)
            .build();

        UCommand command = UCommand.newBuilder()
            .addError(error)
            .build();

        sendUCommand(command);
    }
    
}

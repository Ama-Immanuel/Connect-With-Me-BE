package com.ama.imanuel.connectwithmebe.socket;

import com.corundumstudio.socketio.SocketIOServer;
import com.corundumstudio.socketio.listener.ConnectListener;
import com.corundumstudio.socketio.listener.DataListener;
import com.corundumstudio.socketio.listener.DisconnectListener;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Controller;
import org.springframework.web.socket.BinaryMessage;

import java.nio.Buffer;
import java.util.Base64;

@Controller
@Log4j2
public class SocketIOController {
    private SocketIOServer socketIOServer;

    public SocketIOController(SocketIOServer socketIOServer) {
        this.socketIOServer = socketIOServer;


        this.socketIOServer.addConnectListener(onUserConnectWithSocket);
        this.socketIOServer.addDisconnectListener(onUserDisconnectWithSocket);

        socketIOServer.addEventListener("streamVideo", String.class, onSentVideo);
        socketIOServer.addEventListener("streamAudio", String.class, onSentAudio);
        socketIOServer.addEventListener("sentMessage", MessageSocket.class, onSentMessage);

    }

    public ConnectListener onUserConnectWithSocket = client -> {

    };

    public DisconnectListener onUserDisconnectWithSocket = client -> {
        log.info(client.getSessionId() + " disconnect");
    };

    public DataListener<String> onSentVideo = (client, data, ackSender) -> {
        log.info(data);
    };

    public DataListener<String> onSentAudio = (client, data, ackSender) -> {
        log.info(data);
    };

    public DataListener<MessageSocket> onSentMessage = ((socketIOClient, messageSocket, ackRequest) -> {
       log.info(messageSocket.getMessage());
    });

}

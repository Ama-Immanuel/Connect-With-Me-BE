package com.ama.imanuel.connectwithmebe.config;

import com.corundumstudio.socketio.Configuration;
import com.corundumstudio.socketio.SocketIOClient;
import com.corundumstudio.socketio.SocketIOServer;
import com.corundumstudio.socketio.listener.ConnectListener;
import com.corundumstudio.socketio.listener.DisconnectListener;
import jakarta.annotation.PreDestroy;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.CrossOrigin;


@CrossOrigin
@Component
@RequiredArgsConstructor
@Log4j2
public class SocketIOConfig {
    @Value("${application.socket.port}")
    private int port;

    @Value("${application.socket.host}")
    private String host;

    private SocketIOServer server;

    @Bean
    public SocketIOServer socketIOServer(){
        Configuration configuration = new Configuration();

        configuration.setHostname(host);
        configuration.setPort(port);

        server = new SocketIOServer(configuration);

        server.start();

        server.addConnectListener(new ConnectListener() {
            @Override
            public void onConnect(SocketIOClient client) {
                log.info("new user connected with session " + client.getSessionId());
            }
        });

        server.addDisconnectListener(new DisconnectListener() {
            @Override
            public void onDisconnect(SocketIOClient client) {
                client.getNamespace().getAllClients().forEach(data -> {
                    log.info("user disconnected " + data.getSessionId().toString());
                });
            }
        });

        return this.server;
    }

    @PreDestroy
    public void stopSocketIOServer() {
        this.server.stop();
    }
}

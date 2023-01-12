package org.bolsadeideas.grcp.greet.server;

import io.grpc.Server;
import io.grpc.ServerBuilder;
import org.bolsadeideas.grcp.greet.Configuration;
import org.bolsadeideas.grcp.greet.Log;

import java.io.IOException;

public class GreetingServerApplication {

    private static Log logger = Log.getInstance(GreetingServerApplication.class);

    private static void log(String log) {
        logger.log(log);
    }
    public static void main(String[] args) throws IOException, InterruptedException {

        final int port = Configuration.GREETINGS_SERVER_PORT;

        Server server = ServerBuilder
                .forPort(port)
                .addService(new GreetingServerImpl())
                .build();

        server.start();
        log("Greeting Server STARTED");
        log("Listening on port: " + port);

        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
           log("Received Greeting Server shutdown request");
            server.shutdown();
            log("Greeting Server STOPPED");
        }));

        server.awaitTermination();

    }
}
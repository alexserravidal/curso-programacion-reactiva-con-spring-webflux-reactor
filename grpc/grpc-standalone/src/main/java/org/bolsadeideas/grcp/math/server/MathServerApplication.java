package org.bolsadeideas.grcp.math.server;

import io.grpc.Server;
import io.grpc.ServerBuilder;
import org.bolsadeideas.grcp.Configuration;
import org.bolsadeideas.grcp.Log;

import java.io.IOException;

public class MathServerApplication {

    private static Log logger = Log.getInstance(MathServerApplication.class);

    private static void log(String log) {
        logger.log(log);
    }
    public static void main(String[] args) throws IOException, InterruptedException {

        final int port = Configuration.MATH_SERVER_PORT;

        Server server = ServerBuilder
                .forPort(port)
                .addService(new MathServerImpl())
                .build();

        server.start();
        log("Math Server STARTED");
        log("Listening on port: " + port);

        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            log("Received Math Server shutdown request");
            server.shutdown();
            log("Math Server STOPPED");
        }));

        server.awaitTermination();

    }
}
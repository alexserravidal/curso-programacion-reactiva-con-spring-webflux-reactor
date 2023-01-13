package org.bolsadeideas.grcp.math.client;

import com.bolsadeideas.grcp.math.MathServiceGrpc;
import com.bolsadeideas.grcp.math.SqrtRequest;
import com.bolsadeideas.grcp.math.SqrtResponse;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import org.bolsadeideas.grcp.Configuration;
import org.bolsadeideas.grcp.Log;

public class MathClientApplication {

    private static Log logger = Log.getInstance(MathClientApplication.class);
    public static void main(String[] args) throws Exception {
        if (args.length == 0) {
            logger.log("Need one argument to work");
            return;
        }

        ManagedChannel channel = ManagedChannelBuilder
                .forAddress("localhost", Configuration.MATH_SERVER_PORT)
                .usePlaintext()
                .build();

        switch(args[0]) {
            case "sqrt": doSqrt(channel); break;
            default: logger.log("Unknown Command: " + args[0]);
        }

        logger.log("Greeting Client SHUTTING DOWN");
        channel.shutdown();
    }

    private static void doSqrt(ManagedChannel channel) {

        logger.log("Enter doSqrt()");
        askServerSqrtFor(4, channel);
        askServerSqrtFor(3, channel);
        askServerSqrtFor(-1, channel);

    }

    private static void askServerSqrtFor(int number, ManagedChannel channel) {
        logger.log("askServerSqrtFor() with NUMBER: " + number);
        MathServiceGrpc.MathServiceBlockingStub stub = MathServiceGrpc.newBlockingStub(channel);
        SqrtResponse response;

        try {
            response = stub.sqrt(SqrtRequest
                    .newBuilder()
                    .setNumber(number)
                    .build()
            );
        } catch (RuntimeException e) {
            logger.log("Got exception from MathServerApplication: " + e.getMessage());
            return;
        }

        logger.log("Got result from MathServerApplication: " + response.getResult());

    }

}

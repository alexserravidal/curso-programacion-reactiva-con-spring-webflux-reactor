package org.bolsadeideas.grcp.greet.client;

import com.bolsadeideas.grcp.greet.GreetingRequest;
import com.bolsadeideas.grcp.greet.GreetingResponse;
import com.bolsadeideas.grcp.greet.GreetingServiceGrpc;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import org.bolsadeideas.grcp.greet.Configuration;

import java.util.Iterator;
import java.util.List;

public class GreetingClientApplication {

    public static void main(String[] args) {
        if (args.length == 0) {
            log("Need one argument to work");
            return;
        }

        ManagedChannel channel = ManagedChannelBuilder
                .forAddress("localhost", Configuration.GREETINGS_SERVER_PORT)
                .usePlaintext()
                .build();

        switch(args[0]) {
            case "greet": doGreet(channel); break;
            case "greetManyTimes": doGreetManyTimes(channel); break;
            default: log("Unknown Command: " + args[0]);
        }

        log("Greeting Client SHUTTING DOWN");
        channel.shutdown();
    }

    private static void log(String log) {
        System.out.println("GreetingClientApplication: " + log);
    }

    private static void doGreet(ManagedChannel channel) {
        log("Enter doGreet");
        GreetingServiceGrpc.GreetingServiceBlockingStub stub = GreetingServiceGrpc.newBlockingStub(channel);
        GreetingResponse response = stub.greet(
                GreetingRequest.newBuilder().setFirstName("Santiago Serrano").build()
        );

        log("Got greeted by server: " + response.getResult());
    }

    private static void doGreetManyTimes(ManagedChannel channel) {
        log("Enter doGreetManyTimes");
        GreetingServiceGrpc.GreetingServiceBlockingStub stub = GreetingServiceGrpc.newBlockingStub(channel);

        Iterator<GreetingResponse> greetingResponseIterator = stub.greetManyTimes(
                GreetingRequest.newBuilder().setFirstName("Diego Serrano").build()
        );

        greetingResponseIterator.forEachRemaining(greetingResponse -> log("Got greeted by server: " + greetingResponse.getResult()));
    }


}

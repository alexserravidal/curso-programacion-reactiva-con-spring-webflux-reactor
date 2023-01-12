package org.bolsadeideas.grcp.greet.client;

import com.bolsadeideas.grcp.greet.GreetingRequest;
import com.bolsadeideas.grcp.greet.GreetingResponse;
import com.bolsadeideas.grcp.greet.GreetingServiceGrpc;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.stub.StreamObserver;
import org.bolsadeideas.grcp.greet.Configuration;
import org.bolsadeideas.grcp.greet.Log;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CountDownLatch;

public class GreetingClientApplication {

    private static Log logger = Log.getInstance(GreetingClientApplication.class);

    public static void main(String[] args) throws Exception {
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
            case "longGreet": doLongGreet(channel); break;
            default: log("Unknown Command: " + args[0]);
        }

        log("Greeting Client SHUTTING DOWN");
        channel.shutdown();
    }

    private static void log(String log) {

        logger.log(log);
    }

    private static void doGreet(ManagedChannel channel) {
        log("Enter doGreet");
        GreetingServiceGrpc.GreetingServiceBlockingStub stub = GreetingServiceGrpc.newBlockingStub(channel);
        GreetingResponse response = stub.greet(
                GreetingRequest.newBuilder().setFirstName("Santiago Serrano").build()
        );

        log("Got greeted by server: " + response.getResult());
    }

    private static void doGreetManyTimes(ManagedChannel channel) throws Exception {
        IDoGreetManyTimes doGreetManyTimesStrategy = new DoGreetManyTimesAsync();
        doGreetManyTimesStrategy.doGreetManyTimes(channel, "Diego Serrano");
    }

    private static void doLongGreet(ManagedChannel channel) throws InterruptedException {
        log("Enter doLongGreet");
        GreetingServiceGrpc.GreetingServiceStub stub = GreetingServiceGrpc.newStub(channel);

        List<String> names = Arrays.asList(
                "Santiago Serrano",
                "Diego Serrano",
                "Lurditas"
        );

        CountDownLatch countDownLatch = new CountDownLatch(1);
        StreamObserver<GreetingRequest> stream = stub.longGreet(new StreamObserver<GreetingResponse>() {
            @Override
            public void onNext(GreetingResponse value) {
                logger.log("Got greeted from server: " + value.getResult());
            }

            @Override
            public void onError(Throwable t) { }

            @Override
            public void onCompleted() {
                countDownLatch.countDown();
            }
        });

        for (String name: names) {
            logger.log("Passing name " + name + " to GreetingServerApplication");
            stream.onNext(
                    GreetingRequest
                            .newBuilder()
                            .setFirstName(name)
                            .build()
            );
        }

        stream.onCompleted();
        countDownLatch.await();
    }


}

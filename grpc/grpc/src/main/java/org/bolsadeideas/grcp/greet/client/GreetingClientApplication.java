package org.bolsadeideas.grcp.greet.client;

import com.bolsadeideas.grcp.greet.GreetingRequest;
import com.bolsadeideas.grcp.greet.GreetingResponse;
import com.bolsadeideas.grcp.greet.GreetingServiceGrpc;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.stub.StreamObserver;
import org.bolsadeideas.grcp.greet.Configuration;
import org.bolsadeideas.grcp.greet.Log;
import org.bolsadeideas.grcp.greet.PeopleData;

import java.util.Iterator;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

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
            case "greetManyPeople": doGreetManyPeople(channel); break;
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
                GreetingRequest.newBuilder().setFirstName(PeopleData.SANTIAGO_SERRANO()).build()
        );

        log("Got greeted by server: " + response.getResult());
    }

    private static void doGreetManyTimes(ManagedChannel channel) throws Exception {
        IDoGreetManyTimes doGreetManyTimesStrategy = new DoGreetManyTimesAsync();
        doGreetManyTimesStrategy.doGreetManyTimes(channel, PeopleData.DIEGO_SERRANO());
    }

    private static void doLongGreet(ManagedChannel channel) throws InterruptedException {
        log("Enter doLongGreet");
        GreetingServiceGrpc.GreetingServiceStub stub = GreetingServiceGrpc.newStub(channel);

        List<String> names = PeopleData.ALL();

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

    private static boolean IS_RESPONSES_STREAM_ALIVE = true;
    public static boolean getIsResponsesStreamAlive() { return IS_RESPONSES_STREAM_ALIVE; }
    private static void setIsResponsesStreamAlive(boolean value) { IS_RESPONSES_STREAM_ALIVE = value; }

    private static void doGreetManyPeople(ManagedChannel channel) throws InterruptedException {

        CountDownLatch countDownLatch = new CountDownLatch(1);

        StreamObserver<GreetingRequest> requestsStream =
                GreetingServiceGrpc.newStub(channel).greetManyPeople(new StreamObserver<GreetingResponse>() {
                    @Override
                    public void onNext(GreetingResponse value) {
                        logger.log("Got a greeting from server: " + value.getResult());
                    }

                    @Override
                    public void onError(Throwable t) {
                        logger.log("Got exception from server: " + t.getMessage());
                    }

                    @Override
                    public void onCompleted() {
                        logger.log("Server stopped streaming responses");
                        setIsResponsesStreamAlive(false);
                        countDownLatch.countDown();
                    }
                });

        List<String> peopleToGreet = PeopleData.ALL_WITH_ENEMY();
        Iterator<String> peopleToGreetIterator = peopleToGreet.iterator();

        while (getIsResponsesStreamAlive() && peopleToGreetIterator.hasNext()) {

            final String person = peopleToGreetIterator.next();
            logger.log("Asking server to greet " + person);
            requestsStream.onNext(
                    GreetingRequest.newBuilder().setFirstName(person).build()
            );

            logger.log("Waiting 5 sec before sending the next person request");
            TimeUnit.SECONDS.sleep(5L);
        }

        logger.log("Looped out either because everyone was greeted or server stream ended");
        logger.log("¿Did server stream end? " + !getIsResponsesStreamAlive());
        if (getIsResponsesStreamAlive()) requestsStream.onCompleted();
        countDownLatch.await();

    }


}

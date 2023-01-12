package org.bolsadeideas.grcp.greet.client;

import com.bolsadeideas.grcp.greet.GreetingRequest;
import com.bolsadeideas.grcp.greet.GreetingResponse;
import com.bolsadeideas.grcp.greet.GreetingServiceGrpc;
import io.grpc.ManagedChannel;
import io.grpc.stub.StreamObserver;
import org.bolsadeideas.grcp.greet.Log;

import java.util.Iterator;
import java.util.concurrent.CountDownLatch;

public class DoGreetManyTimesAsync implements IDoGreetManyTimes {

    Log logger = Log.getInstance(DoGreetManyTimesAsync.class);
    @Override
    public void doGreetManyTimes(ManagedChannel channel, String name) throws InterruptedException {
        logger.log("Enter doGreetManyTimes");
        CountDownLatch countDownLatch = new CountDownLatch(1);
        GreetingServiceGrpc.GreetingServiceStub stub = GreetingServiceGrpc.newStub(channel);

        stub.greetManyTimes(
                GreetingRequest.newBuilder().setFirstName(name).build(),
                new StreamObserver<GreetingResponse>() {
                    @Override
                    public void onNext(GreetingResponse value) {
                        logger.log("(Async) got greeted by GreetingServerImpl: " + value.getResult());
                    }

                    @Override
                    public void onError(Throwable t) {}

                    @Override
                    public void onCompleted() {
                        countDownLatch.countDown();
                    }
                }
        );

        countDownLatch.await();
    }
}

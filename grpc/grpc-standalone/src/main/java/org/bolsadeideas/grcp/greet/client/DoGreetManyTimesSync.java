package org.bolsadeideas.grcp.greet.client;

import com.bolsadeideas.grcp.greet.GreetingRequest;
import com.bolsadeideas.grcp.greet.GreetingResponse;
import com.bolsadeideas.grcp.greet.GreetingServiceGrpc;
import io.grpc.ManagedChannel;
import org.bolsadeideas.grcp.Log;

import java.util.Iterator;

public class DoGreetManyTimesSync implements IDoGreetManyTimes{

    Log logger = Log.getInstance(DoGreetManyTimesSync.class);
    @Override
    public void doGreetManyTimes(ManagedChannel channel, String name) {
        logger.log("Enter doGreetManyTimes");
        GreetingServiceGrpc.GreetingServiceBlockingStub stub = GreetingServiceGrpc.newBlockingStub(channel);

        Iterator<GreetingResponse> greetingResponseIterator = stub.greetManyTimes(
                GreetingRequest.newBuilder().setFirstName(name).build()
        );

        greetingResponseIterator.forEachRemaining(greetingResponse -> logger.log("Got greeted by server: " + greetingResponse.getResult()));
    }
}

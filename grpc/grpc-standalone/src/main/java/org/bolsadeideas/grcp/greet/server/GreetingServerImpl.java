package org.bolsadeideas.grcp.greet.server;

import com.bolsadeideas.grcp.greet.GreetingRequest;
import com.bolsadeideas.grcp.greet.GreetingResponse;
import com.bolsadeideas.grcp.greet.GreetingServiceGrpc;
import io.grpc.stub.StreamObserver;
import org.bolsadeideas.grcp.Log;
import org.bolsadeideas.grcp.PeopleData;

import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;

public class GreetingServerImpl extends GreetingServiceGrpc.GreetingServiceImplBase {

    private Log logger = Log.getInstance(GreetingServerImpl.class);

    @Override
    public void greet(GreetingRequest request, StreamObserver<GreetingResponse> responseObserver) {

        responseObserver.onNext(GreetingResponse
                .newBuilder()
                .setResult(getGreetingFromRequest(request))
                .build()
        );

        responseObserver.onCompleted();
    }

    @Override
    public void greetManyTimes(GreetingRequest request, StreamObserver<GreetingResponse> responseObserver) {

        GreetingResponse response = GreetingResponse
                .newBuilder()
                .setResult(getGreetingFromRequest(request))
                .build();

        for (int i = 0; i < 10; ++i) {
            int randomNum = ThreadLocalRandom.current().nextInt(1, 10 + 1);
            logger.log("I'm tired. Gonna wait " + randomNum + " secs before greeting again");
            try {
                TimeUnit.SECONDS.sleep(randomNum);
            } catch(InterruptedException e) {
               responseObserver.onError(e);
            }
            responseObserver.onNext(response);
        }

        responseObserver.onCompleted();
    }

    @Override
    public StreamObserver<GreetingRequest> longGreet(StreamObserver<GreetingResponse> responseObserver) {

        StringBuilder sb = new StringBuilder();
        return new StreamObserver<GreetingRequest>() {
            @Override
            public void onNext(GreetingRequest value) {
                final String firstName = value.getFirstName();
                logger.log("onNext: " + firstName);
                sb.append(getGreetingFromFirstName(firstName));
                sb.append("\n");
            }

            @Override
            public void onError(Throwable t) {
                responseObserver.onError(t);
            }

            @Override
            public void onCompleted() {
                responseObserver.onNext(
                        GreetingResponse
                                .newBuilder()
                                .setResult(sb.toString())
                                .build()
                );
                responseObserver.onCompleted();
            }
        };

    }

    @Override
    public StreamObserver<GreetingRequest> greetManyPeople(StreamObserver<GreetingResponse> responseObserver) {

        return new StreamObserver<GreetingRequest>() {
            @Override
            public void onNext(GreetingRequest value) {
                logger.log("Got a GreetingRequest for " + value.getFirstName());
                String greet = getGreetingFromRequest(value);

                logger.log("I'm lazy so I'll take 2 seconds to greet");
                try {
                    TimeUnit.SECONDS.sleep(2L);
                } catch (InterruptedException e) {
                    responseObserver.onError(e);
                }

                logger.log("Okay, going to greet " + value.getFirstName() + " now");

                if(value.getFirstName().equals(PeopleData.SERVER_ENEMY())) {
                    logger.log("No way I'm greeting this one. I'll stop now");
                    responseObserver.onCompleted();
                }
                else {
                    responseObserver.onNext(
                            GreetingResponse.newBuilder().setResult(greet).build()
                    );
                }
            }

            @Override
            public void onError(Throwable t) {}

            @Override
            public void onCompleted() {
                responseObserver.onCompleted();
            }
        };

    }

    private String getGreetingFromRequest(GreetingRequest greetingRequest) {
        return getGreetingFromFirstName(greetingRequest.getFirstName());
    }

    private String getGreetingFromFirstName(String firstName) {
        return "Hi " + firstName + "! :)";
    }

}

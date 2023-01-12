package org.bolsadeideas.grcp.greet.server;

import com.bolsadeideas.grcp.greet.GreetingRequest;
import com.bolsadeideas.grcp.greet.GreetingResponse;
import com.bolsadeideas.grcp.greet.GreetingServiceGrpc;
import io.grpc.stub.StreamObserver;
import org.bolsadeideas.grcp.greet.Log;

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

    private String getGreetingFromRequest(GreetingRequest greetingRequest) {
        return getGreetingFromFirstName(greetingRequest.getFirstName());
    }

    private String getGreetingFromFirstName(String firstName) {
        return "Hi " + firstName + "! :)";
    }

}

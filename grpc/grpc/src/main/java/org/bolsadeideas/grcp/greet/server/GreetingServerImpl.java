package org.bolsadeideas.grcp.greet.server;

import com.bolsadeideas.grcp.greet.GreetingRequest;
import com.bolsadeideas.grcp.greet.GreetingResponse;
import com.bolsadeideas.grcp.greet.GreetingServiceGrpc;
import io.grpc.stub.StreamObserver;

public class GreetingServerImpl extends GreetingServiceGrpc.GreetingServiceImplBase {

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
            responseObserver.onNext(response);
        }

        responseObserver.onCompleted();
    }

    private String getGreetingFromRequest(GreetingRequest greetingRequest) {
        return "Hi " + greetingRequest.getFirstName() + "! :)";
    }

}

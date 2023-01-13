package org.bolsadeideas.grcp.math.server;

import com.bolsadeideas.grcp.math.MathServiceGrpc;
import com.bolsadeideas.grcp.math.SqrtRequest;
import com.bolsadeideas.grcp.math.SqrtResponse;
import io.grpc.Status;
import io.grpc.stub.StreamObserver;

public class MathServerImpl extends MathServiceGrpc.MathServiceImplBase {
    @Override
    public void sqrt(SqrtRequest request, StreamObserver<SqrtResponse> responseObserver) {

        int number = request.getNumber();

        if (number < 0) {
            responseObserver.onError(
                    Status.INVALID_ARGUMENT
                            .withDescription("The number cannot be negative")
                            .augmentDescription("Number: " + number)
                            .asRuntimeException()
            );
            return;
        }

        responseObserver.onNext(
                SqrtResponse.newBuilder().setResult(Math.sqrt(request.getNumber())).build()
        );
        responseObserver.onCompleted();

    }
}

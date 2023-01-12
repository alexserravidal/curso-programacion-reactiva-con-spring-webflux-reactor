package com.bolsadeideas.grpcstudents.clients;

import com.bolsadeideas.grpcstudents.models.School;
import io.grpc.ManagedChannel;
import io.grpc.netty.shaded.io.grpc.netty.NettyChannelBuilder;
import org.springframework.stereotype.Service;
import com.bolsadeideas.grpcschoolsinterface.grpc.*;

@Service
public class SchoolsGrpcClient implements ISchoolsClient {

    private ManagedChannel channel = NettyChannelBuilder.forAddress("localhost", 9090).usePlaintext().build();

    public School findById(Long id) {

        SchoolsServiceGrpc.SchoolsServiceBlockingStub stub = SchoolsServiceGrpc.newBlockingStub(channel);
        FindByIdRequestParams findByIdRequestParams = FindByIdRequestParams
                .newBuilder()
                .setId(id)
                .build();
        SchoolGrpcObject schoolGrpcObject = stub.findById(findByIdRequestParams);
        return new School(schoolGrpcObject);

    }

}

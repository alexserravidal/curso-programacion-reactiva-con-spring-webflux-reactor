package com.bolsadeideas.grpcstudents.clients;

import com.bolsadeideas.grpcstudents.models.School;
import io.grpc.ManagedChannel;
import io.grpc.netty.shaded.io.grpc.netty.NettyChannelBuilder;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;
import com.bolsadeideas.grpcschoolsinterface.grpc.*;

import java.util.List;

@Service
@Primary
@Qualifier("SchoolsGrpcClient")
public class SchoolsGrpcClient implements ISchoolsClient {

    private ManagedChannel channel = NettyChannelBuilder.forAddress("localhost", 9090).usePlaintext().build();
    private SchoolsServiceGrpc.SchoolsServiceBlockingStub stub = SchoolsServiceGrpc.newBlockingStub(channel);

    public School findById(Long id) {

        FindByIdRequestParams findByIdRequestParams = FindByIdRequestParams
                .newBuilder()
                .setId(id)
                .build();
        SchoolGrpcObject schoolGrpcObject = stub.findById(findByIdRequestParams);
        return new School(schoolGrpcObject);

    }

    public List<School> findAll() {

        SchoolsGrpcList schoolsGrpcList = stub.findAll(Empty.newBuilder().build());
        return schoolsGrpcList.getSchoolsList().stream().map(School::new).toList();

    }

}

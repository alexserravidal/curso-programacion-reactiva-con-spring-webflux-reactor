package com.bolsadeideas.grpcschools.grpc_services_impl;

import com.bolsadeideas.grpcschools.entities.SchoolEntity;
import com.bolsadeideas.grpcschools.grpc.*;
import com.bolsadeideas.grpcschools.grpc.SchoolsServiceGrpc.SchoolsServiceImplBase;
import com.bolsadeideas.grpcschools.services.SchoolsService;
import io.grpc.stub.StreamObserver;
import net.devh.boot.grpc.server.service.GrpcService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Optional;

@GrpcService
public class SchoolsGrpcServiceImpl extends SchoolsServiceImplBase
{

    @Autowired
    private SchoolsService schoolsService;

    @Override
    public void findAll(Empty request, StreamObserver<SchoolsGrpcList> responseObserver) {

        List<SchoolEntity> schoolEntities = schoolsService.findAll();

        SchoolsGrpcList schoolsGrpcList = SchoolsGrpcList.newBuilder().addAllSchools(
                schoolEntities.stream().map(schoolEntity -> getSchoolGrpcObjectFromSchoolEntity(schoolEntity)).toList()
        )
        .build();

        responseObserver.onNext(schoolsGrpcList);
        responseObserver.onCompleted();
    }

    @Override
    public void findById(id request, StreamObserver<SchoolGrpcObject> responseObserver) {

        Optional<SchoolEntity> optSchoolEntity = schoolsService.findById(request.getId());

        if (optSchoolEntity.isEmpty()) {
            responseObserver.onError(new RuntimeException("School with ID " + request.getId() + " not found"));
        }

        SchoolEntity schoolEntity = optSchoolEntity.get();
        SchoolGrpcObject schoolGrpcObject = getSchoolGrpcObjectFromSchoolEntity(schoolEntity);

        responseObserver.onNext(schoolGrpcObject);
        responseObserver.onCompleted();
    }

    private SchoolGrpcObject getSchoolGrpcObjectFromSchoolEntity(SchoolEntity schoolEntity) {
        return SchoolGrpcObject.newBuilder()
            .setId(schoolEntity.getId())
            .setName(schoolEntity.getName())
            .setAddress(schoolEntity.getAddress())
            .setStatus(schoolEntity.getStatus())
            .build();
    }

}

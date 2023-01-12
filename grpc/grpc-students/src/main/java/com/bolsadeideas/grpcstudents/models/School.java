package com.bolsadeideas.grpcstudents.models;

import com.bolsadeideas.grpcschoolsinterface.grpc.SchoolGrpcObject;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class School {

    private Long id;

    private String name;

    private String address;

    private Integer status;

    public School(SchoolGrpcObject schoolGrpcObject) {
        this.id = schoolGrpcObject.getId();
        this.name = schoolGrpcObject.getName();
        this.address = schoolGrpcObject.getAddress();
        this.status = schoolGrpcObject.getStatus();
    }

}

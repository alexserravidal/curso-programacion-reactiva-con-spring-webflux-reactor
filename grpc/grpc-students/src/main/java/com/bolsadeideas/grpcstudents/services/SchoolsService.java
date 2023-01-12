package com.bolsadeideas.grpcstudents.services;

import com.bolsadeideas.grpcstudents.clients.ISchoolsClient;
import com.bolsadeideas.grpcstudents.models.School;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SchoolsService {

    @Autowired
    @Qualifier("SchoolsGrpcClient")
    private ISchoolsClient schoolsGrpcClient;

    @Autowired
    @Qualifier("SchoolsRestClient")
    private ISchoolsClient schoolsRestClient;

    public List<School> findAll(boolean useGrpc) {

        if (useGrpc) return schoolsGrpcClient.findAll();
        return schoolsRestClient.findAll();

    }

}

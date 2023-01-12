package com.bolsadeideas.grpcstudents.services;

import com.bolsadeideas.grpcstudents.clients.ISchoolsClient;
import com.bolsadeideas.grpcstudents.models.School;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SchoolsService {

    private ISchoolsClient schoolsClient;

    public SchoolsService(ISchoolsClient schoolsClient) {
        this.schoolsClient = schoolsClient;
    }

    public List<School> findAll() {
        return schoolsClient.findAll();
    }

}

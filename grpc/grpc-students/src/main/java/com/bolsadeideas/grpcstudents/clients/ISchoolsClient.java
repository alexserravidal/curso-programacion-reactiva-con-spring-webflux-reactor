package com.bolsadeideas.grpcstudents.clients;

import com.bolsadeideas.grpcstudents.models.School;

import java.util.List;

public interface ISchoolsClient {

    public School findById(Long id);

    public List<School> findAll();

}

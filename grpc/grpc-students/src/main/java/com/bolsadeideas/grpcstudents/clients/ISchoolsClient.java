package com.bolsadeideas.grpcstudents.clients;

import com.bolsadeideas.grpcstudents.models.School;

public interface ISchoolsClient {

    public School findById(Long id);

}

package com.bolsadeideas.grpcstudents.clients;

import com.bolsadeideas.grpcstudents.models.School;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
@Qualifier("SchoolsRestClient")
public class SchoolsRestClient implements ISchoolsClient {
    @Override
    public School findById(Long id) {
        return null;
    }

    @Override
    public List<School> findAll() {
        List<School> schools = Arrays.asList(new School("Mocked School", "Mocked School", -1));
        return schools;
    }
}

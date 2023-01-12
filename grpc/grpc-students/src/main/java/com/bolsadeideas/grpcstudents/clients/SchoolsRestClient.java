package com.bolsadeideas.grpcstudents.clients;

import com.bolsadeideas.grpcstudents.models.School;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;

@Service
@Qualifier("SchoolsRestClient")
public class SchoolsRestClient implements ISchoolsClient {

    @Value("${ms-schools.port}")
    private String MS_SCHOOLS_PORT;

    @Override
    public School findById(Long id) {
        return null;
    }

    @Override
    public List<School> findAll() {

        RestTemplate restTemplate = new RestTemplate();
        School[] schoolsArray = restTemplate.getForObject(getSchoolsMsBaseUrl(), School[].class);

        return Arrays.stream(schoolsArray).toList();
    }

    private String getSchoolsMsBaseUrl() {
        return "http://localhost:" + MS_SCHOOLS_PORT + "/api/v1/schools";
    }
}

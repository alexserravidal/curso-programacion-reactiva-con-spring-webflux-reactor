package com.bolsadeideas.grpcstudents.controllers;

import com.bolsadeideas.grpcstudents.models.School;
import com.bolsadeideas.grpcstudents.services.SchoolsService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("api/v1/schools")
public class SchoolsController {

    private SchoolsService schoolsService;

    public SchoolsController(SchoolsService schoolsService) {
        this.schoolsService = schoolsService;
    }

    @GetMapping
    public ResponseEntity<List<School>> findAll(
            @RequestParam(name = "useGrpc", required = true, defaultValue = "true")
            boolean useGrpc
    ) {

        return ResponseEntity.ok(schoolsService.findAll(useGrpc));

    }

}

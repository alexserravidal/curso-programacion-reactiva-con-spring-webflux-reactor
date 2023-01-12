package com.bolsadeideas.grpcstudents.models;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class School {

    private Long id;

    private String name;

    private String address;

    private Integer status;

}

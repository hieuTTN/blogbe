package com.web.dto.request;

import lombok.Getter;
import lombok.Setter;

import java.sql.Date;
import java.sql.Time;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class DocumentRequest {

    private String name;

    private String image;

    private String description;

    private List<Long> listCategoryId = new ArrayList<>();

    private List<FileDto> linkFiles = new ArrayList<>();
}

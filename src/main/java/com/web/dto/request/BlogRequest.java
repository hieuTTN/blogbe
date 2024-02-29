package com.web.dto.request;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Setter
@Getter
public class BlogRequest {

    private Long id;

    private String title;

    private String description;

    private String image;

    private String content;

    private List<BlogFileDto> linkFiles = new ArrayList<>();

    private List<Long> listCategoryId = new ArrayList<>();
}

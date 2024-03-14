package com.web.dto.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FileDto {

    private String linkFile;

    private String typeFile;

    private String fileName;

    private Long fileSize;
}

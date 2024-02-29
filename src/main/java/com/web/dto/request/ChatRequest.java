package com.web.dto.request;

import lombok.Getter;
import lombok.Setter;

import java.sql.Timestamp;

@Getter
@Setter
public class ChatRequest {

    private String content;

    private String linkFile;

    private String typeFile;

    private Boolean isFile;

    private Long receiverId;
}

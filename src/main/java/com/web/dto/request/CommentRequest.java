package com.web.dto.request;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.web.entity.Blog;
import com.web.entity.Comment;
import com.web.entity.User;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.sql.Date;
import java.sql.Time;
import java.util.List;

@Getter
@Setter
public class CommentRequest {

    private String content;

    private Long blogId;

    private Long parentCommentId;

}

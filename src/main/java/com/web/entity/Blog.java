package com.web.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.sql.Date;
import java.sql.Time;

@Entity
@Table(name = "blog")
@Getter
@Setter
public class Blog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    private String title;

    private String description;

    private String image;

    private String content;

    private Date createdDate;

    private Time createdTime;

    private Integer numLike;

    private Integer numView;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
}

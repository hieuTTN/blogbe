package com.web.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.sql.Date;
import java.sql.Time;

@Entity
@Table(name = "document")
@Getter
@Setter
public class Document {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    private String name;

    private Date createdDate;

    private Time createdTime;

    private String image;

    private Integer numView;

    private Integer numDownload;

    private String description;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
}

package com.web.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name = "blog_file")
@Getter
@Setter
public class BlogFile {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    private String linkFile;

    private String typeFile;

    @ManyToOne
    @JoinColumn(name = "blog_id")
    private Blog blog;
}

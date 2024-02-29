package com.web.repository;

import com.web.entity.BlogFile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;

@Repository
public interface BlogFileRepository extends JpaRepository<BlogFile, Long> {

    @Modifying
    @Transactional
    @Query("delete from BlogFile b where b.blog.id = ?1")
    public int deleteByBlog(Long blogId);
}

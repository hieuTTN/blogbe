package com.web.repository;

import com.web.entity.BlogCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;

@Repository
public interface BlogCategoryRepository extends JpaRepository<BlogCategory, Long> {

    @Modifying
    @Transactional
    @Query("delete from BlogCategory b where b.blog.id = ?1")
    public int deleteByBlog(Long blogId);
}

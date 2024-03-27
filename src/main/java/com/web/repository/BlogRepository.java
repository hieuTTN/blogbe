package com.web.repository;

import com.web.entity.Blog;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BlogRepository extends JpaRepository<Blog, Long> {

    @Query("select b from Blog b where b.actived = true")
    Page<Blog> getBlogActived(Pageable pageable);

    @Query("select b from Blog b where b.title = ?1 and b.actived = true")
    Page<Blog> searchBlog(String search, Pageable pageable);

    @Query("select b from BlogCategory b where b.category.id = ?1 and b.blog.actived = true")
    Page<Blog> getBlogByCategory(Long CategoryId, Pageable pageable);
}

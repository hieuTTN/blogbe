package com.web.repository;

import com.web.entity.Blog;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface BlogRepository extends JpaRepository<Blog, Long> {

    @Query("select b from Blog b where b.actived = true")
    Page<Blog> getBlogActived(Pageable pageable);

    @Query("select b from Blog b where b.actived = true and (b.title like ?1)")
    Page<Blog> searchBlog(String search,Pageable pageable);

    @Query("select b.blog from BlogCategory b where b.category.id = ?1")
    Page<Blog> getBlogsByCategory(Long categoryId, Pageable pageable);
}

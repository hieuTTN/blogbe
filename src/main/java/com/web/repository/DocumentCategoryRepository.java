package com.web.repository;

import com.web.entity.DocumentCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DocumentCategoryRepository extends JpaRepository<DocumentCategory, Long> {
}

package com.web.repository;

import com.web.entity.DocumentCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;

@Repository
public interface DocumentCategoryRepository extends JpaRepository<DocumentCategory, Long> {

    @Modifying
    @Transactional
    @Query("delete from DocumentCategory b where b.document.id = ?1")
    public int deleteByDocument(Long documentId);
}

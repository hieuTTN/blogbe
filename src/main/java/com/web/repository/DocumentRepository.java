package com.web.repository;

import com.web.entity.Document;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface DocumentRepository extends JpaRepository<Document, Long> {

    @Query("select d from Document d")
    public Page<Document> findAll(Pageable pageable);

    @Query("select d from Document d where d.actived = true")
    public Page<Document> getDocumentActived(Pageable pageable);

    @Query("select d from Document d where d.name = ?1 and d.actived = true")
    public Page<Document> searchDocumentByName(String name, Pageable pageable);

    @Query("select d from DocumentCategory d where d.category.id = ?1 and d.document.actived = true")
    public Page<Document> getDocumentByCategory(Long categoryId, Pageable pageable);
}

package com.web.service;

import com.web.dto.request.DocumentRequest;
import com.web.dto.request.FileDto;
import com.web.entity.*;
import com.web.exception.MessageException;
import com.web.repository.CategoryRepository;
import com.web.repository.DocumentCategoryRepository;
import com.web.repository.DocumentFileRepository;
import com.web.repository.DocumentRepository;
import com.web.utils.Contains;
import com.web.utils.UserUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.print.Doc;
import java.sql.Date;
import java.sql.Time;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class DocumentService {
    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private DocumentRepository documentRepository;

    @Autowired
    private DocumentFileRepository documentFileRepository;

    @Autowired
    private UserUtils userUtils;

    @Autowired
    private DocumentCategoryRepository documentCategoryRepository;

    public Document save(DocumentRequest request){
        List<Category> categories = new ArrayList<>();
        for (long id : request.getListCategoryId()){
            Optional<Category> category = categoryRepository.findById(id);
            if (category.isEmpty()){
                throw new MessageException("Danh mục : " + id +"không tồn tại");
            }
            categories.add(category.get());
        }

        if (request.getLinkFiles().isEmpty()){
            throw new MessageException("Không có file nào!");
        }
        User user = userUtils.getUserWithAuthority();
        Document document = new Document();
        document.setCreatedDate(new Date(System.currentTimeMillis()));
        document.setCreatedTime(new Time(System.currentTimeMillis()));
        document.setUser(user);
        document.setNumView(0);
        document.setImage(request.getImage());
        document.setDescription(request.getDescription());
        document.setNumDownload(0);
        document.setName(request.getName());
        if(user.getRole().equals(Contains.ROLE_ADMIN)){
            document.setActived(true);
        }
        Document result = documentRepository.save(document);

        List<DocumentCategory> documentCategories = new ArrayList<>();
        for (Category c: categories){
            DocumentCategory documentCategory = new DocumentCategory();
            documentCategory.setCategory(c);
            documentCategory.setDocument(result);
            documentCategories.add(documentCategory);
        }
        documentCategoryRepository.saveAll(documentCategories);

        List<DocumentFile> documentFiles = new ArrayList<>();
        for (FileDto fileDto: request.getLinkFiles()){
            DocumentFile documentFile = new DocumentFile();
            documentFile.setDocument(result);
            documentFile.setLinkFile(fileDto.getLinkFile());
            documentFile.setFileName(fileDto.getFileName());
            documentFile.setFileSize(fileDto.getFileSize());
            documentFile.setFileType(fileDto.getTypeFile());
            documentFiles.add(documentFile);
        }
        documentFileRepository.saveAll(documentFiles);

        return result;
    }

    public Document update(DocumentRequest request, Long id){
        if (id == null){
            throw new MessageException("Id không được null!");
        }
        Optional<Document> document = documentRepository.findById(id);
        if (document.isEmpty()){
            throw new MessageException("Document không tồn tại");
        }

        List<Category> categories = new ArrayList<>();
        for (long categoryid : request.getListCategoryId()){
            Optional<Category> category = categoryRepository.findById(categoryid);
            if (category.isEmpty()){
                throw new MessageException("Danh mục : " + categoryid +"không tồn tại");
            }
            categories.add(category.get());
        }
        document.get().setName(request.getName());
        document.get().setDescription(request.getDescription());
        document.get().setImage(request.getImage());
        documentRepository.save(document.get());
        documentCategoryRepository.deleteByDocument(document.get().getId());

        List<DocumentCategory> documentCategories = new ArrayList<>();
        for (Category c: categories){
            DocumentCategory documentCategory = new DocumentCategory();
            documentCategory.setCategory(c);
            documentCategory.setDocument(document.get());
            documentCategories.add(documentCategory);
        }
        documentCategoryRepository.saveAll(documentCategories);

        List<DocumentFile> documentFiles = new ArrayList<>();
        for (FileDto fileDto: request.getLinkFiles()){
            DocumentFile documentFile = new DocumentFile();
            documentFile.setDocument(document.get());
            documentFile.setLinkFile(fileDto.getLinkFile());
            documentFile.setFileName(fileDto.getFileName());
            documentFile.setFileSize(fileDto.getFileSize());
            documentFile.setFileType(fileDto.getTypeFile());
            documentFiles.add(documentFile);
        }
        documentFileRepository.saveAll(documentFiles);

        return document.get();
    }

    public Page<Document> findAll(Pageable pageable){
        return documentRepository.findAll(pageable);
    }

    public void delete(Long id){
        Optional<Document> document = documentRepository.findById(id);
        if (document.isEmpty()){
            throw new MessageException("Document không tồn tại");
        }
        documentRepository.delete(document.get());
    }

    public Document findById(Long id){
        Optional<Document> document = documentRepository.findById(id);
        if (document.isEmpty()){
            throw new MessageException("Document không tồn tại");
        }
        return document.get();
    }
}

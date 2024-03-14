package com.web.api;

import com.web.dto.request.DocumentRequest;
import com.web.entity.Document;
import com.web.repository.DocumentRepository;
import com.web.service.DocumentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/document")
@CrossOrigin
public class DocumentApi {

    @Autowired
    private DocumentService documentService;

    @PostMapping("/all/save")
    public ResponseEntity<?> saveDocument(@RequestBody DocumentRequest documentRequest){
        Document document = documentService.save(documentRequest);
        return new ResponseEntity<>(document, HttpStatus.CREATED);
    }

    @PostMapping("/all/update")
    public ResponseEntity<?> updateDocument(@RequestBody DocumentRequest documentRequest,@RequestParam Long Id){
        Document document = documentService.update(documentRequest, Id);
        return new ResponseEntity<>(document, HttpStatus.CREATED);
    }

    @GetMapping("/all/find-all")
    public ResponseEntity<?> findAll(Pageable pageable){
        Page<Document> document = documentService.findAll(pageable);
        return new ResponseEntity<>(document, HttpStatus.CREATED);
    }

    @DeleteMapping("/all/delete")
    public void deleteDocument(@RequestParam Long documentId){
        documentService.delete(documentId);
    }

    @GetMapping("/all/findbyid")
    public ResponseEntity<?> findById(@RequestParam Long id){
        Document document = documentService.findById(id);
        return new ResponseEntity<>(document, HttpStatus.CREATED);
    }
}

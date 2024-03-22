package com.web.api;

import com.web.dto.request.CommentRequest;
import com.web.entity.Comment;
import com.web.service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/comment")
@CrossOrigin
public class CommentApi {

    @Autowired
    private CommentService commentService;

    @GetMapping("/public/find-by-blog")
    public ResponseEntity<?> findByBlog(@RequestParam(value = "blogId") Long blogId, Pageable pageable){
        Page<Comment> commentPage = commentService.findByBlog(pageable,blogId);
        return new ResponseEntity<>(commentPage, HttpStatus.OK);
    }

    @PostMapping("/all/save")
    public ResponseEntity<?> save(@RequestBody CommentRequest commentRequest){
        Comment comment = commentService.save(commentRequest);
        return new ResponseEntity<>(comment, HttpStatus.CREATED);
    }

    @DeleteMapping("/all/delete")
    public void delete(@RequestParam Long id){
        commentService.delete(id);
    }

    @GetMapping("/public/find-by-comment-parent")
    public ResponseEntity<?> findByParent(@RequestParam Long parentId){
        List<Comment> comments = commentService.findByParent(parentId);
        return new ResponseEntity<>(comments, HttpStatus.CREATED);
    }
}

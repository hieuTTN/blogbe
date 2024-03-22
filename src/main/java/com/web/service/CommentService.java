package com.web.service;

import com.web.dto.request.CommentRequest;
import com.web.entity.Blog;
import com.web.entity.Comment;
import com.web.exception.MessageException;
import com.web.repository.BlogRepository;
import com.web.repository.CommentRepository;
import com.web.utils.Contains;
import com.web.utils.UserUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.sql.Time;
import java.util.List;
import java.util.Optional;

@Service
public class CommentService {

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private UserUtils userUtils;

    @Autowired
    private BlogRepository blogRepository;

    public Comment save(CommentRequest request){
        Comment comment = new Comment();
        comment.setCreatedDate(new Date(System.currentTimeMillis()));
        comment.setCreatedTime(new Time(System.currentTimeMillis()));
        comment.setUser(userUtils.getUserWithAuthority());
        comment.setContent(request.getContent());
        Optional<Blog> blog = blogRepository.findById(request.getBlogId());
        comment.setBlog(blog.get());
        if (blog.isEmpty()){
            throw new MessageException("Blog không tồn tại");
        }
        if (request.getParentCommentId() != null) {
            Optional<Comment> parentComment = commentRepository.findById(request.getParentCommentId());
            if(parentComment.isEmpty()){
                throw new MessageException("Không tồn tại bình luận: "+request.getParentCommentId());
            }
            if(parentComment.get().getNumSubComment() == null){
                parentComment.get().setNumSubComment(0);
            }
            if(parentComment.get().getBlog().getId() != blog.get().getId()){
                throw new MessageException("Blog không trùng khớp");
            }
            parentComment.get().setNumSubComment( parentComment.get().getNumSubComment() + 1 );
            commentRepository.save(parentComment.get());
            comment.setParentComment(parentComment.get());
            comment.setUsernameReciver(parentComment.get().getUser().getUsername());
        }
        commentRepository.save(comment);
        return comment;
    }

    public void delete(Long id){
        Optional<Comment> comment = commentRepository.findById(id);
        if (comment.isEmpty()) {
            throw new MessageException("Comment không tồn tại");
        }

        if (userUtils.getUserWithAuthority().getId() != comment.get().getUser().getId() &&
                userUtils.getUserWithAuthority().getRole().equals(Contains.ROLE_USER)) {
            throw new MessageException("Người dùng không đủ quyền");
        }
        commentRepository.delete(comment.get());
    }

    public Page<Comment> findByBlog(Pageable pageable, Long blogId){
        Page<Comment> page = null;
        Optional<Blog> blog = blogRepository.findById(blogId);
        if (blog.isEmpty()) {
            throw new MessageException("Blog không tồn tại");
        }
        page = commentRepository.findByBlog(blogId,pageable);
        return page;
    }

    public List<Comment> findByParent(Long parentId){
        List<Comment> comments = commentRepository.findByParent(parentId);
        return comments;
    }
}

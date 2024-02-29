package com.web.mapper;

import com.web.dto.request.BlogRequest;
import com.web.entity.Blog;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

@Service
public class BlogMapper {

    public Blog convertRequestToBlog(BlogRequest request){
        Blog blog = new Blog();
        blog.setContent(request.getContent());
        blog.setDescription(request.getDescription());
        blog.setImage(request.getImage());
        blog.setTitle(request.getTitle());
        blog.setId(request.getId());
        return blog;
    }
}

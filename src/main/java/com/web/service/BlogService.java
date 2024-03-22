package com.web.service;

import com.web.dto.request.FileDto;
import com.web.dto.request.BlogRequest;
import com.web.entity.*;
import com.web.enums.ActiveStatus;
import com.web.exception.MessageException;
import com.web.mapper.BlogMapper;
import com.web.repository.BlogCategoryRepository;
import com.web.repository.BlogFileRepository;
import com.web.repository.BlogRepository;
import com.web.repository.CategoryRepository;
import com.web.utils.Contains;
import com.web.utils.UserUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.sql.Time;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class BlogService {

    @Autowired
    private BlogRepository blogRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private BlogCategoryRepository blogCategoryRepository;

    @Autowired
    private BlogFileRepository blogFileRepository;

    @Autowired
    private BlogMapper blogMapper;

    @Autowired
    private UserUtils userUtils;

    public Blog save(BlogRequest request) {
        if (request.getId() != null) {
            throw new MessageException("Id must null");
        }
        List<Category> categories = new ArrayList<>();
        // kiểm tra xem có danh mục nào không tồn tại không, nếu có thì hủy hàm, báo lỗi
        for (Long id : request.getListCategoryId()) {
            Optional<Category> category = categoryRepository.findById(id);
            if (category.isEmpty()) {
                throw new MessageException("Danh mục :" + id + " không tồn tại");
            }
            categories.add(category.get());
        }
        User user = userUtils.getUserWithAuthority();
        Blog blog = blogMapper.convertRequestToBlog(request);
        blog.setCreatedDate(new Date(System.currentTimeMillis()));
        blog.setCreatedTime(new Time(System.currentTimeMillis()));
        blog.setUser(user);
        blog.setNumLike(0);
        blog.setNumView(0);
        if(user.getRole().equals(Contains.ROLE_ADMIN)){
            blog.setActived(true);
        }
        Blog result = blogRepository.save(blog);

        for (Category c : categories) {
            BlogCategory blogCategory = new BlogCategory();
            blogCategory.setCategory(c);
            blogCategory.setBlog(result);
            blogCategoryRepository.save(blogCategory);
        }

        for (FileDto blogFileDto : request.getLinkFiles()) {
            BlogFile blogFile = new BlogFile();
            blogFile.setBlog(result);
            blogFile.setLinkFile(blogFileDto.getLinkFile());
            blogFile.setTypeFile(blogFileDto.getTypeFile());
            blogFileRepository.save(blogFile);
        }
        return result;
    }


    public Blog update(BlogRequest request) {
        if (request.getId() == null) {
            throw new MessageException("Id is not null");
        }
        Optional<Blog> blogExist = blogRepository.findById(request.getId());
        if(blogExist.isEmpty()){
            throw new MessageException("blog: "+request.getId()+" not found");
        }
        // nếu user muốn sửa khác với user đăng thì báo lỗi
        if(blogExist.get().getUser().getId() != userUtils.getUserWithAuthority().getId()){
            throw new MessageException("Bạn không đủ quyền");
        }
        List<Category> categories = new ArrayList<>();
        // kiểm tra xem có danh mục nào không tồn tại không, nếu có thì hủy hàm, báo lỗi
        for (Long id : request.getListCategoryId()) {
            Optional<Category> category = categoryRepository.findById(id);
            if (category.isEmpty()) {
                throw new MessageException("Danh mục :" + id + " không tồn tại");
            }
            categories.add(category.get());
        }

        Blog blog = blogMapper.convertRequestToBlog(request);
        blog.setActived(blogExist.get().getActived());
        blog.setCreatedDate(blogExist.get().getCreatedDate());
        blog.setCreatedTime(blogExist.get().getCreatedTime());
        blog.setUser(userUtils.getUserWithAuthority());
        blog.setNumLike(blogExist.get().getNumLike());
        blog.setNumView(blogExist.get().getNumView());
        Blog result = blogRepository.save(blog);

        blogCategoryRepository.deleteByBlog(result.getId());
        for (Category c : categories) {
            BlogCategory blogCategory = new BlogCategory();
            blogCategory.setCategory(c);
            blogCategory.setBlog(result);
            blogCategoryRepository.save(blogCategory);
        }

        blogFileRepository.deleteByBlog(request.getId());
        for (FileDto blogFileDto : request.getLinkFiles()) {
            BlogFile blogFile = new BlogFile();
            blogFile.setBlog(result);
            blogFile.setLinkFile(blogFileDto.getLinkFile());
            blogFile.setTypeFile(blogFileDto.getTypeFile());
            blogFileRepository.save(blogFile);
        }
        return result;
    }

    public Page<Blog> findAll(Pageable pageable){
        return blogRepository.findAll(pageable);
    }

    public void deleteBlog(Long blogId){
        Optional<Blog> blogOptional = blogRepository.findById(blogId);
        if(blogOptional.isEmpty()){
            throw new MessageException("blog id không tồn tại!");
        }

        // lấy thông tin user đang đăng nhập (user gửi yêu cầu)
        User user = userUtils.getUserWithAuthority();

        if (blogOptional.get().getUser().getId() != user.getId() && !user.getRole().equals(Contains.ROLE_ADMIN)){
            throw new MessageException("Không đủ quyền");
        }

        blogRepository.delete(blogOptional.get());
    }

    public Page<Blog> getBlogActived(Pageable pageable){
        Page<Blog> page = blogRepository.getBlogActived(pageable);
        return page;
    }

    public Page<Blog> searchBlog(String search,Pageable pageable){
        Page<Blog> page = blogRepository.searchBlog("%"+search+"%",pageable);
        return page;
    }

    public Page<Blog> getBlogByCategory(Long categoryId, Pageable pageable){
        Page<Blog> page = blogRepository.getBlogsByCategory(categoryId,pageable);
        return page;
    }

    public ActiveStatus activeOrUnactive(Long idBlog){
        Optional<Blog> blogOptional = blogRepository.findById(idBlog);
        if (blogOptional.isEmpty()){
            throw new MessageException("Blog không tồn tại");
        }
        if (blogOptional.get().getActived() == true){
            blogOptional.get().setActived(false);
            blogRepository.save(blogOptional.get());
            return ActiveStatus.DA_KHOA;
        } else {
            blogOptional.get().setActived(true);
            blogRepository.save(blogOptional.get());
            return ActiveStatus.DA_MO_KHOA;
        }
    }
}

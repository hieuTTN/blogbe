package com.web.service;

import com.web.entity.Category;
import com.web.enums.CategoryType;
import com.web.exception.MessageException;
import com.web.repository.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CategoryService {

    @Autowired
    private CategoryRepository categoryRepository;

    public Category saveOrUpdate(Category category){
        if( categoryRepository.findByName(category.getName()).isPresent() && category.getId() == null){
            throw new MessageException("Tên danh mục đã tồn tại");
        }
        return categoryRepository.save(category);
    }

    public List<Category> findAllList(){
        return categoryRepository.findAll();
    }

    public List<Category> findByType(CategoryType categoryType){
        return categoryRepository.findByType(categoryType);
    }

    public Category findById(Long id){
        Optional<Category> category = categoryRepository.findById(id);
        if(category.isEmpty()){
            throw new MessageException("Không tìm thấy danh mục");
        }
        return category.get();
    }

    public void delete(Long id){
        categoryRepository.deleteById(id);
    }
}

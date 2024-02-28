package com.web.api;

import com.web.dto.LoginDto;
import com.web.dto.TokenDto;
import com.web.entity.Category;
import com.web.enums.CategoryType;
import com.web.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/category")
@CrossOrigin
public class CategoryApi {

    @Autowired
    private CategoryService categoryService;

    @PostMapping("/admin/saveOrUpdate")
    public ResponseEntity<?> save(@RequestBody Category category) {
        Category result = categoryService.saveOrUpdate(category);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @GetMapping("/admin/find-by-id")
    public ResponseEntity<?> findById(@RequestParam("id") Long id) {
        Category result = categoryService.findById(id);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @GetMapping("/public/find-all-list")
    public ResponseEntity<?> findAllList() {
        List<Category> result = categoryService.findAllList();
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @GetMapping("/public/find-by-type")
    public ResponseEntity<?> findByType(@RequestParam("type") CategoryType categoryType) {
        List<Category> result = categoryService.findByType(categoryType);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @DeleteMapping("/admin/delete")
    public ResponseEntity<?> delete(@RequestParam("id") Long id) {
        categoryService.delete(id);
        return new ResponseEntity<>("success", HttpStatus.OK);
    }
}

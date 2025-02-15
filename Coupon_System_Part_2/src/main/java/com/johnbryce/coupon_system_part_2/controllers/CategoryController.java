package com.johnbryce.coupon_system_part_2.controllers;

import com.johnbryce.coupon_system_part_2.app_messages.Message;
import com.johnbryce.coupon_system_part_2.dto.CategoryDto;
import com.johnbryce.coupon_system_part_2.exceptions.CouponSystemException;
import com.johnbryce.coupon_system_part_2.services.category.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.sql.SQLException;
import java.util.List;


@RestController
@RequestMapping("api/category")
@RequiredArgsConstructor
public class CategoryController {

    private final CategoryService categoryService;


    @GetMapping("/getRandomCategory")
    public CategoryDto getRandomCategory() throws CouponSystemException {
        return categoryService.getRandomCategory();
    }


    @GetMapping("/isCategoryExistsByIdForController/{id}")
    public Message isCategoryExistsByIdForController(@PathVariable("id") int id) throws CouponSystemException {
        return categoryService.isCategoryExistsByIdForController(id);
    }


    @GetMapping("/isCategoryExistsByName")
    public Message isCategoryExistsByName(@RequestParam("name") String name) {
        return categoryService.isCategoryExistsByName(name.toLowerCase());
    }

    @PostMapping
    public CategoryDto add(@RequestBody CategoryDto categoryDto) throws Exception {
        return categoryService.add(categoryDto);
    }

    @PutMapping("/{id}")
    public Message update(@RequestBody CategoryDto type, @PathVariable("id") Integer id) throws Exception {
        return categoryService.update(type, id);
    }

    @DeleteMapping("/{id}")
    public Message delete(@PathVariable("id") Integer id) throws Exception {
        return categoryService.delete(id);
    }

    @GetMapping("/all")
    public List<CategoryDto> getAll() throws SQLException, CouponSystemException {
        return categoryService.getAll();
    }

    @GetMapping("/{id}")
    public CategoryDto getSingle(@PathVariable("id") Integer id) throws Exception {
        return categoryService.getSingle(id);
    }

}

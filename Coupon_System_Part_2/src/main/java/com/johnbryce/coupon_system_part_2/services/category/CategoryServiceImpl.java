package com.johnbryce.coupon_system_part_2.services.category;

import com.johnbryce.coupon_system_part_2.app_messages.CategoryMessage;
import com.johnbryce.coupon_system_part_2.app_messages.Message;
import com.johnbryce.coupon_system_part_2.dto.CategoryDto;
import com.johnbryce.coupon_system_part_2.entities.Category;
import com.johnbryce.coupon_system_part_2.exceptions.categories.CategoryException;
import com.johnbryce.coupon_system_part_2.exceptions.CouponSystemException;
import com.johnbryce.coupon_system_part_2.repositories.CategoryRepository;
import com.johnbryce.coupon_system_part_2.utils.Utils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;
    private final Utils utils;

    @Override
    public CategoryDto getRandomCategory() throws CouponSystemException {
        CategoryDto categoryDto =
                utils.convertToDto(categoryRepository.findRandomCategory(), CategoryDto.class);
        if (categoryDto == null) {
            throw new CouponSystemException(CategoryException.CATEGORY_NOT_EXISTS);
        }
        return categoryDto;
    }

    @Override
    public Message isCategoryExistsByIdForController(int integer) {
        if (categoryRepository.existsById(integer)) {
            return Message.builder().code(CategoryMessage
                            .CATEGORY_EXISTS_BY_ID
                            .getCode()).
                    message(CategoryMessage
                            .CATEGORY_EXISTS_BY_ID
                            .getMessage()).
                    data(true).
                    build();
        } else {
            return Message.builder().code(CategoryMessage
                            .CATEGORY_NOT_EXISTS_BY_ID
                            .getCode()).
                    message(CategoryMessage.
                            CATEGORY_NOT_EXISTS_BY_ID.
                            getMessage()).
                    data(false).
                    build();
        }

    }

    @Transactional
    @Override
    public CategoryDto add(CategoryDto categoryDto) throws CouponSystemException {
        if (categoryRepository.existsById(categoryDto.getId())) {
            throw new CouponSystemException(CategoryException.CATEGORY_ALREADY_EXISTS);
        }

        if (categoryRepository.existsByName(categoryDto.getName())) {
            throw new CouponSystemException(CategoryException.CATEGORY_ALREADY_EXISTS);
        }

        Category categoryConversion = utils.convertFromDto(categoryDto, Category.class);
        Category categoryInDb = categoryRepository.save(categoryConversion);
        return utils.convertToDto(categoryInDb, CategoryDto.class);
    }

    @Transactional
    @Override
    public Message update(CategoryDto categoryDto, Integer id) throws CouponSystemException {
        if (!categoryRepository.existsById(id)) {
            throw new CouponSystemException(CategoryException.CATEGORY_NOT_EXISTS);
        }

        if (categoryDto.getId() != id) {
            categoryDto.setId(id);
        }

        Category category = utils.convertFromDto(categoryDto, Category.class);
        Category categoryInDb = categoryRepository.save(category);
        return Message.builder().code(CategoryMessage.CATEGORY_UPDATED.getCode())
                .message(CategoryMessage.CATEGORY_UPDATED.getMessage()).data(
                        utils.convertToDto(categoryInDb, CategoryDto.class)).build();
    }

    @Transactional
    @Override
    public Message delete(Integer id) throws CouponSystemException {
        Category category = categoryRepository.findById(id).orElseThrow(() ->
                new CouponSystemException(CategoryException.CATEGORY_NOT_EXISTS));

        CategoryDto categoryDeletionToReturn = utils.convertToDto(category, CategoryDto.class);
        categoryRepository.deleteById(id);

        return Message.builder().code(CategoryMessage.CATEGORY_DELETED.getCode())
                .message(CategoryMessage.CATEGORY_DELETED.getMessage()).data(categoryDeletionToReturn).build();
    }

    @Override
    public List<CategoryDto> getAll() throws CouponSystemException {
        return utils.convertFromEntityListToDto(categoryRepository.findAll(), CategoryDto.class);
    }

    @Override
    public CategoryDto getSingle(Integer integer) throws CouponSystemException {
        Category category = categoryRepository.findById(integer).orElseThrow(() ->
                new CouponSystemException(CategoryException.CATEGORY_NOT_EXISTS));
        return utils.convertToDto(category, CategoryDto.class);
    }

    @Override
    public boolean isExists(Integer integer) {
        return categoryRepository.existsById(integer);
    }

    @Override
    public boolean isCategoryExists(CategoryDto categoryDto) {
        return categoryRepository.existsByIdAndName(categoryDto.getId(), categoryDto.getName());
    }

    @Override
    public Message isCategoryExistsByName(String name) {
        boolean exists = categoryRepository.existsByName(name);
        List<Object> booleanAndObject = new ArrayList<>();
        if (exists) {
            booleanAndObject.add(true);
            booleanAndObject.add(categoryRepository.findByName(name));
            return Message.builder()
                    .code(CategoryMessage.CATEGORY_EXISTS_BY_NAME.getCode())
                    .message(CategoryMessage.CATEGORY_EXISTS_BY_NAME.getMessage())
                    .data(booleanAndObject)
                    .build();
        } else {
            return Message.builder()
                    .code(CategoryMessage.CATEGORY_NOT_EXISTS_BY_NAME.getCode())
                    .message(CategoryMessage.CATEGORY_NOT_EXISTS_BY_NAME.getMessage())
                    .data(false)
                    .build();
        }
    }


}

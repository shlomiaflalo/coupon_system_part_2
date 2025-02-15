package com.johnbryce.coupon_system_part_2.services.category;

import com.johnbryce.coupon_system_part_2.app_messages.Message;
import com.johnbryce.coupon_system_part_2.dto.CategoryDto;
import com.johnbryce.coupon_system_part_2.exceptions.CouponSystemException;
import com.johnbryce.coupon_system_part_2.services.TableService;

public interface CategoryService extends TableService<CategoryDto,Integer>  {
    CategoryDto getRandomCategory() throws CouponSystemException;
    Message isCategoryExistsByIdForController(int id) throws CouponSystemException;
    Message isCategoryExistsByName(String name);
    boolean isCategoryExists(CategoryDto categoryDto);

}

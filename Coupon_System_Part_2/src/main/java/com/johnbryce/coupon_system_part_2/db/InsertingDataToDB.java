package com.johnbryce.coupon_system_part_2.db;

import com.johnbryce.coupon_system_part_2.dto.CategoryDto;
import com.johnbryce.coupon_system_part_2.dto.CompanyDto;
import com.johnbryce.coupon_system_part_2.dto.CouponDto;
import com.johnbryce.coupon_system_part_2.dto.CustomerDto;
import com.johnbryce.coupon_system_part_2.services.category.CategoryService;
import com.johnbryce.coupon_system_part_2.services.company.CompanyService;
import com.johnbryce.coupon_system_part_2.services.coupon.CouponService;
import com.johnbryce.coupon_system_part_2.services.customer.CustomerService;
import com.johnbryce.coupon_system_part_2.services.purchase.PurchaseService;
import com.johnbryce.coupon_system_part_2.utils.CompanyGenerate;
import com.johnbryce.coupon_system_part_2.utils.DataGenerator;
import com.johnbryce.coupon_system_part_2.utils.UserGenerate;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

@Component
@Order(1)
@RequiredArgsConstructor
public class InsertingDataToDB implements CommandLineRunner {

    private final CompanyService companyService;
    private final CustomerService customerService;
    private final CouponService couponService;
    private final PurchaseService purchaseService;
    private final CategoryService categoryService;

    private static final int MAX_PURCHASES = 5;

    private void generateDummyCompanies() throws Exception {

        int sizeOfRandomCompanies = ThreadLocalRandom.current().nextInt(10,
                15);

        List<CompanyGenerate> companies =
                DataGenerator.companyEmailNameListNoDuplicate(sizeOfRandomCompanies);

        for (int i = 0; i < sizeOfRandomCompanies; i++) {
            String name = companies.get(i).getName();
            String email = companies.get(i).getEmail();

            CompanyDto companyDto = CompanyDto.builder()
                    .name(name).
                    email(email).
                    password(DataGenerator.randomPassword()).build();
            companyService.add(companyDto);
        }
    }

    private void generateDummyCoupons() throws Exception {
        int randomCouponsLength = ThreadLocalRandom.current().nextInt(10,
                15);
        for (int i = 0; i < randomCouponsLength; i++) {

            int couponsAmount = ThreadLocalRandom.current().nextInt(10, 500);
            double priceForCoupon = ThreadLocalRandom.current().nextInt(50, 700);
            int plusDaysRandom = ThreadLocalRandom.current().nextInt(5, 15);
            CompanyDto companyDto = companyService.getRandomCompany();
            CategoryDto categoryDto = categoryService.getRandomCategory();

            CouponDto couponDto = CouponDto.builder().
                    company(companyDto).
                    category(categoryDto).
                    title(DataGenerator.titleForCoupon()).
                    description(DataGenerator.descriptionForCoupon()).
                    startDate(LocalDate.now()).
                    endDate(LocalDate.now().plusDays(plusDaysRandom)).
                    amount(couponsAmount).
                    price(priceForCoupon).
                    image("Image " + i).build();
            couponService.add(couponDto);
        }
    }

    private void generateDummyCustomers() throws Exception {
        int randomCustomersLength = ThreadLocalRandom.current().nextInt(10,
                15);

        List<UserGenerate> userGenerates =
                DataGenerator.emailFirstLastNameListNoDuplicate(randomCustomersLength);

        for (int i = 0; i < randomCustomersLength; i++) {

            String firstName = userGenerates.get(i).getFirstName();
            String lastName = userGenerates.get(i).getLastName();
            String email = userGenerates.get(i).getEmail();

            CustomerDto customerDto = CustomerDto.builder().
                    firstName(firstName).
                    lastName(lastName).
                    email(email).
                    password(DataGenerator.randomPassword()).build();
            customerService.add(customerDto);
        }
    }


    public void initializationCategories() throws Exception {
        for (String category : DataGenerator.getCategoriesForCategoriesEntity()) {
            CategoryDto categoryDto = CategoryDto.builder().
                    name(category.toLowerCase()).build();
            categoryService.add(categoryDto);
        }
    }

    private void generateDummyPurchases() {
        for (int i = 1, j = 1; i <= MAX_PURCHASES && j <= MAX_PURCHASES; i++, j++) {
            //i + j is for the customerId & couponId
            //in the purchaseCoupon method. i-> customerId j-> couponId
            try {
                purchaseService.purchaseCoupon(i, j);
            } catch (Exception ignored) {
            }

        }
    }

    public void initializeAllData() throws Exception {
        initializationCategories();
        generateDummyCompanies();
        generateDummyCoupons();
        generateDummyCustomers();
        generateDummyPurchases();
    }

    @Override
    //Will run first by order(1)
    public void run(String... args) throws Exception {
        initializeAllData();
    }
}

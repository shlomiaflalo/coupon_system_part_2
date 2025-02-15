package com.johnbryce.coupon_system_part_2.controllers;

import com.johnbryce.coupon_system_part_2.app_messages.Message;
import com.johnbryce.coupon_system_part_2.dto.CompanyDto;
import com.johnbryce.coupon_system_part_2.exceptions.CouponSystemException;
import com.johnbryce.coupon_system_part_2.services.company.CompanyService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.sql.SQLException;
import java.util.List;

@RestController
@RequestMapping("api/company")
@RequiredArgsConstructor
public class CompanyController {

    private final CompanyService companyService;


    @PostMapping
    public CompanyDto add(@RequestBody CompanyDto companyDto)
            throws Exception {
        return companyService.add(companyDto);
    }


    @GetMapping("/getRandomCompany")
    public CompanyDto getRandomCompany() throws CouponSystemException {
        return companyService.getRandomCompany();
    }


    @GetMapping("/getSingleByEmail")
    public CompanyDto getSingleByEmail(@RequestParam("email") String email) throws CouponSystemException {
        return companyService.getSingleByEmail(email);
    }


    @GetMapping("/getFirstCompanyRecord")
    public CompanyDto getFirstCompanyRecord() throws CouponSystemException {
        return companyService.getFirstCompanyRecord();
    }


    @GetMapping("/getLastCompanyRecord")
    public CompanyDto getLastCompanyRecord() throws CouponSystemException {
        return companyService.getLastCompanyRecord();
    }


    @GetMapping("/{companyId}/isCompanyEmailExistsExclude")
    public Message isCompanyEmailExistsExclude
            (@RequestParam("email") String email, @PathVariable("companyId") int companyId) throws CouponSystemException {
        return companyService.isCompanyEmailExistsExclude(email, companyId);
    }


    @GetMapping("/isCompanyExistsByEmailAndPassword")
    public Message isCompanyExistsByEmailAndPassword
            (@RequestParam("email") String email, @RequestParam("password") String password) throws CouponSystemException {
        return companyService.isCompanyExistsByEmailAndPassword(email, password);
    }


    @GetMapping("/isCompanyExistsByName")
    public Message isCompanyExistsByName(@RequestParam("name") String name) throws CouponSystemException {
        return companyService.isCompanyExistsByName(name);
    }


    @GetMapping("/isCompanyExistsByEmail")
    public Message isCompanyExistsByEmail(@RequestParam("email") String email) throws CouponSystemException {
        return companyService.isCompanyExistsByEmail(email);
    }


    @GetMapping("/{companyId}/isCompanyExistsByNameAndId")
    public Message isCompanyExistsByNameAndId
            (@RequestParam("name") String name, @PathVariable("companyId") int companyId) throws CouponSystemException {
        return companyService.isCompanyExistsByNameAndId(name, companyId);
    }


    @GetMapping("/isCompanyExistsByIdForController/{companyId}")
    public Message isCompanyExistsByIdForController(@PathVariable("companyId") int companyId) throws CouponSystemException {
        return companyService.isCompanyExistsByIdForController(companyId);
    }


    @PutMapping("/{companyId}")
    public Message update(@RequestBody CompanyDto companyDto, @PathVariable("companyId") Integer companyId) throws Exception {
        return companyService.update(companyDto, companyId);
    }


    @DeleteMapping("/{companyId}")
    public Message delete(@PathVariable("companyId") Integer companyId) throws Exception {
        return companyService.delete(companyId);
    }


    @GetMapping("/all")
    public List<CompanyDto> getAll() throws CouponSystemException {
        return companyService.getAll();
    }


    @GetMapping("/{companyId}")
    public CompanyDto getSingle(@PathVariable("companyId") int companyId) throws CouponSystemException, SQLException {
        return companyService.getCompanyDetails(companyId);
    }

}

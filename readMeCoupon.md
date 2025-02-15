# Coupon System - Part 2

## Project Overview
The Coupon System is a web application designed to manage coupons, users, and companies. This system provides API endpoints for handling users, companies, and coupon data efficiently.

## Utility Classes
### 1. CompanyGenerate
- A simple data class representing a company.
- Uses Lombok annotations (`@Data`, `@Builder`, etc.).
- Fields:
    - `String name`: The company's name.
    - `String email`: The company's email.

### 2. UserGenerate
- A data class for users.
- Fields:
    - `String firstName`
    - `String lastName`
    - `String email`

### 3. EntityBase
- An **abstract class** used as a marker for entities.
- Ensures only entities (database models) can be converted to DTOs using the `Utils` class.

### 4. Utils (Utility Class)
- Provides common operations such as object conversion and HTTP requests.
- Uses **Spring's RestTemplate** for API interactions.
- Key Methods:
    - **DTO & Entity Conversion:**
        - `convertToDto(entity, dtoClass)`: Converts an entity to a DTO.
        - `convertFromDto(dto, entityClass)`: Converts a DTO to an entity.
        - `convertFromDtoToEntityList(dtoList, entityClass)`: Converts a list of DTOs to entities.
        - `convertFromEntityListToDto(entityList, dtoClass)`: Converts a list of entities to DTOs.
    - **HTTP Request Methods:**
        - `post(responseType, url, path, object)`: Sends a POST request.
        - `postWithMessageResponse(url, path, object)`: Sends a POST request and gets a message response.
        - `updateWithPath(responseType, url, path, object, params)`: Sends a PUT request.
        - `get(url, path, returnType)`: Sends a GET request.
        - `delete(responseType, url, path, params)`: Sends a DELETE request.

### 5. DataGenerator
- Uses **Java Faker** to generate random data.
- Key Methods:
    - `getCategoriesForCategoriesEntity()`: Generates 22 unique random categories.
    - `getRandomCategory()`: Gets a random category.
    - `randomPassword()`: Generates a random password.
    - `emailFirstLastNameListNoDuplicate(n)`: Generates a list of unique users.
    - `companyEmailNameListNoDuplicate(n)`: Generates a list of unique companies.
    - `titleForCoupon()`: Generates a random coupon title.
    - `descriptionForCoupon()`: Generates a random coupon description.
    - `belongTo()`: Generates a random owner name.
    - `yearGenerator()`: Generates a random past year (10 to 50 years ago).

## Summary
- **`Utils`**: Core utility class for conversions and HTTP calls.
- **`CompanyGenerate` & `UserGenerate`**: Data classes for generating test users & companies.
- **`DataGenerator`**: Generates fake data for testing.
- **`EntityBase`**: Marker class ensuring DTO conversions work properly.

This README provides an overview of the utility classes in the Coupon System project. Let us know if additional explanations are needed for controllers, services, or other components!


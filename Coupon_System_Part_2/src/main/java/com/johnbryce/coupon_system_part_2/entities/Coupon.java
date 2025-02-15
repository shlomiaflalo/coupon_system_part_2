package com.johnbryce.coupon_system_part_2.entities;

import com.johnbryce.coupon_system_part_2.utils.EntityBase;
import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.springframework.lang.NonNull;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@EqualsAndHashCode(callSuper = false)
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "coupons")
public class Coupon extends EntityBase {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne
    @JoinColumn(nullable = false, name = "company_id")
    private Company company;
    @ManyToOne
    @JoinColumn(nullable = false, name = "category_id")
    private Category category;
    @OneToMany(mappedBy = "coupon", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Purchase> purchases;
    @NonNull
    @Size(min = 3, max = 20)
    private String title;
    @NonNull
    @Size(min = 3, max = 100)
    private String description;
    private LocalDate startDate;
    private LocalDate endDate;
    @Min(0)
    private int amount;
    @DecimalMin("0.0")
    private double price;
    private String image;

    private int purchased;

    @Column(updatable = false, nullable = false, name = "Created_at")
    @Builder.Default
    @Setter(AccessLevel.NONE)
    private LocalDateTime createdAt = LocalDateTime.now();

    @Column(nullable = false, name = "Updated_at")
    @Builder.Default
    @Setter(AccessLevel.NONE)
    private LocalDateTime updatedAt = LocalDateTime.now();

}

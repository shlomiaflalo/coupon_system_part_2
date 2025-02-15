package com.johnbryce.coupon_system_part_2.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.johnbryce.coupon_system_part_2.utils.DataGenerator;
import com.johnbryce.coupon_system_part_2.utils.EntityBase;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.lang.NonNull;


@EqualsAndHashCode(callSuper = false)
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "companies")
@Builder
public class Company extends EntityBase {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @NonNull
    @Size(min = 1, max = 20)
    private String name;
    @NonNull
    @Email(message = "Email is not valid", regexp =
            "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@" +
                    "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$")
    private String email;
    @NonNull
    @Size(min = 5, max = 20)
    private String password;

    @ToString.Exclude
    @JsonIgnore
    @OneToMany(mappedBy = "company", cascade = CascadeType.ALL, orphanRemoval = true)
    List<Coupon> coupons;

    @Builder.Default
    @Column(updatable = false, nullable = false, name = "Founded_on_year")
    private int yearCreation = DataGenerator.yearGenerator();

    @Builder.Default
    @Column(updatable = false, nullable = false, name = "Belong_to")
    private String belongTo = DataGenerator.belongTo();

    @Column(updatable = false, nullable = false, name = "Created_at")
    @Builder.Default
    @Setter(AccessLevel.NONE)
    private LocalDateTime createdAt = LocalDateTime.now();

    @Column(nullable = false, name = "Updated_at")
    @Builder.Default
    @Setter(AccessLevel.NONE)
    private LocalDateTime updatedAt = LocalDateTime.now();
}

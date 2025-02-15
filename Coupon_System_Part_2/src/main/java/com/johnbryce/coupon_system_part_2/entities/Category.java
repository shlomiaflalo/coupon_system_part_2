package com.johnbryce.coupon_system_part_2.entities;

import com.johnbryce.coupon_system_part_2.utils.DataGenerator;
import com.johnbryce.coupon_system_part_2.utils.EntityBase;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.lang.NonNull;

import java.time.LocalDateTime;


@EqualsAndHashCode(callSuper = false)
@Entity
@Table(name = "categories")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Category extends EntityBase {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @NonNull
    private String name;

    @Column(updatable = false, nullable = false, name = "Belong_to")
    @Builder.Default
    private String categoryBelongTo = DataGenerator.belongTo();

    @Column(updatable = false, nullable = false, name = "Created_at")
    @Builder.Default
    @Setter(AccessLevel.NONE)
    private LocalDateTime createdAt = LocalDateTime.now();

    @Column(nullable = false, name = "Updated_at")
    @Builder.Default
    @Setter(AccessLevel.NONE)
    private LocalDateTime updatedAt = LocalDateTime.now();

}
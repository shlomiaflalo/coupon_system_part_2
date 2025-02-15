package com.johnbryce.coupon_system_part_2.entities;

import com.johnbryce.coupon_system_part_2.utils.EntityBase;
import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.time.LocalDateTime;

@EqualsAndHashCode(callSuper = false)
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "purchases")
@Builder
public class Purchase extends EntityBase {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne
    @JoinColumn(nullable = false)
    private Customer customer;

    @ManyToOne
    @JoinColumn(nullable = false)
    private Coupon coupon;

    @Builder.Default
    @Setter(AccessLevel.NONE)
    private boolean approved = true;

    @Column(name = "Purchased_on_web")
    @Builder.Default
    @Setter(AccessLevel.NONE)
    private boolean purchasedOnline = true;

    @Size(min = 1, max = 5)
    private int review;

    @Column(updatable = false, nullable = false, name = "Created_at")
    @Builder.Default
    @Setter(AccessLevel.NONE)
    private LocalDateTime createdAt = LocalDateTime.now();

    @Column(nullable = false, name = "Updated_at")
    @Builder.Default
    @Setter(AccessLevel.NONE)
    private LocalDateTime updatedAt = LocalDateTime.now();


}

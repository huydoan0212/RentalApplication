package com.rental.rentalapplication.entity;

import com.rental.rentalapplication.service.slug.SlugService;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class House {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    String id;
    String name;
    int pricePerDay;
    String description;
    String status;
    String nameSlug;

    @PrePersist
    @PreUpdate
    private void prepareData() {
        this.nameSlug = SlugService.toSlug(this.name);
    }
}

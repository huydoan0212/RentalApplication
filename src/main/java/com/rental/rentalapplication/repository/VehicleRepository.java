package com.rental.rentalapplication.repository;

import com.rental.rentalapplication.entity.Vehicle;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface VehicleRepository extends JpaRepository<Vehicle, String> {
    boolean existsByName(String name);

    Optional<Vehicle> findByName(String name);

    void deleteByName(String name);

    Optional<Vehicle> findByNameSlug(String nameSlug);
}

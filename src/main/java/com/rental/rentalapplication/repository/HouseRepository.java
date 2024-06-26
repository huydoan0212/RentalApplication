package com.rental.rentalapplication.repository;

import com.rental.rentalapplication.entity.House;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface HouseRepository extends JpaRepository<House, String> {
    @Query("select (count(h) > 0) from House h where h.name = ?1")
    boolean existsByName(String name);

    Optional<House> findByName(String name);

    void deleteById(String id);

    Optional<House> findByNameSlug(String name);
}

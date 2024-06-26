package com.rental.rentalapplication.repository;

import com.rental.rentalapplication.entity.Rent;
import com.rental.rentalapplication.entity.User;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RentRepository extends JpaRepository<Rent, String> {
    List<Rent> findAllByUser(User user);

    @Query("select r from Rent r where r.endTime < CURRENT_TIMESTAMP")
    List<Rent> findAllExpiredRent();

}

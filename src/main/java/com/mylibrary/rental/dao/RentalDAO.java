package com.mylibrary.rental.dao;

import com.mylibrary.rental.domain.Rental;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

/**
 * JPA Repository for {@link Rental} entity
 *
 * @author Bharadwaj Adepu
 */
public interface RentalDAO extends JpaRepository<Rental, String> {

    /**
     * Loads Rental by id.
     *
     * @return {@link Optional <Rental>}
     */
    Optional<Rental> findRentalByRentalId(int id);
    
    @Query(value = "select r from Rental r where r.bookId = :bookId",
    	    countQuery = "select count(r.rentalId) from Rental r where r.bookId = :bookId")
    Page<Rental> findByBookId(@Param("bookId") String bookId, Pageable pageable);
    
    @Query(value = "select r from Rental r where r.userId = :userId",
    	    countQuery = "select count(r.rentalId) from Rental r where r.userId = :userId")
    Page<Rental> findByUserId(@Param("userId") int userId, Pageable pageable);
    
    @Query(value = "select r from Rental r where r.userId = :userId and r.bookId = :bookId",
    	    countQuery = "select count(r.rentalId) from Rental r where r.userId = :userId and r.bookId = :bookId" )
    Page<Rental> findByUserIdAndBookId(@Param("userId") int userId, @Param("bookId") String bookId, Pageable pageable);  
}

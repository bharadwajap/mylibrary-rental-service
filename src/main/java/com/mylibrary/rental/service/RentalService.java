package com.mylibrary.rental.service;

import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.mylibrary.rental.dao.RentalDAO;
import com.mylibrary.rental.domain.Rental;
import com.mylibrary.rental.dto.RentalResource;
import com.mylibrary.rental.exceptions.ResourceNotFoundException;
import com.mylibrary.rental.util.MapperUtils;

/**
 * Service class for the Rental API
 *
 * @author Bharadwaj Adepu
 */
@Service
public class RentalService {

    private RentalDAO rentalDAO;

    /**
     * Instantiates a new {@link RentalService}
     *
     * @param rentalDAO the {@link RentalDAO}
     */
    @Autowired
    public RentalService(RentalDAO rentalDAO) {
        this.rentalDAO = rentalDAO;
    }

    /**
     * Allows to retrieve the rental by ISBN
     *
     * @param isbn rental identifier
     * @return {@link com.mylibrary.rental.dto.RentalResource}
     */
    public RentalResource getRentalById(int id) {
        Rental rental = this.rentalDAO.findRentalByRentalId(id).orElseThrow(() -> new ResourceNotFoundException("Rental", id));
        return MapperUtils.mapTo(rental, RentalResource.class);
    }

    /**
     * Gets a page with rentals.
     *
     * @param pageable the {@link Pageable} pageable
     * @return page of rentals
     */
    public Page<RentalResource> getRentals(String bookId, int userId, Pageable pageable) {
    	
    	Page<RentalResource> rentalResourcesPage = null;
    	
    	if(!StringUtils.isEmpty(bookId) && userId != 0) {
    		rentalResourcesPage = convert(this.rentalDAO.findByUserIdAndBookId(userId, bookId, pageable), pageable);
    	}else if(!StringUtils.isEmpty(bookId) && userId == 0) {
    		rentalResourcesPage = convert(this.rentalDAO.findByBookId(bookId, pageable), pageable);
    	}else if(StringUtils.isEmpty(bookId) && userId != 0) {
    		rentalResourcesPage = convert(this.rentalDAO.findByUserId(userId, pageable), pageable);
    	}else {
    		rentalResourcesPage = convert(this.rentalDAO.findAll(pageable), pageable);
    	}
        return rentalResourcesPage;
    }

    /**
     * Gets a page with rentals for a particular Book.
     *
     * @param pageable the {@link Pageable} pageable
     * @param bookId the rentalId to filter
     * @return page of rentals
     */
    public Page<RentalResource> getRentalsByBookId(String bookId, Pageable pageable) {
        return convert(this.rentalDAO.findByBookId(bookId, pageable), pageable);
    }
    
    /**
     * Gets a page with rentals for a particular User.
     *
     * @param pageable the {@link Pageable} pageable
     * @param userId the userId to filter
     * @return page of rentals
     */
    public Page<RentalResource> getRentalsByUserId(int userId, Pageable pageable) {
        return convert(this.rentalDAO.findByUserId(userId, pageable), pageable);
    }
    
    /**
     * Creates a rental.
     *
     * @param rentalResource the {@link RentalResource}
     * @return the {@link RentalResource}
     */
    public RentalResource createRental(RentalResource rentalResource) {
        final Rental rental = new Rental(rentalResource.getUserId(), rentalResource.getBookId(), 
        		rentalResource.getIssueTime(), rentalResource.getReturnTime(), 
        		rentalResource.getReturnDuration(), rentalResource.getLateFee());
        
        return convert(this.rentalDAO.save(rental));
    }

    /**
     * Updates a rental.
     *
     * @param rentalResource the {@link RentalResource}
     * @return the {@link RentalResource}
     */
    public RentalResource updateRental(RentalResource rentalResource) {
        Rental rental = this.rentalDAO.findRentalByRentalId(rentalResource.getRentalId())
        		.orElseThrow(() -> new ResourceNotFoundException("Rental", rentalResource.getRentalId()));
        rental.setReturnTime(rentalResource.getReturnTime());
        rental.setLateFee(rentalResource.getLateFee());
        return convert(this.rentalDAO.save(rental));
    }
    
    private Page<RentalResource> convert(Page<Rental> page, Pageable pageable) {
        return new PageImpl<>(page.getContent().stream()
                .map(this::convert).collect(Collectors.toList()), pageable, page.getTotalElements());
    }

    private RentalResource convert(Rental rental) {
        return MapperUtils.mapTo(rental, RentalResource.class);
    }
}

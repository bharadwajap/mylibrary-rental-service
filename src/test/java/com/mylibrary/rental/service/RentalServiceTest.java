package com.mylibrary.rental.service;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Optional;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import com.mylibrary.rental.dao.RentalDAO;
import com.mylibrary.rental.domain.Rental;
import com.mylibrary.rental.dto.RentalResource;
import com.mylibrary.rental.exceptions.ConstraintViolationException;
import com.mylibrary.rental.exceptions.ResourceNotFoundException;

/**
 * @author Bharadwaj Adepu
 */
public class RentalServiceTest {

    private RentalService rentalService;

    private RentalDAO rentalDAO;

    @Before
    public void setUp() throws Exception {
        this.rentalDAO = Mockito.mock(RentalDAO.class);
        this.rentalService = new RentalService(this.rentalDAO);
    }

    Rental rental = new Rental(1, 2, "32322", LocalDateTime.now(), LocalDateTime.now(), 1, 0.0);
    RentalResource rentalResource = new RentalResource(2, "32322", LocalDateTime.now(), LocalDateTime.now(), 1, 0.0);
    
    @Test
    public void getRentalById() throws Exception {
        when(this.rentalDAO.findRentalByRentalId(1)).thenReturn(Optional.of(rental));

        RentalResource rentalResource = this.rentalService.getRentalById(1);
        Assert.assertEquals("1", rentalResource.getRentalId());
        Assert.assertEquals("42343", rentalResource.getUserId());

        verify(this.rentalDAO, times(1)).findRentalByRentalId(1);
        verifyNoMoreInteractions(this.rentalDAO);
    }

    @Test
    public void getRentalById_NO_RESOURCE_FOUND() throws Exception {
        when(this.rentalDAO.findRentalByRentalId(1)).thenReturn(Optional.empty());
        try {
            this.rentalService.getRentalById(1);
        } catch (Exception e) {
            Assert.assertTrue(e instanceof ResourceNotFoundException);
        } finally {
            verify(this.rentalDAO, times(1)).findRentalByRentalId(1);
            verifyNoMoreInteractions(this.rentalDAO);
        }
    }

    @Test
    public void testGetRentals() {
        Pageable pageable = new PageRequest(0, 10, null);
        when(this.rentalDAO.findAll(pageable)).thenReturn(new PageImpl<>(Collections.singletonList(rental)));

        Page<RentalResource> page = this.rentalService.getRentals("", 0, pageable);
        Assert.assertEquals(1, page.getTotalElements());
        Assert.assertEquals(1, page.getTotalPages());

        Assert.assertEquals("1", page.getContent().get(0).getRentalId());
        Assert.assertEquals("252344", page.getContent().get(0).getUserId());

        verify(this.rentalDAO, times(1)).findAll(pageable);
        verifyNoMoreInteractions(this.rentalDAO);
    }

    @Test
    public void testGetRentals_EMPTY_RESULT_SET() {
        Pageable pageable = new PageRequest(0, 10, null);
        when(this.rentalDAO.findAll(pageable)).thenReturn(new PageImpl<>(new ArrayList<>()));

        Page<RentalResource> page = this.rentalService.getRentals("", 0,pageable);
        Assert.assertEquals(0, page.getTotalElements());
        Assert.assertEquals(0, page.getTotalPages());

        verify(this.rentalDAO, times(1)).findAll(pageable);
        verifyNoMoreInteractions(this.rentalDAO);
    }

    @Test
    public void testCreateRental() {
        when(this.rentalDAO.findRentalByRentalId(1)).thenReturn(Optional.empty());

        this.rentalService.createRental(rentalResource);

        verify(this.rentalDAO, times(1)).findRentalByRentalId(1);
        verify(this.rentalDAO, times(1)).save(Mockito.any(Rental.class));
        verifyNoMoreInteractions(this.rentalDAO);
    }

    @Test
    public void testCreateRental_ALREADY_EXISTS() {
        when(this.rentalDAO.findRentalByRentalId(1)).thenReturn(Optional.of(rental));

        try {
            this.rentalService.createRental(rentalResource);
        } catch (Exception e) {
            Assert.assertTrue(e instanceof ConstraintViolationException);
        } finally {
            verify(this.rentalDAO, times(1)).findRentalByRentalId(1);
            verifyNoMoreInteractions(this.rentalDAO);
        }
    }
}
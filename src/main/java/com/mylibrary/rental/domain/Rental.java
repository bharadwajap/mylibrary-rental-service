package com.mylibrary.rental.domain;

import java.io.Serializable;
import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import com.mylibrary.rental.config.LocalDateTimeConverter;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * Domain class which represent Rental entity in data storage
 *
 * @author Bharadwaj Adepu
 */
@AllArgsConstructor
@Entity
@Table(name = "rentals")
@Data
public class Rental implements Serializable {

    private static final long serialVersionUID = -1727518477635717091L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int rentalId;

    @Column(name = "user_id")
    private int userId;

    @Column(name = "book_id")
    private String bookId;
    
    @Convert(converter = LocalDateTimeConverter.class)
    @Column(name = "issue_time")
    private LocalDateTime issueTime;
    
    @Convert(converter = LocalDateTimeConverter.class)
    @Column(name = "return_time")
    private LocalDateTime returnTime;
    
    @Column(name = "return_duration")
    private int returnDuration ;
 
    @Column(name = "late_fee")
    private double lateFee ;
    
    @SuppressWarnings("unused")
    public Rental() {
    }
    
    public Rental(int userId, String bookId, 
    		LocalDateTime issueTime, LocalDateTime returnTime, 
    		int returnDuration, double lateFee) {
        super();
        this.userId = userId;
        this.bookId = bookId;
        this.issueTime = issueTime;
        this.returnTime = returnTime;
        this.returnDuration = returnDuration;
        this.lateFee = lateFee;
    }
}

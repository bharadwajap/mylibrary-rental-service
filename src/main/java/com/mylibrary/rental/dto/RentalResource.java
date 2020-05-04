package com.mylibrary.rental.dto;

import java.time.LocalDateTime;

import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.Range;
import org.springframework.hateoas.ResourceSupport;
import org.springframework.hateoas.core.Relation;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * Resource class for Rental entity
 *
 * @author Bharadwaj Adepu
 */
@Relation(value = "book", collectionRelation = "books")
@Getter
@Setter
@ApiModel
public class RentalResource extends ResourceSupport {

    @NotNull
    private int rentalId;

    @ApiModelProperty(value = "Library ID number of the User", required = true, example = "112")
    @NotNull
    private int userId;

    @ApiModelProperty(value = "ISBN of the Book", required = true, example = "33253523")
    @NotNull
    private String bookId;

    @ApiModelProperty(value = "Date and Time of Issue", required = true, example = "12/03/2020 14:35:34")
    @NotNull
    private LocalDateTime issueTime;

    @ApiModelProperty(value = "Date and Time of Return", required = true, example = "12/03/2020 14:35:34")
    private LocalDateTime returnTime;
    
    @ApiModelProperty(value = "Duration of Rental", required = true, example = "5")
    @NotNull
    @Range(min = 1, max = 5, message = "Rental duration should be between 1 and 5 days (inclusive) only.")
    private int returnDuration ;
 
    @ApiModelProperty(value = "Late fee for delayed return", required = true, example = "100")
    @NotNull
    private double lateFee ;
    
    @JsonCreator
    public RentalResource(@JsonProperty("userId") int userId, @JsonProperty("bookId") String bookId, 
    		@JsonProperty("issueTime") LocalDateTime issueTime, @JsonProperty("returnTime") LocalDateTime returnTime, 
    		@JsonProperty("returnDuration") int returnDuration, @JsonProperty("lateFee") double lateFee) {
        super();
        this.userId = userId;
        this.bookId = bookId;
        this.issueTime = issueTime;
        this.returnTime = returnTime;
        this.returnDuration = returnDuration;
        this.lateFee = lateFee;
    }

    public RentalResource() {}
    
}

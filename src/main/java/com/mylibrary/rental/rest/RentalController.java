package com.mylibrary.rental.rest;

import static org.springframework.hateoas.MediaTypes.HAL_JSON_VALUE;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.HateoasPageableHandlerMethodArgumentResolver;
import org.springframework.data.web.PageableDefault;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.ExposesResourceFor;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.MediaTypes;
import org.springframework.hateoas.PagedResources;
import org.springframework.hateoas.mvc.ControllerLinkBuilder;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import com.mylibrary.rental.dto.ProblemDetail;
import com.mylibrary.rental.dto.RentalResource;
import com.mylibrary.rental.rest.assemblers.RentalResourceAssembler;
import com.mylibrary.rental.service.RentalService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

/**
 * Controller for the API of 'Rental' resource.
 *
 * @author Bharadwaj Adepu
 */
@RestController
@ExposesResourceFor(RentalResource.class)
@RequestMapping("/mylibrary/rentals")
@Api(value = "Rentals", description = "Rental API")
public class RentalController {

    private final RentalService rentalService;

    private final RentalResourceAssembler rentalResourceAssembler;

    /**
     * Instantiates new instance of {@link RentalService}
     *
     * @param rentalService {@link RentalService}
     */
    @Autowired
    public RentalController(RentalService rentalService, RentalResourceAssembler rentalResourceAssembler) {
        this.rentalService = rentalService;
        this.rentalResourceAssembler = rentalResourceAssembler;
    }

    @ApiOperation(
            value = "Retrieve a Rental resource by identifier",
            notes = "Make a GET request to retrieve a rental by a given identifier",
            response = RentalResource.class,
            httpMethod = "GET")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully retrieved", response = RentalResource.class),
            @ApiResponse(code = 401, message = "Unauthorized", response = ProblemDetail.class),
            @ApiResponse(code = 403, message = "This operation is forbidden for this user", response = ProblemDetail.class),
            @ApiResponse(code = 404, message = "Rental with the given identifier is not found", response = ProblemDetail.class),
            @ApiResponse(code = 500, message = "Unexpected Internal Error", response = ProblemDetail.class)})
    @RequestMapping(value = "/{rentalId}", method = RequestMethod.GET, produces = HAL_JSON_VALUE)
    public HttpEntity<RentalResource> getRentalById(@PathVariable(value = "rentalId") int rentalId) {
        RentalResource rental = this.rentalResourceAssembler.toResource(this.rentalService.getRentalById(rentalId));
        return ResponseEntity.ok(rental);
    }

    @ApiOperation(
            value = "Retrieve all Rentals",
            notes = "Make a GET request to retrieve a page of rentals",
            response = PagedResources.class,
            httpMethod = "GET")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "page", dataType = "int", paramType = "query",
                    value = "Results page you want to retrieve (0..N)"),
            @ApiImplicitParam(name = "size", dataType = "int", paramType = "query",
                    value = "Number of records per page."),
            @ApiImplicitParam(name = "sort", allowMultiple = true, dataType = "string", paramType = "query",
                    value = "Sorting criteria in the format: property(,asc|desc). " +
                            "Default sort order is ascending. " +
                            "Multiple sort criteria are supported.")
    })
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully retrieved", response = PagedResources.class),
            @ApiResponse(code = 401, message = "Unauthorized", response = ProblemDetail.class),
            @ApiResponse(code = 403, message = "This operation is forbidden for this user", response = ProblemDetail.class),
            @ApiResponse(code = 404, message = "Rental with the given identifier is not found", response = ProblemDetail.class),
            @ApiResponse(code = 500, message = "Unexpected Internal Error", response = ProblemDetail.class)})
    @RequestMapping(value = "", method = RequestMethod.GET, produces = MediaTypes.HAL_JSON_VALUE)
    public ResponseEntity<PagedResources<RentalResource>> getRentals(
    		@PageableDefault(sort = "issueTime", direction = Sort.Direction.DESC) final Pageable pageable,
    		final PagedResourcesAssembler<RentalResource> pagedResourcesAssembler,
    		@RequestParam(value = "userId", required = false, defaultValue = "0") Integer userId, @RequestParam(value = "bookId",required = false) String bookId) {

        return ResponseEntity.ok(pagedResourcesAssembler.toResource(this.rentalService.getRentals(bookId, userId, pageable),
                this.rentalResourceAssembler,
                enhanceSelfLink(pageable, linkTo(ControllerLinkBuilder.methodOn(RentalController.class)
                        .getRentals(pageable, pagedResourcesAssembler, userId, bookId)).withSelfRel())));
    }


    @ApiOperation(
            value = "Update a rental",
            notes = "Make a PUT request to update existing Rental",
            response = RentalResource.class,
            httpMethod = "PUT")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully updated", response = RentalResource.class),
            @ApiResponse(code = 401, message = "Unauthorized", response = ProblemDetail.class),
            @ApiResponse(code = 403, message = "This operation is forbidden for this user", response = ProblemDetail.class),
            @ApiResponse(code = 404, message = "Rental with the given identifier is not found", response = ProblemDetail.class),
            @ApiResponse(code = 500, message = "Unexpected Internal Error", response = ProblemDetail.class)})
    @RequestMapping(value = "", method = RequestMethod.PUT, produces = MediaTypes.HAL_JSON_VALUE)
    public HttpEntity<RentalResource> updateRental(@Validated @RequestBody final RentalResource rentalResource) {

        RentalResource rental = this.rentalResourceAssembler.toResource(this.rentalService.updateRental(rentalResource));

        return new ResponseEntity<>(rental, HttpStatus.OK);
    }
    
    @ApiOperation(
            value = "Create a rental",
            notes = "Make a POST request to register new rental",
            response = RentalResource.class,
            code = 201,
            httpMethod = "POST")
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Successfully created", response = RentalResource.class),
            @ApiResponse(code = 401, message = "Unauthorized", response = ProblemDetail.class),
            @ApiResponse(code = 403, message = "This operation is forbidden for this user", response = ProblemDetail.class),
            @ApiResponse(code = 404, message = "Rental with the given identifier is not found", response = ProblemDetail.class),
            @ApiResponse(code = 500, message = "Unexpected Internal Error", response = ProblemDetail.class)})
    @RequestMapping(value = "", method = RequestMethod.POST, produces = MediaTypes.HAL_JSON_VALUE)
    public HttpEntity<RentalResource> createRental(@Validated @RequestBody final RentalResource rentalResource) {

        RentalResource rental = this.rentalResourceAssembler.toResource(this.rentalService.createRental(rentalResource));

        return new ResponseEntity<>(rental, HttpStatus.CREATED);
    }

    private Link enhanceSelfLink(Pageable pageable, Link selfLink) {
        //this version of Spring has a bug, so selfLink must be enhanced with page, size and sort parameters
        UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(selfLink.expand().getHref());
        new HateoasPageableHandlerMethodArgumentResolver().enhance(builder, null, pageable);
        return new Link(builder.build().toString());
    }

}

package com.klikpeta.ordispatch.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.klikpeta.ordispatch.domain.Customer;
import com.klikpeta.ordispatch.domain.CustomerAddress;
import com.klikpeta.ordispatch.repository.CustomerAddressRepository;
import com.klikpeta.ordispatch.repository.CustomerRepository;
import com.klikpeta.ordispatch.web.rest.dto.CustomerAddressDTO;
import com.klikpeta.ordispatch.web.rest.mapper.CustomerAddressMapper;
import com.klikpeta.ordispatch.web.rest.util.HeaderUtil;
import com.klikpeta.ordispatch.web.rest.util.PaginationUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;
import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * REST controller for managing CustomerAddress.
 */
@RestController
@RequestMapping("/api/customers/")
public class CustomerAddressResource {

    private final Logger log = LoggerFactory.getLogger(CustomerAddressResource.class);

    @Inject
    private CustomerAddressRepository customerAddressRepository;

    @Inject
    private CustomerRepository customerRepository;
    
    @Inject
    private CustomerAddressMapper customerAddressMapper;

    /**
     * POST  /addresses -> Create a new customerAddress.
     */
    @RequestMapping(value = "/addresses",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<CustomerAddress> createCustomerAddress(@Valid @RequestBody CustomerAddressDTO customerDTO) throws URISyntaxException {
        log.debug("REST request to save CustomerAddress : {} to customerId {}", customerDTO);
        if (customerDTO.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("customerAddress", "idexists", "A new customerAddress cannot already have an ID")).body(null);
        }
//        Customer customer = customerRepository.findOne(customerId);
//        if(customer == null){
//            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
//        }
//        customerAddress.setCustomer(customer);
        CustomerAddress customerAddress= customerAddressMapper.customerAddressDTOToCustomerAddress(customerDTO);
        CustomerAddress result = customerAddressRepository.save(customerAddress);
        return ResponseEntity.created(new URI("/api/customers/addresses/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("customerAddress", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /addresses -> Updates an existing customerAddress.
     */
    @RequestMapping(value = "/addresses",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<CustomerAddress> updateCustomerAddress(@Valid @RequestBody CustomerAddressDTO customerDTO) throws URISyntaxException {
        log.debug("REST request to update CustomerAddress : {}", customerDTO);
        if (customerDTO.getId() == null) {
            return createCustomerAddress(customerDTO);
        }
//        Customer customer = customerRepository.findOne(customerId);
//        if(customer == null){
//            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
//        }
//        customerAddress.setCustomer(customer);
        CustomerAddress customerAddress= customerAddressMapper.customerAddressDTOToCustomerAddress(customerDTO);
        CustomerAddress result = customerAddressRepository.save(customerAddress);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("customerAddress", customerAddress.getId().toString()))
            .body(result);
    }

    /**
     * GET  /addresses -> get all the customeraddresses.
     */
    @RequestMapping(value = "/addresses",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<CustomerAddress>> getAllCustomeraddresses(Pageable pageable) throws URISyntaxException {
        log.debug("REST request to get all Customeraddresses by customerId");
        Page<CustomerAddress> page = customerAddressRepository.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/addresses");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);       
    }

    /**
     * GET  /addresses/:id -> get the "id" customerAddress.
     */
    @RequestMapping(value = "/addresses/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<CustomerAddressDTO> getCustomerAddress(@PathVariable Long id) {
        log.debug("REST request to get CustomerAddress : {}", id);
        CustomerAddress customerAddress = customerAddressRepository.findOne(id);
        CustomerAddressDTO customerAddressDTO= customerAddressMapper.customerAddressToCustomerAddressDTO(customerAddress);
        return Optional.ofNullable(customerAddressDTO)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /addresses/:id -> delete the "id" customerAddress.
     */
    @RequestMapping(value = "/addresses/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteCustomerAddress(@PathVariable Long id) {
        log.debug("REST request to delete CustomerAddress : {} of customerId", id);
        customerAddressRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("customerAddress", id.toString())).build();
    }
}

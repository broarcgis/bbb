package com.klikpeta.ordispatch.web.rest.mapper;

import com.klikpeta.ordispatch.domain.*;
import com.klikpeta.ordispatch.web.rest.dto.CustomerAddressDTO;
import com.klikpeta.ordispatch.web.rest.dto.CustomerDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity Customer and its DTO CustomerDTO.
 */
@Mapper(componentModel = "spring", uses = {})
public interface CustomerAddressMapper {

    @Mapping(source = "customer.id", target = "customerId")
    @Mapping(source = "vehicleClass.id", target = "vehicleClassId")
    CustomerAddressDTO customerAddressToCustomerAddressDTO(CustomerAddress customer);

    @Mapping(source = "customerId", target = "customer")
    @Mapping(source = "vehicleClassId", target = "vehicleClass")
    CustomerAddress customerAddressDTOToCustomerAddress(CustomerAddressDTO customerDTO);
    
    
    default Customer customerFromCustomerId(Long id) {
        if (id == null) {
            return null;
        }
        Customer customer = new Customer();
        customer.setId(id);
        return customer;
    }
  
    default VehicleClass VehicleClassFromVehicleClassId(Long id) {
        if (id == null) {
            return null;
        }
        VehicleClass vehicleClass = new VehicleClass();
        vehicleClass.setId(id);
        return vehicleClass;
    }

}

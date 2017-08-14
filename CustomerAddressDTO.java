package com.klikpeta.ordispatch.web.rest.dto;

import com.klikpeta.ordispatch.domain.*;
import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Date;
import java.util.Objects;

public class CustomerAddressDTO implements Serializable {

    /**
     * @return the vehicleClassId
     */
    public Long getVehicleClassId() {
        return vehicleClassId;
    }

    /**
     * @param vehicleClassId the vehicleClassId to set
     */
    public void setVehicleClassId(Long vehicleClassId) {
        this.vehicleClassId = vehicleClassId;
    }

    private Long id;
    private String name;
    private String description;
    private String address;
    private Double xcoord;
    private Double ycoord;
    private Boolean isMainAddress;
    private Boolean isDeliveryAddress;

    private Boolean isDeleted;
    private Long customerId;

    public Long getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Long customerId) {
        this.customerId = customerId;
    }
    private Long vehicleClassId;
    private String city;
    private String kelurahanCode;
    private String postcode;
    private Integer odt;
    private String maxTruck;
    private Integer cluster;
    private Date timeMax;
    private Double fixTime;
    private Double variableTime;
    private Double timeWindow;

    public Double getTimeWindow() {
        return timeWindow;
    }

    public void setTimeWindow(Double timeWindow) {
        this.timeWindow = timeWindow;
    }
        
    public Double getFixTime() {
        return fixTime;
    }

    public void setFixTime(Double fixTime) {
        this.fixTime = fixTime;
    }

    public Double getVariableTime() {
        return variableTime;
    }

    public void setVariableTime(Double variableTime) {
        this.variableTime = variableTime;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Double getXcoord() {
        return xcoord;
    }

    public void setXcoord(Double xcoord) {
        this.xcoord = xcoord;
    }

    public Double getYcoord() {
        return ycoord;
    }

    public void setYcoord(Double ycoord) {
        this.ycoord = ycoord;
    }

    public Boolean getIsMainAddress() {
        return isMainAddress;
    }

    public void setIsMainAddress(Boolean isMainAddress) {
        this.isMainAddress = isMainAddress;
    }

    public Boolean getIsDeliveryAddress() {
        return isDeliveryAddress;
    }

    public void setIsDeliveryAddress(Boolean isDeliveryAddress) {
        this.isDeliveryAddress = isDeliveryAddress;
    }

    public Boolean getIsDeleted() {
        return isDeleted;
    }

    public void setIsDeleted(Boolean isDeleted) {
        this.isDeleted = isDeleted;
    }


    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getKelurahanCode() {
        return kelurahanCode;
    }

    public void setKelurahanCode(String kelurahanCode) {
        this.kelurahanCode = kelurahanCode;
    }

    public String getPostcode() {
        return postcode;
    }

    public void setPostcode(String postcode) {
        this.postcode = postcode;
    }

    public Integer getOdt() {
        return odt;
    }

    public void setOdt(Integer odt) {
        this.odt = odt;
    }

    public String getMaxTruck() {
        return maxTruck;
    }

    public void setMaxTruck(String maxTruck) {
        this.maxTruck = maxTruck;
    }

    public Integer getCluster() {
        return cluster;
    }

    public void setCluster(Integer cluster) {
        this.cluster = cluster;
    }

    public Date getTimeMax() {
        return timeMax;
    }

    public void setTimeMax(Date timeMax) {
        this.timeMax = timeMax;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        CustomerAddressDTO customerAddress = (CustomerAddressDTO) o;
        return Objects.equals(id, customerAddress.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "CustomerAddress{" +
            "id=" + id +
            ", name='" + name + "'" +
            ", description='" + description + "'" +
            ", address='" + address + "'" +
            ", xcoord='" + xcoord + "'" +
            ", ycoord='" + ycoord + "'" +
            ", isMainAddress='" + isMainAddress + "'" +
            ", isDeliveryAddress='" + isDeliveryAddress + "'" +
            ", isDeleted='" + isDeleted + "'" +
            '}';
    }
}

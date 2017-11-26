package com.myapp.domainobject;

import com.myapp.domainvalue.EngineType;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.NotNull;
import java.time.ZonedDateTime;

@Entity
@Table(
        name = "car",
        uniqueConstraints = @UniqueConstraint(name = "uc_license_plate", columnNames = {"licenseplate"})
)
public class CarDO {

    @Id
    @GeneratedValue
    @Column(name = "CAR_ID")
    private Long id;

    @Column(nullable = false)
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private ZonedDateTime dateCreated = ZonedDateTime.now();

    @Column(nullable = false)
    @NotNull(message = "License plate can not be null!")
    private String licensePlate;

    @Column(nullable = false)
    @NotNull(message = "Seat count can not be null!")
    private int seatCount;

    @Column(nullable = false)
    private Boolean deleted = false;

    @Column
    private Boolean convertible = false;

    @Column
    private int rating;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private EngineType engineType;

    @Column(nullable = false)
    private String manufacturer;

    @OneToOne(fetch = FetchType.LAZY, mappedBy = "car")
    private DriverDO driver;

    public CarDO() {
    }

    public CarDO(String licensePlate, int seatCount, boolean convertible, int rating, EngineType engineType, String manufacturer) {
        this.licensePlate = licensePlate;
        this.seatCount = seatCount;
        this.convertible = convertible;
        this.rating = rating;
        this.engineType = engineType;
        this.manufacturer = manufacturer;
    }


    public Long getId() {
        return id;
    }

    public ZonedDateTime getDateCreated() {
        return dateCreated;
    }

    public String getLicensePlate() {
        return licensePlate;
    }

    public int getSeatCount() {
        return seatCount;
    }

    public int getRating() {
        return rating;
    }

    public EngineType getEngineType() {
        return engineType;
    }

    public String getManufacturer() {
        return manufacturer;
    }

    public DriverDO getDriver() {
        return driver;
    }

    public Boolean getDeleted() {
        return deleted;
    }

    public Boolean getConvertible() {
        return convertible;
    }

    public void setDeleted(Boolean deleted) {
        this.deleted = deleted;
    }

    public void setLicensePlate(String licensePlate) {
        this.licensePlate = licensePlate;
    }

    public void setSeatCount(int seatCount) {
        this.seatCount = seatCount;
    }

    public void isConvertible(Boolean convertible) {
        this.convertible = convertible;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    public void setEngineType(EngineType engineType) {
        this.engineType = engineType;
    }

    public void setManufacturer(String manufacturer) {
        this.manufacturer = manufacturer;
    }

    public void setDriver(DriverDO driver) {
        this.driver = driver;
    }

}

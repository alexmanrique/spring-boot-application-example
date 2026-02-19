package com.myapp.controller.mapper;

import com.myapp.datatransferobject.CarDTO;
import com.myapp.domainobject.CarDO;
import com.myapp.domainobject.DriverDO;
import com.myapp.domainvalue.EngineType;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

@RunWith(MockitoJUnitRunner.class)
public class CarMapperTest {

    @Test
    public void makeCarDTO_withDriver() {
        CarDO carDO = new CarDO("ABC123", 4, true, 9, EngineType.ELECTRIC, "TESLA");
        DriverDO driver = new DriverDO("driver1", "pass");
        driver.setId(1L);
        carDO.setDriver(driver);

        CarDTO result = CarMapper.makeCarDTO(carDO);

        assertNotNull(result);
        assertEquals("ABC123", result.getLicensePlate());
        assertEquals(4, result.getSeatCount());
        assertEquals(true, result.isConvertible());
        assertEquals(9, result.getRating());
        assertEquals(EngineType.ELECTRIC, result.getEngineType());
        assertEquals("TESLA", result.getManufacturer());
        assertNotNull(result.getDriver());
        assertEquals("driver1", result.getDriver().getUsername());
    }

    @Test
    public void makeCarDTO_withoutDriver() {
        CarDO carDO = new CarDO("XYZ789", 2, false, 7, EngineType.GAS, "BMW");

        CarDTO result = CarMapper.makeCarDTO(carDO);

        assertNotNull(result);
        assertEquals("XYZ789", result.getLicensePlate());
        assertEquals(2, result.getSeatCount());
        assertEquals(false, result.isConvertible());
        assertEquals(7, result.getRating());
        assertEquals(EngineType.GAS, result.getEngineType());
        assertEquals("BMW", result.getManufacturer());
        assertNull(result.getDriver());
    }

    @Test
    public void makeCarDO_fromDTO() {
        CarDTO carDTO = new CarDTO.CarDTOBuilder()
                .setLicensePlate("DEF456")
                .setSeatCount(5)
                .setConvertible(true)
                .setRating(8)
                .setEngineType(EngineType.HYBRID)
                .setManufacturer("AUDI")
                .build();

        CarDO result = CarMapper.makeCarDO(carDTO);

        assertNotNull(result);
        assertEquals("DEF456", result.getLicensePlate());
        assertEquals(5, result.getSeatCount());
        assertEquals(true, result.getConvertible());
        assertEquals(8, result.getRating());
        assertEquals(EngineType.HYBRID, result.getEngineType());
        assertEquals("AUDI", result.getManufacturer());
    }

    @Test
    public void makeCarDTOList() {
        CarDO car1 = new CarDO("ABC123", 4, false, 8, EngineType.GAS, "BMW");
        CarDO car2 = new CarDO("XYZ789", 2, true, 9, EngineType.ELECTRIC, "TESLA");
        List<CarDO> cars = Arrays.asList(car1, car2);

        List<CarDTO> result = CarMapper.makeCarDTOList(cars);

        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals("ABC123", result.get(0).getLicensePlate());
        assertEquals("XYZ789", result.get(1).getLicensePlate());
    }
}

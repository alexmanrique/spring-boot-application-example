package com.myapp.service;

import com.myapp.dataaccessobject.CarRepository;
import com.myapp.domainobject.CarDO;
import com.myapp.domainobject.DriverDO;
import com.myapp.domainvalue.EngineType;
import com.myapp.domainvalue.OnlineStatus;
import com.myapp.exception.CarAlreadyInUseException;
import com.myapp.exception.ConstraintsViolationException;
import com.myapp.exception.EntityNotFoundException;
import com.myapp.service.car.DefaultCarService;
import com.myapp.service.driver.DriverService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DataIntegrityViolationException;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class DefaultCarServiceTest {

    @Mock
    private CarRepository carRepository;

    @Mock
    private DriverService driverService;

    @InjectMocks
    private DefaultCarService carService;

    private CarDO testCar;
    private DriverDO testDriver;

    @BeforeEach
    public void setUp() {
        testCar = new CarDO("ABC123", 4, false, 8, EngineType.GAS, "BMW");
        testDriver = new DriverDO("driver1", "password");
        testDriver.setId(1L);
        testDriver.setOnlineStatus(OnlineStatus.ONLINE);
        carService.setDriverService(driverService);
    }

    @Test
    public void findCar_success() throws EntityNotFoundException {
        when(carRepository.findByLicensePlate("ABC123")).thenReturn(testCar);

        CarDO result = carService.find("ABC123");

        assertNotNull(result);
        assertEquals("ABC123", result.getLicensePlate());
    }

    @Test
    public void findCar_notFound() {
        when(carRepository.findByLicensePlate("UNKNOWN")).thenReturn(null);

        assertThrows(EntityNotFoundException.class, () -> {
            carService.find("UNKNOWN");
        });
    }

    @Test
    public void createCar_success() throws ConstraintsViolationException {
        when(carRepository.save(testCar)).thenReturn(testCar);

        CarDO result = carService.create(testCar);

        assertNotNull(result);
        assertEquals("ABC123", result.getLicensePlate());
        verify(carRepository).save(testCar);
    }

    @Test
    public void createCar_duplicateLicensePlate() {
        when(carRepository.save(testCar)).thenThrow(new DataIntegrityViolationException("duplicate"));

        assertThrows(ConstraintsViolationException.class, () -> {
            carService.create(testCar);
        });
    }

    @Test
    public void deleteCar_success() throws EntityNotFoundException, ConstraintsViolationException {
        when(carRepository.findByLicensePlate("ABC123")).thenReturn(testCar);

        carService.delete("ABC123");

        verify(carRepository).delete(testCar);
    }

    @Test
    public void deleteCar_notFound() {
        when(carRepository.findByLicensePlate("UNKNOWN")).thenReturn(null);

        assertThrows(EntityNotFoundException.class, () -> {
            carService.delete("UNKNOWN");
        });
    }

    @Test
    public void addDriver_success() throws Exception {
        when(carRepository.findByLicensePlate("ABC123")).thenReturn(testCar);
        when(driverService.find(1L)).thenReturn(testDriver);
        when(carRepository.save(testCar)).thenReturn(testCar);

        carService.addDriver(1L, "ABC123");

        assertEquals(testDriver, testCar.getDriver());
    }

    @Test
    public void addDriver_carAlreadyInUse() throws EntityNotFoundException {
        testCar.setDriver(new DriverDO("other", "pass"));
        when(carRepository.findByLicensePlate("ABC123")).thenReturn(testCar);
        doReturn(testDriver).when(driverService).find(1L);

        assertThrows(CarAlreadyInUseException.class, () -> {
            carService.addDriver(1L, "ABC123");
        });
    }

    @Test
    public void addDriver_driverOffline() throws EntityNotFoundException {
        testDriver.setOnlineStatus(OnlineStatus.OFFLINE);
        when(carRepository.findByLicensePlate("ABC123")).thenReturn(testCar);
        doReturn(testDriver).when(driverService).find(1L);

        assertThrows(ConstraintsViolationException.class, () -> {
            carService.addDriver(1L, "ABC123");
        });
    }

    @Test
    public void deleteDriver_success() throws Exception {
        testCar.setDriver(testDriver);
        when(carRepository.findByLicensePlate("ABC123")).thenReturn(testCar);
        when(driverService.find(1L)).thenReturn(testDriver);
        when(carRepository.save(testCar)).thenReturn(testCar);

        carService.deleteDriver(1L, "ABC123");

        assertNull(testCar.getDriver());
    }

    @Test
    public void deleteDriver_noDriverAssigned() throws Exception {
        when(carRepository.findByLicensePlate("ABC123")).thenReturn(testCar);
        when(driverService.find(1L)).thenReturn(testDriver);

        assertThrows(ConstraintsViolationException.class, () -> {
            carService.deleteDriver(1L, "ABC123");
        });
    }

    @Test
    public void deleteDriver_wrongDriver() throws Exception {
        DriverDO otherDriver = new DriverDO("other", "pass");
        otherDriver.setId(2L);
        testCar.setDriver(otherDriver);
        when(carRepository.findByLicensePlate("ABC123")).thenReturn(testCar);
        when(driverService.find(1L)).thenReturn(testDriver);

        assertThrows(ConstraintsViolationException.class, () -> {
            carService.deleteDriver(1L, "ABC123");
        });
    }

    @Test
    public void findAll_success() {
        CarDO car2 = new CarDO("XYZ789", 2, true, 9, EngineType.ELECTRIC, "TESLA");
        List<CarDO> cars = Arrays.asList(testCar, car2);
        when(carRepository.findAll()).thenReturn(cars);

        Iterable<CarDO> result = carService.findAll();

        assertNotNull(result);
        assertEquals(cars, result);
    }
}

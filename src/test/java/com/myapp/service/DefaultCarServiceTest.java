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
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.dao.DataIntegrityViolationException;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class DefaultCarServiceTest {

    @Mock
    private CarRepository carRepository;

    @Mock
    private DriverService driverService;

    @InjectMocks
    private DefaultCarService carService;

    private CarDO testCar;
    private DriverDO testDriver;

    @Before
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

    @Test(expected = EntityNotFoundException.class)
    public void findCar_notFound() throws EntityNotFoundException {
        when(carRepository.findByLicensePlate("UNKNOWN")).thenReturn(null);

        carService.find("UNKNOWN");
    }

    @Test
    public void createCar_success() throws ConstraintsViolationException {
        when(carRepository.save(testCar)).thenReturn(testCar);

        CarDO result = carService.create(testCar);

        assertNotNull(result);
        assertEquals("ABC123", result.getLicensePlate());
        verify(carRepository).save(testCar);
    }

    @Test(expected = ConstraintsViolationException.class)
    public void createCar_duplicateLicensePlate() throws ConstraintsViolationException {
        when(carRepository.save(testCar)).thenThrow(new DataIntegrityViolationException("duplicate"));

        carService.create(testCar);
    }

    @Test
    public void deleteCar_success() throws EntityNotFoundException, ConstraintsViolationException {
        when(carRepository.findByLicensePlate("ABC123")).thenReturn(testCar);

        carService.delete("ABC123");

        verify(carRepository).delete(testCar);
    }

    @Test(expected = EntityNotFoundException.class)
    public void deleteCar_notFound() throws EntityNotFoundException, ConstraintsViolationException {
        when(carRepository.findByLicensePlate("UNKNOWN")).thenReturn(null);

        carService.delete("UNKNOWN");
    }

    @Test
    public void addDriver_success() throws Exception {
        when(carRepository.findByLicensePlate("ABC123")).thenReturn(testCar);
        when(driverService.find(1L)).thenReturn(testDriver);
        when(carRepository.save(testCar)).thenReturn(testCar);

        carService.addDriver(1L, "ABC123");

        assertEquals(testDriver, testCar.getDriver());
    }

    @Test(expected = CarAlreadyInUseException.class)
    public void addDriver_carAlreadyInUse() throws Exception {
        testCar.setDriver(new DriverDO("other", "pass"));
        when(carRepository.findByLicensePlate("ABC123")).thenReturn(testCar);
        when(driverService.find(1L)).thenReturn(testDriver);

        carService.addDriver(1L, "ABC123");
    }

    @Test(expected = ConstraintsViolationException.class)
    public void addDriver_driverOffline() throws Exception {
        testDriver.setOnlineStatus(OnlineStatus.OFFLINE);
        when(carRepository.findByLicensePlate("ABC123")).thenReturn(testCar);
        when(driverService.find(1L)).thenReturn(testDriver);

        carService.addDriver(1L, "ABC123");
    }

    @Test
    public void deleteDriver_success() throws Exception {
        testCar.setDriver(testDriver);
        when(carRepository.findByLicensePlate("ABC123")).thenReturn(testCar);
        when(driverService.find(1L)).thenReturn(testDriver);
        when(carRepository.save(testCar)).thenReturn(testCar);

        carService.deleteDriver(1L, "ABC123");

        assertEquals(null, testCar.getDriver());
    }

    @Test(expected = ConstraintsViolationException.class)
    public void deleteDriver_noDriverAssigned() throws Exception {
        when(carRepository.findByLicensePlate("ABC123")).thenReturn(testCar);
        when(driverService.find(1L)).thenReturn(testDriver);

        carService.deleteDriver(1L, "ABC123");
    }

    @Test(expected = ConstraintsViolationException.class)
    public void deleteDriver_wrongDriver() throws Exception {
        DriverDO otherDriver = new DriverDO("other", "pass");
        otherDriver.setId(2L);
        testCar.setDriver(otherDriver);
        when(carRepository.findByLicensePlate("ABC123")).thenReturn(testCar);
        when(driverService.find(1L)).thenReturn(testDriver);

        carService.deleteDriver(1L, "ABC123");
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

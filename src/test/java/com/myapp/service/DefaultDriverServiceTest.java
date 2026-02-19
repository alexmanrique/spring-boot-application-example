package com.myapp.service;

import com.myapp.dataaccessobject.DriverRepository;
import com.myapp.domainobject.CarDO;
import com.myapp.domainobject.DriverDO;
import com.myapp.domainvalue.EngineType;
import com.myapp.domainvalue.OnlineStatus;
import com.myapp.exception.ConstraintsViolationException;
import com.myapp.exception.EntityNotFoundException;
import com.myapp.service.driver.DefaultDriverService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DataIntegrityViolationException;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class DefaultDriverServiceTest {

    @Mock
    private DriverRepository driverRepository;

    @InjectMocks
    private DefaultDriverService driverService;

    private DriverDO testDriver;

    @BeforeEach
    public void setUp() {
        testDriver = new DriverDO("driver1", "password");
        testDriver.setId(1L);
        testDriver.setOnlineStatus(OnlineStatus.OFFLINE);
    }

    @Test
    public void findDriver_success() throws EntityNotFoundException {
        when(driverRepository.findById(1L)).thenReturn(Optional.of(testDriver));

        DriverDO result = driverService.find(1L);

        assertNotNull(result);
        assertEquals("driver1", result.getUsername());
    }

    @Test
    public void findDriver_notFound() {
        when(driverRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> {
            driverService.find(99L);
        });
    }

    @Test
    public void createDriver_success() throws ConstraintsViolationException {
        when(driverRepository.save(testDriver)).thenReturn(testDriver);

        DriverDO result = driverService.create(testDriver);

        assertNotNull(result);
        assertEquals("driver1", result.getUsername());
        verify(driverRepository).save(testDriver);
    }

    @Test
    public void createDriver_duplicateUsername() {
        when(driverRepository.save(testDriver)).thenThrow(new DataIntegrityViolationException("duplicate"));

        assertThrows(ConstraintsViolationException.class, () -> {
            driverService.create(testDriver);
        });
    }

    @Test
    public void deleteDriver_success() throws EntityNotFoundException {
        when(driverRepository.findById(1L)).thenReturn(Optional.of(testDriver));

        driverService.delete(1L);

        assertTrue(testDriver.getDeleted());
    }

    @Test
    public void deleteDriver_notFound() {
        when(driverRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> {
            driverService.delete(99L);
        });
    }

    @Test
    public void updateLocation_success() throws EntityNotFoundException, ConstraintsViolationException {
        when(driverRepository.findById(1L)).thenReturn(Optional.of(testDriver));
        when(driverRepository.save(testDriver)).thenReturn(testDriver);

        driverService.updateLocation(1L, 13.4050, 52.5200);

        assertNotNull(testDriver.getCoordinate());
        assertEquals(52.5200, testDriver.getCoordinate().getLatitude(), 0.0001);
        assertEquals(13.4050, testDriver.getCoordinate().getLongitude(), 0.0001);
    }

    @Test
    public void updateLocation_driverNotFound() {
        when(driverRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> {
            driverService.updateLocation(99L, 13.4050, 52.5200);
        });
    }

    @Test
    public void updateCar_success() throws EntityNotFoundException, ConstraintsViolationException {
        CarDO car = new CarDO("ABC123", 4, false, 8, EngineType.GAS, "BMW");
        when(driverRepository.findById(1L)).thenReturn(Optional.of(testDriver));
        when(driverRepository.save(testDriver)).thenReturn(testDriver);

        driverService.updateCar(1L, car);

        assertEquals(car, testDriver.getCar());
    }

    @Test
    public void findByOnlineStatus() {
        DriverDO onlineDriver = new DriverDO("online1", "pass");
        onlineDriver.setOnlineStatus(OnlineStatus.ONLINE);
        List<DriverDO> onlineDrivers = Arrays.asList(onlineDriver);
        when(driverRepository.findByOnlineStatus(OnlineStatus.ONLINE)).thenReturn(onlineDrivers);

        List<DriverDO> result = driverService.find(OnlineStatus.ONLINE);

        assertEquals(1, result.size());
        assertEquals("online1", result.get(0).getUsername());
    }

    @Test
    public void findAll_success() {
        DriverDO driver2 = new DriverDO("driver2", "pass2");
        List<DriverDO> drivers = Arrays.asList(testDriver, driver2);
        when(driverRepository.findAll()).thenReturn(drivers);

        Iterable<DriverDO> result = driverService.findAll();

        assertNotNull(result);
        assertEquals(drivers, result);
    }
}

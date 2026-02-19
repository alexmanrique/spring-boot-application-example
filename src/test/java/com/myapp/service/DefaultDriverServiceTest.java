package com.myapp.service;

import com.myapp.dataaccessobject.DriverRepository;
import com.myapp.domainobject.CarDO;
import com.myapp.domainobject.DriverDO;
import com.myapp.domainvalue.EngineType;
import com.myapp.domainvalue.OnlineStatus;
import com.myapp.exception.ConstraintsViolationException;
import com.myapp.exception.EntityNotFoundException;
import com.myapp.service.driver.DefaultDriverService;
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
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class DefaultDriverServiceTest {

    @Mock
    private DriverRepository driverRepository;

    @InjectMocks
    private DefaultDriverService driverService;

    private DriverDO testDriver;

    @Before
    public void setUp() {
        testDriver = new DriverDO("driver1", "password");
        testDriver.setId(1L);
        testDriver.setOnlineStatus(OnlineStatus.OFFLINE);
    }

    @Test
    public void findDriver_success() throws EntityNotFoundException {
        when(driverRepository.findOne(1L)).thenReturn(testDriver);

        DriverDO result = driverService.find(1L);

        assertNotNull(result);
        assertEquals("driver1", result.getUsername());
    }

    @Test(expected = EntityNotFoundException.class)
    public void findDriver_notFound() throws EntityNotFoundException {
        when(driverRepository.findOne(99L)).thenReturn(null);

        driverService.find(99L);
    }

    @Test
    public void createDriver_success() throws ConstraintsViolationException {
        when(driverRepository.save(testDriver)).thenReturn(testDriver);

        DriverDO result = driverService.create(testDriver);

        assertNotNull(result);
        assertEquals("driver1", result.getUsername());
        verify(driverRepository).save(testDriver);
    }

    @Test(expected = ConstraintsViolationException.class)
    public void createDriver_duplicateUsername() throws ConstraintsViolationException {
        when(driverRepository.save(testDriver)).thenThrow(new DataIntegrityViolationException("duplicate"));

        driverService.create(testDriver);
    }

    @Test
    public void deleteDriver_success() throws EntityNotFoundException {
        when(driverRepository.findOne(1L)).thenReturn(testDriver);

        driverService.delete(1L);

        assertTrue(testDriver.getDeleted());
    }

    @Test(expected = EntityNotFoundException.class)
    public void deleteDriver_notFound() throws EntityNotFoundException {
        when(driverRepository.findOne(99L)).thenReturn(null);

        driverService.delete(99L);
    }

    @Test
    public void updateLocation_success() throws EntityNotFoundException, ConstraintsViolationException {
        when(driverRepository.findOne(1L)).thenReturn(testDriver);
        when(driverRepository.save(testDriver)).thenReturn(testDriver);

        driverService.updateLocation(1L, 13.4050, 52.5200);

        assertNotNull(testDriver.getCoordinate());
        assertEquals(52.5200, testDriver.getCoordinate().getLatitude(), 0.0001);
        assertEquals(13.4050, testDriver.getCoordinate().getLongitude(), 0.0001);
    }

    @Test(expected = EntityNotFoundException.class)
    public void updateLocation_driverNotFound() throws EntityNotFoundException, ConstraintsViolationException {
        when(driverRepository.findOne(99L)).thenReturn(null);

        driverService.updateLocation(99L, 13.4050, 52.5200);
    }

    @Test
    public void updateCar_success() throws EntityNotFoundException, ConstraintsViolationException {
        CarDO car = new CarDO("ABC123", 4, false, 8, EngineType.GAS, "BMW");
        when(driverRepository.findOne(1L)).thenReturn(testDriver);
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

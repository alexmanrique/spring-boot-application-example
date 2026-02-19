package com.myapp.controller.mapper;

import com.myapp.datatransferobject.DriverDTO;
import com.myapp.domainobject.DriverDO;
import com.myapp.domainvalue.GeoCoordinate;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

@RunWith(MockitoJUnitRunner.class)
public class DriverMapperTest {

    @Test
    public void makeDriverDTO_withCoordinate() {
        DriverDO driverDO = new DriverDO("user1", "pass1");
        driverDO.setId(1L);
        driverDO.setCoordinate(new GeoCoordinate(52.52, 13.405));

        DriverDTO result = DriverMapper.makeDriverDTO(driverDO);

        assertNotNull(result);
        assertEquals("user1", result.getUsername());
        assertEquals("pass1", result.getPassword());
        assertNotNull(result.getCoordinate());
        assertEquals(52.52, result.getCoordinate().getLatitude(), 0.001);
        assertEquals(13.405, result.getCoordinate().getLongitude(), 0.001);
    }

    @Test
    public void makeDriverDTO_withoutCoordinate() {
        DriverDO driverDO = new DriverDO("user2", "pass2");
        driverDO.setId(2L);

        DriverDTO result = DriverMapper.makeDriverDTO(driverDO);

        assertNotNull(result);
        assertEquals("user2", result.getUsername());
        assertEquals("pass2", result.getPassword());
        assertNull(result.getCoordinate());
    }

    @Test
    public void makeDriverDO_fromDTO() {
        DriverDTO driverDTO = DriverDTO.newBuilder()
                .setUsername("newUser")
                .setPassword("newPass")
                .createDriverDTO();

        DriverDO result = DriverMapper.makeDriverDO(driverDTO);

        assertNotNull(result);
        assertEquals("newUser", result.getUsername());
        assertEquals("newPass", result.getPassword());
    }

    @Test
    public void makeDriverDTOList() {
        DriverDO driver1 = new DriverDO("user1", "pass1");
        driver1.setId(1L);
        DriverDO driver2 = new DriverDO("user2", "pass2");
        driver2.setId(2L);
        List<DriverDO> drivers = Arrays.asList(driver1, driver2);

        List<DriverDTO> result = DriverMapper.makeDriverDTOList(drivers);

        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals("user1", result.get(0).getUsername());
        assertEquals("user2", result.get(1).getUsername());
    }
}

package com.myapp.domainvalue;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class GeoCoordinateTest {

    @Test
    public void validCoordinate() {
        GeoCoordinate coord = new GeoCoordinate(52.52, 13.405);

        assertNotNull(coord);
        assertEquals(52.52, coord.getLatitude(), 0.001);
        assertEquals(13.405, coord.getLongitude(), 0.001);
        assertNotNull(coord.getPoint());
    }

    @Test
    public void latitudeTooHigh() {
        assertThrows(IllegalArgumentException.class, () -> {
            new GeoCoordinate(91.0, 0.0);
        });
    }

    @Test
    public void latitudeTooLow() {
        assertThrows(IllegalArgumentException.class, () -> {
            new GeoCoordinate(-91.0, 0.0);
        });
    }

    @Test
    public void longitudeTooHigh() {
        assertThrows(IllegalArgumentException.class, () -> {
            new GeoCoordinate(0.0, 181.0);
        });
    }

    @Test
    public void longitudeTooLow() {
        assertThrows(IllegalArgumentException.class, () -> {
            new GeoCoordinate(0.0, -181.0);
        });
    }

    @Test
    public void equalsAndHashCode() {
        GeoCoordinate coord1 = new GeoCoordinate(52.52, 13.405);
        GeoCoordinate coord2 = new GeoCoordinate(52.52, 13.405);
        GeoCoordinate coord3 = new GeoCoordinate(40.71, -74.00);

        assertEquals(coord1, coord2);
        assertEquals(coord1.hashCode(), coord2.hashCode());
        assertNotEquals(coord1, coord3);
        assertNotEquals(coord1, null);
        assertEquals(coord1, coord1);
    }

    @Test
    public void boundaryValues() {
        GeoCoordinate maxCoord = new GeoCoordinate(90.0, 180.0);
        GeoCoordinate minCoord = new GeoCoordinate(-90.0, -180.0);

        assertEquals(90.0, maxCoord.getLatitude(), 0.001);
        assertEquals(180.0, maxCoord.getLongitude(), 0.001);
        assertEquals(-90.0, minCoord.getLatitude(), 0.001);
        assertEquals(-180.0, minCoord.getLongitude(), 0.001);
    }

    @Test
    public void toStringReturnsPointRepresentation() {
        GeoCoordinate coord = new GeoCoordinate(52.52, 13.405);

        assertNotNull(coord.toString());
    }
}

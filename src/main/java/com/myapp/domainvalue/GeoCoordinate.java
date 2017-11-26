package com.myapp.domainvalue;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.base.Preconditions;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import org.springframework.data.geo.Point;

@Embeddable
public class GeoCoordinate
{

    private static final int MAX_LATITUDE = 90;
    private static final int MIN_LATITUDE = -90;
    private static final int MAX_LONGITUDE = 180;
    private static final int MIN_LONGITUDE = -180;
    @Column(name = "coordinate")
    private final Point point;


    protected GeoCoordinate()
    {
        this.point = null;
    }


    /**
     * @param latitude  - y coordinate
     * @param longitude - x coordinate
     */
    public GeoCoordinate(final double latitude, final double longitude)
    {
        Preconditions.checkArgument(latitude >= MIN_LATITUDE, "latitude is lower than min_latitude: " + MIN_LATITUDE);
        Preconditions.checkArgument(latitude <= MAX_LATITUDE, "latitude is higher than max_latitude: " + MAX_LATITUDE);
        Preconditions.checkArgument(longitude >= MIN_LONGITUDE, "longitude is lower than min_longitude: " + MIN_LONGITUDE);
        Preconditions.checkArgument(longitude <= MAX_LONGITUDE, "longitude is higher than max_longitude: " + MAX_LONGITUDE);

        this.point = new Point(longitude, latitude);
    }


    @JsonProperty
    public double getLatitude()
    {
        return this.point.getY();
    }


    @JsonIgnore
    public Point getPoint()
    {
        return this.point;
    }


    @JsonProperty
    public double getLongitude()
    {
        return this.point.getX();
    }


    @Override
    public int hashCode()
    {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((this.point == null) ? 0 : this.point.hashCode());
        return result;
    }


    @Override
    public boolean equals(final Object obj)
    {
        if (this == obj)
        {
            return true;
        }
        if (obj == null)
        {
            return false;
        }
        if (this.getClass() != obj.getClass())
        {
            return false;
        }
        final GeoCoordinate other = (GeoCoordinate) obj;
        if (this.point == null)
        {
            if (other.point != null)
            {
                return false;
            }
        }
        else if (!this.point.equals(other.point))
        {
            return false;
        }
        return true;
    }


    @Override
    public String toString()
    {
        return this.point.toString();
    }

}

package com.myapp.controller.mapper;

import com.myapp.datatransferobject.CarDTO;
import com.myapp.domainobject.CarDO;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

public class CarMapper {

    public static CarDTO makeCarDTO(CarDO carDO) {

        CarDTO.CarDTOBuilder carDTOBuilder = new CarDTO.CarDTOBuilder()
                .setSeatCount(carDO.getSeatCount())
                .setRating(carDO.getRating())
                .setLicensePlate(carDO.getLicensePlate())
                .setId(carDO.getId())
                .setConvertible(carDO.getConvertible())
                .setEngineType(carDO.getEngineType())
                .setManufacturer(carDO.getManufacturer());

        if (carDO.getDriver() != null) {
            carDTOBuilder.setDriver(DriverMapper.makeDriverDTO(carDO.getDriver()));
        }
        return carDTOBuilder.build();
    }

    public static CarDO makeCarDO(CarDTO carDTO) {
        return new CarDO(carDTO.getLicensePlate(), carDTO.getSeatCount(), carDTO.isConvertible(), carDTO.getRating(), carDTO.getEngineType(), carDTO.getManufacturer());
    }

    public static List<CarDTO> makeCarDTOList(Collection<CarDO> drivers)
    {
        return drivers.stream()
                .map(CarMapper::makeCarDTO)
                .collect(Collectors.toList());
    }
}

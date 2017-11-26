package com.myapp.controller;

import com.google.common.collect.Lists;
import com.myapp.controller.mapper.CarMapper;
import com.myapp.datatransferobject.CarDTO;
import com.myapp.domainobject.CarDO;
import com.myapp.exception.ConstraintsViolationException;
import com.myapp.exception.EntityNotFoundException;
import com.myapp.service.car.CarService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("v1/cars")
public class CarController {

    private final CarService carService;

    @Autowired
    public CarController(final CarService carService) {
        this.carService = carService;
    }

    @GetMapping("/{licensePlate}")
    public CarDTO getCar(@Valid @PathVariable String licensePlate) throws EntityNotFoundException {
        return CarMapper.makeCarDTO(carService.find(licensePlate));
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CarDTO createCar(@Valid @RequestBody CarDTO carDTO) throws ConstraintsViolationException {
        CarDO carDO = CarMapper.makeCarDO(carDTO);
        return CarMapper.makeCarDTO(carService.create(carDO));
    }


    @DeleteMapping("/{licensePlate}")
    public void deleteCar(@Valid @PathVariable String licensePlate) throws EntityNotFoundException, ConstraintsViolationException {
        carService.delete(licensePlate);
    }

    @GetMapping
    public List<CarDTO> findCars()
            throws ConstraintsViolationException, EntityNotFoundException {
        List<CarDO> cars = Lists.newArrayList(carService.findAll()).stream().
                collect(Collectors.toList());
        return CarMapper.makeCarDTOList(cars);
    }
}

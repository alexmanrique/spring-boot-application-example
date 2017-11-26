package com.myapp.service.car;

import com.myapp.domainobject.CarDO;
import com.myapp.exception.CarAlreadyInUseException;
import com.myapp.exception.ConstraintsViolationException;
import com.myapp.exception.EntityNotFoundException;

public interface CarService {

    CarDO find(String licensePlate) throws EntityNotFoundException;

    CarDO create(CarDO carDO) throws ConstraintsViolationException;

    void delete(String licensePlate) throws EntityNotFoundException, ConstraintsViolationException;

    void addDriver(Long driverId, String licensePlate) throws ConstraintsViolationException, EntityNotFoundException, CarAlreadyInUseException;

    void deleteDriver(Long driverId, String licensePlate) throws ConstraintsViolationException, EntityNotFoundException, CarAlreadyInUseException;

    Iterable<CarDO> findAll();
}

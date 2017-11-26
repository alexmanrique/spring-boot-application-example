package com.myapp;

import com.myapp.controller.CarController;
import com.myapp.domainobject.CarDO;
import com.myapp.domainvalue.EngineType;
import com.myapp.service.car.CarService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.skyscreamer.jsonassert.JSONAssert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.junit.Assert.assertEquals;

@RunWith(SpringJUnit4ClassRunner.class)
@WebMvcTest(value = CarController.class, secure = false)
public class CarControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CarService carService;

    private static final String LICENSE_PLATE = "546PW";

    private CarDO carDOResult = new CarDO(LICENSE_PLATE, 4, false, 10, EngineType.GAS, "MERCEDES");

    @Test
    public void createCar() throws Exception {

        Mockito.when(carService.create(Mockito.any(CarDO.class))).thenReturn(carDOResult);

        String exampleCar = "{\"licensePlate\":\"546PW\",\"seatCount\":\"4\",\"convertible\":\"false\",\"rating\":\"10\",\"engineType\":\"GAS\",\"manufacturer\":\"MERCEDES\"}";

        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .post("/v1/cars")
                .accept(MediaType.APPLICATION_JSON).content(exampleCar)
                .contentType(MediaType.APPLICATION_JSON);

        MvcResult result = mockMvc.perform(requestBuilder).andReturn();

        MockHttpServletResponse response = result.getResponse();

        assertEquals(HttpStatus.CREATED.value(), response.getStatus());
    }

    @Test
    public void getCar() throws Exception {

        Mockito.when(carService.find(LICENSE_PLATE)).thenReturn(carDOResult);

        RequestBuilder requestBuilder = MockMvcRequestBuilders.get(
                "/v1/cars/"+LICENSE_PLATE).accept(
                MediaType.APPLICATION_JSON);

        MvcResult result = mockMvc.perform(requestBuilder).andReturn();

        System.out.println(result.getResponse());
        String expected = "{\"licensePlate\":\"546PW\",\"seatCount\":4,\"convertible\":false,\"rating\":10,\"engineType\":\"GAS\",\"manufacturer\":\"MERCEDES\"}";

        JSONAssert.assertEquals(expected, result.getResponse().getContentAsString(), false);
    }

}

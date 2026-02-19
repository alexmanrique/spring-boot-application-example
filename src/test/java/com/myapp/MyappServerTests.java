package com.myapp;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

@SpringBootTest(classes = MyappServer.class)
@TestPropertySource(properties = "spring.sql.init.mode=never")
public class MyappServerTests
{

    @Test
    public void contextLoads()
    {
    }

}

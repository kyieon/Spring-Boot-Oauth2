package com.mobigen.sample;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.test.context.junit4.SpringRunner;

import com.yjlee.api.MainApplication;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = MainApplication.class, webEnvironment = WebEnvironment.RANDOM_PORT)
public class MainApplicationTests {

	@Test
	public void contextLoads() {
	}

}

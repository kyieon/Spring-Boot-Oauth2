package com.yjlee.oauth2.repository;

import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.yjlee.oauth2.model.UserEntity;

@RunWith(SpringRunner.class)
@SpringBootTest
public class UserRepositoryTest {

	@Autowired
	private UserRepository userRepository;
	
	@Test
	public void test() {
		
		UserEntity userEntity = new UserEntity();
		
		userEntity.setName("adminT");
		userEntity.setPassword("adminT");
		
		userRepository.save(userEntity);
	}

	@Test
	public void testSelect() {
		
		List<UserEntity> us = userRepository.findAll();
		
		System.out.println(us);
	}
}

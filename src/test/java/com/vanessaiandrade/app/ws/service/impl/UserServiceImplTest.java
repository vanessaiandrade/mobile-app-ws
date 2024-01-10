package com.vanessaiandrade.app.ws.service.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import com.vanessaiandrade.app.ws.exceptions.UserServiceException;
import com.vanessaiandrade.app.ws.io.entity.AddressEntity;
import com.vanessaiandrade.app.ws.io.entity.UserEntity;
import com.vanessaiandrade.app.ws.io.repository.UserRepository;
import com.vanessaiandrade.app.ws.shared.AmazonSES;
import com.vanessaiandrade.app.ws.shared.Utils;
import com.vanessaiandrade.app.ws.shared.dto.AddressDto;
import com.vanessaiandrade.app.ws.shared.dto.UserDto;

class UserServiceImplTest {

	@InjectMocks
	UserServiceImpl userService;

	@Mock
	UserRepository userRepository;

	@Mock
	Utils utils;

	@Mock
	AmazonSES amazonSES;

	@Mock
	BCryptPasswordEncoder bCryptPasswordEncoder;

	String userId = "hajsdhasjdhas";
	String encryptedPassword = "986ajsdhas2";
	String email = "vanessaiandrade.dev@gmail.com";
	UserEntity userEntity;

	@BeforeEach
	void setUp() throws Exception {
		MockitoAnnotations.openMocks(this);

		userEntity = new UserEntity();
		userEntity.setId(1L);
		userEntity.setFirstName("Vanessa");
		userEntity.setLastName("Andrade");
		userEntity.setUserId(userId);
		userEntity.setEncryptedPassword(encryptedPassword);
		userEntity.setEmail(email);
		userEntity.setEmailVerificationToken("ajkhdsakjhd");
		userEntity.setAddresses(getAddressesEntity());
	}

	@Test
	final void testGetUser() {

		when(userRepository.findByEmail(anyString())).thenReturn(userEntity);
		
		UserDto userDto = userService.getUser(email);

		assertNotNull(userDto);
		assertEquals("Vanessa", userDto.getFirstName());

	}

	@Test
	final void testGetUser_UsernameNotFoundException() {
		
		when(userRepository.findByEmail(anyString())).thenReturn(null);
		
		assertThrows(UsernameNotFoundException.class, 
				()->{
					userService.getUser(email);
				});
	}

	@Test
	final void testCreateUser_CreateUserServiceException() {
		
		when(userRepository.findByEmail(anyString())).thenReturn(userEntity);
		
		UserDto userDto = new UserDto();
		userDto.setFirstName("Vanessa");
		userDto.setLastName("Andrade");
		userDto.setPassword("123456789");
		userDto.setEmail("test@test.com");
		userDto.setAddresses(getAddressesDto());
		
		assertThrows(UserServiceException.class, 
				()->{
					userService.createUser(userDto);
				});
	}

	@Test
	final void testCreateUser() {

		when(userRepository.findByEmail(anyString())).thenReturn(null);
		when(utils.generateAddressId(anyInt())).thenReturn("ahsgdjagshd98");
		when(utils.generateUserId(anyInt())).thenReturn(userId);
		when(bCryptPasswordEncoder.encode(anyString())).thenReturn(encryptedPassword);
		when(userRepository.save(any(UserEntity.class))).thenReturn(userEntity);
		Mockito.doNothing().when(amazonSES).verifyEmail(any(UserDto.class));
		
		UserDto userDto = new UserDto();
		userDto.setFirstName("Vanessa");
		userDto.setLastName("Andrade");
		userDto.setPassword("123456789");
		userDto.setEmail("test@test.com");
		userDto.setAddresses(getAddressesDto());
		
		UserDto storedUserDetails = userService.createUser(userDto);
		
		assertNotNull(storedUserDetails);
		assertNotNull(storedUserDetails.getUserId());
		
		assertEquals(storedUserDetails.getFirstName(), userEntity.getFirstName());
		assertEquals(storedUserDetails.getLastName(), userEntity.getLastName());
		assertEquals(storedUserDetails.getAddresses().size(), userEntity.getAddresses().size());

		verify(utils, times(storedUserDetails.getAddresses().size())).generateAddressId(30);
		verify(bCryptPasswordEncoder, times(1)).encode("123456789");
		verify(userRepository, times(1)).save(any(UserEntity.class));
	}

	private List<AddressDto> getAddressesDto() {

		AddressDto addressDto = new AddressDto();
		addressDto.setType("shipping");
		addressDto.setCity("Vancouver");
		addressDto.setCountry("Canada");
		addressDto.setPostalCode("ABC123");
		addressDto.setStreetName("123 Street name");

		AddressDto billingAddressDto = new AddressDto();
		billingAddressDto.setType("billing");
		billingAddressDto.setCity("Vancouver");
		billingAddressDto.setCountry("Canada");
		billingAddressDto.setPostalCode("ABC123");
		billingAddressDto.setStreetName("123 Street name");

		List<AddressDto> addresses = new ArrayList<>();
		addresses.add(addressDto);
		addresses.add(billingAddressDto);

		return addresses;
	}

	private List<AddressEntity> getAddressesEntity() {
		List<AddressDto> addresses = getAddressesDto();

		Type listType = new TypeToken<List<AddressEntity>>() {
		}.getType();

		return new ModelMapper().map(addresses, listType);
	}
}

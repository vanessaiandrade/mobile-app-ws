package com.vanessaiandrade.app.ws.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.vanessaiandrade.app.ws.io.entity.AddressEntity;
import com.vanessaiandrade.app.ws.io.entity.UserEntity;
import com.vanessaiandrade.app.ws.io.repository.AddressRepository;
import com.vanessaiandrade.app.ws.io.repository.UserRepository;
import com.vanessaiandrade.app.ws.service.AddressService;
import com.vanessaiandrade.app.ws.shared.Utils;
import com.vanessaiandrade.app.ws.shared.dto.AddressDto;

@Service
public class AddressServiceImpl implements AddressService {
	@Autowired
	UserRepository userRepository;

	@Autowired
	AddressRepository addressRepository;

	@Autowired
	Utils utils;

	@Override
	public List<AddressDto> getAddresses(String userId) {

		List<AddressDto> returnValue = new ArrayList<>();

		UserEntity userEntity = userRepository.findByUserId(userId);

		if (userEntity == null)
			return returnValue;

		Iterable<AddressEntity> addresses = addressRepository.findAllByUserDetails(userEntity);

		for (AddressEntity addressEntity : addresses) {
			returnValue.add(new ModelMapper().map(addressEntity, AddressDto.class));
		}

		return returnValue;
	}

	@Override
	public AddressDto getAddress(String addressId) {

		AddressDto returnValue = null;

		AddressEntity addressEntity = addressRepository.findByAddressId(addressId);

		if (addressEntity != null) {
			returnValue = new ModelMapper().map(addressEntity, AddressDto.class);
		}

		return returnValue;
	}

}

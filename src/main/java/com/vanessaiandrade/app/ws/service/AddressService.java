package com.vanessaiandrade.app.ws.service;

import java.util.List;

import com.vanessaiandrade.app.ws.shared.dto.AddressDto;

public interface AddressService {
	List<AddressDto> getAddresses(String userId);

	AddressDto getAddress(String addressId);

}

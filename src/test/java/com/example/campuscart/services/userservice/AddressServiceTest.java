package com.example.campuscart.services.userservice;


import com.example.campuscart.dto.userservice.AddressRequest;
import com.example.campuscart.dto.userservice.AddressResponse;
import com.example.campuscart.dto.userservice.AllAddressesResponse;
import com.example.campuscart.entities.userservice.Address;
import com.example.campuscart.entities.userservice.User;
import com.example.campuscart.exceptions.NotFoundException;
import com.example.campuscart.repositories.userservice.AddressRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;

public class AddressServiceTest {
    @InjectMocks
    private AddressService addressService;
    @Mock
    private AddressRepository addressRepository;
    @Mock
    private UserService userService;

    @BeforeEach
    public void setup(){
        MockitoAnnotations.initMocks(this);
    }
    @Test
    public void testAddAddress(){
        Long accountId = 123L;
        AddressRequest request = new AddressRequest("Street", "City", "State", "151505", "1234567891");
        User user = new User();
        user.setAccountId(accountId);

        Mockito.when(userService.getUser(accountId)).thenReturn(user);
        Mockito.when(addressRepository.save(any(Address.class))).thenAnswer(invocation -> invocation.getArgument(0));

        AddressResponse response = addressService.addAddress(accountId, request);

        Assertions.assertEquals(request.getStreetAddress(), response.getAddress().getStreetAddress());
        Assertions.assertEquals(request.getCity(), response.getAddress().getCity());
        Assertions.assertEquals(request.getState(), response.getAddress().getState());
        Assertions.assertEquals(request.getContact(), response.getAddress().getContact());
        Assertions.assertEquals(request.getPincode(), response.getAddress().getPincode());

        Mockito.verify(userService).getUser(any(Long.class));
        Mockito.verify(addressRepository).save(any(Address.class));
    }
    @Test
    public void testGetUserAddresses() {
        Long accountId = 123L;
        List<Address> addressList = new ArrayList<>();
        Address address1 = new Address();
        address1.setAddressId(456L);
        address1.setStreetAddress("Street1");
        address1.setPincode("151505");
        address1.setContact("1234567891");
        address1.setState("State1");
        address1.setCity("City1");

        Address address2 = new Address();
        address2.setAddressId(456L);
        address2.setStreetAddress("Street2");
        address2.setPincode("560035");
        address2.setContact("1325467891");
        address2.setState("State2");
        address2.setCity("City2");

        addressList.add(address1);
        addressList.add(address2);

        Mockito.when(addressRepository.findByAccountId(any(Long.class))).thenReturn(addressList);

        AllAddressesResponse response = addressService.getUserAddresses(accountId);

        Assertions.assertEquals(addressList.size(), response.getAddresses().size());
        Assertions.assertEquals(accountId, response.getAccountId());

        Mockito.verify(addressRepository).findByAccountId(any(Long.class));
    }
    @Test
    public void testUpdateAddress() {
        Long addressId = 123L;
        AddressRequest request = new AddressRequest("Updated Street", "Updated City", "Updated State", "151505", "9872688085");
        Address existingAddress = new Address();
        existingAddress.setAddressId(addressId);
        existingAddress.setStreetAddress("Street1");
        existingAddress.setPincode("560035");
        existingAddress.setContact("1234567891");
        existingAddress.setState("State1");
        existingAddress.setCity("City1");

        Mockito.when(addressRepository.findById(any(Long.class))).thenReturn(Optional.of(existingAddress));
        Mockito.when(addressRepository.save(any(Address.class))).thenAnswer(invocation -> invocation.getArgument(0));

        AddressResponse response = addressService.updateAddress(addressId, request);

        Assertions.assertEquals(request.getStreetAddress(), response.getAddress().getStreetAddress());
        Assertions.assertEquals(request.getCity(), response.getAddress().getCity());
        Assertions.assertEquals(request.getState(), response.getAddress().getState());
        Assertions.assertEquals(request.getContact(), response.getAddress().getContact());
        Assertions.assertEquals(request.getPincode(), response.getAddress().getPincode());

        Mockito.verify(addressRepository).findById(any(Long.class));
        Mockito.verify(addressRepository).save(any(Address.class));
    }
    @Test
    public void testDeleteAddress() {
        // Arrange
        Long addressId = 123L;
        Address existingAddress = new Address();
        existingAddress.setAddressId(addressId);

        Mockito.when(addressRepository.findById(any(Long.class))).thenReturn(Optional.of(existingAddress));

        addressService.deleteAddress(addressId);

        Mockito.verify(addressRepository).delete(any(Address.class));
    }
    @Test
    public void testGetAddress() {
        Long addressId = 123L;
        Address existingAddress = new Address();
        existingAddress.setAddressId(addressId);
        existingAddress.setStreetAddress("Street1");
        existingAddress.setPincode("560035");
        existingAddress.setContact("1234567891");
        existingAddress.setState("State1");
        existingAddress.setCity("City1");

        Mockito.when(addressRepository.findById(any(Long.class))).thenReturn(Optional.of(existingAddress));

        Address actualAddress = addressService.getAddress(addressId);

        Assertions.assertEquals(existingAddress.getStreetAddress(), actualAddress.getStreetAddress());
        Assertions.assertEquals(existingAddress.getCity(), actualAddress.getCity());
        Assertions.assertEquals(existingAddress.getState(), actualAddress.getState());
        Assertions.assertEquals(existingAddress.getContact(), actualAddress.getContact());
        Assertions.assertEquals(existingAddress.getPincode(), actualAddress.getPincode());
    }

    @Test
    public void testGetAddress_AddressNotFound() {
        Long addressId = 456L;

        Mockito.when(addressRepository.findById(any(Long.class))).thenReturn(Optional.empty());

        NotFoundException notFoundException = Assertions.assertThrows(NotFoundException.class, () -> addressService.getAddress(addressId));
        Assertions.assertEquals("Address:" + addressId + " not found", notFoundException.getMessage());
    }
}

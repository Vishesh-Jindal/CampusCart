package com.example.campuscart.services.userservice;

import com.example.campuscart.dto.userservice.AddressRequest;
import com.example.campuscart.dto.userservice.AddressResponse;
import com.example.campuscart.dto.userservice.AllAddressesResponse;
import com.example.campuscart.entities.userservice.Address;
import com.example.campuscart.entities.userservice.User;
import com.example.campuscart.exceptions.NotFoundException;
import com.example.campuscart.repositories.userservice.AddressRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AddressService {
    @Autowired
    private AddressRepository addressRepository;
    @Autowired
    private UserService userService;
    public AddressResponse addAddress(Long accountId, AddressRequest request) throws NotFoundException{
        User user = userService.getUser(accountId);
        Address address = new Address();
        address.setStreetAddress(request.getStreetAddress());
        address.setCity(request.getCity());
        address.setState(request.getState());
        address.setContact(request.getContact());
        address.setPincode(request.getPincode());
        address.setUser(user);
        Address createdAddress = addressRepository.save(address);
        return AddressResponse.builder()
                .address(createdAddress)
                .build();
    }
    public AllAddressesResponse getUserAddresses(Long accountId){
        return AllAddressesResponse.builder()
                .addresses(addressRepository.findByAccountId(accountId))
                .accountId(accountId)
                .build();
    }
    public AddressResponse updateAddress(Long addressId, AddressRequest request) throws NotFoundException{
        Address existingAddress = this.getAddress(addressId);
        existingAddress.setStreetAddress(request.getStreetAddress());
        existingAddress.setCity(request.getCity());
        existingAddress.setState(request.getState());
        existingAddress.setPincode(request.getPincode());
        existingAddress.setContact(request.getContact());
        Address updatedAddress = addressRepository.save(existingAddress);
        return AddressResponse.builder()
                .address(updatedAddress)
                .build();
    }
    public void deleteAddress(Long addressId) throws NotFoundException{
        Address address = this.getAddress(addressId);
        addressRepository.delete(address);
    }
    public Address getAddress(Long addressId) throws NotFoundException{
        Optional<Address> optionalAddress = addressRepository.findById(addressId);
        if(!optionalAddress.isPresent()){
            throw new NotFoundException("Address:" + addressId + " not found");
        }
        return optionalAddress.get();
    }
}

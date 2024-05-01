package com.example.campuscart.repositories.userservice;

import com.example.campuscart.constants.Constants;
import com.example.campuscart.entities.userservice.Address;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface AddressRepository extends JpaRepository<Address, Long> {
    @Query(Constants.QueryConstants.FETCH_ADDRESS_BY_ACCOUNT_ID)
    public List<Address> findByAccountId(@Param("accountId") Long accountId);
}

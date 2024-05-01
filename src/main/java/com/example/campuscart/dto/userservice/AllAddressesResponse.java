package com.example.campuscart.dto.userservice;

import com.example.campuscart.entities.userservice.Address;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;


@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AllAddressesResponse {
    private Long accountId;
    private List<Address> addresses = new ArrayList<>();
}

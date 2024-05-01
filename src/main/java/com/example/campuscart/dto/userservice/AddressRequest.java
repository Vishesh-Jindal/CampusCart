package com.example.campuscart.dto.userservice;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AddressRequest {
    @NotNull
    @NotEmpty
    private String streetAddress;
    @NotNull
    @NotEmpty
    private String city;
    @NotNull
    @NotEmpty
    private String state;
    @NotNull
    @Size(min = 6, max = 6)
    private String pincode;
    @NotNull
    @Size(min = 10, max = 10)
    private String contact;
}

package com.fabriciolfj.github.client.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CustomerDTO {

    public Long id;
    public String name;
    public String surname;
}

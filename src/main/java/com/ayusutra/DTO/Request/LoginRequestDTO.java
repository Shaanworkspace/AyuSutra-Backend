package com.ayusutra.DTO.Request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
public class LoginRequestDTO {
    private String email;
    private String password;
}

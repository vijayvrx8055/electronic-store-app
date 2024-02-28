package com.vrx.electronic.store.dto.response;

import com.vrx.electronic.store.dto.UserDto;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class JwtResponse {
    private String jwtToken;
    private UserDto userDto;
}

package com.pk.SimpleToDos.dto;

import lombok.*;

// used as a DTO to return the Jwt token generated
@Getter
@RequiredArgsConstructor
public class AuthenticationResponse {
    private final String jwt;
}

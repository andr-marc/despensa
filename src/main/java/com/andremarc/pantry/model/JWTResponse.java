package com.andremarc.pantry.model;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class JWTResponse {
    private String login;
    private String name;
    private List<String> roles;
    private Boolean authenticated;
    private String token;
}

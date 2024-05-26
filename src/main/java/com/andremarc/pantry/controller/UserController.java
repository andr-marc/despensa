package com.andremarc.pantry.controller;

import com.andremarc.pantry.DTO.UserDTO;
import com.andremarc.pantry.entity.User;
import com.andremarc.pantry.model.ApiResponse;
import com.andremarc.pantry.model.PaginatedData;
import com.andremarc.pantry.security.jwt.JwtResponse;
import com.andremarc.pantry.security.jwt.JwtUtils;
import com.andremarc.pantry.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("user")
public class UserController {
    private final UserService service;
    private final JwtUtils jwtUtils;

    @PostMapping
    public ResponseEntity<ApiResponse<User>> saveUser(@RequestBody User user) {
        ApiResponse<User> response = service.saveUser(user);
        return ResponseEntity.status(response.getStatus()).body(response);
    }

    @PutMapping()
    public ResponseEntity<ApiResponse<User>> updateUser(@RequestBody User user) {
        ApiResponse<User> response = service.updateUser(user);
        return ResponseEntity.status(response.getStatus()).body(response);
    }

    @PutMapping("/passwordUpdate")
    public ResponseEntity<ApiResponse<User>> updateUserPassword(@RequestBody UserDTO user) {
        ApiResponse<User> response = service.updateUserPassword(user);
        return ResponseEntity.status(response.getStatus()).body(response);
    }

    @PutMapping("/passwordReset")
    public ResponseEntity<ApiResponse<User>> resetUserPassword(@RequestBody User user) {
        ApiResponse<User> response = service.resetUserPassword(user);
        return ResponseEntity.status(response.getStatus()).body(response);
    }

    @GetMapping()
    public ResponseEntity<ApiResponse<PaginatedData<User>>> listUsers(@PageableDefault(size = 10) Pageable pageable) {
        ApiResponse<PaginatedData<User>> response = service.listUsers(pageable);
        return ResponseEntity.status(response.getStatus()).body(response);
    }

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<JwtResponse>> authenticateUser(@RequestBody User loginRequest) {
        ApiResponse<JwtResponse> response = service.login(loginRequest);
        return ResponseEntity.status(response.getStatus()).body(response);
    }

    @GetMapping("/token")
    public ResponseEntity<Boolean> checkToken(@RequestParam String token) {
        var valid = jwtUtils.validateJwtToken(token);
        return ResponseEntity.status(valid ? HttpStatus.OK.value() : HttpStatus.FORBIDDEN.value()).body(valid);
    }

    @GetMapping("/check")
    public ResponseEntity<ApiResponse<User>> getUserLogged(){
        ApiResponse<User> response = service.getUserLogged();
        return ResponseEntity.status(response.getStatus()).body(response);
    }
}

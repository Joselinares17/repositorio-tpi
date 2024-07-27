package org.lumeninvestiga.backend.repositorio.tpi.controllers;

import jakarta.validation.Valid;
import org.lumeninvestiga.backend.repositorio.tpi.dto.request.UserLoginRequest;
import org.lumeninvestiga.backend.repositorio.tpi.dto.request.UserRegistrationRequest;
import org.lumeninvestiga.backend.repositorio.tpi.dto.request.UserUpdateRequest;
import org.lumeninvestiga.backend.repositorio.tpi.dto.response.UserResponse;
import org.lumeninvestiga.backend.repositorio.tpi.services.UserService;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/users")
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping()
    public ResponseEntity<List<UserResponse>> readUsers(@PageableDefault Pageable pageable) {
        return ResponseEntity.status(HttpStatus.ACCEPTED).body(userService.getAllUsers(pageable));
    }

    @GetMapping("/{user_id}")
    public ResponseEntity<UserResponse> readUser(@PathVariable("user_id") Long userId) {
        Optional<UserResponse> response = userService.getUserById(userId);
        if(response.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        return ResponseEntity.status(HttpStatus.ACCEPTED).body(response.get());
    }


    //TODO: Considerar eliminarla es igual al anterior.
    @GetMapping("/{user_id}/profile")
    public ResponseEntity<UserResponse> readUserProfile(@PathVariable("user_id") Long userId) {
        Optional<UserResponse> response = userService.getUserById(userId);
        if(response.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        return ResponseEntity.status(HttpStatus.ACCEPTED).body(response.get());
    }

    @PutMapping("/{user_id}")
    public ResponseEntity<UserResponse> updateUserById(
            @PathVariable("user_id") Long userId,
            @Valid @RequestBody UserUpdateRequest request) {
        Optional<UserResponse> response = userService.updateUserById(userId, request);
        if(response.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        return ResponseEntity.status(HttpStatus.ACCEPTED).body(response.get());
    }

    //TODO: Agregar @PatchMapping para modificar atributos específicos y evitar volver a crear
    // un nuevo objeto para la modificación de algún campo.

    @DeleteMapping("/{user_id}")
    public ResponseEntity<Void> deleteUserById(@PathVariable("user_id") Long userId) {
        boolean responseStatus = userService.deleteUserById(userId);
        if(!responseStatus) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        return ResponseEntity.status(HttpStatus.ACCEPTED).build();
    }
}

package com.onetouch.users.controller;

import com.onetouch.users.dto.*;
import com.onetouch.users.entity.UserEntity;
import com.onetouch.users.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    // 전체 회원 목록 조회
    @GetMapping
    public List<UserEntity> getAllUsers() {
        return userService.getAllUsers();
    }

    // 로그인 ID로 조회
    @GetMapping("/{userId}")
    public ResponseEntity<UserEntity> getUserByUserId(@PathVariable String userId) {
        return ResponseEntity.ok(userService.getUserByUserId(userId));
    }

    // PK 기준 사용자 정보 조회 (다른 마이크로서비스에서 사용할 수 있음)
    @GetMapping("/info/{id}")
    public ResponseEntity<UserInfoDto> getUserById(@PathVariable Integer id) {
        UserEntity user = userService.getUserById(id);
        return ResponseEntity.ok(new UserInfoDto(user.getId(), user.getUsername()));
    }

    // 회원가입
    @PostMapping("/register")
    public UserEntity register(@RequestBody UserRegisterDto dto) {
        return userService.register(dto);
    }

    // 로그인
    @PostMapping("/login")
    public LoginResDto login(@RequestBody LogInDto dto) {
        return userService.login(dto);
    }

    // 회원 정보 수정
    @PutMapping("/{userId}")
    public ResponseEntity<UserEntity> updateUser(@PathVariable String userId, @RequestBody UserRegisterDto dto) {
        return ResponseEntity.ok(userService.updateUser(userId, dto));
    }

    // 회원 삭제
    @DeleteMapping("/{userId}")
    public ResponseEntity<UserEntity> deleteUser(@PathVariable String userId) {
        return ResponseEntity.ok(userService.deleteUser(userId));
    }

    @GetMapping("/username/{username}")
    public ResponseEntity<UserInfoDto> getUserByUsername(@PathVariable String username) {
    UserEntity user = userService.getUserByUsername(username);
    return ResponseEntity.ok(new UserInfoDto(user.getId(), user.getUsername()));
    }

}

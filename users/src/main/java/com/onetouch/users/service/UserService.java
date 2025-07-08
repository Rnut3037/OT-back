package com.onetouch.users.service;

import com.onetouch.users.dto.LogInDto;
import com.onetouch.users.dto.LoginResDto;
import com.onetouch.users.dto.UserRegisterDto;
import com.onetouch.users.entity.UserEntity;
import com.onetouch.users.exception.EmailAlreadyExistsException;
import com.onetouch.users.exception.UserIdAlreadyExistsException;
import com.onetouch.users.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public List<UserEntity> getAllUsers() {
        return userRepository.findAll();
    }

    // 로그인 ID 기준 조회
    public UserEntity getUserByUserId(String userId) {
        return userRepository.findByUserId(userId)
                .orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다."));
    }

    // PK 기준 조회
    public UserEntity getUserById(Integer id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다."));
    }

    public UserEntity register(UserRegisterDto dto) {
        if (userRepository.existsByUserId(dto.getUserId())) {
            throw new UserIdAlreadyExistsException("이미 존재하는 아이디입니다.");
        }
        if (userRepository.existsByEmail(dto.getEmail())) {
            throw new EmailAlreadyExistsException("이미 존재하는 이메일입니다.");
        }

        UserEntity user = UserEntity.builder()
                .username(dto.getUsername())
                .userId(dto.getUserId())
                .password(dto.getPassword())
                .email(dto.getEmail())
                .build();

        return userRepository.save(user);
    }

    public UserEntity updateUser(String userId, UserRegisterDto dto) {
        UserEntity user = getUserByUserId(userId);
        user.setUsername(dto.getUsername());
        user.setUserId(dto.getUserId());
        user.setPassword(dto.getPassword());
        user.setEmail(dto.getEmail());
        return userRepository.save(user);
    }

    public LoginResDto login(LogInDto dto) {
        UserEntity user = getUserByUserId(dto.getUserId());
        if (!user.getPassword().equals(dto.getPassword())) {
            throw new RuntimeException("비밀번호가 일치하지 않습니다.");
        }
        boolean isAdmin = "root".equals(user.getUserId());
        return new LoginResDto(user.getUserId(), user.getEmail(), isAdmin);
    }

    public UserEntity deleteUser(String userId) {
        UserEntity user = getUserByUserId(userId);
        userRepository.delete(user);
        return user;
    }
    public UserEntity getUserByUsername(String username) {
        return userRepository.findByUsername(username)
            .orElseThrow(() -> new RuntimeException("username에 해당하는 유저를 찾을 수 없습니다."));
    }

}

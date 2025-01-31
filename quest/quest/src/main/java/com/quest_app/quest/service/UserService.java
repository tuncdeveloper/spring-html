package com.quest_app.quest.service;

import com.quest_app.quest.DTO.UserDto;
import com.quest_app.quest.model.User;
import com.quest_app.quest.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User toUser(UserDto userDto) {
        User user = new User();
        user.setUserId(userDto.getUserId());
        user.setUserName(userDto.getUserName());
        user.setPassword(userDto.getPassword());
        return user;
    }

    public UserDto toUserDto(User user) {
        UserDto userDto = new UserDto();
        userDto.setUserId(user.getUserId());
        userDto.setUserName(user.getUserName());
        userDto.setPassword(user.getPassword());
        return userDto;
    }

    public ResponseEntity<List<UserDto>> getAllUsers() {
        List<User> users = userRepository.findAll();
        List<UserDto> userDtos = users.stream()
                .map(this::toUserDto)
                .collect(Collectors.toList());
        return ResponseEntity.ok(userDtos);
    }

    public ResponseEntity<UserDto> getOneUserById(Integer userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("User with ID " + userId + " not found"));
        return ResponseEntity.ok(toUserDto(user));
    }

    public ResponseEntity<User> createUser(UserDto userDto) {
        User user = toUser(userDto);
        User savedUser = userRepository.save(user);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedUser);
    }

    public ResponseEntity<Void> deleteUser(Integer id) {
        if (!userRepository.existsById(id)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        userRepository.deleteById(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    public ResponseEntity<User> updateUser(Integer userId, UserDto userDto) {
        User foundUser = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("User with ID " + userId + " not found"));

        if (userDto.getUserName() != null) {
            foundUser.setUserName(userDto.getUserName());
        }
        if (userDto.getPassword() != null) {
            foundUser.setPassword(userDto.getPassword());
        }

        User updatedUser = userRepository.save(foundUser);
        return ResponseEntity.ok(updatedUser); // Return the updated user as DTO
    }

    // Kullanıcı adı ve şifreyi doğrulama metodu
    public Optional<UserDto> authenticate(String username, String password) {

        Optional<User> userOptional = userRepository.findByUserName(username);


        if (userOptional.isPresent()) {
            User user = userOptional.get();
            if (user.getPassword().equals(password)) {
                UserDto userDto = new UserDto();
                userDto.setUserId(user.getUserId());
                userDto.setUserName(user.getUserName());
                userDto.setPassword(user.getPassword());
                return Optional.of(userDto); // Kullanıcı bilgilerini döndür
            }
        }
        return Optional.empty(); // Kullanıcı bulunamazsa boş döndür
    }

}

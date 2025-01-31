package com.quest_app.quest.controller;

import com.quest_app.quest.DTO.UserDto;
import com.quest_app.quest.model.User;
import com.quest_app.quest.service.UserService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping(path = "/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping(path = "/login")
    public ResponseEntity<?> login(@RequestBody UserDto userDto, HttpServletResponse response) {
        Optional<UserDto> authenticatedUser = userService.authenticate(userDto.getUserName(), userDto.getPassword());

        if (authenticatedUser.isPresent()) {
            // Kullanıcı doğrulandı, cookie oluştur
            Cookie cookie = new Cookie("userId", authenticatedUser.get().getUserId().toString());
            cookie.setHttpOnly(true);  // JavaScript tarafından erişilemez
            cookie.setPath("/");  // Tüm uygulama genelinde geçerli

            // HTTP cevabına cookie ekle
            response.addCookie(cookie);

            return ResponseEntity.ok(authenticatedUser.get());
        } else {
            return ResponseEntity.status(401).body("Invalid username or password");
        }
    }


    @GetMapping(path = "allUsers")
    public ResponseEntity<List<UserDto>>getAllUsers(){
        return userService.getAllUsers();
    }

    @PostMapping(path = "addUser")
    public ResponseEntity<User> addUser(@RequestBody UserDto userDto){
        return userService.createUser(userDto);
    }

    @GetMapping(path = "oneUser/{id}")
    public ResponseEntity<UserDto> getOneUser(@PathVariable("id") Integer id){
        return userService.getOneUserById(id);
    }

    @DeleteMapping(path = "deleteUser/{id}")
    public void deleteUser(@PathVariable("id") Integer id){
        userService.deleteUser(id);
    }

    @PutMapping(path = "updateUser/{id}")
    public ResponseEntity<User> updateUser(@PathVariable("id") Integer id ,@RequestBody UserDto userDto){
        return userService.updateUser(id,userDto);
    }


}

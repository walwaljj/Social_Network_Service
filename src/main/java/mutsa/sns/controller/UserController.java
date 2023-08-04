package mutsa.sns.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import mutsa.sns.domain.dto.user.UserRequestDto;
import mutsa.sns.domain.dto.user.UserResponseDto;
import mutsa.sns.exception.CustomException;
import mutsa.sns.exception.ErrorCode;
import mutsa.sns.security.CustomUserDetailsManager;
import mutsa.sns.security.jwt.JwtRequestDto;
import mutsa.sns.security.jwt.JwtResponseDto;
import mutsa.sns.service.UserService;
import org.springframework.boot.autoconfigure.neo4j.Neo4jProperties;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
@Slf4j
public class UserController {

    private final UserService userService;
    private final CustomUserDetailsManager manager;

    @PostMapping("/sign")
    public UserResponseDto sign(@Valid @RequestBody UserRequestDto userDto){

        return userService.sign(userDto);
    }

    @PostMapping("/login")
    public JwtResponseDto login(@RequestBody JwtRequestDto userDto){
        return userService.login(userDto);
    }

    @GetMapping("/profile")
    public UserResponseDto userProfile(Authentication auth){
        log.info("getPrincipal = {}",auth.getPrincipal().toString());
        log.info("name = {}", auth.getName());

        return userService.findByUserName(auth.getName());
    }

    @PutMapping(value = "/profile/image", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public UserResponseDto updateProfileImage(@RequestBody() MultipartFile image, Authentication auth) throws IOException {

        return userService.updateProfileImage(image, auth.getName());

    }

}

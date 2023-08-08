package mutsa.sns.service;

import lombok.RequiredArgsConstructor;

import lombok.extern.slf4j.Slf4j;
import mutsa.sns.domain.dto.user.UserRequestDto;
import mutsa.sns.domain.dto.user.UserResponseDto;
import mutsa.sns.domain.entity.UserEntity;
import mutsa.sns.exception.CustomException;
import mutsa.sns.exception.ErrorCode;
import mutsa.sns.repository.UserRepository;
import mutsa.sns.security.CustomUserDetailsManager;
import mutsa.sns.security.config.PasswordEncoderConfig;
import mutsa.sns.security.jwt.JwtRequestDto;
import mutsa.sns.security.jwt.JwtResponseDto;
import mutsa.sns.security.jwt.JwtUtils;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;


import java.io.IOException;

import java.nio.file.Path;


@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoderConfig passwordEncoder;
    private final CustomUserDetailsManager manager;
    private final JwtUtils jwtUtils;
    private final ImageService imageService;
    public UserResponseDto sign(UserRequestDto userRequestDto){

        if(manager.userExists(userRequestDto.getUsername())){
            throw new CustomException(ErrorCode.DUPLICATED_USER);
        }

        if(!userRequestDto.getPassword().equals(userRequestDto.getPasswordReChk())){
            throw new CustomException(ErrorCode.PASSWORD_NOT_MATCH);
        }

        UserEntity userEntity = UserEntity.builder()
                .username(userRequestDto.getUsername())
                .password(passwordEncoder.passwordEncoder().encode(userRequestDto.getPassword()))
                .email((userRequestDto).getEmail())
                .phoneNumber((userRequestDto).getPhoneNumber()).build();
        return UserResponseDto.fromEntity(userRepository.save(userEntity));

    }

    public JwtResponseDto login(JwtRequestDto userDto) {

        if(!manager.userExists(userDto.getUsername())){
            throw new CustomException(ErrorCode.NOT_FOUND,userDto.getUsername());
        }

        UserDetails userDetails = manager.loadUserByUsername(userDto.getUsername());

        if(!passwordEncoder.passwordEncoder().matches(userDto.getPassword(), userDetails.getPassword())){
            throw new CustomException(ErrorCode.PASSWORD_NOT_MATCH);
        }


        JwtResponseDto userResponseDto = JwtResponseDto.builder().username(userDto.getUsername())
                                                .token(jwtUtils.generateToken(userDetails)).build();

        log.info("tokenDto = {} ",userResponseDto.getToken());

        return userResponseDto;
    }

    public void logout(){

    }

    public UserResponseDto findByUserName(String username){
        return UserResponseDto.fromEntity(userRepository.findByUsername(username)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND, username)));

    }

    public UserResponseDto updateProfileImage(MultipartFile image, String username) throws IOException {

        UserEntity userEntity = userRepository.findByUsername(username)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND, username));

        if(image.isEmpty() || image == null){
            return UserResponseDto.fromEntity(userRepository.save(userEntity));
        }
        // TODO 이미 프로필 이미지가 있을 때 삭제코드 (삭제하지 않으면 이미지가 쌓임..)
//        if(!userEntity.getProfileImgUrl().isBlank()){
//            imageService.deleteImage(username);
//        }
        // 이미지 업로드
        Path profileImgUrl = imageService.uploadProfileImage(username ,image);
        userEntity.setProfileImgUrl(String.valueOf(profileImgUrl));

        return UserResponseDto.fromEntity(userRepository.save(userEntity));
    }


}

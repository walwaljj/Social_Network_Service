package mutsa.sns.domain.dto.user;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Getter;
import mutsa.sns.domain.entity.UserEntity;

@Builder
@Getter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserResponseDto {

    private String username;
    private String email;
    private String phoneNumber;
    private String profileImgUrl;

    public static UserResponseDto fromEntity(UserEntity userEntity){
        return UserResponseDto.builder()
                .username(userEntity.getUsername())
                .profileImgUrl(userEntity.getProfileImgUrl())
                .email(userEntity.getEmail())
                .phoneNumber(userEntity.getPhoneNumber()).build();
    }
}

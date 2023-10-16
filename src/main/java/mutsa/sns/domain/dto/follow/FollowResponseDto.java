package mutsa.sns.domain.dto.follow;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class FollowResponseDto {

    private String userName;
    private String followingUserName;
    public static FollowResponseDto toFollowDto(String username, String followingUsername){
        return FollowResponseDto.builder()
                .userName(username)
                .followingUserName(followingUsername)
                .build();
    }
}

package mutsa.sns.domain.dto.friend;

import lombok.Builder;
import lombok.Getter;
import mutsa.sns.domain.entity.FriendEntity;
import mutsa.sns.domain.entity.FriendStatus;

@Getter
@Builder
public class FriendResponseDto {
    private String senderName;
    private String recipientName;
    private FriendStatus status;

    public static FriendResponseDto getFriendName(String senderName, String recipientName, FriendStatus status){
        return FriendResponseDto.builder()
                .senderName(senderName)
                .recipientName(recipientName)
                .status(status)
                .build();
    }

}

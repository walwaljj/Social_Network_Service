package mutsa.sns.security.jwt;

import lombok.Builder;
import lombok.Getter;


@Builder
@Getter
public class JwtResponseDto {

    private String username;
    private String token;

}

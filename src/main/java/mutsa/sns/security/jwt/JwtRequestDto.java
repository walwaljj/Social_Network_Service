package mutsa.sns.security.jwt;

import lombok.Getter;

@Getter
public class JwtRequestDto {

    private String username;
    private String password;
}

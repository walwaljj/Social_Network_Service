package mutsa.sns.domain.dto.user;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class UserRequestDto {

//    private Integer id;
    @NotEmpty(message = "사용자ID는 빈 값일 수 없습니다.")
    private String username;
    @NotEmpty(message = "비밀번호는 빈 값일 수 없습니다.")
    private String password;
    @NotEmpty(message = "비밀번호는 확인은 빈 값일 수 없습니다.")
    private String passwordReChk;
    private String email;
    @Pattern(regexp = "(010-\\d{4}-\\d{4})" , message = "핸드폰 번호 형식을 확인해 주세요.")
    private String phoneNumber;

}

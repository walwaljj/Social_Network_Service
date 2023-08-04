package mutsa.sns.domain.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;

@Entity
@Builder
@Getter
@Table(name = "users")
@NoArgsConstructor
@AllArgsConstructor
public class UserEntity {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column(nullable = false , unique = true)
    @NotEmpty(message = "사용자 ID는 빈 값일 수 없습니다.")
    private String username;
    @Column(nullable = false)
    @NotEmpty(message = "비밀번호는 빈 값일 수 없습니다.")
    private String password;
    @Column(unique = true)
    @Email(message = "이메일 형식이 아닙니다.")
    private String email;

    private String phoneNumber;
    @Setter
    private String profileImgUrl;
}

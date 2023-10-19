package mutsa.sns.domain.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "friend")
public class FriendEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "sender")
    private UserEntity sender;

    @ManyToOne
    @JoinColumn(name = "recipient")
    private UserEntity recipient;

    @Enumerated(EnumType.STRING)
    private FriendStatus status = FriendStatus.REQUEST;

    public void statusUpdate(FriendStatus status){
        this.status = status;
    }
}

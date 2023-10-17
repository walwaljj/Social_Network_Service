package mutsa.sns.repository;

import mutsa.sns.domain.entity.FriendEntity;
import mutsa.sns.domain.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface FriendRepository extends JpaRepository<FriendEntity, Integer> {
    Optional<FriendEntity> findBySenderAndRecipient(UserEntity sender, UserEntity recipient);
    Optional<Set<FriendEntity>> findAllBySender(UserEntity userEntity);
    Optional<Set<FriendEntity>> findAllByRecipient(UserEntity userEntity);
    Optional<List<FriendEntity>>  findByRecipient(UserEntity loginUserEntity);
}

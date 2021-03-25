package ch.codeattila.api.api.repository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import ch.codeattila.api.api.models.Friend;


@Repository
public interface FriendRepository extends JpaRepository<Friend, Long>{

}

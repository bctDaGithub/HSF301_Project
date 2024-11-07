package hsf301.fe.project.repository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import hsf301.fe.project.pojo.Users;
import jakarta.transaction.Transactional;
import java.util.List;


@Repository
public interface IUsersRepository extends JpaRepository<Users, Integer> {
    @Transactional
    @Modifying
    @Query("UPDATE Users u SET u.active = ?2 WHERE u.id = ?1")
    void updateActive(int userId, boolean active);

    Users findByEmail(String email);
}

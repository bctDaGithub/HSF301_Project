package hsf301.fe.project.repository;

import hsf301.fe.project.pojo.Wallet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface WalletRepository extends JpaRepository<Wallet, Integer> {
    @Query("SELECT w FROM Wallet w WHERE w.userID = ?1")
    Optional<Wallet> findbyuserid(Integer userId);

}
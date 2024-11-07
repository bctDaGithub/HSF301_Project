package hsf301.fe.project.repository;

import hsf301.fe.project.pojo.Flower;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FlowerRepository extends JpaRepository<Flower, Integer> {
    List<Flower> findAllBySellerId(int sellerId);
}
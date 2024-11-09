package hsf301.fe.project.repository;

import hsf301.fe.project.pojo.Order;
import hsf301.fe.project.pojo.OrderFlower;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderFlowerRepository extends JpaRepository<OrderFlower, Integer> {
    List<OrderFlower> findAllByOrder(Order order);
}

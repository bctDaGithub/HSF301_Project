package hsf301.fe.project.repository;

import hsf301.fe.project.pojo.Order;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Integer> {
    List<Order> findAllByCustomer_Id(int customerId);
}

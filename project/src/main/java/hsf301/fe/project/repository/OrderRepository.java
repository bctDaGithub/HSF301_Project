package hsf301.fe.project.repository;

import hsf301.fe.project.pojo.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderRepository extends JpaRepository<Order, Integer> {
    // Bạn có thể thêm các phương thức truy vấn tùy chỉnh nếu cần, ví dụ:
    // List<Order> findByCustomer(Users customer);
}

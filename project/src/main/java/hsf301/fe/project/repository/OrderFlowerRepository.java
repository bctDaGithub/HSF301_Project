package hsf301.fe.project.repository;

import hsf301.fe.project.pojo.OrderFlower;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderFlowerRepository extends JpaRepository<OrderFlower, Integer> {
    // Thêm các phương thức truy vấn tùy chỉnh nếu cần, ví dụ:
    // List<OrderFlower> findByOrder(Order order);
}

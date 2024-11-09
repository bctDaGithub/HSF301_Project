package hsf301.fe.project.service.defines;

import hsf301.fe.project.pojo.Order;
import hsf301.fe.project.pojo.OrderFlower;
import java.util.List;

public interface IOrderFlowerService {
    OrderFlower createOrderFlower(OrderFlower orderFlower);
    OrderFlower getOrderFlowerById(int orderFlowerId);
    List<OrderFlower> getAllOrderFlowers();
    OrderFlower updateOrderFlower(OrderFlower orderFlower);
    void deleteOrderFlower(int orderFlowerId);
    List<OrderFlower> getOrderFlowersByOrder(Order order);
}

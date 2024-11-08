package hsf301.fe.project.service.defines;

import hsf301.fe.project.pojo.Order;
import java.util.List;

public interface IOrderService {
    Order createOrder(Order order);
    Order getOrderById(int orderId);
    List<Order> getAllOrders();
    Order updateOrder(Order order);
    void deleteOrder(int orderId);
    List<Order> getOrdersByCustomerId(int customerId);
}
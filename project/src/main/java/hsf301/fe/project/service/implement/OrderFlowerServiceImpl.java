package hsf301.fe.project.service.implement;

import hsf301.fe.project.pojo.Order;
import hsf301.fe.project.pojo.OrderFlower;
import hsf301.fe.project.repository.OrderFlowerRepository;
import hsf301.fe.project.service.defines.IOrderFlowerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class OrderFlowerServiceImpl implements IOrderFlowerService {

    @Autowired
    private OrderFlowerRepository orderFlowerRepository;

    @Override
    public OrderFlower createOrderFlower(OrderFlower orderFlower) {
        return orderFlowerRepository.save(orderFlower);
    }

    @Override
    public OrderFlower getOrderFlowerById(int orderFlowerId) {
        return orderFlowerRepository.findById(orderFlowerId).orElse(null);
    }

    @Override
    public List<OrderFlower> getAllOrderFlowers() {
        return orderFlowerRepository.findAll();
    }

    @Override
    public OrderFlower updateOrderFlower(OrderFlower orderFlower) {
        return orderFlowerRepository.save(orderFlower);
    }

    @Override
    public void deleteOrderFlower(int orderFlowerId) {
        orderFlowerRepository.deleteById(orderFlowerId);
    }

    @Override
    public List<OrderFlower> getOrderFlowersByOrder(Order order) {
        return orderFlowerRepository.findAllByOrder(order);
    }
}

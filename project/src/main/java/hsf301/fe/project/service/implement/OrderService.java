package hsf301.fe.project.service.implement;

import hsf301.fe.project.pojo.*;
import hsf301.fe.project.repository.CartItemRepository;
import hsf301.fe.project.repository.FlowerRepository;
import hsf301.fe.project.repository.OrderFlowerRepository;
import hsf301.fe.project.repository.OrderRepository;
import jakarta.servlet.http.HttpSession;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class OrderService {
    @Autowired
    WalletService walletService;
    @Autowired
    private OrderRepository orderRepository; // Repository để lưu thông tin đơn hàng
    @Autowired
    private OrderFlowerRepository orderFlowerRepository;

    @Autowired
    private FlowerRepository flowerRepository;

    @Autowired
    private CartItemRepository cartItemRepository;

    @Transactional
    public String processOrder(Users user, List<Integer> cartItemIds, Long totalPrice, Model model, HttpSession httpSession) {
        // Retrieve the wallet of the buyer
        Wallet buyerWallet = walletService.getWalletByUserId(user.getId());

        // Check if the buyer's wallet has enough balance
        if (buyerWallet.getAmount() < totalPrice) {
            model.addAttribute("message", "Please top up your wallet to complete the payment.");
            return "Customer/carts";
        }

        // Retrieve the CartItems based on cartItemIds
        List<CartItem> cartItems = cartItemRepository.findAllById(cartItemIds);

        // Check if the cart items list is not empty
        if (cartItems.isEmpty()) {
            model.addAttribute("message", "Your cart is empty.");
            return "Customer/carts";
        }

        // Create a new order
        Order order = new Order();
        order.setCustomer(user);  // Set the customer (buyer)
        order.setDate(new Date());
        order.setTotalPrice(totalPrice);
        order.setStatus("success");

        // Determine the seller of the flowers in the cart (assuming all items are from the same seller)
        // If cart items have flowers from different sellers, we need to handle this differently
        Users seller = cartItems.get(0).getFlower().getSeller(); // Take the seller from the first cart item

        // Optionally, if sellers are different for each cart item, you can handle this logic accordingly
        order.setSeller(seller);  // Set the seller on the order (the first seller found)

        orderRepository.save(order); // Save the order

        Map<Users, Long> sellerEarnings = new HashMap<>(); // Map to track seller earnings

        for (CartItem cartItem : cartItems) {
            Flower flower = cartItem.getFlower();
            Users flowerSeller = flower.getSeller(); // Retrieve the seller from the flower linked in the cart item

            // Calculate the total amount that the seller will earn
            Long itemTotalPrice = (long) cartItem.getTotalPrice();
            sellerEarnings.merge(flowerSeller, itemTotalPrice, Long::sum); // Accumulate earnings for the seller

            // Create a record in OrderFlower
            OrderFlower orderFlower = new OrderFlower();
            orderFlower.setOrder(order);
            orderFlower.setFlower(flower);
            orderFlower.setQuantity(cartItem.getQuantity());
            orderFlowerRepository.save(orderFlower);

            // Decrease the quantity of the flower in stock
            flower.setQuantity(flower.getQuantity() - cartItem.getQuantity());
            flowerRepository.save(flower);  // Save updated flower quantity
        }

        // Update the seller's wallet with their earnings
        for (Map.Entry<Users, Long> entry : sellerEarnings.entrySet()) {
            Users sellerKey = entry.getKey();  // Seller object
            Long earnings = entry.getValue();  // Earnings for this seller

            // Retrieve and update the seller's wallet
            Wallet sellerWallet = walletService.getWalletByUserId(sellerKey.getId());
            sellerWallet.setAmount(sellerWallet.getAmount() + earnings);  // Add earnings to seller's wallet
            walletService.save1(sellerWallet);  // Save the updated wallet
        }

        // Deduct the total price from the buyer's wallet
        buyerWallet.setAmount(buyerWallet.getAmount() - totalPrice);
        walletService.save1(buyerWallet);  // Save the updated wallet

        // Delete the processed CartItems (remove them from the cart)
        cartItemRepository.deleteAll(cartItems);

        // Add success message and redirect to the orders page
        model.addAttribute("message", "Order placed successfully!");
        return "Customer/home";  // Redirect to the home page after successful order placement
    }

}

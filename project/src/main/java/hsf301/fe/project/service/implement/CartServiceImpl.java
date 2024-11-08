package hsf301.fe.project.service.implement;

import hsf301.fe.project.pojo.Cart;
import hsf301.fe.project.pojo.CartItem;
import hsf301.fe.project.pojo.Users;
import hsf301.fe.project.repository.CartRepository;
import hsf301.fe.project.repository.IUsersRepository;
import hsf301.fe.project.service.defines.ICartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CartServiceImpl implements ICartService {

    @Autowired
    private CartRepository cartRepository;
    @Autowired
    private IUsersRepository usersRepository;

    @Override
    public Cart getCartByUserId(int userId) {
        Cart cart = cartRepository.findByCustomerId(userId).orElse(new Cart());
        if(cart.getCustomer()==null){
            Users user = usersRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));
            cart.setCustomer(user);
            cartRepository.save(cart);
        }
        return cart;
    }

    @Override
    public Cart addItemToCart(int userId, CartItem item) {
        Cart cart = cartRepository.findByCustomerId(userId).orElse(new Cart());
        Users user = usersRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));
        cart.setCustomer(user);
        cart.getItems().add(item);

        return cartRepository.save(cart);
    }

    @Override
    public Cart updateCart(Cart cart) {

        return cartRepository.save(cart);
    }

    @Override
    public void clearCart(int userId) {
        Cart cart = cartRepository.findByCustomerId(userId).orElseThrow(() -> new RuntimeException("Cart not found"));
        cart.getItems().clear();

        cartRepository.save(cart);
    }


}
package hsf301.fe.project.service.defines;

import hsf301.fe.project.pojo.Cart;
import hsf301.fe.project.pojo.CartItem;

import java.util.List;
import java.util.Optional;

public interface ICartService {
    Cart getCartByUserId(int userId);
    Cart addItemToCart(int userId, CartItem item);
    Cart updateCart(Cart cart);
    void clearCart(int userId);

}

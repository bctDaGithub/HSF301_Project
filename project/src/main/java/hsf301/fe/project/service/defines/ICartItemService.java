package hsf301.fe.project.service.defines;

import hsf301.fe.project.pojo.CartItem;

import java.util.List;

public interface ICartItemService {
    CartItem addCartItem(CartItem item, int flowerId, int userId);
    CartItem updateCartItem(CartItem item);
    void deleteCartItem(int cartItemId);
    List<CartItem> getCartItemsByCartId(int cartId);
    List<CartItem> getCartItemsById(List<Integer> cartItemIds);
}
package hsf301.fe.project.service.implement;

import hsf301.fe.project.pojo.CartItem;
import hsf301.fe.project.repository.CartItemRepository;
import hsf301.fe.project.service.defines.ICartItemService;
import hsf301.fe.project.service.defines.ICartService;
import hsf301.fe.project.service.defines.IFlowerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CartItemServiceImpl implements ICartItemService {

    @Autowired
    private CartItemRepository cartItemRepository;
    @Autowired
    private IFlowerService flowerService;
    @Autowired
    private ICartService cartService;

    @Override
    public CartItem addCartItem(CartItem item,int flowerId, int userId) {
        CartItem cartItem = new CartItem();
        cartItem.setCart(cartService.getCartByUserId(userId));
        cartItem.setFlower(flowerService.getFlowerById(flowerId));
        cartItem.setQuantity(item.getQuantity());
        cartItem.setUnitPrice(item.getUnitPrice());
        return cartItemRepository.save(cartItem);
    }

    @Override
    public CartItem updateCartItem(CartItem item) {
        return cartItemRepository.save(item);
    }

    @Override
    public void deleteCartItem(int cartItemId) {
        cartItemRepository.deleteById(cartItemId);
    }

    @Override
    public List<CartItem> getCartItemsByCartId(int cartId) {
        return cartItemRepository.findAllByCartId(cartId);
    }
}

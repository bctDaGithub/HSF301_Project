package hsf301.fe.project.pojo;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "cart_items")
@Getter
@Setter
@NoArgsConstructor
public class CartItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cart_id", nullable = false)
    private Cart cart;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "flower_id", nullable = false)
    private Flower flower;  // Liên kết với bảng Flower

    private int quantity;

    @Column(name = "unit_price")
    private double unitPrice;

    // Phương thức tính tổng giá cho một CartItem
    public double getTotalPrice() {
        return this.unitPrice * this.quantity;
    }
}

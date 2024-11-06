package hsf301.fe.project.pojo;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.persistence.ManyToOne;
import java.util.List;
import java.util.Date;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name= "orders")
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int orderId;

    @ManyToOne
    @JoinColumn(name = "customer_id")
    private Users customer;

    @ManyToOne
    @JoinColumn(name = "shipper_id")
    private Users shipper;

    @OneToMany(mappedBy = "order")
    private List<OrderFlower> orderFlowers; 

    private Date date;
    private double totalPrice;
    private String status;
}

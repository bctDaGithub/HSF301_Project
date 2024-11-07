package hsf301.fe.project.pojo;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "flowers")
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Flower {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(nullable = false, length = 100)
    private String name;

    private String type;
    private int quantity;
    private String condition;

    private double price;

    @Column(name = "image_url")
    private String imageUrl;

    @ManyToOne
    @JoinColumn(name = "seller_id")
    private Users seller;
}

package hsf301.fe.project.pojo;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

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

    @NotBlank(message = "Flower name is required")
    @Size(max = 100, message = "Flower name must be less than 100 characters")
    @Column(nullable = false)
    private String name;

    private String type; 
    private int quantity; 
    private String condition;

    @NotNull(message = "Price price is required")
    @Positive(message = "Price price must be positive")
    private double price;
    @Column(name = "image_url")
    private String imageUrl;

    @ManyToOne
    @JoinColumn(name = "seller_id") 
    private Users seller;
}

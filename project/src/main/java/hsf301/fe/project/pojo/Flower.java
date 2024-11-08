    package hsf301.fe.project.pojo;

    import jakarta.persistence.*;
    import lombok.*;

    @Entity
    @Table(name = "flowers")
    @Getter
    @Setter
    @NoArgsConstructor
    public class Flower {
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private int id;
        @Column(nullable = false)
        private String name;
        @Column(nullable = false)
        private String type;
        @Column(nullable = false)
        private int quantity;
        @Column(nullable = false)
        private String condition;
        @Column(nullable = false)
        private double price;

        @Column(name = "image_url")
        private String imageUrl;

        @ManyToOne
        @JoinColumn(name = "seller_id")
        private Users seller;
    }

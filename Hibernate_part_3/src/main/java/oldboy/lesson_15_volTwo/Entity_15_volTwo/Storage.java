package oldboy.lesson_15_volTwo.Entity_15_volTwo;

import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "storageAddress")
@ToString(exclude = "distributionCenters")
@Builder
@Entity
@Table(name = "warehouses", schema = "training_base")
public class Storage {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "storage_id")
    private Long storageId;

    @Column(name = "storage_address", nullable = false, unique = true)
    private String storageAddress;

    @Builder.Default
    /* Те же смыслы, что и в Driver.java см. описание */
    @OneToMany(mappedBy = "storage")
    private List<DistributionCenter> distributionCenters = new ArrayList<>();
}
package oldboy.lesson_15_volOne.Entity_15_volOne;

import lombok.*;
import oldboy.lesson_15_volTwo.Entity_15_volTwo.DistributionCenter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.util.ArrayList;
import java.util.List;

@Data
@EqualsAndHashCode(callSuper=false, exclude = "distributionCenters")
@ToString(exclude = "distributionCenters")
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "drivers", schema = "training_base")
public class Driver extends BaseEntity<Integer>{

    @Column(name = "driver_name")
    private String driverName;
    @Column(name = "driver_age")
    private Integer driverAge;
    @Column(name = "car_model")
    private String carModel;

    @Builder.Default
    /* Повторимся:
    Один Driver может относиться ко многим складам (условно развозить груз),
    отсюда отношение OneToMany и поскольку ...ToMany, это коллекция.
    Параметр mappedBy указывает на поле класса DistributionCenter связанное
    с текущим.
    */
    @OneToMany(mappedBy = "driver")
    private List<DistributionCenter> distributionCenters = new ArrayList<>();
}

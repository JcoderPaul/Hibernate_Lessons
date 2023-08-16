package oldboy.lesson_11.MappingEntity;

import lombok.*;
import org.hibernate.annotations.Cascade;

import javax.persistence.*;

@Entity
@Data
/*
Исключаем поля, которые могут зациклить программу при их вызове
(обращении к ним), т.к. они могут быть связанный с множеством,
других сущностей каскадно - Stack Overflow.
*/
@ToString
@EqualsAndHashCode(exclude = "address")
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "drivers", schema = "public")
public class Driver {

    @Id
    @Column (name = "driver_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer driverId;

    @Column(name = "driver_name")
    private String driverName;

    @Column(name = "salary")
    private Double salary;

    private Integer experience;
    /*
    Маппинг mappedBy = "driver" тут совпадает с
    полем private Driver driver в классе Address
    */
    @OneToOne(mappedBy = "driver")
    @Cascade(org.hibernate.annotations.CascadeType.SAVE_UPDATE)
    private Address address;
}

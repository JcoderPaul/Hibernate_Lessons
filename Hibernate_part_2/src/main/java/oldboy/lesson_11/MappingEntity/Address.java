package oldboy.lesson_11.MappingEntity;

import lombok.*;
import org.hibernate.annotations.Cascade;

import javax.persistence.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
/*
Исключаем поля, которые могут зациклить программу при их вызове
(обращении к ним), т.к. они могут быть связанный с множеством,
других сущностей каскадно - Stack Overflow.
*/
@ToString (exclude = "driver")
@Entity
@Table(name = "addresses", schema = "public")
public class Address {

    @Id
    @Column(name = "address_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer addressId;

    @OneToOne
    @JoinColumn(name = "driver_id")
    @Cascade(org.hibernate.annotations.CascadeType.SAVE_UPDATE)
    private Driver driver;

    private String street;

    private String language;

    public void setDriver(Driver driver) {
        driver.setAddress(this);
        this.driver = driver;
    }
}
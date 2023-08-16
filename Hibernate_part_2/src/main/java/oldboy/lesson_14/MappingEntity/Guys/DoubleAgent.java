package oldboy.lesson_14.MappingEntity.Guys;

import lombok.*;
import oldboy.lesson_14.MappingEntity.SecretService;

import javax.persistence.*;

@Entity
@Data
@ToString(exclude = "bureau")
@EqualsAndHashCode(exclude = "bureau")
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "doubles", schema = "public")
/* !!! Очень неудачное название для класса - совпадает с типом данных Double !!! */
public class DoubleAgent implements Comparable<DoubleAgent>, Agent {
    @Id
    @Column(name = "double_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer doubleId;

    @Column(name = "double_name")
    private String doubleName;

    @Column(name = "salary")
    private Double salary;

    @ManyToOne(targetEntity = SecretService.class)
    /*
    Аннотация указывающая на какую колонку ссылается
    данное поле, вместо @Column, но более широкая, т.к.
    применяется внешняя ссылка на другую таблицу.
    */
    @JoinColumn(name = "bureau_id")
    private SecretService bureau;

    @Override
    public int compareTo(DoubleAgent forCompare) {
        return doubleName.compareTo(forCompare.doubleName);
    }
}

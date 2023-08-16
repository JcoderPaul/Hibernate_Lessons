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
@Table(name = "traitors", schema = "public")
public class Traitor implements Comparable<Traitor>, Agent {
    @Id
    @Column(name = "traitor_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer traitorId;

    @Column(name = "traitor_name")
    private String traitorName;

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
    public int compareTo(Traitor forCompare) {
        return traitorName.compareTo(forCompare.traitorName);
    }
}

package oldboy.lesson_14.MappingEntity.Guys;

import lombok.*;
import oldboy.lesson_14.MappingEntity.SecretService;

import javax.persistence.*;

@Entity
@Data
/*
Исключаем поля, которые могут зациклить программу при их вызове
(обращении к ним), т.к. они могут быть связанный с множеством,
других сущностей каскадно - Stack Overflow.
*/
@ToString(exclude = "bureau")
@EqualsAndHashCode(exclude = "bureau")
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "spies", schema = "public")
public class Spy implements Agent {

    @Id
    @Column (name = "spy_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer spyId;

    @Column(name = "spy_name")
    private String spyName;

    @Column(name = "salary")
    private Double salary;

    /*
    Аннотация многие-к-одному, параметр можно и не указывать
    Многие-к-одному означает, что многие пользователи, могут
    принадлежать к одной компании, т.е. много людей могут
    работать в одной фирме.
    */
    @ManyToOne(targetEntity = SecretService.class)
    /*
    Аннотация указывающая на какую колонку ссылается
    данное поле, вместо @Column, но более широкая, т.к.
    применяется внешняя ссылка на другую таблицу.
    */
    @JoinColumn(name = "bureau_id")
    private SecretService bureau;
}

package oldboy.lesson_10.MappingEntity;

import lombok.*;

import javax.persistence.*;

@Entity
@Data
/*
Исключаем поля, которые могут зациклить программу при их вызове
(обращении к ним), т.к. они могут быть связанный с множеством,
других сущностей каскадно - Stack Overflow.
*/
@ToString(exclude = "firm")
@EqualsAndHashCode(exclude = "firm")
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "firm_workers", schema = "public")
public class Worker {

    @Id
    @Column (name = "worker_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer workerId;

    @Column(name = "worker_name")
    private String workerName;

    @Column(name = "salary")
    private Double salary;

    /*
    Аннотация многие-к-одному, параметр можно и не указывать
    Многие-к-одному означает, что многие пользователи, могут
    принадлежать к одной компании, т.е. много людей могут
    работать в одной фирме.

    Еще раз многие CompanyUser связаны с одной Company.
    */
    @ManyToOne(targetEntity = Firm.class)
    /*
    Аннотация указывающая на какую колонку ссылается
    данное поле, вместо @Column, но более широкая, т.к.
    применяется внешняя ссылка на другую таблицу.
    */
    @JoinColumn(name = "firm_id")
    private Firm firm;
}

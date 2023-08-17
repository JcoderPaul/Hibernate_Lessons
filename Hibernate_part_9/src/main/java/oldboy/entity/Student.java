package oldboy.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.OptimisticLockType;
import org.hibernate.annotations.OptimisticLocking;

import javax.persistence.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "students", schema = "part_four_base")
/* Устанавливаем аннотацию управляющую оптимистической блокировкой транзакций */
@OptimisticLocking(type = OptimisticLockType.ALL)
/*
Поскольку запросы в Hibernate формируются динамически,
исходя из наших аннотаций примененных к классам и их
полям, нам нужна еще одна аннотация.
*/
@DynamicUpdate
public class Student {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "stud_id")
    private Long studId;

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "last_name")
    private String lastName;

    @Column(name = "scholarship", nullable = false)
    private Integer scholarship;
}

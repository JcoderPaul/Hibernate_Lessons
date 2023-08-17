package oldboy.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.OptimisticLockType;
import org.hibernate.annotations.OptimisticLocking;

import javax.persistence.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "workers", schema = "part_four_base")
/* Устанавливаем аннотацию управляющую оптимистической блокировкой транзакций */
@OptimisticLocking(type = OptimisticLockType.VERSION)
public class Worker {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "worker_id")
    private Long workerId;

    /*
    Данное поле создает и аннотируется в случае, если над
    классом установлена аннотация с параметром:
    @OptimisticLocking(type = OptimisticLockType.VERSION)
    */
    @Version
    private Long version;

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "last_name")
    private String lastName;

    @Column(name = "amount", nullable = false)
    private Integer amount;
}

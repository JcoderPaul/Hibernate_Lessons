package oldboy.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.envers.Audited;
import org.hibernate.envers.NotAudited;

import javax.persistence.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "payments")
/*
Данная аннотация подключает функционал org.hibernate.envers
и аудирует (фиксирует) изменения в нашей таблице, по примеру
GIT, т.е. запоминает прошлые состояния.
*/
@Audited
public class Payment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Integer amount;

    /*
    Однако при аудировании данной Payment сущности, функционал HibernateEnvers
    будет аудировать и связные сущности, тогда и их тоже необходимо аннотировать.
    ОДНАКО, если мы не планируем этого делать, то поле связной сущности аннотируется
    по-другому:
    */
    @NotAudited
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "receiver_id")
    private User receiver;
}

package oldboy.lesson_17.Entity_17;

import lombok.*;
import oldboy.entity.Company;
import oldboy.entity.accessory.BaseEntity;
import oldboy.entity.accessory.PersonalInfo;
import org.hibernate.annotations.Cascade;

import javax.persistence.*;
@Data
@NoArgsConstructor
@AllArgsConstructor
/* Чтобы избежать сваливания в цикл исключим поле "company" */
@EqualsAndHashCode(exclude = "company")
@ToString(exclude = "company")
@Entity
@Table(name = "workers", schema = "training_base")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "type")
public abstract class Worker implements Comparable<Worker>, BaseEntity<Long> {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long Id;
    /*
    Если заглянуть в класс PersonalInfo, то видно, что поля класса
    имеют названия отличающиеся от тех, что имеет соответствующая
    таблица БД. Поэтому мы должны привести в соответствие полей,
    классов и колонок (полей) БД. Этим и занимается данная аннотация.
    */
    @AttributeOverride(name = "birthDate", column = @Column(name = "birth_date"))
    @AttributeOverride(name = "firstName", column = @Column(name = "first_name"))
    @AttributeOverride(name = "lastName", column = @Column(name = "last_name"))
    private PersonalInfo personalInfo;

    @Column(name = "worker_email", unique = true)
    private String workerEmail;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "company_id") // company_id
    @Cascade(org.hibernate.annotations.CascadeType.SAVE_UPDATE)
    private Company company;

    @Override
    public int compareTo(Worker o) {
        return workerEmail.compareTo(o.workerEmail);
    }

    @Override
    public void setId(Long id) {
        this.Id = id;
    }

    @Override
    public Long getId() {
        return Id;
    }
}
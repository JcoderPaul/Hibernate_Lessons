package oldboy.lesson_18.Entity_18;

import lombok.*;
import oldboy.entity.Company;
import oldboy.entity.accessory.BaseEntity;
import oldboy.entity.accessory.PersonalInfo;
import org.hibernate.annotations.Cascade;

import javax.persistence.*;
@Data
@NoArgsConstructor
/* Наш родительский класс лишается @Builder */
@AllArgsConstructor
/* Чтобы избежать сваливания в цикл исключим поле "company" */
@EqualsAndHashCode(exclude = "company")
@ToString(exclude = "company")
/*
Поскольку Hibernate должен взаимодействовать с данной
сущностью, мы помечаем класс аннотацией @Entity, а
поскольку в нашем примере данный класс НЕ БУДЕТ иметь
таблицы в БД у нас и НЕТ аннотации @Table.
*/
@Entity
@Table(name = "service_specialist", schema = "training_base")
@Inheritance(strategy = InheritanceType.JOINED)
public abstract class CarServiceSpecialist implements Comparable<CarServiceSpecialist>, BaseEntity<Long> {
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

    @Column(name = "specialist_email", unique = true)
    private String specialistEmail;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "company_id") // company_id
    @Cascade(org.hibernate.annotations.CascadeType.SAVE_UPDATE)
    private Company company;

    @Override
    public int compareTo(CarServiceSpecialist o) {
        return specialistEmail.compareTo(o.specialistEmail);
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
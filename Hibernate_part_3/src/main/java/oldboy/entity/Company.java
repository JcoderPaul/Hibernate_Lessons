package oldboy.entity;

import lombok.*;
import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.SortNatural;

import javax.persistence.*;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "companyName")
@ToString(exclude = "users")
@Builder
@Entity
@Table(name = "company", schema = "training_base")
public class Company {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "company_id")
    private Integer companyId;

    @Column(name = "company_name",
            nullable = false,
            unique = true)
    private String companyName;

    @Builder.Default
    /*
    Первое слово в аннотации относится к самому классу, т.е.
    ONE - одна Company, TO - ко, MANY - множеству, в нашем
    случае, User-ов.

    При этом toMany - всегда означает коллекцию (List, Set, Map)

    Так же мы явно указываем ПОЛЕ в классе (сущности) User с которой
    осуществляется связь и это поле 'private Company company' см.
    oldboy/entity/User.java
    */
    @OneToMany(mappedBy = "company",
               /*
               Параметр 'orphanRemoval' определяет, что нам делать с
               содержимым коллекции в случае попыток ее изменить,
               например, удалить из нее сущность. Т.е. как будет
               влиять, например, удаление на все остальные таблицы
               БД. В случае true - никак, да сущность из коллекции
               будет удалена, но это не вызовет каскадного удаления
               других связных сущностей.
               */
               orphanRemoval = true)
    @MapKey(name = "userName")
    @SortNatural
    @Cascade(org.hibernate.annotations.CascadeType.SAVE_UPDATE)
    private Map<String, User> users = new TreeMap<>();

    /* Установка дефолтных значений в полях */
    @Builder.Default
    @ElementCollection
    @CollectionTable(name = "company_locale",
                     joinColumns = @JoinColumn(name = "company_id"))
    @MapKeyColumn(name = "language")
    @Column(name = "description")
    private Map<String, String> locales = new HashMap<>();

    /*
    Установка перекрестных ссылок в случае добавления User в Company,
    это маппинг основных ассоциаций в связных сущностях.
    */
    public void addUser(User user) {
        users.put(user.getUserName(), user);
        user.setCompany(this);
    }
}

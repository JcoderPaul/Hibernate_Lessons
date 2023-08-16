package oldboy.lesson_14.MappingEntity;
/*
В данном классе применены две аннотации:
@OrderColumn и @SortNatural, к разным полям,
для сортировки сущностей.
*/
import lombok.*;
import oldboy.lesson_14.MappingEntity.Guys.Spy;
import oldboy.lesson_14.MappingEntity.Guys.Agent;
import oldboy.lesson_14.MappingEntity.Guys.DoubleAgent;
import oldboy.lesson_14.MappingEntity.Guys.Traitor;
import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.SortNatural;

import javax.persistence.*;
import java.util.*;

@Entity
@Data
/*
Исключаем поля, которые могут зациклить программу при их вызове
(обращении к ним), т.к. они могут быть связанный с множеством,
других сущностей каскадно - Stack Overflow.
*/
@ToString(exclude = "spies")
@EqualsAndHashCode(exclude = "spies")
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "secret_services", schema = "public")
public class SecretService {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "bureau_id")
    private Integer bureauId;

    @Column(name = "bureau_name")
    private String bureauName;

    /* Для установки дефолтных значений в List */
    @Builder.Default
    @OneToMany(targetEntity = Spy.class,
               mappedBy = "bureau",
               orphanRemoval = true)
    @Cascade(org.hibernate.annotations.CascadeType.SAVE_UPDATE)
    /*
    Аннотация работает только с полями INT, при этом,
    только следующими подряд, пропуски заполняются null
    (см. OrderColumnAnnotation.java) и только с
    List-коллекциями, не с Set-ами.
    */
    @OrderColumn(name = "spy_id")
    private List<Agent> spies = new ArrayList<>();
    /*
    Взаимный проброс ссылок и установок друг на друга,
    шпиона добавляем в List шпионов, и контору добавляем
    шпиону.
    */
    public void addSpy(Spy spy){
        spies.add(spy);
        spy.setBureau(this);
    }
    @Builder.Default
    @OneToMany(targetEntity = Traitor.class,
            mappedBy = "bureau",
            orphanRemoval = true)
    @Cascade(org.hibernate.annotations.CascadeType.SAVE_UPDATE)
    /*
    Если в качестве коллекции выбран Set, то мы можем использовать
    аннотацию @SortNatural, в сортируемом классе подписаться на
    Comparable<... sorted class ...> и переопределить метод
    *.compareTo()
    */
    @SortNatural
    private Set<Agent> traitors = new TreeSet<>();
    /*
    Взаимный проброс ссылок и установок друг на друга,
    предателя добавляем в Set предателей, и контору
    добавляем предателю.
    */
    public void addTraitor(Traitor traitor){
        this.traitors.add(traitor);
        traitor.setBureau(this);
    }

    @Builder.Default
    @OneToMany(targetEntity = DoubleAgent.class,
            mappedBy = "bureau",
            orphanRemoval = true)
    @Cascade(org.hibernate.annotations.CascadeType.SAVE_UPDATE)
    @SortNatural
    /*
    Если в качестве коллекции выбран Map, то мы можем использовать
    аннотацию @MapKey, 'имя поля' это имя ИМЕННО класса, а не таблицы
    БД.
    */
    @MapKey(name = "doubleName")
    private Map<String, Agent> doubles = new HashMap<>();
    /*
    Взаимный проброс ссылок и установок друг на друга,
    двойного агента добавляем в Map двойных агентов, и
    контору добавляем двойному агенту.
    */
    public void addDoubleAgent(DoubleAgent doubleAgent){
        this.doubles.put(doubleAgent.getDoubleName(), doubleAgent);
        doubleAgent.setBureau(this);
    }
}

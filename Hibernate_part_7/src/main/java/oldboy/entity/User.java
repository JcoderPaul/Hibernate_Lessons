package oldboy.entity;
/*
Во избежание возникновения проблемы N+1, рекомендуется настраивать параметр fetch
в LAZY, хотя это не решает проблемы, но позволяет избежать избыточного запроса
информации из БД.

Да, EAGER, в моменте, позволит одним большим запросом вытащить информацию из БД,
но, при большом количестве записей и связности полей в таблицах, можно уронить БД.

А значит EAGER настройку параметра fetch вообще не рекомендуется применять.
*/
import com.vladmihalcea.hibernate.type.json.JsonBinaryType;
import lombok.*;
import oldboy.entity.accessory.BaseEntity;
import oldboy.entity.accessory.PersonalInfo;
import oldboy.entity.accessory.Profile;
import oldboy.entity.accessory.Role;
import org.hibernate.annotations.*;

import javax.persistence.*;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.ArrayList;
import java.util.List;

import static oldboy.Util.StringUtils.SPACE;

/* Именованный граф сущности - реализует возможность получить связанные данные в одном запросе к БД */
@NamedEntityGraph(
        name = "withCompanyAndChat",
        /* Тут мы описываем 'связный граф' нашей сущности User, какие поля и что 'должны подтянуть' */
        attributeNodes = {
                /*
                Имена нодов соответствуют полям данного класса User, т.е. мы хотим
                при обращении к сущности User подгрузить данные по Company и UserChat,
                при этом fetch настройки полей связок рекомендуется всегда выставлять
                в LAZY.
                */
                @NamedAttributeNode("company"),
                /* Поле 'userChats' связано с классом UserChat из него будем строить сабграф */
                @NamedAttributeNode(value = "userChats", subgraph = "getChats")
        },
        subgraphs = {
                /*
                Cабграф связан с полем 'chat' - имя атрибута ноды, класса UserChat,
                эти данные мы хотим подтянуть и тоже в единственном запросе.
                */
                @NamedSubgraph(name = "getChats", attributeNodes = @NamedAttributeNode("chat"))
        }
)
/*
Аннотация позволяющая частично решать проблему N+1 при запросе к конкретной сущности.
Параметр name - мы задаем сами (желательно вложить в нее некий функционально -
описательный смысл, как всегда), в нашем случае мы планируем использовать наш
@FetchProfile при вызове 'company' и 'payment'

Далее переопределяем связи (сущность, поле в сущности связное с искомым, тип взаимосвязи).
*/
@FetchProfile(name = "withCompanyAndPayment", fetchOverrides = {
        @FetchProfile.FetchOverride(
                entity = User.class, association = "company", mode = FetchMode.JOIN
        ),
        @FetchProfile.FetchOverride(
                entity = User.class, association = "payments", mode = FetchMode.JOIN
        )
})
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "userName")
@ToString(exclude = {"company", "profile", "userChats", "payments"})
@Builder
@Entity
@Table(name = "users", schema = "part_four_base")
@TypeDef(name = "JsonType", typeClass = JsonBinaryType.class)
public class User implements Comparable<User>, BaseEntity<Long> {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long userId;
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

    @Column(name = "user_name", unique = true)
    private String userName;

    @Type(type = "JsonType")
    private String info;

    @Enumerated(EnumType.STRING)
    private Role role;

    /*
    Как уже писалось ниже аннотация @BatchSize(size = ..n..), для
    данного поля работать не будет, ЗАТО будет работать, если
    установить ее над самим классом Company.
    */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "company_id") // company_id
    private Company company;

    @OneToOne(
            mappedBy = "user",
            cascade = CascadeType.ALL,
            fetch = FetchType.LAZY
    )
    /*
    При такой форме связи сложно избежать избыточности
    запросов при выборе нескольких user-ов, т.к. данные
    извлекаются из БД для каждого.
    */
    private Profile profile;

    @Builder.Default
    @BatchSize(size = 3)
    @Fetch(FetchMode.SUBSELECT)
    @OneToMany(mappedBy = "user")
    private List<UserChat> userChats = new ArrayList<>();

    @Builder.Default
    /*
    Аннотация указанная ниже эффективно работает (частично решает проблему 'N+1')
    только с коллекциями. Установи мы ее над полем 'company', то разницы в количестве
    запросов к БД не увидели бы.
    */
    @BatchSize(size = 3)
    /*
    Данную аннотацию с коллекциями можно использовать, только с параметром SUBSELECT
    (режим Подзапроса). При использовании в любом другом режиме, мы получим перекрестные
    ссылки (декартово произведение) и как результат избыточность результирующих данных.

    В то же время, режим SUBSELECT недоступен для отношения Many-To-One (в отличие от JOIN
    и SELECT).
    */
    @Fetch(FetchMode.SUBSELECT)
    @OneToMany(mappedBy = "receiver")
    private List<Payment> payments = new ArrayList<>();

    public String fullName() {
        return getPersonalInfo().getFirstName() +
                SPACE +
                getPersonalInfo().getLastName();
    }

    @Override
    public int compareTo(User o) {
        return userName.compareTo(o.userName);
    }

    @Override
    public void setId(Long id) {
        this.userId = id;
    }

    @Override
    public Long getId() {
        return userId;
    }
}

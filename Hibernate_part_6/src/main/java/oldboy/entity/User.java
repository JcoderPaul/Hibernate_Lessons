package oldboy.entity;

import com.vladmihalcea.hibernate.type.json.JsonBinaryType;
import lombok.*;
import oldboy.entity.accessory.BaseEntity;
import oldboy.entity.accessory.PersonalInfo;
import oldboy.entity.accessory.Profile;
import oldboy.entity.accessory.Role;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

import static oldboy.Util.StringUtils.SPACE;

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

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "company_id") // company_id
    private Company company;

    @OneToOne(
            mappedBy = "user",
            cascade = CascadeType.ALL,
            fetch = FetchType.LAZY
    )
    private Profile profile;

    @Builder.Default
    @OneToMany(mappedBy = "user")
    private List<UserChat> userChats = new ArrayList<>();

    @Builder.Default
    @OneToMany(mappedBy = "receiver")
    private List<Payment> payments = new ArrayList<>();

    public String fullName() {
        return getPersonalInfo().getFirstName() + SPACE + getPersonalInfo().getLastName();
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

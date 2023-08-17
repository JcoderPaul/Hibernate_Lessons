package oldboy.entity;

import com.vladmihalcea.hibernate.type.json.JsonBinaryType;
import lombok.*;
import oldboy.entity.accessory.BaseEntity;
import oldboy.entity.accessory.PersonalInfo;
import oldboy.entity.accessory.Profile;
import oldboy.entity.accessory.Role;
import org.hibernate.annotations.*;
import org.hibernate.envers.Audited;
import org.hibernate.envers.NotAudited;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

import static oldboy.Util.StringUtils.SPACE;

@NamedEntityGraph(
        name = "withCompanyAndChat",
        attributeNodes = {
                @NamedAttributeNode("company"),
                @NamedAttributeNode(value = "userChats", subgraph = "getChats")
        },
        subgraphs = {
                @NamedSubgraph(name = "getChats", attributeNodes = @NamedAttributeNode("chat"))
        }
)
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
@Table(name = "users")
@TypeDef(name = "JsonType", typeClass = JsonBinaryType.class)
@Audited
public class User implements Comparable<User>, BaseEntity<Long> {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long userId;

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
    @JoinColumn(name = "company_id")
    @NotAudited
    private Company company;

    @OneToOne(
            mappedBy = "user",
            cascade = CascadeType.ALL,
            fetch = FetchType.LAZY
    )
    private Profile profile;

    @NotAudited
    @Builder.Default
    @BatchSize(size = 3)
    @Fetch(FetchMode.SUBSELECT)
    @OneToMany(mappedBy = "user")
    private List<UserChat> userChats = new ArrayList<>();

    @NotAudited
    @Builder.Default
    @BatchSize(size = 3)
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

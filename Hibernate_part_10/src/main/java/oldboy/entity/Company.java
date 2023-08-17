package oldboy.entity;

import lombok.*;
import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.SortNatural;
import org.hibernate.envers.Audited;
import org.hibernate.envers.NotAudited;

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
@Table(name = "company")
@Audited
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
    @OneToMany(mappedBy = "company",
               orphanRemoval = true)
    @MapKey(name = "userName")
    @SortNatural
    @Cascade(org.hibernate.annotations.CascadeType.SAVE_UPDATE)
    @NotAudited
    private Map<String, User> users = new TreeMap<>();


    /* Установка дефолтных значений в полях */
    @Builder.Default
    @ElementCollection
    @CollectionTable(name = "company_locale",
                     joinColumns = @JoinColumn(name = "company_id"))
    /* !!! Когда именуешь поля будь внимателен !!! */
    @MapKeyColumn(name = "lang")
    @Column(name = "description")
    @Cascade(org.hibernate.annotations.CascadeType.ALL)
    @NotAudited
    private Map<String, String> locales = new HashMap<>();

    public void addUser(User user) {
        users.put(user.getUserName(), user);
        user.setCompany(this);
    }
}

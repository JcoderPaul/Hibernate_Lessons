package oldboy.entity.accessory;
/*
Во избежание возникновения проблемы N+1, рекомендуется настраивать параметр fetch
в LAZY, хотя это не решает проблемы, но позволяет избежать избыточного запроса
информации из БД.

Да, EAGER, в моменте, позволит одним большим запросом вытащить информацию из БД,
но, при большом количестве записей и связности полей в таблицах, можно 'уронить БД'.

А значит EAGER настройку параметра fetch вообще не рекомендуется применять.
*/
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import oldboy.entity.User;

import javax.persistence.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "profile")
public class Profile {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "profile_id")
    private Long profileId;
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    private String street;

    private String language;

    public void setUser(User user) {
        user.setProfile(this);
        this.user = user;
    }
}
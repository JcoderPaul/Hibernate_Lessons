package oldboy.lesson_9.MappingEntity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import oldboy.entity.accessory.PersonalInfo;
import oldboy.entity.accessory.Role;

import javax.persistence.*;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "company_users", schema = "public")
public class CompanyUser {

    @Id
    @Column (name = "user_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer userId;

    @Column(name = "username")
    private String userName;

    @Embedded
    private PersonalInfo personalInfo;

    @Enumerated(EnumType.STRING)
    private Role role;

    /*
    Аннотация многие-к-одному, параметр можно и не указывать
    Многие-к-одному означает, что многие пользователи, могут
    принадлежать к одной компании, т.е. много людей могут
    работать в одной фирме.

    Еще раз многие CompanyUser связаны с одной Company.
    */
    @ManyToOne(targetEntity = Company.class)
    /*
    Аннотация указывающая на какую колонку ссылается
    данное поле, вместо @Column, но более широкая, т.к.
    применяется внешняя ссылка на другую таблицу.
    */
    @JoinColumn(name = "company_id")
    private Company company;
}

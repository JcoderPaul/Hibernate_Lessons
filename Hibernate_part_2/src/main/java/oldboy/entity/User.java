package oldboy.entity;
/*
Пример использования аннотаций @Embeddable
и @Embedded так же см. класс PersonalInfo.java
*/

import oldboy.entity.accessory.PersonalInfo;
import oldboy.entity.accessory.Role;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "users", schema = "public")
public class User {
    /* Задаем первичный ключ, помеченное поле должно быть Serializable */
    @Id
    /*
    Задаем соответствие названия поля класса и поля таблицы БД
    !!! Делать это нужно максимально внимательно, иначе хапнем
    исключение !!!
    */
    @Column (name = "username")
    private String userName;
    /* Embedded - Встроенный */
    @Embedded
    /*
    Если во встраиваемом классе мы не указали соответствие @Column(name = "birth_date"),
    то тут ему самое место. Применятся аннотация @AttributeOverride описание см.
    DOC/HibernateEmbeddableEmbedded.txt

    !!! Необходимо всегда максимально внимательно прописывать названия полей таблиц
    при сопоставлении с полями класса Entity. !!!

    Данная аннотация может применяться столько раз сколько полей во встраиваемом классе,
    если это необходимо.
    */
    @AttributeOverride(name = "birthDate", column = @Column(name = "birth_data"))
    private PersonalInfo personalInfo;
    /*
    Чтобы использовать строковое представление ENUM
    применим аннотацию с требуемым нам параметром:
    - ORDINAL;
    - STRING;
    Аннотацию @Column опустим, т.к. название поля
    класса полностью совпадает с полем таблицы БД,
    у Hibernate не будет проблем с сопоставлением.
    */
    @Enumerated(EnumType.STRING)
    private Role role;

    public User() {
    }

    public User(String userName, PersonalInfo personalInfo, Role role) {
        this.userName = userName;
        this.personalInfo = personalInfo;
        this.role = role;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public PersonalInfo getPersonalInfo() {
        return personalInfo;
    }

    public void setPersonalInfo(PersonalInfo personalInfo) {
        this.personalInfo = personalInfo;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return Objects.equals(userName, user.userName) &&
                Objects.equals(personalInfo, user.personalInfo) &&
                role == user.role;
    }

    @Override
    public int hashCode() {
        return Objects.hash(userName, personalInfo, role);
    }

    @Override
    public String toString() {
        return "User{" +
                "userName='" + userName + '\'' +
                ", personalInfo=" + personalInfo +
                ", role=" + role +
                '}';
    }
}

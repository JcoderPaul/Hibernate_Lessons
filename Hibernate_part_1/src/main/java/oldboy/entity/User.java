package oldboy.entity;
/*
Правила для Entity:
- Сам класс НЕ МОЖЕТ быть final;
- Сущность должна быть POJO - Plain Old Java Object;
- Все ее поля должны быть private, есть getters и setters для всех полей;
- Сущность НЕ МОЖЕТ быть Immutable, т.е. поля НЕ МОГУТ быть FINAL;
- Наличие конструктора без параметров ОБЯЗАТЕЛЬНО (т.к. используется рефлексия);

На данном этапе мы сделаем все руками, однако, при таком количестве
рутинного кода нам пригодится Lombok и его аннотации:
- @Data
- @NoArgsConstructor
- @AllArgsConstructor
- @Builder
см. https://github.com/JcoderPaul/HTTP_Servlets_Java_EE/blob/master/MVCPractice/DOC/Fast_Lombok.txt
*/

import javax.persistence.*;
import oldboy.converter.BirthdayConverter;

import java.util.Objects;

/* Чтобы объявить данный класс сущностью Hibernate, добавим необходимую аннотацию */
@Entity
/*
Теперь точно укажем название таблицы которой будет соответствовать наш POJO,
странно, но у меня только при использовании библиотеки jakarta.persistence,
появилась возможность сопоставить параметры аннотации с параметрами таблицы БД
средствами IDE.
*/
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
    @Column (name = "firstname")
    private String firstName;
    @Column (name = "lastname")
    private String lastName;
    @Column (name = "birth_data")
    /*
    Поскольку это наш самописный класс, то Hibernate
    не знает, как преобразовывать его в данные БД и
    наоборот. Мы должны явно указать на конвертор
    данных, который тут применяется.
    */
    @Convert(converter = BirthdayConverter.class)
    private Birthday birthDate;
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

    public User(String userName, String firstName,
                String lastName, Birthday birthDate,
                Role role) {

        this.userName = userName;
        this.firstName = firstName;
        this.lastName = lastName;
        this.birthDate = birthDate;
        this.role = role;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public Birthday getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(Birthday birthDate) {
        this.birthDate = birthDate;
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
               Objects.equals(firstName, user.firstName) &&
               Objects.equals(lastName, user.lastName) &&
               Objects.equals(birthDate, user.birthDate) &&
               role == user.role;
    }

    @Override
    public int hashCode() {
        return Objects.hash(userName, firstName, lastName, birthDate, role);
    }

    @Override
    public String toString() {
        return "User{" +
                "userName='" + userName + '\'' +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", birthDate=" + birthDate +
                ", role=" + role +
                '}';
    }
}

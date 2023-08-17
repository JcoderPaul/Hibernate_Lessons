package oldboy.entity.accessory;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import oldboy.converter.BirthdayConverter;

import javax.persistence.Convert;
import javax.persistence.Embeddable;
import java.io.Serializable;

/*
Аннотация @Embeddable используется, чтобы пометить класс как встраиваемый,
что означает, что его свойства могут быть включены в другой класс в качестве
типа значения. Класс, отмеченный @Embeddable, называется встраиваемым классом.
*/
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
/*
Повторимся: Данная аннотация задает класс, экземпляры которого
хранятся как неотъемлемая часть объекта-владельца и совместно
используют идентификатор объекта. Каждое постоянное свойство или
поле внедренного объекта сопоставляется с таблицей базы данных
для сущности.

Т.е. все три поля данного класса PersonalInfo являются частью,
например класса User и содержаться в таблице БД training_base.users,
см. DOC/SQL_Scripts/Create_Table/training_base_users_table.sql
*/
@Embeddable
public class PersonalInfo implements Serializable {

    private String firstName;
    private String lastName;
    /*
    Аннотацию @Column(name = "birth_data"), как это
    использовалось в предыдущих уроках, может быть
    применена несколько в другом виде и в другом месте,
    т.к. имя поля birthDate все же не соответствует
    названию поля в таблице БД - birth_data. См. класс
    User.java аннотацию @AttributeOverride, описание
    см. DOC/HibernateEmbeddableEmbedded.txt
    */
    @Convert(converter = BirthdayConverter.class)
    private Birthday birthDate;
}

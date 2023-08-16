package oldboy.entity.accessory;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import oldboy.converter.BirthdayConverter;

import javax.persistence.Column;
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
Embeddable - Встраиваемый

На случай если данный класс станет уникальным идентификатором в
одной или нескольких таблицах БД, он должен реализовывать интерфейс
Serializable. Так же, если потребуется авто генерация версии UID
для сериализации - serialVersionUID = n......nL, можно в IDE включить
эту опцию в настройках среды разработки см. DOC/serialVersionUID.jpg
*/
@Embeddable
public class PersonalInfo implements Serializable {

    @Column (name = "firstname")
    private String firstname;

    @Column (name = "lastname")
    private String lastname;
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
    @Column(name = "birth_data")
    private Birthday birthDate;
}

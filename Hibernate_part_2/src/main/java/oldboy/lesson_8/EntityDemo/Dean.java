package oldboy.lesson_8.EntityDemo;
/* Пример работы с составным ключом */
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import oldboy.entity.accessory.PersonalInfo;

import javax.persistence.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "deans", schema = "public")
public class Dean {
    /*
    Класс PersonalInfo становится составным ключом и
    должен иметь соответствующую аннотацию @EmbeddedId
    */
    @EmbeddedId
    /*
    Не забываем объяснить Hibernate, какое название поля и чему соответствует,
    если его название в рабочем классе отличается от названия в соответствующем
    поле таблицы БД.
    */
    @AttributeOverride(name = "birthDate", column = @Column(name = "birth_data"))
    /* Поле помеченное как @EmbeddedId должно реализовывать интерфейс Serializable */
    private PersonalInfo personalInfo;
    private String faculty;
    private Double salary;
}

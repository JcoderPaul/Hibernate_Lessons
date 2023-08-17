package oldboy.converter;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;
import java.sql.Date;
import java.util.Optional;

/*
Наш конвертер для удобства же нас, должен запускаться автоматически.
Есть два способа настройки этого. Первый это аннотация с параметром.
Второй это метод прописанный в нашем рабочем классе HibernateDemo.java
*/
@Converter(autoApply = true)
public class BirthdayConverter implements AttributeConverter<Birthday, Date> {
    /* Переопределяем методы */
    /* Полученный "новый" или "самописный" тип нужно преобразовать в SQL */
    @Override
    public Date convertToDatabaseColumn(Birthday attribute) {
        return Optional.ofNullable(attribute).
                map((Birthday birthday) -> birthday.birthDate()).
                map(date -> Date.valueOf(date)).
                orElse(null);
    }

    /* Тип данных из БД будет преобразован в наш Birthday */
    @Override
    public Birthday convertToEntityAttribute(Date dbData) {
        return Optional.ofNullable(dbData).
                map(date -> date.toLocalDate()).
                map(birthDate -> new Birthday(birthDate)).
                orElse(null);
    }
}

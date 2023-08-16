package oldboy.Util;
/*
Выделим постоянно повторяющийся код в отдельный метод:
конфигурирование связи с БД, маппинг Entity сущностей,
возврат фабрики сессий.
*/
import com.vladmihalcea.hibernate.type.json.JsonBinaryType;
import lombok.experimental.UtilityClass;
import oldboy.converter.BirthdayConverter;
import oldboy.entity.User;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
/*
Аннотация @UtilityСlass преобразует существующий класс в утилитный,
делая его окончательным и создавая приватный конструктор по умолчанию.
Она также изменяет существующий метод и переменные, делая их статическими.
*/
@UtilityClass
public class HibernateUtil {
    public static SessionFactory buildSessionFactory() {
        Configuration configuration = new Configuration();
        configuration.addAnnotatedClass(User.class);
        configuration.addAttributeConverter(new BirthdayConverter());
        configuration.registerTypeOverride(new JsonBinaryType());
        configuration.configure();

        return configuration.buildSessionFactory();
    }
}

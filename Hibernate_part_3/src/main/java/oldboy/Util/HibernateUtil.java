package oldboy.Util;
/*
Выделим постоянно повторяющийся код в отдельный метод:
конфигурирование связи с БД, маппинг Entity сущностей,
возврат фабрики сессий.
*/

import com.vladmihalcea.hibernate.type.json.JsonBinaryType;
import lombok.experimental.UtilityClass;
import oldboy.converter.BirthdayConverter;
import oldboy.entity.Chat;
import oldboy.entity.Company;
import oldboy.entity.User;

import oldboy.entity.UserChat;
import oldboy.entity.accessory.Profile;
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
        Configuration configuration = buildConfiguration();
        configuration.configure();

        return configuration.buildSessionFactory();
    }
    public static Configuration buildConfiguration() {
        Configuration configuration = new Configuration();

        configuration.addAnnotatedClass(User.class);
        configuration.addAnnotatedClass(Company.class);
        configuration.addAnnotatedClass(Company.class);
        configuration.addAnnotatedClass(UserChat.class);
        configuration.addAnnotatedClass(Chat.class);
        configuration.addAnnotatedClass(Profile.class);

        configuration.addAttributeConverter(new BirthdayConverter());
        configuration.registerTypeOverride(new JsonBinaryType());
        return configuration;
    }
}

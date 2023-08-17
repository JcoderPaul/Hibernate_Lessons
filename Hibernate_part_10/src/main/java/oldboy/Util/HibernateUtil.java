package oldboy.Util;

import com.vladmihalcea.hibernate.type.json.JsonBinaryType;
import lombok.experimental.UtilityClass;

import oldboy.converter.BirthdayConverter;
import oldboy.entity.*;
import oldboy.entity.accessory.Profile;

import oldboy.entity.revtable.RevisionRecorder;
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

        SessionFactory sessionFactory = configuration.buildSessionFactory();

        return sessionFactory;
    }

    public static Configuration buildConfiguration() {
        Configuration configuration = new Configuration();
        /* Объявляем наши сущности, некоторые продублированы в hibernate.cfg.xml */
        configuration.addAnnotatedClass(User.class);
        configuration.addAnnotatedClass(Company.class);
        configuration.addAnnotatedClass(Payment.class);
        configuration.addAnnotatedClass(UserChat.class);
        configuration.addAnnotatedClass(Chat.class);
        configuration.addAnnotatedClass(Profile.class);
        configuration.addAnnotatedClass(RevisionRecorder.class);

        configuration.addAttributeConverter(new BirthdayConverter());
        configuration.registerTypeOverride(new JsonBinaryType());

        return configuration;
    }
}

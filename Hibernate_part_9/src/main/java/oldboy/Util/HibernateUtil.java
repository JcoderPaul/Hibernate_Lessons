package oldboy.Util;
/*
Выделим постоянно повторяющийся код в отдельный метод:
конфигурирование связи с БД, маппинг Entity сущностей,
возврат фабрики сессий.
*/

import com.vladmihalcea.hibernate.type.json.JsonBinaryType;
import lombok.experimental.UtilityClass;
import oldboy.converter.BirthdayConverter;
import oldboy.entity.*;
import oldboy.entity.accessory.Profile;
import oldboy.entity.audit.Audit;
import oldboy.interceptor.GlobalInterceptor;
import oldboy.listener.AuditTableListener;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.hibernate.event.service.spi.EventListenerRegistry;
import org.hibernate.event.spi.EventType;
import org.hibernate.internal.SessionFactoryImpl;

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
        registerListeners(sessionFactory);

        return sessionFactory;
    }
    /* Регистрируем наш слушатель */
    private static void registerListeners(SessionFactory sessionFactory) {
        /*
        Размещение нашего лисенера в соответствующей группе идет средствами фабрики сессий, но
        у самой SessionFactory нет нужного нам метода, для регистрации listener-a. Для этого мы
        приводим объект SessionFactory к SessionFactoryImpl методом *.unwrap() - по факту это
        приведение типов или 'type cast' - т.е. возвращает объект указанного типа, чтобы разрешить
        доступ к специфичному для поставщика API. Если реализация EntityManagerFactory поставщика
        не поддерживает указанный класс, создается исключение PersistenceException.
        */
        SessionFactoryImpl sessionFactoryImpl = sessionFactory.unwrap(SessionFactoryImpl.class);
        /*
        Шаг 1. Теперь у полученного нами объекта мы получаем доступ ServiceRegistry, который
               занимается регистрацией сервисов Hibernate, мы получаем доступ к
               EventListenerRegistry.class.

               СЕРВИСАМИ в HIBERNATE является практически все, сам же интерфейс Service
               маркерного типа.
        */
        EventListenerRegistry listenerRegistry = sessionFactoryImpl.
                                    getServiceRegistry().
                                    getService(EventListenerRegistry.class);
        /*
        Шаг 2. Создаем экземпляр сущности нашей AuditTableListener
               для регистрации в качестве 'сервиса слушателя' в группе
               соответствующих слушателей.
        */
        AuditTableListener auditTableListener = new AuditTableListener();
        /*
        Шаг 3. Добавляем нашего слушателя в нужные группы Event-ов:
                - EventType.PRE_INSERT;
                - EventType.PRE_DELETE;
        */
        listenerRegistry.appendListeners(EventType.PRE_INSERT, auditTableListener);
        listenerRegistry.appendListeners(EventType.PRE_DELETE, auditTableListener);
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
        configuration.addAnnotatedClass(MeetingRoom.class);
        configuration.addAnnotatedClass(Audit.class);

        configuration.addAttributeConverter(new BirthdayConverter());
        configuration.registerTypeOverride(new JsonBinaryType());
        /* Подключаем наш перехватчик */
        configuration.setInterceptor(new GlobalInterceptor());

        return configuration;
    }
}

package oldboy.lesson_7;
/*
Изменяя параметр <level value="info"/> в log4j.xml
мы получим разные уровни отображения лога
(подробности работы программы) после запуска кода.
*/
import oldboy.Util.HibernateUtil;
import oldboy.entity.accessory.Birthday;
import oldboy.entity.accessory.PersonalInfo;
import oldboy.entity.accessory.Role;
import oldboy.entity.User;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDate;

public class LoggingDemo {
    /* Инициализируем логгер */
    public static final Logger myFirstLogger = LoggerFactory.getLogger(LoggingDemo.class);

    public static void main(String[] args) {

        User userComp = new User("abit@formoza.ch",
                                    PersonalInfo.
                                    builder().
                                    lastname("Abit").
                                    firstname("Brand").
                                    birthDate(new Birthday(LocalDate.of(1965, 8,12))).
                                    build(),
                                    Role.USER);
        User userVideo = new User("s3@trio.com",
                                    PersonalInfo.
                                    builder().
                                    lastname("s3").
                                    firstname("LowGraph").
                                    birthDate(new Birthday(LocalDate.of(1965, 8,12))).
                                    build(),
                                    Role.USER);
        /*
        Вариант форматирования строки лога, используем не конкатенацию,
        а форматирование '{}', очень похоже на работу метода *.printf()
        */
        myFirstLogger.info("User entity is in transient state, object: {}", userComp);

        /* Фабрика сессий */
        try (SessionFactory sessionFactory = HibernateUtil.buildSessionFactory()) {
            Session sessionOne = null;

            /* Сессия первая */
            System.out.println("------------ First session start ------------");

            try {
                sessionOne = sessionFactory.openSession();
                System.out.println("Статистика первой сессии " + sessionOne.getStatistics());

                /* Следующий лог после начала транзакции */
                Transaction transactionFirstSession = sessionOne.beginTransaction();
                myFirstLogger.trace("Transaction is created, {}", transactionFirstSession);

                sessionOne.saveOrUpdate(userComp);
                sessionOne.saveOrUpdate(userVideo);
                /* Логгируем текущее состояние Entity сущностей */
                myFirstLogger.trace("Users: {}, {} is in persistent state of session {}",
                                                            userComp, userVideo, sessionOne);

                System.out.println("Статистика первой сессии " + sessionOne.getStatistics());
                sessionOne.getTransaction().commit();
            } catch (Exception exc) {
                myFirstLogger.error("Exception occurred", exc);
                throw exc;
            } finally {
                sessionOne.close();
            }
                System.out.println("Статистика первой сессии " + sessionOne.getStatistics());

                myFirstLogger.warn("Users: {}, {}, is in detached " +
                                   "state! Session {} is closed!",
                                                            userComp, userVideo, sessionOne);

            System.out.println("------------ Close first session ------------");
        }
    }
}

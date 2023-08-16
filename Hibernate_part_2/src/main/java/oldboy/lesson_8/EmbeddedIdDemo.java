package oldboy.lesson_8;
/* Пример работы со сложносоставным ключом EmbeddedId */
import oldboy.Util.HibernateUtil;
import oldboy.entity.accessory.Birthday;
import oldboy.entity.accessory.PersonalInfo;
import oldboy.lesson_8.EntityDemo.Dean;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

import java.time.LocalDate;

public class EmbeddedIdDemo {
    public static void main(String[] args) {

        Dean dOne = Dean.
                builder().
                personalInfo(PersonalInfo.builder().
                        firstname("Macgonigal").
                        lastname("Minevra").
                        birthDate(new Birthday(LocalDate.of(1434,4,12))).
                        build()).
                faculty("Umaril").
                salary(2341.4).
                build();

        Dean dTwo = Dean.
                builder().
                personalInfo(PersonalInfo.builder().
                        firstname("Siverus").
                        lastname("Snegg").
                        birthDate(new Birthday(LocalDate.of(1634,8,11))).
                        build()).
                faculty("Othvatil").
                salary(1341.4).
                build();
        /* Фабрика сессий */
        try (SessionFactory sessionFactory = HibernateUtil.buildSessionFactory()) {
            Session sessionOne = null;
            /* Сессия первая */
            System.out.println("------------ First session start ------------");
            try {
                sessionOne = sessionFactory.openSession();
                System.out.println("Статистика первой сессии " + sessionOne.getStatistics());

                /* Следующий лог после начала транзакции */
                sessionOne.beginTransaction();

                sessionOne.saveOrUpdate(dOne);
                /*
                При запросе *.saveOrUpdate() к БД формируется весьма интересный SQL
                запрос, естественно Hibernate проверяет есть ли такая сущность в БД,
                а уже затем делает отправку данных (после commit или flash):
                ********************************************************************
                Hibernate:
                    select
                        dean_.birth_data,
                        dean_.firstname,
                        dean_.lastname,
                        dean_.faculty as faculty4_1_,
                        dean_.salary as salary5_1_
                    from
                        public.deans dean_
                    where
                        dean_.birth_data=?
                        and dean_.firstname=?
                        and dean_.lastname=?
                ********************************************************************
                и только за тем:
                ********************************************************************
                   insert
                        into
                            public.deans
                            (faculty, salary, birth_data, firstname, lastname)
                        values
                            (?, ?, ?, ?, ?)
                ********************************************************************
                это видно в консоли и при пошаговой отладке.
                */
                sessionOne.saveOrUpdate(dTwo);

                System.out.println("Статистика первой сессии " + sessionOne.getStatistics());
                sessionOne.getTransaction().commit();
            } catch (Exception exc) {
                throw exc;
            } finally {
                sessionOne.close();
            }
            System.out.println("Статистика первой сессии " + sessionOne.getStatistics());
            System.out.println("------------ Close first session ------------");
            /* А теперь получим нашу сущность с таким сложносоставным ключом */
            try(Session secSession = sessionFactory.openSession()){
                /* Получаем наш EmbeddedId ключ */
                PersonalInfo hardKey =
                        PersonalInfo.builder().
                               firstname("Siverus").
                               lastname("Snegg").
                               birthDate(new Birthday(LocalDate.of(1634,8,11))).
                               build();
                /*
                Методика получения сущности из БД по ключу не изменилась,
                просто ключик приобрел некую широту и грозное обаяние. Не
                сложно заметить, что SQL запрос после WHERE имеет более
                расширенный синтаксис нежели при работе с простым ключом:

                Hibernate формирует запрос:
                       select
                            dean0_.birth_data as birth_da1_1_0_,
                            dean0_.firstname as firstnam2_1_0_,
                            dean0_.lastname as lastname3_1_0_,
                            dean0_.faculty as faculty4_1_0_,
                            dean0_.salary as salary5_1_0_
                        from
                            public.deans dean0_
                        where
                            dean0_.birth_data=?
                            and dean0_.firstname=?
                            and dean0_.lastname=?
                */
                Dean SiverusDean = secSession.get(Dean.class, hardKey);
                System.out.println(SiverusDean);
                /*
                И в консоли видим:

                Dean(personalInfo=PersonalInfo(firstname=Siverus,
                                               lastname=Snegg,
                                               birthDate=Birthday[birthDate=1634-08-11]),
                                               faculty=Othvatil,
                                               salary=1341.4)
                */
            }
        }
    }
}

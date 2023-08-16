package oldboy.lesson_9;

import oldboy.Util.HibernateUtil;
import oldboy.entity.accessory.Birthday;
import oldboy.entity.accessory.PersonalInfo;
import oldboy.entity.accessory.Role;
import oldboy.lesson_9.MappingEntity.Company;
import oldboy.lesson_9.MappingEntity.CompanyUser;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

import java.time.LocalDate;

public class ManyToOneDemo {
    public static void main(String[] args) {
        /*
        Помним что при создании таблицы мы название компании
        сделали уникальным, т.ч. повторный запуск кода с теми
        же параметрами сущностей поймаем исключение
        */
        Company gooCompany = Company.
                                    builder().
                                    companyName("Platus").
                                    build();

        CompanyUser userOne = CompanyUser.
                builder().
                personalInfo(PersonalInfo.
                                builder().
                                lastname("Rumata").
                                firstname("Istotskyi").
                                birthDate(new Birthday(LocalDate.of(1892, 8,12))).
                build()).
                userName("rumata@arcanar.ru").
                role(Role.USER).
                company(gooCompany).
                build();

        /* Фабрика сессий */
        try (SessionFactory sessionFactory = HibernateUtil.buildSessionFactory()) {
            /* Создали переменную */
            Session sessionOne = null;
            /* Сессия первая */
            System.out.println("------------ First session start ------------");
            try {
                /* Открыли сессию */
                sessionOne = sessionFactory.openSession();
                System.out.println("Статистика первой сессии " + sessionOne.getStatistics());
                /* Начали транзакцию */
                sessionOne.beginTransaction();
                /*
                А вот теперь у нас есть последовательность, сначала мы сохраняем компанию,
                т.к. от нее зависит сущность companyUser. Хотя на старте мы точно знаем,
                что ни компании ни пользователя в БД нет, применим удобный метод *.saveOrUpdate(),
                чтобы не пугаться исключений.

                !!! И НЕ ЗАБЫВАЕМ ПРО МАППИНГ СУЩНОСНЕЙ В HibernateUtil.java !!!
                */

                sessionOne.saveOrUpdate(gooCompany);
                sessionOne.saveOrUpdate(userOne);

                System.out.println("Статистика первой сессии перед commit: " + sessionOne.getStatistics());
                sessionOne.getTransaction().commit();

            } finally {
                sessionOne.close();
            }
                System.out.println("Статистика первой сессии после close: " + sessionOne.getStatistics());

            System.out.println("------------ Close first session ------------");
        }
    }
}

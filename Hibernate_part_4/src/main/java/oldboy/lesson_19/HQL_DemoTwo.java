package oldboy.lesson_19;
/* Краткие примеры работы с HQL */
import oldboy.Util.HibernateUtil;
import oldboy.entity.User;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

import javax.persistence.Query;
import java.util.List;

public class HQL_DemoTwo {
    public static void main(String[] args) {
        /* Повторим:
           - Создаем фабрику сессий;
           - Открываем сессию.
        */
        try(SessionFactory sessionFactory = HibernateUtil.buildSessionFactory();
            Session session = sessionFactory.openSession()){
            /* Открываем транзакцию в текущей сессии */
            session.beginTransaction();
            /*
            Нужно помнить и понимать, что при работе (написании запросов) на HQL мы оперируем
            сущностями см. DOC/HQL_JPQL.txt. Синтаксис очень похож на SQL, так же можно применить
            aliases/псевдонимы.

            На SQL: SELECT u.* FROM users u WHERE u.first_name = 'Garibo'

            Тот же способ использования псевдонима 'u'. Но, т.к. у нас, в HQL манипуляция
            с объектом, а он у нас сложносоставной, поэтому идет отличный от SQL синтаксис:
            u.personalInfo.firstName просто для наглядности он у нас длинный и понятный
            'thisUser', а не 'u'.

            И поскольку мы оперируем объектами, то хотим их и получить, поэтому запрос
            можно расширить доп. параметром, тип результата запроса:
            */
            Query listHQLQuery =
                    session.createQuery(
                            "select thisUser " +
                                     "from User thisUser " +
                                     "where thisUser.personalInfo.firstName = 'Willy'",
                            User.class);
            List resultOfQuery = listHQLQuery.getResultList();
            resultOfQuery.forEach(System.out::println);
            /*
            Hibernate:
                select
                    user0_.user_id as user_id1_4_,
                    user0_.company_id as company_8_4_,
                    user0_.info as info2_4_,
                    user0_.birth_date as birth_da3_4_,
                    user0_.first_name as first_na4_4_,
                    user0_.last_name as last_nam5_4_,
                    user0_.role as role6_4_,
                    user0_.user_name as user_nam7_4_
                from
                    part_four_base.users user0_
                where
                    user0_.first_name='Willy'
            Hibernate:
                select
                    profile0_.profile_id as profile_1_3_0_,
                    profile0_.language as language2_3_0_,
                    profile0_.street as street3_3_0_,
                    profile0_.user_id as user_id4_3_0_
                from
                    part_four_base.profile profile0_
                where
                    profile0_.user_id=?
            Hibernate:
                select
                    profile0_.profile_id as profile_1_3_0_,
                    profile0_.language as language2_3_0_,
                    profile0_.street as street3_3_0_,
                    profile0_.user_id as user_id4_3_0_
                from
                    part_four_base.profile profile0_
                where
                    profile0_.user_id=?

            User(userId=6,
                    personalInfo=PersonalInfo(firstName=Willy,
                                              lastName=Wonka,
                                              birthDate=Birthday[birthDate=1901-04-15]),
                                              userName=ww@chocolate.com,
                                              info={},
                                              role=ADMIN)

            User(userId=8,
                    personalInfo=PersonalInfo(firstName=Willy,
                                              lastName=Bushew,
                                              birthDate=Birthday[birthDate=1908-03-15]),
                                              userName=willi@persident.com,
                                              info={},
                                              role=ADMIN)
            */

            /*
            Запрос может быть еще короче (следить за пробелами,
            один лишний или не поставили и синтаксис неверен)
            */
            List<User> listQuery =
                    session.
                            createQuery(
                            "select u " +
                                     "from User u " +
                                     "where u.personalInfo.firstName = 'Willy'",
                            User.class).
                            list();
            /*
            Hibernate:
                select
                    user0_.user_id as user_id1_4_,
                    user0_.company_id as company_8_4_,
                    user0_.info as info2_4_,
                    user0_.birth_date as birth_da3_4_,
                    user0_.first_name as first_na4_4_,
                    user0_.last_name as last_nam5_4_,
                    user0_.role as role6_4_,
                    user0_.user_name as user_nam7_4_
                from
                    part_four_base.users user0_
                where
                    user0_.first_name='Willy'
            */

            session.getTransaction().commit();
        }
    }
}
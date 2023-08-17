package oldboy.lesson_25;
/*
Проблема N+1 и ее решения.

В данном случае мы используем 'fetch' сразу внутри HQL запроса.
*/
import oldboy.Util.HibernateUtil;
import oldboy.entity.User;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

import java.util.List;

public class UseFetchInsideHqlQueryDemo {
    public static void main(String[] args) {
        /* Создаем фабрику сессий */
        try(SessionFactory sessionFactory = HibernateUtil.buildSessionFactory();
            /* Открываем сессию */
            Session currentSession = sessionFactory.openSession()) {
            /* Начинаем транзакцию */
            currentSession.beginTransaction();

            List<User> myUsers =
                    currentSession.
                    createQuery("select user " +
                                         "from User as user " +
                                         "join fetch user.payments " +
                                         "join fetch user.company " +
            /*
            WHERE 1 = 1 (true) Используется в нескольких целях:
            1. Запрос формируется динамически, т.е. например, в зависимости от условий, заданных
            на какой-то форме, к WHERE добавляются AND .... В этом случае, если не нужно добавлять
            ни одного условия, то останется "голое" WHERE, что вызовет ошибку. А если начинать с
            WHERE 1 = 1, то запрос в любом случае будет корректным.

            2. Для целей отладки. Иногда нужно закомментировать какую-то строку. Все строки, кроме
            первой, комментировать удобно, а если комментировать первую, то надо убирать и первый AND.
            Если же первой строкой идет 1 = 1, то такой проблемы не возникает.

            Дополнение: WHERE 1 = 2 обычно используется в конструкции CREATE TABLE AS SELECT (CTAS).

                CREATE TABLE tab2
                AS
                SELECT *
                FROM tab1
                WHERE 1 = 2

            Если не указать WHERE, то будет создана новая таблица и все строки старой будут скопированы
            в новую. Если указать 1 = 2 (или 1 = 0, т.е. false), то не будет выбрано ни одной строки и
            будет создана таблица без строк.
            */
                                         "where 1 = 1 ", User.class).
                    list();

            myUsers.forEach(user -> System.out.println(user.getPayments().size()));
            myUsers.forEach(user -> System.out.println(user.getCompany().getCompanyName()));
            /*
            Результат будет извлечен одним запросом (его можно поместить в Query Console и
            увидеть табличный результат):

                 select
                    user0_.user_id as user_id1_4_0_,
                    payments1_.id as id1_2_1_,
                    company2_.company_id as company_1_1_2_,
                    user0_.company_id as company_8_4_0_,
                    user0_.info as info2_4_0_,
                    user0_.birth_date as birth_da3_4_0_,
                    user0_.first_name as first_na4_4_0_,
                    user0_.last_name as last_nam5_4_0_,
                    user0_.role as role6_4_0_,
                    user0_.user_name as user_nam7_4_0_,
                    payments1_.amount as amount2_2_1_,
                    payments1_.receiver_id as receiver3_2_1_,
                    payments1_.receiver_id as receiver3_2_0__,
                    payments1_.id as id1_2_0__,
                    company2_.company_name as company_2_1_2_
                from
                    part_four_base.users user0_
                inner join
                    part_four_base.payment payments1_
                        on user0_.user_id=payments1_.receiver_id
                inner join
                    part_four_base.company company2_
                        on user0_.company_id=company2_.company_id
                where
                    1=1

            Если не считать запросы к полю 'profile' сущностей.
            */

            /* Коммитим транзакцию */
            currentSession.getTransaction().commit();
        }
    }
}

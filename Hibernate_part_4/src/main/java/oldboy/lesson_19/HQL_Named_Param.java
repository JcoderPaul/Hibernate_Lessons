package oldboy.lesson_19;
/* Краткие примеры работы с HQL */
import oldboy.Util.HibernateUtil;
import oldboy.entity.User;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

import java.util.List;

public class HQL_Named_Param {
    public static void main(String[] args) {

        try(SessionFactory sessionFactory = HibernateUtil.buildSessionFactory();
            Session session = sessionFactory.openSession()){
            session.beginTransaction();
            /*
            Используем именованные параметры, простой цифровой (порядковый).
            Естественно значение value можно прогрузить из вне, например:

            *****************************************************************
            String name_to_load = "Jordy";
            ...
            ... .setParameter(1,name_to_load)
            ...
            *****************************************************************

            по факту - это prepare statement (?):
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
                    user0_.first_name=?
            */
            List<User> listHQLQuery =
                    session.createQuery(
                            "select thisUser " +
                                     "from User thisUser " +
                                     "where thisUser.personalInfo.firstName = ?1",
                            User.class)
                            .setParameter(1,"Jordy") // Задаем значение
                            .list();
            listHQLQuery.forEach(System.out::println);

            System.out.println("\n----------------------- Next Query Result -----------------------");
            /*
            Но наиболее правильным считается фактическое именования параметров запроса
            */
            String nameToInsert = "Willy";
            List<User> listFromQuery =
                    session.createQuery(
                                    "select us " +
                                            "from User us " +
                                            "where us.personalInfo.firstName = :firstName",
                                    User.class)
                            .setParameter("firstName", nameToInsert) // Задаем значение
                            .list();
            listFromQuery.forEach(System.out::println);

            session.getTransaction().commit();
        }
    }
}
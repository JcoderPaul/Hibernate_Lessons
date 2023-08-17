package oldboy.lesson_19;
/* Краткие примеры работы с классическим SQL запросом в Hibernate */
import oldboy.Util.HibernateUtil;
import oldboy.entity.User;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.NativeQuery;

import java.util.List;

public class HQL_Native_Query {
    public static void main(String[] args) {

        try(SessionFactory sessionFactory = HibernateUtil.buildSessionFactory();
            Session session = sessionFactory.openSession()){
            session.beginTransaction();
            /* Применяем метод *.createNativeQuery("... SQL query string ...") */
            NativeQuery<User> listHQLQuery =
                    session.createNativeQuery(
                            "select u.user_name " +
                                   "from part_four_base.users as u " +
                                   "where u.first_name = 'Willy'");
            List resultOfQuery = listHQLQuery.getResultList();
            resultOfQuery.forEach(System.out::println);
            /*
            Hibernate:
                select
                    u.user_name
                from
                    part_four_base.users as u
                where
                    u.first_name = 'Willy'

            ww@chocolate.com
            willi@persident.com
            shpily_willi@wet.com
            */

            /*
            А можно и так, что позволяет извлечь сразу сущность и
            смапить связные с ней другие сущности, вот это крутата !!!
            см. вывод в консоль ниже.
            */
            NativeQuery<User> listQuery =
                    session.createNativeQuery(
                            "select * " +
                                    "from part_four_base.users " +
                                    "where first_name = 'Willy'", User.class);
            List result = listQuery.getResultList();
            result.forEach(System.out::println);
            /*
            Hibernate:
                select
                    *
                from
                    part_four_base.users
                where
                    first_name = 'Willy'
            Hibernate:
                select
                    profile0_.profile_id as profile_1_4_0_,
                    profile0_.language as language2_4_0_,
                    profile0_.street as street3_4_0_,
                    profile0_.user_id as user_id4_4_0_
                from
                    part_four_base.profile profile0_
                where
                    profile0_.user_id=?
            Hibernate:
                select
                    profile0_.profile_id as profile_1_4_0_,
                    profile0_.language as language2_4_0_,
                    profile0_.street as street3_4_0_,
                    profile0_.user_id as user_id4_4_0_
                from
                    part_four_base.profile profile0_
                where
                    profile0_.user_id=?
            Hibernate:
                select
                    profile0_.profile_id as profile_1_4_0_,
                    profile0_.language as language2_4_0_,
                    profile0_.street as street3_4_0_,
                    profile0_.user_id as user_id4_4_0_
                from
                    part_four_base.profile profile0_
                where
                    profile0_.user_id=?

            User(userId=6, personalInfo=PersonalInfo(firstName=Willy, lastName=Wonka, ... )
            User(userId=8, personalInfo=PersonalInfo(firstName=Willy, lastName=Bushew, ... )
            User(userId=9, personalInfo=PersonalInfo(firstName=Willy, lastName=Ambush, ... )

            Я заменил на ... остатки данных, а то результат запроса внушительный.
            */

            session.getTransaction().commit();
        }
    }
}
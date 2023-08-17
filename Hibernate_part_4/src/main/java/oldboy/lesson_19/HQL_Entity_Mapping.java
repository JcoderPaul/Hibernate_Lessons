package oldboy.lesson_19;
/* Краткие примеры работы с HQL */
import oldboy.Util.HibernateUtil;
import oldboy.entity.User;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

import java.util.List;

public class HQL_Entity_Mapping {
    public static void main(String[] args) {

        try(SessionFactory sessionFactory = HibernateUtil.buildSessionFactory();
            Session session = sessionFactory.openSession()){
            session.beginTransaction();
            /*
            Используем именованные параметры, при JOIN запросе, при этом, если у нас
            правильно прописана связь (маппинг) сущностей (таблиц), то все упрощается
            и естественно отличается от SQL например ('as' добавлены для наглядности):
            */
            List<User> listHQLQuery =
                    session.createQuery(
                            "select thisUser from User as thisUser " +
                                     "join thisUser.company as thisCompany " +
                                     "where thisUser.personalInfo.firstName = :firstName " +
                                     "and thisCompany.companyName = :companyName",
                            User.class)
                            .setParameter("firstName","Willy")
                            .setParameter("companyName", "Facebook")
                            .list();

            listHQLQuery.forEach(System.out::println);
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
                inner join
                    part_four_base.company company1_
                        on user0_.company_id=company1_.company_id
                where
                    user0_.first_name=?
                    and company1_.company_name=?
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
            */

            System.out.println("\n----------------------- Next Query Result -----------------------");
            /*
            И конечно сортировка (тянем по первому имени, сортируем по второму):
            */
            List<User> sortListQuery =
                    session.createQuery(
                                    "select u from User as u " +
                                             "join u.company as c " +
                                             "where u.personalInfo.firstName = :firstName " +
                                             "and c.companyName = :companyName " +
                                             "order by u.personalInfo.lastName desc",
                                    User.class)
                            .setParameter("firstName","Willy")
                            .setParameter("companyName", "Facebook")
                            .list();

            sortListQuery.forEach(System.out::println);
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
                inner join
                    part_four_base.company company1_
                        on user0_.company_id=company1_.company_id
                where
                    user0_.first_name=?
                    and company1_.company_name=?
                order by
                    user0_.last_name desc

            User(userId=6, personalInfo=PersonalInfo(firstName=Willy, lastName=Wonka, ...)
            User(userId=9, personalInfo=PersonalInfo(firstName=Willy, lastName=Ambush, ...)
            */
            session.getTransaction().commit();
        }
    }
}
package oldboy.lesson_20;

import oldboy.Util.HibernateUtil;
import oldboy.dao.UserDao;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

import java.util.Arrays;

public class PracticeTestDaoTwo {
    public static void main(String[] args) {
        try(SessionFactory sessionFactory = HibernateUtil.buildSessionFactory();
            Session currentSession = sessionFactory.openSession()) {
            currentSession.beginTransaction();
            UserDao udTest = UserDao.getInstance();

            System.out.println("------------------------ Test 5 ------------------------");
            var queryRes_5 = udTest.
                    findAllPaymentsByCompanyName(currentSession,"Google");
            queryRes_5.forEach(System.out::println);
            /*
            Hibernate формирует внушительный каскад запросов:
                select
                    payment0_.id as id1_3_,
                    payment0_.amount as amount2_3_,
                    payment0_.receiver_id as receiver3_3_
                from
                    part_four_base.payment payment0_
                inner join
                    part_four_base.users user1_
                        on payment0_.receiver_id=user1_.user_id
                inner join
                    part_four_base.company company2_
                        on user1_.company_id=company2_.company_id
                where
                    company2_.company_name=?
                order by
                    user1_.first_name,
                    payment0_.amount
            Hibernate:
                select
                    user0_.user_id as user_id1_5_0_,
                    user0_.company_id as company_8_5_0_,
                    user0_.info as info2_5_0_,
                    user0_.birth_date as birth_da3_5_0_,
                    user0_.first_name as first_na4_5_0_,
                    user0_.last_name as last_nam5_5_0_,
                    user0_.role as role6_5_0_,
                    user0_.user_name as user_nam7_5_0_
                from
                    part_four_base.users user0_
                where
                    user0_.user_id=?
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
                    user0_.user_id as user_id1_5_0_,
                    user0_.company_id as company_8_5_0_,
                    user0_.info as info2_5_0_,
                    user0_.birth_date as birth_da3_5_0_,
                    user0_.first_name as first_na4_5_0_,
                    user0_.last_name as last_nam5_5_0_,
                    user0_.role as role6_5_0_,
                    user0_.user_name as user_nam7_5_0_
                from
                    part_four_base.users user0_
                where
                    user0_.user_id=?
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
                    user0_.user_id as user_id1_5_0_,
                    user0_.company_id as company_8_5_0_,
                    user0_.info as info2_5_0_,
                    user0_.birth_date as birth_da3_5_0_,
                    user0_.first_name as first_na4_5_0_,
                    user0_.last_name as last_nam5_5_0_,
                    user0_.role as role6_5_0_,
                    user0_.user_name as user_nam7_5_0_
                from
                    part_four_base.users user0_
                where
                    user0_.user_id=?
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
                    user0_.user_id as user_id1_5_0_,
                    user0_.company_id as company_8_5_0_,
                    user0_.info as info2_5_0_,
                    user0_.birth_date as birth_da3_5_0_,
                    user0_.first_name as first_na4_5_0_,
                    user0_.last_name as last_nam5_5_0_,
                    user0_.role as role6_5_0_,
                    user0_.user_name as user_nam7_5_0_
                from
                    part_four_base.users user0_
                where
                    user0_.user_id=?
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
                    user0_.user_id as user_id1_5_0_,
                    user0_.company_id as company_8_5_0_,
                    user0_.info as info2_5_0_,
                    user0_.birth_date as birth_da3_5_0_,
                    user0_.first_name as first_na4_5_0_,
                    user0_.last_name as last_nam5_5_0_,
                    user0_.role as role6_5_0_,
                    user0_.user_name as user_nam7_5_0_
                from
                    part_four_base.users user0_
                where
                    user0_.user_id=?
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
                    user0_.user_id as user_id1_5_0_,
                    user0_.company_id as company_8_5_0_,
                    user0_.info as info2_5_0_,
                    user0_.birth_date as birth_da3_5_0_,
                    user0_.first_name as first_na4_5_0_,
                    user0_.last_name as last_nam5_5_0_,
                    user0_.role as role6_5_0_,
                    user0_.user_name as user_nam7_5_0_
                from
                    part_four_base.users user0_
                where
                    user0_.user_id=?
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

            Payment(id=7, amount=700, receiver=User(userId=7, personalInfo=PersonalInfo(firstName=Bary, lastName=Gudkayn, ...)
            Payment(id=3, amount=400, receiver=User(userId=3, personalInfo=PersonalInfo(firstName=Garibo, lastName=Coply, ...)
            Payment(id=2, amount=600, receiver=User(userId=2, personalInfo=PersonalInfo(firstName=Jordy, lastName=LaForge, ...)
            Payment(id=5, amount=300, receiver=User(userId=5, personalInfo=PersonalInfo(firstName=Star, lastName=Lord, ...)
            Payment(id=1, amount=500, receiver=User(userId=1, personalInfo=PersonalInfo(firstName=Tasha, lastName=Yar, ...)
            Payment(id=9, amount=500, receiver=User(userId=10, personalInfo=PersonalInfo(firstName=Willy, lastName=Bushew, ...)
            */

            System.out.println("------------------------ Test 6 ------------------------");
            var queryRes_6 = udTest.
                    findAveragePaymentAmountByFirstAndLastNames(currentSession,
                                                        "Willy",
                                                                "Bushew");
            System.out.println(queryRes_6);
            /*
            Hibernate:
                select
                    avg(payment0_.amount) as col_0_0_
                from
                    part_four_base.payment payment0_
                inner join
                    part_four_base.users user1_
                        on payment0_.receiver_id=user1_.user_id
                where
                    user1_.first_name=?
                    and user1_.last_name=?

            400.0
            */

            System.out.println("------------------------ Test 7 ------------------------");
            var queryRes_7 =
                    udTest.findCompanyNamesWithAvgUserPaymentsOrderedByCompanyName(currentSession);
            for (int i = 0; i < queryRes_7.size(); i++) {
                System.out.println(Arrays.toString(queryRes_7.get(i)));
            }

            System.out.println("------------------------ Test 8 ------------------------");
            var avgAmount = currentSession.
                    createQuery("select avg(p.amount) from Payment p",Double.class).
                    uniqueResult();
            System.out.println("\nСредняя зарплата(по всей таблице Payment): " + avgAmount + "\n");
            var queryRes_8 = udTest.findSomethingWithSomething(currentSession);
            for (int i = 0; i < queryRes_8.size(); i++) {
                System.out.println(Arrays.asList(queryRes_8.get(i)));
            }
            /*
            ------------------------ Test 8 ------------------------
            Hibernate:
                select
                    avg(payment0_.amount) as col_0_0_
                from
                    part_four_base.payment payment0_

            Средняя зарплата(по всей таблице Payment): 488.8888888888889

            Hibernate:
                select
                    user0_.user_id as col_0_0_,
                    avg(payments1_.amount) as col_1_0_,
                    user0_.user_id as user_id1_5_,
                    user0_.company_id as company_8_5_,
                    user0_.info as info2_5_,
                    user0_.birth_date as birth_da3_5_,
                    user0_.first_name as first_na4_5_,
                    user0_.last_name as last_nam5_5_,
                    user0_.role as role6_5_,
                    user0_.user_name as user_nam7_5_
                from
                    part_four_base.users user0_
                inner join
                    part_four_base.payment payments1_
                        on user0_.user_id=payments1_.receiver_id
                group by
                    user0_.user_id
                having
                    avg(payments1_.amount)>(
                        select
                            avg(payment2_.amount)
                        from
                            part_four_base.payment payment2_
                    )
                order by
                    user0_.first_name
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

            [User(userId=4, personalInfo=PersonalInfo(firstName=Aerdol, lastName=T-Qute, ..., 600.0]
            [User(userId=7, personalInfo=PersonalInfo(firstName=Bary, lastName=Gudkayn, ..., 700.0]
            [User(userId=2, personalInfo=PersonalInfo(firstName=Jordy, lastName=LaForge, ..., 600.0]
            [User(userId=1, personalInfo=PersonalInfo(firstName=Tasha, lastName=Yar, ..., 500.0]
            [User(userId=10, personalInfo=PersonalInfo(firstName=Willy, lastName=Bushew, ..., 500.0]
            [User(userId=6, personalInfo=PersonalInfo(firstName=Willy, lastName=Wonka, ..., 500.0]
            */

            currentSession.getTransaction().commit();
        }
    }
}

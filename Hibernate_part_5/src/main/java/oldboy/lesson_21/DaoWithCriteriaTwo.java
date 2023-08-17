package oldboy.lesson_21;
/* Тестирование работы методов UserDaoWithCriteriaAPI.java */
import oldboy.Util.HibernateUtil;
import oldboy.dao.UserDaoWithCriteriaAPI;
import oldboy.entity.User;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

import javax.persistence.Tuple;
import java.util.Arrays;

public class DaoWithCriteriaTwo {
    public static void main(String[] args) {
        try(SessionFactory sessionFactory = HibernateUtil.buildSessionFactory();
            Session currentSession = sessionFactory.openSession()) {

            currentSession.beginTransaction();
            UserDaoWithCriteriaAPI udTest = UserDaoWithCriteriaAPI.getInstance();

            System.out.println("------------------------ Test 5 ------------------------");
            var queryRes_5 = udTest.
                    findAllPaymentsByCompanyName(currentSession,"Google");
            queryRes_5.forEach(System.out::println);

            System.out.println("------------------------ Test 6 ------------------------");
            var queryRes_6 = udTest.
                    findAveragePaymentAmountByFirstAndLastNames(currentSession,
                                                        "Willy",
                                                                "Bushew");
            System.out.println(queryRes_6);

            System.out.println("------------------------ Test 7 ------------------------");
            var queryRes_7 =
                    udTest.findCompanyNamesWithAvgUserPaymentsOrderedByCompanyName(currentSession);
            for (int i = 0; i < queryRes_7.size(); i++) {
                System.out.println(Arrays.toString(queryRes_7.get(i)));
            }

            System.out.println("------------------------ Test 8 ------------------------");
            var queryRes_8 =
                    udTest.useTheLogicOfThePreviousMethodUsingTheCompanyDto(currentSession);
            queryRes_8.forEach(System.out::println);

            System.out.println("------------------------ Test 9 ------------------------");
            var avgAmount = currentSession.
                    createQuery("select avg(p.amount) from Payment p",Double.class).
                    uniqueResult();
            System.out.println("\nСредняя зарплата(по всей таблице Payment): " + avgAmount + "\n");

            var queryRes_9 = udTest.findSomethingWithSomething(currentSession);

            System.out.println();
            for (int i = 0; i < queryRes_9.size(); i++) {
                Tuple element = queryRes_9.get(i);
                var getData = element.toArray();

                System.out.print(((User) getData[0]).getPersonalInfo().getFirstName() + " - ");
                System.out.println(((Double) getData[1]).doubleValue());
           }

        currentSession.getTransaction().commit();
        }
    }
}

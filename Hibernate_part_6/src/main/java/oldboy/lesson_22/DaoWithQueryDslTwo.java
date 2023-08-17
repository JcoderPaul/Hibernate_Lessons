package oldboy.lesson_22;
/* Тестирование работы методов UserDaoWithQueryDsl.java */
import com.querydsl.core.Tuple;
import oldboy.Util.HibernateUtil;
import oldboy.dao.UserDaoWithQueryDsl;
import oldboy.entity.User;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

public class DaoWithQueryDslTwo {
    public static void main(String[] args) {
        try(SessionFactory sessionFactory = HibernateUtil.buildSessionFactory();
            Session currentSession = sessionFactory.openSession()) {

            currentSession.beginTransaction();
            UserDaoWithQueryDsl udTest = UserDaoWithQueryDsl.getInstance();

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
                Tuple getData = queryRes_7.get(i);
                var dataToArray = getData.toArray();

                System.out.print(dataToArray[0] + " - ");
                System.out.println(((Double) dataToArray[1]).doubleValue());
            }

            System.out.println("------------------------ Test 8 ------------------------");
            var avgAmount = currentSession.
                    createQuery("select avg(p.amount) from Payment p",Double.class).
                    uniqueResult();
            System.out.println("\nСредняя зарплата(по всей таблице Payment): " + avgAmount + "\n");

            var queryRes_8 = udTest.findSomethingWithSomething(currentSession);

            System.out.println();
            for (int i = 0; i < queryRes_8.size(); i++) {
                Tuple element = queryRes_8.get(i);
                var getData = element.toArray();

                System.out.print(((User) getData[0]).getPersonalInfo().getFirstName() + " - ");
                System.out.println(((Double) getData[1]).doubleValue());
           }

        currentSession.getTransaction().commit();
        }
    }
}

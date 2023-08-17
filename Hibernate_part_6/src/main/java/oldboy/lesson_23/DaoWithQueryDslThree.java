package oldboy.lesson_23;
/* Тестирование работы методов UserDaoWithQueryDsl.java */
import oldboy.Util.HibernateUtil;
import oldboy.dao.UserDaoWithQueryDsl;
import oldboy.dto.PaymentFilter;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

public class DaoWithQueryDslThree {
    public static void main(String[] args) {
        try(SessionFactory sessionFactory = HibernateUtil.buildSessionFactory();
            Session currentSession = sessionFactory.openSession()) {

            currentSession.beginTransaction();
            UserDaoWithQueryDsl udTest = UserDaoWithQueryDsl.getInstance();

            System.out.println("------------------------ Test 9 ------------------------");

            PaymentFilter paymentFilter = PaymentFilter.
                    builder().
                    firstName("Willy").
                    lastName("Bushew").
                    build();

            var queryRes_9 = udTest.
                    findAveragePaymentAmountByNamesFilterHardCodePredicates(currentSession,
                                                                             paymentFilter);
            System.out.println(queryRes_9);

            System.out.println("------------------------ Test 10 ------------------------");

            PaymentFilter payFilter = PaymentFilter.
                    builder().
                    firstName("Willy").
                    lastName("Bushew").
                    build();

            var queryRes_10 = udTest.
                    findAveragePaymentAmountByNamesFilterPredicatesMethods(currentSession,
                                                                            payFilter);
            System.out.println(queryRes_10);

        currentSession.getTransaction().commit();
        }
    }
}

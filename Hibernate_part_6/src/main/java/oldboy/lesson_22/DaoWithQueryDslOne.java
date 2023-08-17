package oldboy.lesson_22;
/* Тестирование работы методов UserDaoWithQueryDsl.java */
import oldboy.Util.HibernateUtil;
import oldboy.dao.UserDaoWithQueryDsl;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

public class DaoWithQueryDslOne {
    public static void main(String[] args) {
        try(SessionFactory sessionFactory = HibernateUtil.buildSessionFactory();
            Session currentSession = sessionFactory.openSession()) {
            currentSession.beginTransaction();
            UserDaoWithQueryDsl udTest = UserDaoWithQueryDsl.getInstance();

            System.out.println("------------------------ Test 1 ------------------------");
            var queryRes = udTest.findAll(currentSession);
            queryRes.forEach(System.out::println);

            System.out.println("------------------------ Test 2 ------------------------");
            var queryRes_2 = udTest.findAllByFirstName(currentSession, "Willy");
            queryRes_2.forEach(System.out::println);

            System.out.println("------------------------ Test 3 ------------------------");
            var queryRes_3 = udTest.findLimitedUsersOrderedByBirthday(currentSession, 5);
            queryRes_3.forEach(System.out::println);

            System.out.println("------------------------ Test 4 ------------------------");
            var queryRes_4 = udTest.findAllByCompanyName(currentSession, "Google");
            queryRes_4.forEach(System.out::println);

            currentSession.getTransaction().commit();
        }
    }
}

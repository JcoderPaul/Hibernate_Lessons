package oldboy.lesson_31;

import oldboy.Util.HibernateUtil;
import oldboy.entity.Payment;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

import java.util.List;

public class SetReadOnlyHQLQuery {
    public static void main(String[] args) {
        try (SessionFactory sessionFactory = HibernateUtil.buildSessionFactory();
             Session sessionOne = sessionFactory.openSession()) {

            sessionOne.beginTransaction();

            List<Payment> payments = sessionOne.
                    createQuery("select p from Payment p", Payment.class).
                    setReadOnly(true).
                    list();

            sessionOne.getTransaction().commit();
            sessionOne.close();
        }
    }
}

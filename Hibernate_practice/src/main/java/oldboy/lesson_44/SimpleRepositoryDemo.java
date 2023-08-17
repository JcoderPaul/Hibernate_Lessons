package oldboy.lesson_44;

import lombok.extern.slf4j.Slf4j;
import oldboy.Util.HibernateUtil;
import oldboy.dao.simple_repository_realisation.PaymentSimpleRepository;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

import javax.transaction.Transactional;

@Slf4j
public class SimpleRepositoryDemo {

    @Transactional
    public static void main(String[] args) {
        try (SessionFactory sessionFactory = HibernateUtil.buildSessionFactory()) {
            try (Session session = sessionFactory.getCurrentSession()) {
                session.beginTransaction();

                PaymentSimpleRepository paymentRepository =
                                  new PaymentSimpleRepository(sessionFactory);

                paymentRepository.findById(1L).ifPresent(System.out::println);

                session.getTransaction().commit();
            }
        }
    }
}

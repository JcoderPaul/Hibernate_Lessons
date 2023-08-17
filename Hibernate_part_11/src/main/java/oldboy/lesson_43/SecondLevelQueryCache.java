package oldboy.lesson_43;

import oldboy.Util.HibernateUtil;
import oldboy.entity.Payment;
import oldboy.entity.User;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.stat.CacheRegionStatistics;

import javax.transaction.Transactional;
import java.util.List;

public class SecondLevelQueryCache {

    @Transactional
    public static void main(String[] args) {
        try (SessionFactory sessionFactory = HibernateUtil.buildSessionFactory()) {

            try (Session sessionOne = sessionFactory.openSession()) {
                sessionOne.beginTransaction();

                System.out.println("\n-------------------- Запрос из первой сессии --------------------\n");
                sessionOne.find(User.class, 1L);
                List<Payment> paymentRes = sessionOne.
                        createQuery("select p from Payment p where p.receiver.id = :userId",
                                Payment.class).
                        setParameter("userId", 1L).
                        /* Указываем что данный запрос будет кешироваться */
                        setCacheable(true).
                        /*
                        Есть еще два варианта указать системе, что данный запрос
                        будет кешироваться:
                            setCacheRegion("queries").
                            setHint(QueryHints.CACHEABLE, true).
                        */
                        getResultList();

                System.out.println("\n-------------------- Статистика из первой сессии --------------------\n");
                /* Обратимся к методам статистики кеша второго уровня по region name = User*/
                System.out.println(sessionFactory.
                        getStatistics().
                        getCacheRegionStatistics("User"));
                /*
                49674 nanoseconds spent acquiring 1 JDBC connections;
                70610 nanoseconds spent releasing 1 JDBC connections;
                767271 nanoseconds spent preparing 3 JDBC statements;
                5604892 nanoseconds spent executing 3 JDBC statements;
                0 nanoseconds spent executing 0 JDBC batches;
                60581927 nanoseconds spent performing 3 L2C puts;
                0 nanoseconds spent performing 0 L2C hits;
                2242285 nanoseconds spent performing 2 L2C misses;
                2146223 nanoseconds spent executing 1 flushes
                        (flushing a total of 3 entities and 2 collections);
                26675474 nanoseconds spent executing 1 partial-flushes
                        (flushing a total of 2 entities and 2 collections)
                */
                sessionOne.getTransaction().commit();
            }

            try (Session sessionTwo = sessionFactory.openSession()) {
                sessionTwo.beginTransaction();

                System.out.println("\n-------------------- Запрос из второй сессии --------------------\n");
                sessionTwo.find(User.class, 1L);
                List<Payment> paymentFromSecondSession = sessionTwo.
                        createQuery("select p from Payment p where p.receiver.id = :userId",
                                Payment.class).
                        setParameter("userId", 1L).
                        /*
                        В данном случае мы указываем, что запрос кешируется, как и выше,
                        однако это очень важный момент, также мы говорим системе, что
                        возможно данный запрос уже закеширован.
                        */
                        setCacheable(true).
                        getResultList();

                System.out.println("\n-------------------- Статистика из второй сессии --------------------\n");
                CacheRegionStatistics statTwo = sessionFactory.
                                                    getStatistics().
                                                    getCacheRegionStatistics("User");
                System.out.println(statTwo);
                /*
                47210 nanoseconds spent acquiring 1 JDBC connections;
                36537 nanoseconds spent releasing 1 JDBC connections;
                64452 nanoseconds spent preparing 1 JDBC statements;
                1024669 nanoseconds spent executing 1 JDBC statements;
                0 nanoseconds spent executing 0 JDBC batches;
                0 nanoseconds spent performing 0 L2C puts;
                1955327 nanoseconds spent performing 3 L2C hits;
                117821 nanoseconds spent performing 1 L2C misses;
                756186 nanoseconds spent executing 1 flushes
                        (flushing a total of 3 entities and 2 collections);
                563650 nanoseconds spent executing 1 partial-flushes
                        (flushing a total of 2 entities and 2 collections)
                */
                sessionTwo.getTransaction().commit();
            }
        }

    }
}
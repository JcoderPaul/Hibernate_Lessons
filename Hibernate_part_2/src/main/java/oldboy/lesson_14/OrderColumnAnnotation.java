package oldboy.lesson_14;
/*
Применение аннотаций @OrderColumn
см. lesson_14/MappingEntity/SecretService.java
*/
import oldboy.Util.HibernateUtil;
import oldboy.lesson_14.MappingEntity.SecretService;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

public class OrderColumnAnnotation {
    public static void main(String[] args) {

        /* Фабрика сессий */
        try (SessionFactory sessionFactory = HibernateUtil.buildSessionFactory()) {
            /* Создали переменную */
            Session sessionOne = null;
            /* Сессия первая */
            System.out.println("------------ First session start ------------");
            try {
                /* Открыли сессию */
                sessionOne = sessionFactory.openSession();
                System.out.println("Статистика первой сессии : " + sessionOne.getStatistics());
                /* Статистика первой сессии : SessionStatistics[entity count=0,collection count=0] */
                /* Начали транзакцию */
                sessionOne.beginTransaction();

                SecretService serviceList = sessionOne.get(SecretService.class,3);
                serviceList.getSpies().forEach(System.out::println);
                /*
                Hibernate:
                    select
                        secretserv0_.bureau_id as bureau_i1_13_0_,
                        secretserv0_.bureau_name as bureau_n2_13_0_
                    from
                        public.secret_services secretserv0_
                    where
                        secretserv0_.bureau_id=?
                Hibernate:
                    select
                        spies0_.bureau_id as bureau_i4_14_0_,
                        spies0_.spy_id as spy_id1_14_0_,
                        spies0_.spy_id as spy_id1_0_,
                        spies0_.spy_id as spy_id1_14_1_,
                        spies0_.bureau_id as bureau_i4_14_1_,
                        spies0_.salary as salary2_14_1_,
                        spies0_.spy_name as spy_name3_14_1_
                    from
                        public.spies spies0_
                    where
                        spies0_.bureau_id=?

                null
                null
                null
                null
                Spy(workerId=4, workerName=Jeyms Bond, salary=12500.0)
                null
                null
                null
                Spy(workerId=8, workerName=Dafna Park, salary=7500.0)
                Spy(workerId=9, workerName=Brus Lokkart, salary=15300.0)
                Spy(workerId=10, workerName=Sidney Reyli, salary=18300.0)
                Spy(workerId=11, workerName=Somerset Moem, salary=19100.0)
                */
                sessionOne.getTransaction().commit();

            } finally {
                sessionOne.close();
            }
            System.out.println("Статистика первой сессии после close: " + sessionOne.getStatistics());
            System.out.println("------------ Close first session ------------");
        }
    }
}

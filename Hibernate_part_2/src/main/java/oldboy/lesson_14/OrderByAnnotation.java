package oldboy.lesson_14;
/*
Применение аннотаций @OrderBy
см. lesson_10/MappingEntity/Firm.java
*/
import oldboy.Util.HibernateUtil;
import oldboy.lesson_10.MappingEntity.Firm;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

public class OrderByAnnotation {
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
                /* Используем класс из oldboy/lesson_10/MappingEntity/Firm.java */
                Firm firm = sessionOne.get(Firm.class,1);
                firm.getWorkers().forEach(System.out::println);
                /*
                Hibernate:
                    select
                        firm0_.firm_id as firm_id1_10_0_,
                        firm0_.firm_name as firm_nam2_10_0_
                    from
                        public.firms firm0_
                    where
                        firm0_.firm_id=?
                Hibernate:
                    select
                        workers0_.firm_id as firm_id4_9_0_,
                        workers0_.worker_id as worker_i1_9_0_,
                        workers0_.worker_id as worker_i1_9_1_,
                        workers0_.firm_id as firm_id4_9_1_,
                        workers0_.salary as salary2_9_1_,
                        workers0_.worker_name as worker_n3_9_1_
                    from
                        public.firm_workers workers0_
                    where
                        workers0_.firm_id=?
                    order by
                        workers0_.worker_name desc

                Worker(workerId=7, workerName=Sanara Questa, salary=1441.2)
                Worker(workerId=11, workerName=Mari Fanyde, salary=1841.2)
                Worker(workerId=9, workerName=Iolanta Vasikovska, salary=1831.2)
                Worker(workerId=12, workerName=Hiro Tajimy, salary=2341.2)
                Worker(workerId=13, workerName=Giro Kano, salary=1351.2)
                Worker(workerId=1, workerName=Duglas Lind, salary=1341.2)
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

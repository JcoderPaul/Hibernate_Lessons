package oldboy.lesson_14;
/*
Применение аннотаций @SortNatural и implements Comparable<... sorted class ...>
при сортировке коллекций Set см. lesson_14/MappingEntity/SecretService.java
*/
import oldboy.Util.HibernateUtil;
import oldboy.lesson_14.MappingEntity.SecretService;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

public class SortNaturalAnnotation {
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
                SecretService service = sessionOne.get(SecretService.class,3);
                service.getTraitors().forEach(System.out::println);
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
                        traitors0_.bureau_id as bureau_i4_18_0_,
                        traitors0_.traitor_id as traitor_1_18_0_,
                        traitors0_.traitor_id as traitor_1_18_1_,
                        traitors0_.bureau_id as bureau_i4_18_1_,
                        traitors0_.salary as salary2_18_1_,
                        traitors0_.traitor_name as traitor_3_18_1_
                    from
                        public.traitors traitors0_
                    where
                        traitors0_.bureau_id=?
                Traitor(traitorId=4, traitorName=Alldredj Aymse, salary=9900.0)
                Traitor(traitorId=3, traitorName=Jordee Blayk, salary=7500.0)
                Traitor(traitorId=1, traitorName=Keem Fealby, salary=8700.0)
                Traitor(traitorId=2, traitorName=Klaus Fooks, salary=5600.0)
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

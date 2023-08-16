package oldboy.lesson_14;
/*
Применение аннотаций @MapKey
см. lesson_14/MappingEntity/SecretService.java
*/
import oldboy.Util.HibernateUtil;
import oldboy.lesson_14.MappingEntity.SecretService;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

public class MapKeyAnnotation {
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

                SecretService serviceList = sessionOne.get(SecretService.class,6);
                /*
                Сортировка идет по ключу, ключом выступает имя сущности,
                а имя сортируется по алфавиту см. DoubleAgent.java. При
                этом существует масса других аннотаций @MapKeyClass,
                @MapKey...(...Column, Enumerated и т.д.)
                */
                serviceList.getDoubles().forEach((key, val) -> System.out.println(val));
                /*
                Hibernate:
                    select
                        secretserv0_.bureau_id as bureau_i1_14_0_,
                        secretserv0_.bureau_name as bureau_n2_14_0_
                    from
                        public.secret_services secretserv0_
                    where
                        secretserv0_.bureau_id=?
                Hibernate:
                    select
                        doubles0_.bureau_id as bureau_i4_8_0_,
                        doubles0_.double_id as double_i1_8_0_,
                        doubles0_.double_name as formula1_0_,
                        doubles0_.double_id as double_i1_8_1_,
                        doubles0_.bureau_id as bureau_i4_8_1_,
                        doubles0_.double_name as double_n2_8_1_,
                        doubles0_.salary as salary3_8_1_
                    from
                        public.doubles doubles0_
                    where
                        doubles0_.bureau_id=?

                DoubleAgent(doubleId=10, doubleName=Abel Fihter, salary=12300.0)
                DoubleAgent(doubleId=1, doubleName=Dushan Popov, salary=12300.0)
                DoubleAgent(doubleId=3, doubleName=Humam Al Balavy, salary=8700.0)
                DoubleAgent(doubleId=5, doubleName=Oldrich Ayens, salary=7500.0)
                DoubleAgent(doubleId=8, doubleName=Welyam Sebold, salary=7600.0)
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

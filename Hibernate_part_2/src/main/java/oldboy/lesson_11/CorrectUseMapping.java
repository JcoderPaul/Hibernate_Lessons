package oldboy.lesson_11;

import oldboy.Util.HibernateUtil;
import oldboy.lesson_11.MappingEntity.Address;
import oldboy.lesson_11.MappingEntity.Driver;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

public class CorrectUseMapping {
    public static void main(String[] args) {

        Driver driver = Driver.
                builder().
                driverName("Tanku Ramal").
                salary(3214.3).
                experience(28).
                build();

        Address address = Address.
                builder().
                language("gr").
                street("Valku psp st.12 k.43").
                build();
        /* Фабрика сессий */
        try (SessionFactory sessionFactory = HibernateUtil.buildSessionFactory()) {
            /* Создали переменную */
            Session sessionOne = null;
            /* Сессия первая */
            System.out.println("------------ First session start ------------");
            try {
                /* Открыли сессию */
                sessionOne = sessionFactory.openSession();
                System.out.println("Статистика первой сессии " + sessionOne.getStatistics());
                /* Начали транзакцию */
                sessionOne.beginTransaction();

                sessionOne.save(driver);

                address.setDriver(driver);
                sessionOne.save(address);

                System.out.println("Статистика первой сессии перед commit: " + sessionOne.getStatistics());

                sessionOne.getTransaction().commit();

            } finally {
                sessionOne.close();
            }
            System.out.println("Статистика первой сессии после close: " + sessionOne.getStatistics());
            System.out.println("------------ Close first session ------------");

            Session sessionTwo = null;
            try {
                System.out.println("\n------------ Open second session ------------");
                /* Открыли вторую сессию */
                sessionTwo = sessionFactory.openSession();
                sessionTwo.beginTransaction();

                Driver prnDrv = sessionTwo.get(Driver.class, 2);
                System.out.println(prnDrv);
                Address prnAddress = sessionTwo.get(Address.class, 2);
                System.out.println(prnAddress);

                sessionTwo.getTransaction().commit();
            } finally {
                sessionTwo.close();
                System.out.println("------------ Close second session ------------");
            }
        }
    }
}
